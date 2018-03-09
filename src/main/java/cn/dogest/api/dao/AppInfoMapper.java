package cn.dogest.api.dao;

import cn.dogest.api.model.AppInfo;

public interface AppInfoMapper {
    AppInfo getAppInfo(String code);
}
