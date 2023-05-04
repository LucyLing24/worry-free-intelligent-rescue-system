package com.estar.track.entity;

import android.content.Intent;

import java.util.Date;
import java.util.List;

public class Task {
    String id;
    String name;
    String code;
    Integer status;
    Integer memberNum;
    String level;
    Integer currNum;
    Date createTime;
    String elderlyId;
    String elderlyName;
    Integer elderlyGender;
    Integer elderlyAge;
    Double elderlyHeight;
    Date elderlyLostTime;
    String elderlyProvince;
    String elderlyCity;
    String elderlyArea;
    String elderlyLostAddress;
    Double elderlyLostLng;
    Double elderlyLostLat;
    Boolean elderlyMentalMedicalHistory;
    String elderlyLook;
    String elderlyJob;
    String elderlyIdCard;
    String elderlyDescription;
    Date elderlyCreateTime;
    Date elderlyUpdateTime;
    String elderlyRemark;
    String lostReasonName;
    List<User> members;




    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", memberNum='" + memberNum + '\'' +
                ", level='" + level + '\'' +
                ", currNum='" + currNum + '\'' +
                ", createTime=" + createTime +
                ", elderlyId='" + elderlyId + '\'' +
                ", elderlyName='" + elderlyName + '\'' +
                ", elderlyGender=" + elderlyGender +
                ", elderlyAge=" + elderlyAge +
                ", elderlyHeight=" + elderlyHeight +
                ", elderlyLostTime=" + elderlyLostTime +
                ", elderlyProvince='" + elderlyProvince + '\'' +
                ", elderlyCity='" + elderlyCity + '\'' +
                ", elderlyArea='" + elderlyArea + '\'' +
                ", elderlyLostAddress='" + elderlyLostAddress + '\'' +
                ", elderlyLostLng=" + elderlyLostLng +
                ", elderlyLostLat=" + elderlyLostLat +
                ", elderlyMentalMedicalHistory=" + elderlyMentalMedicalHistory +
                ", elderlyLook='" + elderlyLook + '\'' +
                ", elderlyJob='" + elderlyJob + '\'' +
                ", elderlyIdCard='" + elderlyIdCard + '\'' +
                ", elderlyDescription='" + elderlyDescription + '\'' +
                ", elderlyCreateTime=" + elderlyCreateTime +
                ", elderlyUpdateTime=" + elderlyUpdateTime +
                ", elderlyRemark='" + elderlyRemark + '\'' +
                ", lostReasonName='" + lostReasonName + '\'' +
                ", members=" + members +
                '}';
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getCurrNum() {
        return currNum;
    }

