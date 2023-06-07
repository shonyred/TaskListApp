package com.tasklist.tasklistapp;
/*
**  This class represents a task to be used in the task list. Its instance variables are the name and description of
**  the task (both separate strings), and two values for the difficulty and priority of the task.
*/

public final class Task {

    // Instance Variables
    private final String name;
    private final String description;
    private final int priority;
    private final int difficulty;
    private final int index;

    // Constructors
    // Creates a new task with a given name, description, priority, and difficulty.
    public Task(String newName, String newDescription, int newPriority, int newDifficulty) {
        name = newName;
        description = newDescription;
        priority = newPriority;
        difficulty = newDifficulty;
        index = 0;    // index is set later by task list
    }

    // Creates a task with specified index.
    public Task(String newName, String newDescription, int newPriority, int newDifficulty, int newIndex) {
        name = newName;
        description = newDescription;
        priority = newPriority;
        difficulty = newDifficulty;
        index = newIndex;
    }

    // Creates a new task that is identical to the given one.
    public Task(Task oldTask) {
        name = oldTask.getName();
        description = oldTask.getDescription();
        priority = oldTask.getPriority();
        difficulty = oldTask.getDifficulty();
        index = oldTask.getIndex();
    }

    // Observers
    // Returns the name of the task.
    public String getName() {
        return name;
    }

    // Returns the description of the task.
    public String getDescription() {
        return description;
    }

    // Returns the priority of the task.
    public int getPriority() {
        return priority;
    }

    // Returns the difficulty of the task.
    public int getDifficulty() {
        return difficulty;
    }

    // Returns the index of the task in relation to when it was added.
    public int getIndex() {
        return index;
    }

    // Returns the current Task in String form, including the name, description, priority, and difficulty.
    public String printTask() {
        return ("Name: " + this.getName() + "\n" +
                "Description: " + this.getDescription() + "\n" +
                "Priority: " + this.getPriority() + "\n" +
                "Difficulty: " + this.getDifficulty() + "\n" +
                "Task ID: " + this.getIndex() + "\n"
        );

    }

    // Returns a String of the values of the task in the form of a CSV string.
    public String printCSV() {
        return name + "," + description + "," + priority + "," + difficulty + "," + index;
    }

}
