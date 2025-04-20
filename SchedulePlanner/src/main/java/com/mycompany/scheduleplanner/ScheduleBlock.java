package com.mycompany.scheduleplanner;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class ScheduleBlock extends Rectangle{
    
    public boolean isConflict;
    protected ScheduleEvent event;
    protected ScheduleConflict conflict;
    
    public ScheduleBlock(ScheduleEvent event, int day, double startTime, double endTime) {
        super(dayToX(day), timeToY(startTime), 120, timeToY(endTime) - timeToY(startTime));
        
        isConflict = false;
        this.event = event;
        
        setFill(Color.ALICEBLUE);
        setStroke(Color.NAVY);
    }
    
    public ScheduleBlock(ScheduleConflict conflict, int day, double startTime, double endTime) {
        super(dayToX(day), timeToY(startTime), 120, timeToY(endTime) - timeToY(startTime));
        
        isConflict = true;
        this.conflict = conflict;
        setFill(Color.SALMON);
        setStroke(Color.RED);
    }
    
    public static int dayToX(int day) {
        return ((day * 160) + 120);
    }
    
    public static int timeToY(double time) {
        return (int)((time * 20) + 160); 
    }
    
    public ScheduleEvent getEvent() {
        if(!isConflict) {
            return event;
        } else {
            return null;
        }
    }
    
    public ScheduleConflict getConflict() {
        if(isConflict) {
            return conflict;
        } else {
            return null;
        }
    }
    
}
