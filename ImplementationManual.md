# Implementation
## Classes:
### ScheduleItem
This is the parent class for two other important classes, ScheduleEvent and ScheduleConflict. Each ScheduleItem has a start time and end time.
### ScheduleEvent (extends ScheduleItem)
This class represents an event on the schedule. Each ScheduleEvent, alongside a start and end time, has a name and description.
### ScheduleConflict (extends ScheduleItem)
This class represents a conflict between two events. Alongside a start and end time, it has which two ScheduleEvent objects have caused it.
### ScheduleBlock (extends Rectangle)
This class is an extension of javafx's Rectangle class, used to represent schedule events and conflicts visually. It contains the ScheduleEvent or ScheduleConflict that it represents.
ScheduleBlock also has methods to convert a day and time to its corresponding X and Y coordinates on the timeline.

## Adding & Removing Events
All events are stored within an array of 7 ScheduleBlock ArrayLists, each representing a day of the week.
Whenever an event is added, a new ScheduleBlock representing the event is added to one of these ArrayLists, and to the scene. The new event also checks every other event in the sames ArrayList,
and creates conflicts if any are found.

When an event is removed, it is removed from its ArrayList and from the scene.
