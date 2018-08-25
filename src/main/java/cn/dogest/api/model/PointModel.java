package cn.dogest.api.model;

import java.math.BigDecimal;

/**
 * 成绩、学分模型
 * Created by xiaonan.jia on 2017/9/1.
 */
public class PointModel {
    private BigDecimal grade = BigDecimal.ZERO; // 绩点
    private BigDecimal point = BigDecimal.ZERO; // 已修学分(除公选课）
    private BigDecimal gradeTotal = BigDecimal.ZERO; // 总绩点
    private BigDecimal pointTotal = BigDecimal.ZERO; // 总学分（除公选课）
    private BigDecimal elective = BigDecimal.ZERO; // 公选课学分

    public PointModel() {
    }

    public PointModel(BigDecimal grade, BigDecimal point, BigDecimal gradeTotal, BigDecimal pointTotal, BigDecimal elective) {
        this.grade = grade;
        this.point = point;
        this.gradeTotal = gradeTotal;
        this.pointTotal = pointTotal;
        this.elective = elective;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public BigDecimal getPointTotal() {
        return pointTotal;
    }

    public BigDecimal getElective() {
        return elective;
    }

    public BigDecimal getGradeTotal() {
        return gradeTotal;
    }
}
