package cn.dogest.api.model.mail;

import cn.dogest.api.model.AppInfo;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

public final class RegisterMail extends Mail {

    public RegisterMail(AppInfo appInfo, String recv, Map<String, String> params) {
        this.smtpConfigure = new SmtpConfigure(appInfo);
        this.recv = recv;
        this.params = params;
    }

    @Override
    protected String getMimeContent() throws Exception {
        File file = ResourceUtils.getFile("classpath:mail/register.template");
        FileReader fr = new FileReader(file);
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[64];
        int len;
        while((len = fr.read(buffer)) != -1) {
            sb.append(buffer, 0, len);
        }
        String content = sb.toString();
        if(params == null) {
            return content;
        }
        for(String key : params.keySet()) {
            content = content.replaceAll("\\{\\s*\\$" + key + "\\s*\\}", params.get(key));
        }
        return content;
    }

    @Override
    protected String getSubject() {
        return "【SDUT信息查询】邮箱用户注册验证（请勿回复该邮件）";
    }
}
