package site.btrainbow.server.utils;

import site.btrainbow.server.model.ResponseVo;

public class ResultUtil {
    /**
     * 请求成功返回
     *
     * @param object 成功提示内容
     * @return 信息完整内容
     */
    @SuppressWarnings("unchecked")
    public static ResponseVo success(Object object) {
        ResponseVo res = new ResponseVo();
        res.setCode(200);
        res.setMsg("Success");
        res.setData(object);
        return res;
    }

    /**
     * 请求成功返回（分页）
     *
     * @param object 成功提示内容
     * @param total  数据总数
     * @return 信息完整内容
     */
    public static ResponseVo success(Object object, Long total) {
        ResponseVo res = success(object);
        res.setTotal(total);
        return res;
    }

    /**
     * 请求成功返回（分页+用时）
     *
     * @param object 成功提示内容
     * @param total  数据总数
     * @param took   用时
     * @return 信息完整内容
     */
    public static ResponseVo success(Object object, Long total, Long took) {
        ResponseVo res = success(object);
        res.setTotal(total);
        res.setTook(took);
        return res;
    }

    /**
     * 请求成功返回(返回空值)
     *
     * @return 信息完整内容
     */
    public static ResponseVo success() {
        return success(null);
    }

    /**
     * 请求错误返回
     *
     * @param code          失败code
     * @param resultMessage 失败提示内容
     * @return 信息完整内容
     */
    public static ResponseVo error(Integer code, String resultMessage) {
        ResponseVo res = new ResponseVo();
        res.setCode(code);
        res.setMsg(resultMessage);
        return res;
    }
}
