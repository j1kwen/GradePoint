package cn.dogest.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Course {
	
	private String term;
	private String type;
	private String name;
	private String point;
	private String test;
	private String origGrade;
	private String reGrade;
	
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
	
	private double getGradeFromString(String gd) {
		try {
			return Float.parseFloat(gd);
		} catch (Exception e) {
			if(gd.equals("优秀") || gd.equals("优")) {
				return 95.0;
			} else if(gd.equals("良好") || gd.equals("良")) {
				return 84.0;
			} else if(gd.equals("中等") || gd.equals("中")) {
				return 73.0;
			} else if(gd.equals("及格")) {
				return 62.0;
			} else if(gd.equals("不及格")) {
				return 0.0;
			} else if(gd.equals("合格")) {
				return 70.0;
			} else if(gd.equals("不合格")) {
				return 0.0;
			} else if(gd.equals("免修")) {
				return 73.0;
			}
			return 0.0;
		}
	}
	@JsonIgnore
	public double getPointValue() {
		return getGradeFromString(this.point);
	}
	@JsonIgnore
	public double getGradeValue() {
		if(!this.reGrade.trim().equals("")) {
			// 有重考成绩
			return getGradeFromString(reGrade);
		}
		return getGradeFromString(origGrade);
	}
	@JsonIgnore
	public double getProductValue() {
		double grade = getGradeValue();
		if(grade < 60.0) {
			grade = 0.0;
		}
		return getPointValue() * grade;
	}

	@Override
	public boolean equals(Object obj) {
		Course o = (Course) obj;
		return (o.getName().equals(this.name) && o.getCourseType() == this.getCourseType());
	}

	public Course getReplacedCourse(Course course) {
		if(this.getGradeValue() <= course.getGradeValue()) {
			//System.out.println("重修成绩较高，取重修成绩。");
			return course;
		}
		//System.out.println("原成绩较高，舍弃重修成绩。");
		return this;
	}
}
