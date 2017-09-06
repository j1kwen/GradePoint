package cn.dogest.api.exception;

import cn.dogest.api.model.StatusCode;

/**
 * Created by xiaonan.jia on 2017/8/30.
 */
public class IdNotFoundException extends GradeBaseException {
    public IdNotFoundException(String message) {
        super(message, StatusCode.ID_ERR);
    }

    public IdNotFoundException(String message, Throwable cause) {
        super(message, StatusCode.ID_ERR, cause);
    }
}
