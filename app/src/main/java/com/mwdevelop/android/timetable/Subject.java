package com.mwdevelop.android.timetable;

import java.util.UUID;

public class Subject {
    private String name;
    private String lecturer;
    private String room;
    private UUID id;
    private String beginTime;
    private String endTime;

    public Subject(){
        name="";
        lecturer="";
        room="";
        id=UUID.randomUUID();
    }
    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
