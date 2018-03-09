package cn.dogest.api.model;

import java.util.Date;

public class InterfaceStatus {

    private String name;
    private int code;
    private String status;
    private Date lastUpdate;

    public InterfaceStatus() {
    }

    public InterfaceStatus(String name, int code, String status, Date lastUpdate) {
        this.name = name;
        this.code = code;
        this.status = status;
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
