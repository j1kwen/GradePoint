package cn.dogest.api.dao;

import cn.dogest.api.model.InterfaceStatus;

public interface InterfaceStatusMapper {

    InterfaceStatus getLastStatus(String inter);
    int updateInterfaceStatus(InterfaceStatus status);
}
