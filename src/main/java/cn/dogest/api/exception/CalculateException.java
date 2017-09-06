package cn.dogest.api.exception;

import cn.dogest.api.model.StatusCode;

/**
 * Created by xiaonan.jia on 2017/8/30.
 */
public class CalculateException extends GradeBaseException {
    public CalculateException(String message) {
        super(message, StatusCode.CALC_ERR);
    }

    public CalculateException(String message, Throwable cause) {
        super(message, StatusCode.CALC_ERR, cause);
    }
}
