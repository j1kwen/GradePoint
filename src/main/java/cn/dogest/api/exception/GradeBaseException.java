package cn.dogest.api.exception;

import cn.dogest.api.model.StatusCode;

/**
 * 异常基类，包含错误信息以及状态码
 * Created by xiaonan.jia on 2017/9/4.
 */
public class GradeBaseException extends Exception {

    protected StatusCode statusCode;

    public GradeBaseException(String message, StatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public GradeBaseException(String message, StatusCode statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    /**
     * 获取异常状态码
     * @return 状态码
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }
}
