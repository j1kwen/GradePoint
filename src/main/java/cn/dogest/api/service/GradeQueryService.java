package cn.dogest.api.service;

import cn.dogest.api.exception.*;
import cn.dogest.api.model.ResultModel;
import cn.dogest.api.model.StatusCode;
import cn.dogest.api.model.Student;
import cn.dogest.api.service.inter.IServiceMonitor;
import cn.dogest.api.utils.GradeCalculator;
import cn.dogest.api.utils.HtmlConverter;
import cn.dogest.api.utils.NetworkProcesser;
import cn.dogest.api.utils.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绩点、学生信息服务类
 * Created by xiaonan.jia on 2017/8/29.
 */
@Service
public class GradeQueryService implements IServiceMonitor {

    /**
     * 获取绩点及成绩信息
     * @param id 学号
     * @return
     */
    public Map<String, Object> getGradePoint(String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查必填参数
            if(id == null || id.trim().equals("")) {
                result.put("message", "参数[id]不能为空！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            // 根据id爬取html内容获取Converter
            HtmlConverter converter = new HtmlConverter(new NetworkProcesser(id));
            // 根据成绩表获取Calculator
            GradeCalculator calculator = new GradeCalculator(converter.getCourseList());
            // 直接从Converter里获取学生信息
            Student student = converter.getStudentInfo();

            Map<String, Object> data = new HashMap<>();

            // 构造主修专业返回数据模型
            ResultModel major = new ResultModel(
                    calculator.getMajorGradePoint(),
                    calculator.getMajorPointPassed(),
                    calculator.getMajorPointTotal(),
                    calculator.getMajorElective(),
                    calculator.getMajorCourses()
            );
            // 构造辅修专业返回数据模型
            ResultModel minor = new ResultModel(
                    calculator.getMinorGradePoint(),
                    calculator.getMinorPointPassed(),
                    calculator.getMinorPointTotal(),
                    calculator.getMinorElective(),
                    calculator.getMinorCourses()
            );

            data.put("student", student);
            data.put("major", major);
            data.put("minor", minor);

            result.put("data", data);
            result.put("message", "success");
            result.put("status", StatusCode.OK);
            result.put("code", StatusCode.OK.ordinal());
        } catch (GradeBaseException e) {
            // 发生异常返回异常状态
            result.put("message", e.getMessage());
            result.put("status", e.getStatusCode());
            result.put("code", e.getStatusCode().ordinal());
        }
        return result;
    }

    /**
     * 获取学生个人信息（模糊查询）
     * @param id 学号
     * @param name 姓名
     * @param pageStr 页号
     * @param sizeStr 每页条目数
     * @return
     */
    public Map<String, Object> getStudentInfo(String id, String name, String pageStr, String sizeStr) {
        Map<String, Object> result = new HashMap<>();
        try {
            if((id == null || id.trim().equals("")) && (name == null || name.trim().equals(""))) {
                // 两查询字段不可同时为空
                result.put("message", "参数[id]和[name]不能同时为空！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            int page = 1;
            if(!(pageStr.matches("[1-9]\\d*") && (page = Integer.parseInt(pageStr)) < 100000)) {
                // 页号不是正整数或者超过10W
                result.put("message", "参数[page]格式错误，请输入[1,100000)区间内的正整数！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            int size = 20;
            if(!(sizeStr.matches("[1-9]\\d*") && (size = Integer.parseInt(sizeStr)) <= 200)) {
                // 每页条目数不是正整数或者超过200
                result.put("message", "参数[size]格式错误，请输入[1,200]区间内的正整数！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            try {
                name = URLEncoder.encode(name, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // nothing
            }
            // 爬取html构造Converter
            HtmlConverter htmlConverter = new HtmlConverter(new NetworkProcesser(id, name));
            // 获取学生信息表
            List<Student> list = htmlConverter.getStudentList();

            // 获取指定的条目索引范围
            int fIndex = (page - 1) * size;
            int eIndex = Math.min(fIndex + size, list.size());

            if(fIndex > list.size()) {
                // 请求的起始索引超过总条目数
                result.put("message", "请求的部分数据超出了数据总长度！请检查[page]和[size]的大小！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            Map<String, Object> data = new HashMap<>();

            // 获取列表指定索引范围的子集
            List<Student> subList = list.subList(fIndex, eIndex);

            data.put("page", page);
            data.put("size", size);
            data.put("total", list.size());
            data.put("current",subList.size());
            data.put("list", subList);

            result.put("data", data);
            result.put("message", "success");
            result.put("status", StatusCode.OK);
            result.put("code", StatusCode.OK.ordinal());
        } catch (GradeBaseException e) {
            result.put("message", e.getMessage());
            result.put("status", e.getStatusCode());
            result.put("code", e.getStatusCode().ordinal());
        } catch (NumberFormatException e) {
            // 产生此异常说明参数非合法的数字
            result.put("message", "参数[page]或[size]格式错误，请输入正确区间内的正整数！");
            result.put("status", "PARAM_ERR");
            result.put("code", -1);
        }
        return result;
    }

    @Override
    public Pair<Integer, String> getStatus() {
        String commonId = "14110572003";
        Map<String, Object> ret = this.getGradePoint(commonId);
        return new Pair<>(Integer.parseInt(ret.get("code").toString()), ret.get("status").toString());
    }

    @Override
    public String getInterfaceName() {
        return "Grade";
    }

    @Override
    public String getCName() {
        return "绩点查询";
    }
}
