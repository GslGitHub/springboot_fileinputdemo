package com.filedemo.dto;public class ChanpinDTO {    private Integer id;    private String chanpinName;    private Integer busId;    private String filePath;    private String fileName;    private String scrq;    public String getFileName() {        return fileName;    }    public void setFileName(String fileName) {        this.fileName = fileName;    }    private String busName;    public String getBusName() {        return busName;    }    public void setBusName(String busName) {        this.busName = busName;    }    public Integer getId() {        return id;    }    public void setId(Integer id) {        this.id = id;    }    public String getChanpinName() {        return chanpinName;    }    public void setChanpinName(String chanpinName) {        this.chanpinName = chanpinName;    }    public Integer getBusId() {        return busId;    }    public void setBusId(Integer busId) {        this.busId = busId;    }    public String getFilePath() {        return filePath;    }    public void setFilePath(String filePath) {        this.filePath = filePath;    }    public String getScrq() {        return scrq;    }    public void setScrq(String scrq) {        this.scrq = scrq;    }}