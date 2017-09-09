package cn.dogest.api.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class BuildingMapper {

    private static Map<String, String[]> map = null;
    static {
        map = new HashMap<>();
        map.put("d1h", new String[]{"E01#", "东区1号公寓"});
        map.put("d2h", new String[]{"E02#", "东区2号公寓"});
        map.put("d4h", new String[]{"E04#", "东区4号公寓"});
        map.put("d6h", new String[]{"E06#", "东区6号公寓"});
        map.put("d8h", new String[]{"E08#", "东区8号公寓"});
        map.put("d9h", new String[]{"E09#", "东区9号公寓"});
        map.put("d10h", new String[]{"E10#", "东区10号公寓"});
        map.put("1nh", new String[]{"01#南", "1号公寓南楼"});
        map.put("1bh", new String[]{"01#北", "1号公寓北楼"});
        map.put("2nh", new String[]{"02#南", "2号公寓南楼"});
        map.put("2bh", new String[]{"02#北", "2号公寓北楼"});
        map.put("3nh", new String[]{"03#南", "3号公寓南楼"});
        map.put("3bh", new String[]{"03#北", "3号公寓北楼"});
        map.put("4nh", new String[]{"04#南", "4号公寓南楼"});
        map.put("4bh", new String[]{"04#北", "4号公寓北楼"});
        map.put("5h", new String[]{"05#", "5号公寓"});
        map.put("6h", new String[]{"06#", "6号公寓"});
        map.put("7h", new String[]{"07#", "7号公寓"});
        map.put("8h", new String[]{"08#", "8号公寓"});
        map.put("9h", new String[]{"09#", "9号公寓"});
        map.put("10h", new String[]{"10#", "10号公寓"});
        map.put("11h", new String[]{"11#", "11号公寓"});
        map.put("12h", new String[]{"12#", "12号公寓"});
        map.put("13nh", new String[]{"13#南", "13号公寓南楼"});
        map.put("13bh", new String[]{"13#北", "13号公寓北楼"});
        map.put("14h", new String[]{"14#", "14号公寓"});
        map.put("15h", new String[]{"15#", "15号公寓"});
        map.put("16h", new String[]{"16#", "16号公寓"});
        map.put("17h", new String[]{"17#", "17号公寓"});
        map.put("18h", new String[]{"18#", "18号公寓"});
        map.put("19h", new String[]{"19#", "19号公寓"});
        map.put("20h", new String[]{"20#", "20号公寓"});
        map.put("21h", new String[]{"21#", "21号公寓"});
        map.put("22h", new String[]{"22#", "22号公寓"});
        map.put("y1h", new String[]{"研女#", "研究生公寓南楼"});
        map.put("y2h", new String[]{"研男#", "研究生公寓北楼"});
    }

    public static String[] getMappingParam(String room) throws UnsupportedEncodingException {
        room = room.toLowerCase();
        int idx = room.indexOf("h");
        String key = room.substring(0, idx + 1);
        String no = room.substring(idx + 1);
        if(map.containsKey(key)) {
            String[] rInfo = map.get(key);
            String para = URLEncoder.encode(rInfo[0].concat(no), "UTF-8");
            return new String[] {para, rInfo[1].concat(no)};
        }
        return null;
    }
}
