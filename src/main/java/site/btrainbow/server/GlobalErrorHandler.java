package site.btrainbow.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import site.btrainbow.server.model.ResponseVo;
import site.btrainbow.server.utils.ResultUtil;

@ControllerAdvice
@ResponseBody
public class GlobalErrorHandler {
    @ExceptionHandler({Exception.class})
    public ResponseVo exceptionHandler(Exception e) {
        // 全都按照系统级错误500处理返回
        return ResultUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
