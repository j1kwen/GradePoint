package cn.dogest.grade.exception;

import cn.dogest.grade.model.StatusCode;

/**
 * Created by xiaonan.jia on 2017/8/30.
 */
public class ConnectionException extends GradeBaseException {
    public ConnectionException(String message) {
        super(message, StatusCode.CONNECT_ERR);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, StatusCode.CONNECT_ERR, cause);
    }
}
