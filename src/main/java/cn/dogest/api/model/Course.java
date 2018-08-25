package cn.dogest.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class Course {
	
    private String term; // 学期
    private String type; // 课程类型
    private String name; // 课程名
    private String point; // 学分
    private String test; // 二专标记
    private String origGrade; // 原考成绩
    private String reGrade; // 重考/补考成绩

    /**
     * 构造一个课程对象
     * @param field 长度为8的字符串数组，分别对应学年，学期，类型，课程名，学分，二专标记，原成绩，补考成绩
     * @throws Exception 数组长度过小时抛出异常
     */
    public Course(String[] field) throws Exception {
        this.term = field[0] + "-" + field[1];
        this.type = field[2];
        this.name = field[3];
        this.point = field[4];
        this.test = field[5];
        this.origGrade = field[6];
        this.reGrade = field[7];
    }

    public String getTerm() {
        return term;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPoint() {
        return point;
    }
    @JsonIgnore
    public String getTest() {
        return test;
    }

    public String getOrigGrade() {
        return origGrade;
    }

    public String getReGrade() {
        return reGrade;
    }

    /**
     * 根据课程类型字符串获取课程类型码
     * @return 课程类型码
     */
    @JsonIgnore
    public CourseType getCourseType() {
        if(test.indexOf("二") != -1) {
            return CourseType.MINOR;
        }
        if(type.startsWith("必修")) {
            return CourseType.OBLIGATORY;
        }
        if(type.startsWith("公选") || type.startsWith("选修")) {
            return CourseType.ELECTIVE;
        }
        return CourseType.PRACTICE;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s", this.term, this.name, this.origGrade, this.reGrade);
    }

    /**
     * 将课程成绩字符串转换成double型分值
     * @param gd
     * @return
     */
    private BigDecimal getGradeFromString(String gd) {
        try {
            return new BigDecimal(gd);
        } catch (Exception e) {
            // 转换发生异常，说明成绩为文字，即等级制分数
            if(gd.equals("优秀") || gd.equals("优")) {
                return new BigDecimal("95.0");
            } else if(gd.equals("良好") || gd.equals("良")) {
                return new BigDecimal("84.0");
            } else if(gd.equals("中等") || gd.equals("中")) {
                return new BigDecimal("73.0");
            } else if(gd.equals("及格")) {
                return new BigDecimal("62.0");
            } else if(gd.equals("不及格")) {
                return BigDecimal.ZERO;
            } else if(gd.equals("合格")) {
                return new BigDecimal("70.0");
            } else if(gd.equals("不合格")) {
                return BigDecimal.ZERO;
            } else if(gd.equals("免修")) {
                return new BigDecimal("73.0");
            }
            // 其余情况均为不合格，即0分
            return BigDecimal.ZERO;
        }
    }

    @JsonIgnore
    public BigDecimal getPointValue() {
        return getGradeFromString(this.point);
    }

    /**
     * 获取计算绩点的成绩分值，以重考为准
     * @return 成绩分值
     */
    @JsonIgnore
    public BigDecimal getGradeValue() {
        if(!this.reGrade.trim().equals("")) {
            // 有重考成绩
            return getGradeFromString(reGrade);
        }
        // 无重考成绩即取原成绩
        return getGradeFromString(origGrade);
    }

    /**
     * 获取课程的学分绩点值，即学分*成绩，不及格为0
     * @return 学分绩点值
     */
    @JsonIgnore
    public BigDecimal getProductValue() {
        BigDecimal grade = getGradeValue();
        if(grade.compareTo(new BigDecimal("60")) < 0) {
            grade = BigDecimal.ZERO;
        }
        return getPointValue().multiply(grade);
    }

    /**
     * 重写equal方法，便于对重修课程去重
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        Course o = (Course) obj;
        return (o.getName().equals(this.name) && o.getCourseType() == this.getCourseType());
    }

    /**
     * 对于重修课程应该取最高成绩计算绩点，此方法返回分值较高的对象（自身或实参）
     * @param course 待比较对象
     * @return 成绩较高者
     */
    public Course getReplacedCourse(Course course) {
        if(this.getGradeValue().compareTo(course.getGradeValue()) <= 0) {
            //System.out.println("重修成绩较高，取重修成绩。");
            return course;
        }
        //System.out.println("原成绩较高，舍弃重修成绩。");
        return this;
    }
}
