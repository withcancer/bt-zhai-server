package site.btrainbow.server.service;

import site.btrainbow.server.model.ResponseVo;
import site.btrainbow.server.model.SearchParamVo;

import javax.servlet.http.HttpServletRequest;

public interface SearchService {
    ResponseVo search(HttpServletRequest request, SearchParamVo param);

    ResponseVo searchSeoWords();

    ResponseVo getRandomTorrent();
}
