package cn.dogest.api.exception;

import cn.dogest.api.model.StatusCode;

/**
 * Created by xiaonan.jia on 2017/8/30.
 */
public class ParseHtmlException extends GradeBaseException {
    public ParseHtmlException(String message) {
        super(message, StatusCode.PARSE_ERR);
    }

    public ParseHtmlException(String message, Throwable cause) {
        super(message, StatusCode.PARSE_ERR, cause);
    }
}
