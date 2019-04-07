package site.btrainbow.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.btrainbow.server.model.ResponseVo;
import site.btrainbow.server.service.InitService;

@RestController
public class InitController {

    @Autowired
    private InitService initService;

    @GetMapping("/init")
    public ResponseVo init() {
        return initService.initFromCache();
    }
}
