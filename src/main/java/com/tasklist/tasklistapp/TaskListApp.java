package com.tasklist.tasklistapp;
/*
** This is the main application file, using JavaFX as the GUI framework.
*/

import java.io.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class TaskListApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Task List Object
        TaskList taskList = new TaskList();
        File TaskListDir = new File(System.getProperty("user.home"), "TaskListApp/TaskLists");
        if (!TaskListDir.exists()) { TaskListDir.mkdirs(); }
        try { taskList.readCSV(); }
        catch (FileNotFoundException ignored) { }
        Text taskListText = new Text();
        taskListText.setFont(Font.font("Abyssinica SIL",FontWeight.BOLD,FontPosture.REGULAR,17));
        File autoSave = new File("currentTaskList.csv");
        if (autoSave.exists()) {
            taskList.readCSVAssigned(autoSave);
        }
        taskListText.setText(taskList.printTaskList());

        // Menus
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem file1 = new MenuItem("Open");
        file1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser file = new FileChooser();
                file.setTitle("Open Task List");
                file.getExtensionFilters().addAll(new ExtensionFilter("CSV List","*.csv"));
                file.setInitialDirectory(TaskListDir);
                File selected = file.showOpenDialog(stage);
                try {
                    taskList.clear();
                    taskList.readCSVAssigned(selected);
                    taskListText.setText(taskList.printTaskList());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem file2 = new MenuItem("Save");
        file2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser file = new FileChooser();
                file.setTitle("Save Task List");
                file.setInitialFileName("mytasklist.csv");
                file.getExtensionFilters().addAll(new ExtensionFilter("CSV List","*.csv"));
                file.setInitialDirectory(TaskListDir);
                File selected = file.showSaveDialog(stage);
                try {
                    taskList.writeCSVAssigned(selected);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem file3 = new MenuItem("Clear");
        file3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert clearAlert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to clear?");
                clearAlert.setTitle("Clear Confirmation");
                clearAlert.hide();
                Optional<ButtonType> clearBtn = clearAlert.showAndWait();
                if (clearBtn.get() == ButtonType.OK) {
                    clearAlert.hide();
                    taskList.clear();
                    taskListText.setText(taskList.printTaskList());
                }
                else { clearAlert.hide();}
            }
        });
        Menu viewMenu = new Menu("View");
        MenuItem view1 = new MenuItem("Sort list by difficulty");
        view1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                taskList.sortAll(0);
                taskListText.setText(taskList.printTaskList());
            }
        });
        MenuItem view2 = new MenuItem("Sort list by order added");
        view2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                taskList.sortAll(1);
                taskListText.setText(taskList.printTaskList());
            }
        });
        Menu taskMenu = new Menu("Task");
        MenuItem task1 = new MenuItem("Add");
        task1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Text addName = new Text("Name:");
                Text addDescription = new Text("Description:");
                Text addPriority = new Text("Priority:");
                Text addDifficulty = new Text("Difficulty:");
                Button submit = new Button("Add Task");
                Button cancel = new Button("Cancel");
                TextField nameField = new TextField();
                TextField descriptionField = new TextField();
                ComboBox<Integer> priorityBox = new ComboBox();
                priorityBox.getItems().addAll(1,2,3,4,5);
                ComboBox<Integer> difficultyBox = new ComboBox();
                difficultyBox.getItems().addAll(1,2,3,4,5);
                Stage addStage = new Stage();
                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        addStage.hide();

                    }
                });
                submit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Task newTask = new Task(nameField.getText(), descriptionField.getText(), priorityBox.getValue(),
                                difficultyBox.getValue());
                        taskList.addTask(newTask);
                        taskListText.setText(taskList.printTaskList());
                        newTask = null;
                        addStage.hide();
                    }
                });
                addStage.setTitle("Enter Task Details:");
                GridPane gridPane = new GridPane();
                gridPane.setMinSize(400, 200);
                gridPane.setAlignment(Pos.CENTER);
                gridPane.add(addName,0,0);
                gridPane.add(nameField,1,0);
                gridPane.add(addDescription,0,1);
                gridPane.add(descriptionField,1,1);
                gridPane.add(addPriority,0,2);
                gridPane.add(priorityBox,1,2);
                gridPane.add(addDifficulty,0,3);
                gridPane.add(difficultyBox,1,3);
                gridPane.add(submit,0,4);
                gridPane.add(cancel,1,4);
                Scene addScene = new Scene(gridPane);
                addStage.setScene(addScene);
                addStage.show();

            }
        });
        MenuItem task2 = new MenuItem("Remove");
        task2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage removeStage = new Stage();
                Text taskID = new Text("Enter Task ID: ");
                TextField idField = new TextField();
                GridPane gridPane = new GridPane();
                Button submit = new Button("Remove Task");
                gridPane.setMinSize(400, 200);
                gridPane.setAlignment(Pos.CENTER);
                gridPane.add(taskID,0,0);
                gridPane.add(idField,1,0);
                gridPane.add(submit,2,0);
                submit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        boolean x = taskList.removeTaskByID(Integer.parseInt(idField.getText()));
                        if (!x) {
                            Alert noId = new Alert(AlertType.ERROR, "No task found with that ID.");
                            noId.setTitle("Error");
                            noId.hide();
                            Optional<ButtonType> idBtn = noId.showAndWait();
                            if (idBtn.get() == ButtonType.OK){
                                noId.hide();
                            }

                        }
                        taskListText.setText(taskList.printTaskList());
                        removeStage.hide();
                    }
                });
                removeStage.setTitle("Remove Task:");
                Scene removeScene = new Scene(gridPane);
                removeStage.setScene(removeScene);
                removeStage.show();

            }
        });
        fileMenu.getItems().addAll(file1,file2,file3);
        viewMenu.getItems().addAll(view1,view2);
        taskMenu.getItems().addAll(task1,task2);
        menuBar.getMenus().addAll(fileMenu,viewMenu,taskMenu);

        // Scroll bar
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(taskListText);

        // Stage stuff
        BorderPane root = new BorderPane(scrollPane);
        root.setCenter(scrollPane);
        root.setTop(menuBar);
        Scene scene = new Scene(root,600,400);
        scrollPane.maxHeightProperty().bind(scene.heightProperty());
        stage.setOnCloseRequest(windowEvent -> {
            try {
                taskList.writeCSVAssigned(autoSave);
            } catch (IOException e) {
               e.printStackTrace();
           }
        });
        stage.setScene(scene);
        stage.setTitle("Task List");
        stage.show();
     }

    public static void main(String[] args) {
        launch(args);
    }
}
