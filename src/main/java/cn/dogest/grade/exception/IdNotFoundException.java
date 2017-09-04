package cn.dogest.grade.exception;

import cn.dogest.grade.model.StatusCode;

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
