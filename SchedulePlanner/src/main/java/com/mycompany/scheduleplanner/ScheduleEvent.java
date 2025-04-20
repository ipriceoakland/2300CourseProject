package com.mycompany.scheduleplanner;

public class ScheduleEvent extends ScheduleItem {
    
    private String eventName;
    private String eventDescription;
    
    public ScheduleEvent(double startTime, double endTime, String eventName, String eventDescription) {
        /* Parent class contains: Getters and setters for startTime and endTime. These are common to
        ScheduleEvent and to ScheduleConflict. */
        super(startTime, endTime);
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }
    
    //Determine if the time scheduled for this event overlaps with that of another, causing a conflict.
    public ScheduleConflict findConflict(ScheduleItem other) {
        //CONDITION 1: Other event fully enclosed by this
        if(this.startTime < other.getStartTime() && this.endTime > other.getEndTime())
        {
            return new ScheduleConflict(other.getStartTime(), other.getEndTime(), this, other);
        }
        //CONDITION 2: This event fully enclosed by other
        if(this.startTime > other.getStartTime() && this.endTime < other.getEndTime())
        {
            return new ScheduleConflict(this.startTime, this.endTime, this, other);
        }
        //CONDITION 3: This event starts before other ends
        if(this.startTime < other.getEndTime() && this.startTime > other.getStartTime())
        {
            return new ScheduleConflict(this.startTime, other.getEndTime(), this, other);
        }
        //CONDITION 4: This event ends after other starts
        if(this.endTime > other.getStartTime() && this.endTime < other.getEndTime())
        {
            return new ScheduleConflict(other.getStartTime(), this.endTime, this, other);
        }
        //If no conditions are met, there are no conflicts.
        return null;
    }
    
    //Getters and Setters
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public String getEventDescription() {
        return eventDescription;
    }
}
