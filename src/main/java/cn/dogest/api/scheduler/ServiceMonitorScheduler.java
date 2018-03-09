package cn.dogest.api.scheduler;

import cn.dogest.api.dao.AppInfoMapper;
import cn.dogest.api.dao.InterfaceStatusMapper;
import cn.dogest.api.model.AppInfo;
import cn.dogest.api.model.InterfaceStatus;
import cn.dogest.api.service.ServiceMonitorFactory;
import cn.dogest.api.service.inter.IServiceMonitor;
import cn.dogest.api.utils.Pair;
import cn.dogest.api.utils.SmsUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class ServiceMonitorScheduler {

    @Resource
    AppInfoMapper appInfoMapper;
    @Resource
    InterfaceStatusMapper interfaceStatusMapper;

    @Scheduled(cron = "0 0 */1 * * ?")
    public void run() {
        List<Object> list = ServiceMonitorFactory.getServiceMonitors();
        for(Object obj : list) {
            IServiceMonitor ism = (IServiceMonitor) obj;
            Pair<Integer, String> pair = ism.getStatus();
            Date now = new Date();
            InterfaceStatus newStatus = new InterfaceStatus(ism.getInterfaceName(), pair.getFirst(), pair.getSecond(), now);
            InterfaceStatus oldStatus = interfaceStatusMapper.getLastStatus(ism.getInterfaceName());
            if(null == oldStatus) {
                oldStatus = new InterfaceStatus(ism.getInterfaceName(), 0, "OK", now);
            }
            if(oldStatus.getCode() == newStatus.getCode()) {
                continue;
            }
            interfaceStatusMapper.updateInterfaceStatus(newStatus);
            AppInfo appInfo = appInfoMapper.getAppInfo("sdut_sqs");
            try {
                SmsUtils.sendSmsInterfaceStatusAlert(appInfo.getPhone(), appInfo, ism.getCName(), oldStatus, newStatus);
                break;
            } catch (Exception e) {

            }
        }
    }
}
