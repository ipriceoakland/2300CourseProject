package com.mycompany.scheduleplanner;

public class ScheduleConflict extends ScheduleItem {
    ScheduleItem parentEvent1;
    ScheduleItem parentEvent2;
    
    public ScheduleConflict(double startTime, double endTime, ScheduleItem parentEvent1, ScheduleItem parentEvent2) {
        super(startTime, endTime);
        this.parentEvent1 = parentEvent1;
        this.parentEvent2 = parentEvent2;
    }
    
    public ScheduleItem getParentEvent1() {
        return parentEvent1;
    }
    
    public ScheduleItem getParentEvent2() {
        return parentEvent2;
    }
}
