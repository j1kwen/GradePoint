package cn.dogest.api.model;

public class AppInfo {
    private int id;
    private String code;
    private String name;
    private String appid;
    private String appkey;
    private String phone;
    private String mailConf;
    private String extra;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMailConf() {
        return mailConf;
    }

    public void setMailConf(String mailConf) {
        this.mailConf = mailConf;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "App Name: " + this.name + "\n"
                + "App Id: " + this.appid + "\n"
                + "App Key: " + this.appkey;
    }


}
