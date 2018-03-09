package cn.dogest.api.service;

import cn.dogest.api.exception.ConnectionException;
import cn.dogest.api.exception.GradeBaseException;
import cn.dogest.api.model.StatusCode;
import cn.dogest.api.service.inter.IServiceMonitor;
import cn.dogest.api.utils.BuildingMapper;
import cn.dogest.api.utils.HttpRequest;
import cn.dogest.api.utils.Pair;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 能源服务类
 * Created by xiaonan.jia on 2017/9/8.
 */
@Service
public class EnergyService implements IServiceMonitor {

    private static final String url1 = "http://lgny.sdut.edu.cn/matrixlambdasupport/lightingbinding.grace";
    private static final String url2 = "http://lgny.sdut.edu.cn/matrixlambdasupport/lightingshow.grace";
    private static final String param1 = "arg0=%s&arg1=%s";
    private static final String param2 = "arg=%s";

    /**
     * 获取能源信息
     * @param room
     * @param id
     * @return
     */
    public Map<String, Object> getEnergy(String room, String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            if((id == null || id.trim().equals("")) || (room == null || room.trim().equals(""))) {
                // 参数不可为空
                result.put("message", "参数[id]和[room]均为必需！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            // 请求第一个接口，获取房间id与学生信息
            String[] roomParas = BuildingMapper.getMappingParam(room);
            if(roomParas == null) {
                // 找不到房间的参数映射
                result.put("message", "房间号有误，请检查后重试！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            String param = String.format(param1, roomParas[0], id);
            String html = HttpRequest.sendGet(url1, param);
            JSONObject json = JSONObject.fromObject(html);
            // 如果房间号为NoRoom，说明房间号有误，若抛出异常，则参数格式错误
            if(json.get("roomId").equals("NoRoom")) {
                result.put("message", "房间号有误，没有该房间！");
                result.put("status", "ROOM_ERR");
                result.put("code", "-3");
                return result;
            }
            // 获取roomId
            String roomId  = String.valueOf(json.get("roomId"));
            // 根据取到的room id请求第二个接口的数据
            param = String.format(param2, roomId);
            String html2 = HttpRequest.sendGet(url2, param);
            // 根据接口返回的数据生成json对象
            JSONObject json2 = JSONObject.fromObject(html2);
            // 将第一个接口返回的个人信息数据添加到json2中
            json2.put("userId", json.get("userId"));
            json2.put("userName", json.get("userName"));
            // 替换房间名称
            json2.put("roomname", roomParas[1]);

            // 将数据放进返回值中
            result.put("data", json2);
            result.put("message", "success");
            result.put("status", StatusCode.OK);
            result.put("code", StatusCode.OK.ordinal());
        } catch (ConnectionException e) {
            result.put("message", e.getMessage() + "可能是学号错误导致源服务器连接异常！");
            result.put("status", e.getStatusCode());
            result.put("code", e.getStatusCode().ordinal());
        } catch (UnsupportedEncodingException e) {
            result.put("message", "服务器内部异常，请稍后重试！");
            result.put("status", "SRV_ERR");
            result.put("code", "-2");
        } catch (JSONException e) {
            // 抛出此异常说明源服务器返回数据非json，则极有可能参数异常
            result.put("message", "源服务器获取数据失败，请检查参数是否正确！");
            result.put("status", "PARAM_ERR");
            result.put("code", "-1");
        }

        return result;
    }

    @Override
    public Pair<Integer, String> getStatus() {
        String commonId = "14110572003";
        String testRoom = "6h429";
        Map<String, Object> ret = this.getEnergy(testRoom, commonId);
        return new Pair<>(Integer.parseInt(ret.get("code").toString()), ret.get("status").toString());
    }

    @Override
    public String getInterfaceName() {
        return "Energy";
    }

    @Override
    public String getCName() {
        return "用电查询";
    }
}
