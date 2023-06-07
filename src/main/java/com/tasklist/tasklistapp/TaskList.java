package com.tasklist.tasklistapp;
/*
**  This class represents the list of tasks set by the user. Despite the name, it is actually a dictionary, with
**  indexes being the priority of the task, and the value being a list of task objects, sorted by either difficulty
*   or order added. It also contains methods for reading/writing from/to a comma-seperated-value file.
*/
import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.net.URL;


public class TaskList {

    // Instance Variables
    private static int indexAdded;
    private static TreeMap<Integer, List<Task>> taskList;

    // Constructor
    public TaskList() {
        indexAdded = 0;
        taskList = new TreeMap<>();
    }

    // Observers
    public String printTaskList() {
        String result = "";
        for (int i = 1; i <= 5; i++) {
            int val = 0;
            result = result + "Priority " + i + ":\n";
            if (taskList.get(i) != null) {
                while (taskList.get(i).size() > val) {
                    Task temp = new Task(taskList.get(i).get(val));
                    result = result + temp.printTask() + "\n";
                    val++;
                }
            }
        }
        return result;
    }

    // Write the current task list to a CSV assigned by the user.
    public void writeCSVAssigned(File file) throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        String[] indexNum = new String[1];
        indexNum[0] = Integer.toString(indexAdded);
        writer.writeNext(indexNum,true);
        for (int i = 1; i <= 5; i++) {
            int val = 0;
            if (taskList.get(i) != null) {
                while ( val < taskList.get(i).size()) {
                    String currentTask = taskList.get(i).get(val).printCSV();
                    String[] taskAry = currentTask.split(",");
                    writer.writeNext(taskAry, true);
                    val++;
                }
            }
        }

        writer.close();
    }

    // Reads a CSV and assigns values to task list.
    public void readCSV() throws FileNotFoundException {
        URL fileURL = TaskList.class.getClassLoader().getResource("currenttasklist.csv");
        while (fileURL != null) {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(false).build();
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(fileURL.getFile())).withCSVParser(csvParser).build();
            String[] nextTask;
            try {
                while ((nextTask = csvReader.readNext()) != null) {
                    Task currentTask = new Task(nextTask[0], nextTask[1], Integer.parseInt(nextTask[2]),
                            Integer.parseInt(nextTask[3]), Integer.parseInt(nextTask[4]));
                    addTask(currentTask);
                }
                sortAll(0);
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

    // Reads a user specified CSV and assigns values to task list
    public void readCSVAssigned(File file) throws FileNotFoundException {
        FileReader fileReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fileReader);
        boolean leftOff = false;
        String[] nextTask;
        try {
            while ((nextTask = csvReader.readNext()) != null) {
                if (!leftOff) {
                    leftOff = true;
                    indexAdded = Integer.parseInt(nextTask[0]);
                }
                else {
                    Task currentTask = new Task(nextTask[0], nextTask[1], Integer.parseInt(nextTask[2]),
                            Integer.parseInt(nextTask[3]), Integer.parseInt(nextTask[4]));
                    addTaskCSV(currentTask);
                }
            }
            sortAll(0);
        }
        catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }

    // Mutators
    // Adds the given task to the task list by priority.
    public void addTask(Task task) {
        int x = task.getPriority();
        indexAdded = indexAdded + 1;
        Task newTask = new Task(task.getName(),task.getDescription(),task.getPriority(),task.getDifficulty(),indexAdded);
        taskList.computeIfAbsent(x, k -> new ArrayList<>(2));
        taskList.get(x).add(newTask);
        sort(1, x);
    }


    // Adds the given task to the task list by priority from a CSV, not incrementing indexAdded.
    public void addTaskCSV(Task task) {
        int x = task.getPriority();
        Task newTask = new Task(task.getName(),task.getDescription(),task.getPriority(),task.getDifficulty(),task.getIndex());
        taskList.computeIfAbsent(x, k -> new ArrayList<>(2));
        taskList.get(x).add(newTask);
        sort(1, x);
    }

    // Removes the task associated with its order index.
    public boolean removeTaskByID(int x) {
        for (int i = 1; i <= 5; i++) {
            int val = 0;
            if (taskList.get(i) != null) {
                while (val < taskList.get(i).size()) {
                    if (taskList.get(i).get(val).getIndex() == x) {
                        taskList.get(i).remove(val);
                        return true;
                    }
                    val++;
                }
            }
        }
        return false;
    }

    // Clears the task list.
    public void clear() {
        indexAdded = 0;
        taskList.clear();
    }

    // Sorts a priority tier based on user input, 0 for difficulty or 1 for order entered.
    public void sort(int y, int x) {
        if (y==0) {
            Collections.sort(taskList.get(x), new TaskSortDifficulty());
        }
        else if (y==1) {
            Collections.sort(taskList.get(x), new TaskSortIndex());
        }
    }


    // Sorts the entire list by user input, 0 for difficulty, 1 for order entered the youngest first, or 2 for order
    // entered the oldest first.
    public void sortAll(int y) {
        if (y==0) {
            for (int i = 1; i <= 5; i++) {
                if (taskList.get(i) != null) {
                    Collections.sort(taskList.get(i), new TaskSortDifficulty());
                }
            }
        }
        else if (y==1) {
            for (int i = 1; i <= 5; i++) {
                if (taskList.get(i) != null) {
                    Collections.sort(taskList.get(i), new TaskSortIndex());
                }
            }
        }
    }


class TaskSortDifficulty implements Comparator<Task> {
        public int compare(Task t1, Task t2){
            if (t1.getDifficulty() == t2.getDifficulty()) {
                return 0;
            }
            else if (t1.getDifficulty() > t2.getDifficulty()) {
                return 1;
            }
            else {
                return -1;
            }
        }
}

class TaskSortIndex implements Comparator<Task> {
        public int compare(Task t1, Task t2){
            if (t1.getIndex() == t2.getIndex()) {
                return 0;
            }
            else if (t1.getIndex() > t2.getIndex()) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

}
