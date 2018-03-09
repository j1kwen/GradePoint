package cn.dogest.api.service.inter;

import cn.dogest.api.utils.Pair;

public interface IServiceMonitor {

    Pair<Integer, String> getStatus();
    String getInterfaceName();
    String getCName();
}
