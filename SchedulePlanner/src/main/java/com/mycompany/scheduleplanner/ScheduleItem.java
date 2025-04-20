package com.mycompany.scheduleplanner;

public class ScheduleItem {
    protected double startTime;
    protected double endTime;
    
    public ScheduleItem(double startTime, double endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }
    
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    
    public double getStartTime() {
        return startTime;
    }
    
    public double getEndTime() {
        return endTime;
    }
}
