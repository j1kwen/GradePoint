package cn.dogest.api.utils;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import cn.dogest.api.model.Student;
import org.springframework.util.ResourceUtils;

/**
 * 测试主类
 */
public class Main {

    public static void main(String[] args) throws Exception {
        File file = ResourceUtils.getFile("classpath:mail/register.template");
        FileReader fr = new FileReader(file);
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[64];
        int len;
        while((len = fr.read(buffer)) != -1) {
            sb.append(buffer, 0, len);
        }
        String content = sb.toString();
        content = content.replaceAll("\\{\\s*\\$link\\s*\\}", "123");
        System.out.println(content);
    }

}
