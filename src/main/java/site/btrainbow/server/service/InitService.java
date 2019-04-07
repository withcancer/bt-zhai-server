package site.btrainbow.server.service;

import site.btrainbow.server.model.InitInfoDo;
import site.btrainbow.server.model.ResponseVo;

public interface InitService {
    ResponseVo initFromCache();

    InitInfoDo init();
}
