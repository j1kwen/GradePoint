package cn.dogest.grade.utils;

import java.util.List;

import cn.dogest.grade.model.Course;
import cn.dogest.grade.model.Student;

public class Main {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        HtmlConverter htmlConverter = new HtmlConverter(new NetworkProcesser("1411057200",""));
        List<Student> list = htmlConverter.getStudentList();
		for(Student student : list) {
			System.out.println(student.getName());
		}
    }

}
