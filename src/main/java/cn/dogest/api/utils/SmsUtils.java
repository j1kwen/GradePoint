package cn.dogest.api.utils;

import cn.dogest.api.model.AppInfo;
import cn.dogest.api.model.InterfaceStatus;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class SmsUtils {

    public static SmsSingleSenderResult sendSmsInterfaceStatusAlert(String phone, AppInfo appInfo, String cName, InterfaceStatus oldStatus, InterfaceStatus newStatus) throws Exception {
        int templateId = 93358;
        SmsSingleSender sender = new SmsSingleSender(Integer.parseInt(appInfo.getAppid()), appInfo.getAppkey());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateF = sdf.format(newStatus.getLastUpdate());
        ArrayList<String> params = new ArrayList<>();
        params.add(newStatus.getCode() == 0 ? "恢复" : "异常");
        params.add(newStatus.getName());
        params.add(newStatus.getStatus());
        params.add(dateF);
        params.add(oldStatus.getStatus());
        params.add(dateDifferString(oldStatus.getLastUpdate(), newStatus.getLastUpdate()));
        SmsSingleSenderResult result = sender.sendWithParam("86", phone, templateId, params, cName, "", "");

        return result;
    }

    private static String dateDifferString(Date oldDate, Date newDate) {
        StringBuilder sb = new StringBuilder();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = newDate.getTime() - oldDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        if(day > 0) {
            sb.append(day + "天");
        }
        // 计算差多少小时
        long hour = diff % nd / nh;
        if(day > 0 || hour > 0) {
            sb.append(hour + "小时");
        }
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        if(day > 0 || hour > 0 || min > 0) {
            sb.append(min + "分");
        }
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        sb.append(sec + "秒");
        return  sb.toString();
    }
}
