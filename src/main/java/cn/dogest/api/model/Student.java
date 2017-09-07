package cn.dogest.api.model;

/**
 * 学生模型
 * Created by xiaonan.jia on 2017/8/29.
 */
public class Student {

    private String id = null; // 学号
    private String name = null; // 姓名
    private String gender = null; // 性别
    private String grade = null; // 年级
    private String college = null; // 学院
    private String major = null; // 专业
    private String cls = null; // 班级
    private String level = null; // 层次（专科、本科、硕士、博士）
    private String stm = null; // 学制

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStm() {
        return stm;
    }

    public void setStm(String stm) {
        this.stm = stm;
    }

    /**
     * 检查学生信息是否完整
     * @return
     */
    public boolean checkOK() {
        if(id == null) {
            return false;
        }
        if(name == null) {
            return false;
        }
        if(gender == null) {
            return false;
        }
        if(grade == null) {
            return false;
        }
        if(college == null) {
            return false;
        }
        if(major == null) {
            return false;
        }
        if(cls == null) {
            return false;
        }
        if(level == null) {
            return false;
        }
        if(stm == null) {
            return false;
        }
        return true;
    }
}
