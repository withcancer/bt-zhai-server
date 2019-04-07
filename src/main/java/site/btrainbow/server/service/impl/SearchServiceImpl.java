package site.btrainbow.server.service.impl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import site.btrainbow.server.model.*;
import site.btrainbow.server.repo.SearchHistoryRepo;
import site.btrainbow.server.repo.SeoWordRepo;
import site.btrainbow.server.repo.TorrentInfoRepo;
import site.btrainbow.server.service.SearchService;
import site.btrainbow.server.utils.IpUtil;
import site.btrainbow.server.utils.ResultUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private TorrentInfoRepo torrentInfoRepo;
    @Autowired
    private SearchHistoryRepo searchHistoryRepo;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SeoWordRepo seoWordRepo;

    /**
     * 种子搜索方法实现
     *
     * @param request HttpServletRequest
     * @param param   搜索参数
     * @return 搜索结果
     * @see SearchParamVo
     * @see ResponseVo
     */
    public ResponseVo search(HttpServletRequest request, SearchParamVo param) {
        var sort = makeSort(param);
        // 统计运行耗时
        var bTime = System.currentTimeMillis();
        var searchQuery = new NativeSearchQueryBuilder()
                .withIndices("torrent_info").withTypes("common")
                .withQuery(QueryBuilders.multiMatchQuery(param.getKeyword(), "name", "files.path"))
                .withHighlightFields(new HighlightBuilder.Field("name").preTags("<strong>").postTags("</strong>").noMatchSize(150).numOfFragments(1), new HighlightBuilder.Field("files.path").preTags("<strong>").postTags("</strong>").noMatchSize(150).numOfFragments(3))
                .withPageable(PageRequest.of(param.getPageNum(), param.getPageSize(), sort))
                .build();
        var torrentInfoPage = elasticsearchTemplate.queryForPage(searchQuery, TorrentInfoDo.class, new SearchResultMapper() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                if (searchResponse.getHits().getHits().length <= 0) {
                    return null;
                }
                var chunk = new ArrayList<>();

                for (var searchHit : searchResponse.getHits()) {
                    // 设置info部分
                    var torrentInfo = new TorrentInfoDo();
                    torrentInfo.setId(searchHit.getId());
                    torrentInfo.setName((String) searchHit.getSourceAsMap().get("name"));
                    torrentInfo.setLength(Long.parseLong(searchHit.getSourceAsMap().get("length").toString()));
                    torrentInfo.setCreate_time(Long.parseLong(searchHit.getSourceAsMap().get("create_time").toString()));
                    torrentInfo.setPopularity((Integer) searchHit.getSourceAsMap().get("popularity"));
                    // ArrayList<Map>->Map->FileList->List<FileList>
                    var resList = ((ArrayList<Map>) searchHit.getSourceAsMap().get("files"));
                    var fileList = new ArrayList<FileList>();
                    for (var map : resList) {
                        FileList file = new FileList();
                        file.setPath((String) map.get("path"));
                        file.setLength(Long.parseLong(map.get("length").toString()));
                        fileList.add(file);
                    }
                    torrentInfo.setFiles(fileList);
                    // 设置highlight部分
                    // 种子名称highlight(一般只有一个)
                    var nameHighlight = searchHit.getHighlightFields().get("name").getFragments()[0].toString();
                    // path highlight列表
                    var pathHighlight = getFileListFromHighLightFields(searchHit.getHighlightFields().get("files.path").fragments(), fileList);
                    torrentInfo.setNameHighLight(nameHighlight);
                    torrentInfo.setPathHighlight(pathHighlight);
                    chunk.add(torrentInfo);
                }
                if (chunk.size() > 0) {
                    // 不设置total返回不了正确的page结果
                    return new AggregatedPageImpl<>((List<T>) chunk, pageable, searchResponse.getHits().getTotalHits());
                }
                return null;
            }
        });
        saveSearchHistory(request, param);
        var eTime = System.currentTimeMillis();
        var costTime = (eTime - bTime);
        if (torrentInfoPage != null) {
            return ResultUtil.success(torrentInfoPage.getContent(), torrentInfoPage.getTotalElements(), costTime);
        } else {
            return ResultUtil.success("No Results", 0L, costTime);
        }
    }

    /**
     * 从原始的HighlightFields中获取可以返回前端的数据
     *
     * @param origin 原始文件列表
     * @param texts  高亮字符array
     * @return 加工完成的 HighlightField String
     */
    private List<FileList> getFileListFromHighLightFields(Text[] texts, List<FileList> origin) {
        var list = new ArrayList<FileList>();
        for (var text : texts) {
            // path
            var fileVo = new FileList();
            fileVo.setPath(text.toString());
            // 根据highlight字符串找到对应的length
            var pathName = text.toString().replaceAll("<strong>", "").replaceAll("</strong>", "");
            var findOpResult = origin.parallelStream().filter(item -> item.getPath().equals(pathName)).findAny();
            findOpResult.ifPresent(fileList -> fileVo.setLength(fileList.getLength()));
            list.add(fileVo);
        }
        return list;
    }

    /**
     * 获得随机的种子
     *
     * @return 种子列表
     */
    List<TorrentInfoDo> getRandomTorrentForInit() {
        return torrentInfoRepo.random(PageRequest.of(0, 10));
    }

    /**
     * 获得随机的种子
     *
     * @return 种子列表
     */
    public ResponseVo getRandomTorrent() {
        var torrentInfoList = torrentInfoRepo.random(PageRequest.of(0, 10));
        return ResultUtil.success(torrentInfoList);
    }

    /**
     * 获得搜索历史中的排名前十的关键字（按周，默认情况下）
     *
     * @return map关键字及搜索次数
     */
    List<Keyword> getTopKeywordByWeek() {
        return getTopKeyword("now-7d");
    }

    /**
     * 获得搜索历史中的排名前十的关键字（按月）
     *
     * @return map关键字及搜索次数
     */
    public List<Keyword> getTopKeywordByMonth() {
        return getTopKeyword("now-30d");
    }

    /**
     * 获得搜索历史中的排名前十的关键字（按年）
     *
     * @return map关键字及搜索次数
     */
    public List<Keyword> getTopKeywordByYear() {
        return getTopKeyword("now-365d");
    }

    /**
     * 保存搜索历史
     *
     * @param request HttpServletRequest
     * @param param   搜索参数
     * @see SearchParamVo
     */
    private void saveSearchHistory(HttpServletRequest request, SearchParamVo param) {
        var historyDo = new SearchHistoryDo();
        historyDo.setAdd_time(System.currentTimeMillis());
        historyDo.setKeyword(param.getKeyword());
        historyDo.setIp(IpUtil.getIpAddress(request));
        searchHistoryRepo.save(historyDo);
    }

    /**
     * 检索用于seo sitemap的词汇
     * @return 搜索结果
     * @see SearchParamVo
     * @see ResponseVo
     */
    public ResponseVo searchSeoWords(){
        List<SeoWord> words = seoWordRepo.findAll().getContent();
        return ResultUtil.success(words);
    }

    /**
     * 根据搜索参数制作搜索排序
     *
     * @param param 搜索参数
     * @return Sort 可用于pageable的sort对象
     * @see SearchParamVo
     */
    private Sort makeSort(SearchParamVo param) {
        Order order = null;
        if (param.getScoreSortDirection().getEnabled()) {
            if (param.getScoreSortDirection().getValue().equals("ASC")) {
                order = new Order(Sort.Direction.ASC, "_score");
            } else {
                order = new Order(Sort.Direction.DESC, "_score");
            }
        }
        if (param.getLengthSortDirection().getEnabled()) {
            if (param.getLengthSortDirection().getValue().equals("ASC")) {
                order = new Order(Sort.Direction.ASC, "length");
            } else {
                order = new Order(Sort.Direction.DESC, "length");
            }
        }
        if (param.getCreateTimeSortDirection().getEnabled()) {
            if (param.getCreateTimeSortDirection().getValue().equals("ASC")) {
                order = new Order(Sort.Direction.ASC, "create_time");
            } else {
                order = new Order(Sort.Direction.DESC, "create_time");
            }
        }
        if (param.getPopularitySortDirection().getEnabled()) {
            if (param.getPopularitySortDirection().getValue().equals("ASC")) {
                order = new Order(Sort.Direction.ASC, "popularity");
            } else {
                order = new Order(Sort.Direction.DESC, "popularity");
            }
        }
        return Sort.by(order);
    }

    /**
     * 获得搜索历史中的排名前十的关键字
     *
     * @param dateRange 请求结果的时间周期
     * @return map关键字及搜索次数
     */
    private List<Keyword> getTopKeyword(String dateRange) {
        var keywordList = new ArrayList<Keyword>();
        var searchQuery = new NativeSearchQueryBuilder()
                .withIndices("search_history").withTypes("common")
                .withFilter(QueryBuilders.rangeQuery("add_time").gte(dateRange))
                .addAggregation(terms("top_hit_keyword").field("keyword").size(10))
                .build();
        var aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        var topKeywords = (StringTerms) aggregations.asMap().get("top_hit_keyword");
        topKeywords.getBuckets().forEach((b) -> {
            Keyword keyword = new Keyword();
            keyword.setValue(b.getKeyAsString());
            keyword.setSearch_times(b.getDocCount());
            keywordList.add(keyword);
        });
        return keywordList;
    }
}
