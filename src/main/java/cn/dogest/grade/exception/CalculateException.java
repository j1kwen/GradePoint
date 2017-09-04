package cn.dogest.grade.exception;

import cn.dogest.grade.model.StatusCode;

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
