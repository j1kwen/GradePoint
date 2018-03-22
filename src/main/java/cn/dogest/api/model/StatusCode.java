package cn.dogest.api.model;

/**
 * 状态码
 * Created by xiaonan.jia on 2017/8/30.
 */
public enum StatusCode {
    OK, // 成功
    ID_ERR, // 学号错误
    CONNECT_ERR, // 连接错误
    CALC_ERR, // 计算错误
    PARSE_ERR, // 解析错误
    EMAIL_ERR, // email错误
    SYS_ERR, // 系统错误
    RID_ERR, // 凭据错误
    EXP_ERR, // 凭据过期
    TOKEN_ERR, // token非法
    USER_EXIST, // 用户已存在
}
