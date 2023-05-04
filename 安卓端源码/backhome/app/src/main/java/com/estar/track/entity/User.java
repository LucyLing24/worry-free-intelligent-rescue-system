package com.estar.track.entity;

public class User {

    String id;
    String name;
    int gender;
    int age;
    String phoneNumber;
    String transportationType;
    Boolean canGo;
    String userId;
    String groupId;

    public User(String id, String name, int gender, int age, String phoneNumber, String transportationType, Boolean canGo, String userId, String groupId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.transportationType = transportationType;
        this.canGo = canGo;
        this.userId = userId;
        this.groupId = groupId;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", transportationType=" + transportationType +
                ", canGo=" + canGo +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
    }

    public Boolean getCanGo() {
        return canGo;
    }

    public void setCanGo(Boolean canGo) {
        this.canGo = canGo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
