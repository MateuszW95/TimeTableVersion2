package com.mwdevelop.android.timetable;

import java.util.ArrayList;
import java.util.UUID;

public class Day {
    private String name;
    private ArrayList<Subject> subjectArrayList;
    private UUID id;

    public Day(){
        subjectArrayList=new ArrayList<>();
        for(int i=0;i<12;++i){
            Subject tmp=new Subject();
            subjectArrayList.add(tmp);
        }
        id=UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Subject> getSubjectArrayList() {
        return subjectArrayList;
    }

    public void setSubjectArrayList(ArrayList<Subject> subjectArrayList) {
        this.subjectArrayList = subjectArrayList;
    }

    public UUID getId() {
        return id;
    }

    public Subject getSubject(UUID id){
        Subject tmp;
        for (Subject s:subjectArrayList) {
            if(s.getId().equals(id)) return s;

        }
        return null;
    }
}
