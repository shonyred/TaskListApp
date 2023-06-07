# TaskListApp
A task list built in JavaFX, where each task has priority and difficulty.

The priority/difficulty span from 1-5, with 1 being the highest priority and least difficult.
The tasks are listed on the left side of the applicaton, with the "ability" to scroll down when tasks run off the screen.
The three menus control the application:

File:

- Load a task list from a CSV file.
- Save the current task list to a CSV file.
- Clear the entire task list.

View:

- Sort every priority tier by the difficulty of the tasks.
- Sort every priority tier by the order in which the tasks were added.

Task:

- Add a task with assigned name, description, priority, and difficulty values. A unique task ID is set to the task after adding.
- Remove a task by its specified task ID.

When closing the task list, the current state of the list is automatically saved to a CSV, and is reloaded upon restart of the app.

Current planned changes are a more user-friendly interface with more color and icons, and a history system to remember each task that was
added/cleared.

+ whatever Jimmy wants
