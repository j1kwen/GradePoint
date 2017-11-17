package cn.dogest.api.dao;

import cn.dogest.api.model.Admin;

/**
 * Created by xiaonan.jia on 2017/11/14.
 */
public interface AdminMapper {

    Admin selectByUser(String user);
}
