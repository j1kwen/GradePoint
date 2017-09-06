package cn.dogest.api.exception;

import cn.dogest.api.model.StatusCode;

/**
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

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
