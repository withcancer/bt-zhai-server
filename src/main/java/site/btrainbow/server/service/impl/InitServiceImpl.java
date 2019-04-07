package site.btrainbow.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import site.btrainbow.server.model.InitInfoDo;
import site.btrainbow.server.model.ResponseVo;
import site.btrainbow.server.repo.InitRepo;
import site.btrainbow.server.repo.TorrentInfoRepo;
import site.btrainbow.server.service.InitService;
import site.btrainbow.server.utils.ResultUtil;


@Service
public class InitServiceImpl implements InitService {
    @Autowired
    private TorrentInfoRepo torrentInfoRepo;
    @Autowired
    private InitRepo initRepo;
    @Autowired
    private SearchServiceImpl searchServiceImpl;

    /**
     * 从缓存中获得画面首页的数据
     * 只有第一次装入服务器时才可能真正运行
     *
     * @return 搜索结果
     * @see ResponseVo
     */
    @Cacheable("initData")
    public ResponseVo initFromCache() {
        var initDo = init();
        return ResultUtil.success(initDo);
    }

    /**
     * 初始化方法，从es中取出之前的结果进行统计排名然后返回
     * 这个方法也可以被定时方法用来更新缓存
     *
     * @return initInfo初始化首页面的数据，此处来自真正的后台
     * @see InitInfoDo
     */
    public InitInfoDo init() {
        var torrentInfoCountNow = torrentInfoRepo.count();
        var initData = initRepo.findById("unique");
        var initDo = new InitInfoDo();

        initDo.setId("unique");
        initDo.setCount_total(torrentInfoCountNow);
        // 如果没有初始化缓存时
        if (!initData.isPresent()) {
            initDo.setCount_today(0L);
            initDo.setPopular_keyword(null);
            initDo.setRandom_torrent(null);
        } else {
            var torrentInfoCountOld = initData.get().getCount_total();
            initDo.setCount_today(torrentInfoCountNow - torrentInfoCountOld);
            initDo.setPopular_keyword(searchServiceImpl.getTopKeywordByWeek());
            var torrentList = searchServiceImpl.getRandomTorrentForInit();
            initDo.setRandom_torrent(torrentList);
        }

        initRepo.save(initDo);
        return initDo;
    }
}