    public void setCurrNum(Integer currNum) {
        this.currNum = currNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(String elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getElderlyName() {
        return elderlyName;
    }

    public void setElderlyName(String elderlyName) {
        this.elderlyName = elderlyName;
    }

    public Integer getElderlyGender() {
        return elderlyGender;
    }

    public void setElderlyGender(Integer elderlyGender) {
        this.elderlyGender = elderlyGender;
    }

    public Integer getElderlyAge() {
        return elderlyAge;
    }

    public void setElderlyAge(Integer elderlyAge) {
        this.elderlyAge = elderlyAge;
    }

    public Double getElderlyHeight() {
        return elderlyHeight;
    }

    public void setElderlyHeight(Double elderlyHeight) {
        this.elderlyHeight = elderlyHeight;
    }

    public Date getElderlyLostTime() {
        return elderlyLostTime;
    }

    public void setElderlyLostTime(Date elderlyLostTime) {
        this.elderlyLostTime = elderlyLostTime;
    }

    public String getElderlyProvince() {
        return elderlyProvince;
    }

    public void setElderlyProvince(String elderlyProvince) {
        this.elderlyProvince = elderlyProvince;
    }

    public String getElderlyCity() {
        return elderlyCity;
    }

    public void setElderlyCity(String elderlyCity) {
        this.elderlyCity = elderlyCity;
    }

    public String getElderlyArea() {
        return elderlyArea;
    }

    public void setElderlyArea(String elderlyArea) {
        this.elderlyArea = elderlyArea;
    }

    public String getElderlyLostAddress() {
        return elderlyLostAddress;
    }

    public void setElderlyLostAddress(String elderlyLostAddress) {
        this.elderlyLostAddress = elderlyLostAddress;
    }

    public Double getElderlyLostLng() {
        return elderlyLostLng;
    }

    public void setElderlyLostLng(Double elderlyLostLng) {
        this.elderlyLostLng = elderlyLostLng;
    }

    public Double getElderlyLostLat() {
        return elderlyLostLat;
    }

    public void setElderlyLostLat(Double elderlyLostLat) {
        this.elderlyLostLat = elderlyLostLat;
    }

    public Boolean getElderlyMentalMedicalHistory() {
        return elderlyMentalMedicalHistory;
    }

    public void setElderlyMentalMedicalHistory(Boolean elderlyMentalMedicalHistory) {
        this.elderlyMentalMedicalHistory = elderlyMentalMedicalHistory;
    }

    public String getElderlyLook() {
        return elderlyLook;
    }

    public void setElderlyLook(String elderlyLook) {
        this.elderlyLook = elderlyLook;
    }

    public String getElderlyJob() {
        return elderlyJob;
    }

    public void setElderlyJob(String elderlyJob) {
        this.elderlyJob = elderlyJob;
    }

    public String getElderlyIdCard() {
        return elderlyIdCard;
    }

    public void setElderlyIdCard(String elderlyIdCard) {
        this.elderlyIdCard = elderlyIdCard;
    }

    public String getElderlyDescription() {
        return elderlyDescription;
    }

    public void setElderlyDescription(String elderlyDescription) {
        this.elderlyDescription = elderlyDescription;
    }

    public Date getElderlyCreateTime() {
        return elderlyCreateTime;
    }

    public void setElderlyCreateTime(Date elderlyCreateTime) {
        this.elderlyCreateTime = elderlyCreateTime;
    }

    public Date getElderlyUpdateTime() {
        return elderlyUpdateTime;
    }

    public void setElderlyUpdateTime(Date elderlyUpdateTime) {
        this.elderlyUpdateTime = elderlyUpdateTime;
    }

    public String getElderlyRemark() {
        return elderlyRemark;
    }

    public void setElderlyRemark(String elderlyRemark) {
        this.elderlyRemark = elderlyRemark;
    }

    public String getLostReasonName() {
        return lostReasonName;
    }

    public void setLostReasonName(String lostReasonName) {
        this.lostReasonName = lostReasonName;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public Task() {


    }

    public Task(String id, String name, String code, Integer status, Integer memberNum, String level, Integer currNum, Date createTime, String elderlyId, String elderlyName, Integer elderlyGender, Integer elderlyAge, Double elderlyHeight, Date elderlyLostTime, String elderlyProvince, String elderlyCity, String elderlyArea, String elderlyLostAddress, Double elderlyLostLng, Double elderlyLostLat, Boolean elderlyMentalMedicalHistory, String elderlyLook, String elderlyJob, String elderlyIdCard, String elderlyDescription, Date elderlyCreateTime, Date elderlyUpdateTime, String elderlyRemark, String lostReasonName, List<User> members) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.status = status;
        this.memberNum = memberNum;
        this.level = level;
        this.currNum = currNum;
        this.createTime = createTime;
        this.elderlyId = elderlyId;
        this.elderlyName = elderlyName;
        this.elderlyGender = elderlyGender;
        this.elderlyAge = elderlyAge;
        this.elderlyHeight = elderlyHeight;
        this.elderlyLostTime = elderlyLostTime;
        this.elderlyProvince = elderlyProvince;
        this.elderlyCity = elderlyCity;
        this.elderlyArea = elderlyArea;
        this.elderlyLostAddress = elderlyLostAddress;
        this.elderlyLostLng = elderlyLostLng;
        this.elderlyLostLat = elderlyLostLat;
        this.elderlyMentalMedicalHistory = elderlyMentalMedicalHistory;
        this.elderlyLook = elderlyLook;
        this.elderlyJob = elderlyJob;
        this.elderlyIdCard = elderlyIdCard;
        this.elderlyDescription = elderlyDescription;
        this.elderlyCreateTime = elderlyCreateTime;
        this.elderlyUpdateTime = elderlyUpdateTime;
        this.elderlyRemark = elderlyRemark;
        this.lostReasonName = lostReasonName;
        this.members = members;
    }
}
