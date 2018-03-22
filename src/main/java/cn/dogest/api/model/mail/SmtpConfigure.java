package cn.dogest.api.model.mail;

import cn.dogest.api.model.AppInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SmtpConfigure {
    private String server;
    private String port;
    private String user;
    private String password;
    private String name;

    private AppInfo appInfo;

    public SmtpConfigure(AppInfo appInfo) {
        this.appInfo = appInfo;

        server = this.getProperty("mail.smtp.srv");
        port = this.getProperty("mail.smtp.port");

        user = this.getProperty("mail.account.user");
        password = this.getProperty("mail.account.pwd");
        name = this.getProperty("mail.account.name");
    }

    private String getProperty(String key) {
        try {
            String fileContext = appInfo.getMailConf();
            key = key.replaceAll("\\.", "\\.");
            Pattern pattern = Pattern.compile("(?<=" + key + "=).*");
            Matcher matcher = pattern.matcher(fileContext);
            if(matcher.find()) {
                return matcher.group();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
