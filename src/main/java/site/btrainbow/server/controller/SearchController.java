package site.btrainbow.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.btrainbow.server.model.ResponseVo;
import site.btrainbow.server.model.SearchParamVo;
import site.btrainbow.server.service.SearchService;
import site.btrainbow.server.utils.ResultUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 根据关键字搜索分页结果
     *
     * @param request HttpServletRequest
     * @param param   搜索参数
     * @return 查询结果
     * @see SearchParamVo
     */

    @PostMapping("/search")
    public ResponseVo search(HttpServletRequest request, @Valid @RequestBody SearchParamVo param) {
        if (param.getKeyword().length() < 2) {
            return ResultUtil.success(null, 0L);
        }
        return searchService.search(request, param);
    }

    /**
     * 随机返回种子
     *
     * @return 查询结果
     * @see SearchParamVo
     */
    @GetMapping("/random")
    public ResponseVo random() {
        return searchService.getRandomTorrent();
    }

    /**
     * 返回可以用于seo优化的词汇
     *
     * @return 查询结果
     * @see SearchParamVo
     */
    @GetMapping("/seo")
    public ResponseVo seo() {
        return searchService.searchSeoWords();
    }
}
