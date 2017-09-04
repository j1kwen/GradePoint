package cn.dogest.grade.service;

import cn.dogest.grade.exception.*;
import cn.dogest.grade.model.ResultModel;
import cn.dogest.grade.model.StatusCode;
import cn.dogest.grade.model.Student;
import cn.dogest.grade.utils.GradeCalculator;
import cn.dogest.grade.utils.HtmlConverter;
import cn.dogest.grade.utils.NetworkProcesser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaonan.jia on 2017/8/29.
 */
@Service
public class GradeQueryService {

    public Map<String, Object> getGradePoint(String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            if(id == null || id.trim().equals("")) {
                result.put("message", "参数[id]不能为空！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            HtmlConverter converter = new HtmlConverter(new NetworkProcesser(id));
            GradeCalculator calculator = new GradeCalculator(converter.getCourseList());
            Student student = converter.getStudentInfo();

            Map<String, Object> data = new HashMap<>();

            ResultModel major = new ResultModel(
                    calculator.getMajorGradePoint(),
                    calculator.getMajorPointPassed(),
                    calculator.getMajorPointTotal(),
                    calculator.getMajorElective(),
                    calculator.getMajorCourses()
            );
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
            result.put("message", e.getMessage());
            result.put("status", e.getStatusCode());
            result.put("code", e.getStatusCode().ordinal());
        }
        return result;
    }

    public Map<String, Object> getStudentInfo(String id, String name, String pageStr, String sizeStr) {
        Map<String, Object> result = new HashMap<>();
        try {
            if((id == null || id.trim().equals("")) && (name == null || name.trim().equals(""))) {
                result.put("message", "参数[id]和[name]不能同时为空！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            int page = 1;
            if(!(pageStr.matches("[1-9]\\d*") && (page = Integer.parseInt(pageStr)) < 100000)) {
                // 不是正整数或者超过10W
                result.put("message", "参数[page]格式错误，请输入[1,100000)区间内的正整数！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            int size = 20;
            if(!(sizeStr.matches("[1-9]\\d*") && (size = Integer.parseInt(sizeStr)) <= 200)) {
                // 不是正整数或者超过10W
                result.put("message", "参数[size]格式错误，请输入[1,200]区间内的正整数！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            HtmlConverter htmlConverter = new HtmlConverter(new NetworkProcesser(id, name));
            List<Student> list = htmlConverter.getStudentList();

            int fIndex = (page - 1) * size;
            int eIndex = Math.min(fIndex + size, list.size());

            if(fIndex >= list.size()) {
                result.put("message", "请求的部分数据超出了数据总长度！请检查[page]和[size]的大小！");
                result.put("status", "PARAM_ERR");
                result.put("code", -1);
                return result;
            }
            Map<String, Object> data = new HashMap<>();
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
            result.put("message", "参数[page]或[size]格式错误，请输入正确区间内的正整数！");
            result.put("status", "PARAM_ERR");
            result.put("code", -1);
        }
        return result;
    }
}
