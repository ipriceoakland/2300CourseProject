package com.mycompany.scheduleplanner;

import java.util.ArrayList;
import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.layout.CornerRadii;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.event.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;


/**
 * JavaFX App
 */
public class App extends Application {
    
    final Font StandardFont = Font.font("Rubik", FontWeight.BOLD, 18.0);
    final Font InputFont = Font.font("Rubik", 14.0);
    final DecimalFormat TIMEFORMAT = new DecimalFormat("00");
    
    final Background AddButtonBackground = new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(10), null));
    final Background RemoveButtonBackground = new Background(new BackgroundFill(Color.LIGHTSALMON, new CornerRadii(10), null));
    final Background ButtonCancelBackground = new Background(new BackgroundFill(Color.GOLD, new CornerRadii(10), null));
    
    ArrayList<ScheduleBlock>[] eventsList;
    ArrayList<ScheduleBlock>[] conflictsList;
    
    Scene mainScene;
    Group sceneGroup;

    boolean isAddingEvent = false; //TRUE FOR DEBUG ONLY
    boolean isRemovingEvent = false;
    
    int lastClickX = 0, lastClickY = 0;
    boolean hasClickedOnce = false;
    
    Button AddItemButton, RemoveItemButton;
    
    @Override
    public void start(Stage stage) {
        eventsList = new ArrayList[7];
        conflictsList = new ArrayList[7];
        for(int i = 0; i < 7; i++) {
            eventsList[i] = new ArrayList();
            conflictsList[i] = new ArrayList();
        }
        
        String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        
        var staticBottomLayer = new Group();
        var staticTopLayer = new Group();
        var controlLayer = new Group();
        
        sceneGroup = new Group(staticBottomLayer, staticTopLayer, controlLayer);
        
        AddItemButton = new Button("Add Schedule Event");
            AddItemButton.setTranslateX(120);
            AddItemButton.setTranslateY(40);
            AddItemButton.setPrefSize(280, 40);
            AddItemButton.setFont(StandardFont);
            AddItemButton.setBackground(AddButtonBackground);
            AddItemButton.setOnAction(this::setAddEventMode);
        RemoveItemButton = new Button("Remove Schedule Event");
            RemoveItemButton.setTranslateX(440);
            RemoveItemButton.setTranslateY(40);
            RemoveItemButton.setPrefSize(280, 40);
            RemoveItemButton.setFont(StandardFont);
            RemoveItemButton.setBackground(RemoveButtonBackground);
            RemoveItemButton.setOnAction(this::setRemoveEventMode);
        //Button EditItemButton = new Button("Edit Schedule Event");
        
        controlLayer.getChildren().add(AddItemButton);
        controlLayer.getChildren().add(RemoveItemButton);
        
        //Layout: Rectangular box to plot schedule on for each day.
        for(int i = 0; i < 7; i++)
        {
            Text text = new Text(120 + (i * 160), 150, dayNames[i]);
            text.setFont(new Font(40));
            Rectangle rect = new Rectangle(120 + (i * 160), 160, 120, 480);
            rect.setFill(Color.LIGHTGRAY);
            
            staticBottomLayer.getChildren().add(text);
            staticBottomLayer.getChildren().add(rect);
        }
        
        //Layout: Hours and lines to mark each hour. 
        for(int i = 0; i < 24; i++)
        {
            Text time = new Text(80, 175 + (i * 20), ("" + i));
            Line line = new Line(80, 180 + (i * 20), 120 + 1080, 180 + (i * 20));
            //Alternating line colors:
            if(i % 2 == 0) {
                line.setStroke(Color.GRAY);
            
            } else {
                line.setStroke(Color.DARKRED);
            }
            //Bright red line at noon:
            if(i == 11) {
                line.setStroke(Color.RED);
            }
            
            staticTopLayer.getChildren().add(time);
            staticTopLayer.getChildren().add(line);
        }
        
        mainScene = new Scene(sceneGroup, 1320, 680);
        mainScene.setOnMousePressed(this::mouseClick);
        
        stage.setScene(mainScene);
        stage.setTitle("Schedule Planner");
        stage.show();
    }

    //Separate mouse clicks into different functions.
    private void mouseClick(MouseEvent event) {
        double mouseX = event.getSceneX();
        double mouseY = event.getSceneY();
        
        if((mouseX >= 110) && (mouseX <= 1210) && (mouseY >= 150) && (mouseY <= 650)) {
            if(isAddingEvent) {     //Add Event
                if(hasClickedOnce) {
                    addNewEvent((int)mouseY);
                    hasClickedOnce = false;
                } else {
                    lastClickX = (int)mouseX;
                    lastClickY = (int)mouseY;
                    hasClickedOnce = true;
                }
            } else if(isRemovingEvent) {    //Remove Event
                removeEvent(mouseX, mouseY);
            } else {    //View Event
                viewEvent(mouseX, mouseY);
            }
        }
    }
    
    private void addNewEvent(int finalY) {
        //The day the event will be added to will always be based on the first click's location.
        int day = screenPosToDate(lastClickX);
        double startTime;
        double endTime;
        
        //Assign values so that the end time always comes after the start time.
        if(lastClickY <= finalY) {
            startTime = screenPosToTime(lastClickY);
            endTime = screenPosToTime(finalY);
        } else {
            startTime = screenPosToTime(finalY);
            endTime = screenPosToTime(lastClickY);
        }
        
        /* This will need to open a window in which the user can enter a name and description for the event
        and possibly edit the start and end time as well. */
        Stage addEventStage = new Stage();
        
        Text addEventText = new Text("Add New Event");
            addEventText.setFont(StandardFont);
        Text namePromptText = new Text("Name:");
            namePromptText.setFont(InputFont);
        TextField nameField = new TextField();
            nameField.setPromptText("Name");
            nameField.setFont(InputFont);
            nameField.setMaxWidth(250);
        Text descPromptText = new Text("Description:");
            descPromptText.setFont(InputFont);
        TextArea descArea = new TextArea ();
            descArea.setPromptText("Description");
            descArea.setFont(InputFont);
            descArea.setWrapText(true);
            descArea.setMaxWidth(250);
        Button addButton = new Button("Add Event");
            addButton.setFont(StandardFont);
        
        VBox addEventVBox = new VBox(addEventText, namePromptText, nameField, descPromptText, descArea, addButton);
            addEventVBox.setSpacing(10);
            addEventVBox.setAlignment(Pos.CENTER);
        
        Scene addEventScene = new Scene(addEventVBox, 300, 400);
        addEventStage.setScene(addEventScene);
        addEventStage.show();
        
        //Event handler for "Add Event" button.
        EventHandler event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) 
            {
                ScheduleEvent newEvent = new ScheduleEvent(
                        startTime,
                        endTime,
                        nameField.getText(),
                        descArea.getText()
                );
                ScheduleBlock newBlock = new ScheduleBlock(newEvent, day, startTime, endTime);
                eventsList[day].add(newBlock);
                sceneGroup.getChildren().add(newBlock);
                findConflicts(newBlock, day);
                
                isAddingEvent = false;
                AddItemButton.setText("Add Schedule Event");
                AddItemButton.setBackground(AddButtonBackground);
                addEventStage.close();
            }
        };
        
        addButton.setOnAction(event);
        
        //Debug
        System.out.println(day);
        System.out.println(screenPosToTime(lastClickY));
    }
    
    private void findConflicts(ScheduleBlock eventBlock, int day) {
        for(ScheduleBlock other : eventsList[day]) {
            ScheduleConflict conflict = eventBlock.getEvent().findConflict(other.getEvent());
            
            if(conflict != null) {
                ScheduleBlock conflictBlock = new ScheduleBlock(conflict, day, conflict.getStartTime(), conflict.getEndTime());
                conflictsList[day].add(conflictBlock);
                sceneGroup.getChildren().add(conflictBlock);
                System.out.println("Found Conflict!");
            }
        }
    }
    
    private void viewEvent(double mouseX, double mouseY) {
        boolean foundEvent = false;
        boolean isConflict = false;
        ScheduleBlock block;
        ScheduleItem item = new ScheduleItem(0, 0);
        ScheduleEvent event = new ScheduleEvent(0, 0, "", "");
        
        block = EventOnPos(conflictsList, mouseX, mouseY);
        if(block != null) {
            foundEvent = true;
            isConflict = true;
            item = block.getConflict();
        } else {
            block = EventOnPos(eventsList, mouseX, mouseY);
            if(block != null) {
                foundEvent = true;
                item = block.getEvent();
                event = block.getEvent();
            }
        }
        
        if(foundEvent) {
            Stage viewEventStage = new Stage();
            VBox viewEventVBox;
            Text titleText;
            Text timeText;
            
            int startHours = (int)(item.getStartTime());
            int startMinutes = (int)((item.getStartTime() % 1) * 60);
            int endHours = (int)(item.getEndTime());
            int endMinutes = (int)((item.getEndTime() % 1) * 60);
            
            timeText = new Text(TIMEFORMAT.format(startHours) + ":" + TIMEFORMAT.format(startMinutes) + " -- " + TIMEFORMAT.format(endHours) + ":" + TIMEFORMAT.format(endMinutes));
            if(!isConflict) {
                titleText = new Text(event.getEventName());
                /*
                TextField nameField = new TextField(event.getEventName());
                    nameField.setEditable(false);
                    nameField.setFont(InputFont);
                    nameField.setMaxWidth(250);
                */
                TextArea descArea = new TextArea(event.getEventDescription());
                    descArea.setEditable(false);
                    descArea.setFont(InputFont);
                    descArea.setWrapText(true);
                    descArea.setMaxWidth(250);
            
                viewEventVBox = new VBox(titleText, timeText, descArea);
            } else {
                titleText = new Text("Schedule Conflict");
                viewEventVBox = new VBox(titleText, timeText);
            }
            
            titleText.setFont(StandardFont);
            timeText.setFont(StandardFont);
            viewEventVBox.setSpacing(10);
            viewEventVBox.setAlignment(Pos.CENTER);
            
            Scene viewEventScene = new Scene(viewEventVBox, 300, 350);
            viewEventStage.setScene(viewEventScene);
            viewEventStage.show();
            
        }
        
    }
    
    private void removeEvent(double mouseX, double mouseY) {
        int day = screenPosToDate((int)mouseX);
        boolean foundConflict = false;
        ScheduleBlock block;
        System.out.println("Trying to find item to remove");
        //Do nothing if a conflict is clicked on, for consistency.
        block = EventOnPos(conflictsList, mouseX, mouseY);
        if(block != null) {
            foundConflict = true;
        }
        
        //If a event Schedule Block is clicked on, remove it from the list and the scene.
        if(!foundConflict) {
            block = EventOnPos(eventsList, mouseX, mouseY);
            if(block != null) {
                System.out.println("Found item to remove");
                eventsList[day].remove(block);
                sceneGroup.getChildren().remove(block);
                
                //Remove any overlapping conflicts.
                for(ScheduleBlock conflict : conflictsList[day]) {
                    if(conflict.getConflict().getParentEvent1() == block.getEvent() || conflict.getConflict().getParentEvent2() == block.getEvent()) {
                        conflictsList[day].remove(conflict);
                        sceneGroup.getChildren().remove(conflict);
                        break;
                    }
                }
                
                isRemovingEvent = false;
                RemoveItemButton.setText("Remove Schedule Event");
                RemoveItemButton.setBackground(RemoveButtonBackground);
            }
        }
    }
    
    private ScheduleBlock EventOnPos(ArrayList<ScheduleBlock>[] list, double mouseX, double mouseY) {
        int day = screenPosToDate((int)mouseX);
        double time = screenPosToTime((int)mouseY);
        
        for(ScheduleBlock event : list[day]) {
            if(event.isConflict) {
                if(time >= event.getConflict().getStartTime() && time <= event.getConflict().getEndTime()) {
                    return event;
                }
            } else {
                if(time >= event.getEvent().getStartTime() && time <= event.getEvent().getEndTime()) {
                    return event;
                }
            }
            
        }
        return null;
    }
    
    void setAddEventMode(ActionEvent event) {
        if(!isAddingEvent) { 
            isAddingEvent = true;
            isRemovingEvent = false;
            AddItemButton.setText("Cancel");
            AddItemButton.setBackground(ButtonCancelBackground);
        } else {
            isAddingEvent = false;
            AddItemButton.setText("Add Schedule Event");
            AddItemButton.setBackground(AddButtonBackground);
        }
    }
    
    void setRemoveEventMode(ActionEvent event) {
        if(!isRemovingEvent) {
            isRemovingEvent = true;
            isAddingEvent = false;
            RemoveItemButton.setText("Cancel");
            RemoveItemButton.setBackground(ButtonCancelBackground);
        } else {
            isRemovingEvent = false;
            RemoveItemButton.setText("Remove Schedule Event");
            RemoveItemButton.setBackground(RemoveButtonBackground);
        }
    }
    
    public int screenPosToDate(int posX) {
        return((posX - 100) / 160);
    }
    
    public double screenPosToTime(int posY) {
        //If mouse is outside of the valid area, correct the value.
        if(posY < 160) {
            posY = 160;
        } else if(posY > 640) {
            posY = 640;
        }
        
        /* The mouse position relative to the timeline, divided to account for scale, then casted to int and
        divided by 4.0 to get a double rounded to the nearest quarter hour. */
        return (Math.round((posY - 160) / 5)) / 4.0;
    }
    
    
    public static void main(String[] args) {
        launch();
    }

}