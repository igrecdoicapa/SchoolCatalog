package sample;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ComboBox classFilter;
    @FXML
    private ComboBox subjectFilter;
    @FXML
    private ComboBox yearShow;
    @FXML
    private ListView studentListView;
    @FXML
    private TableView pointsView;
    @FXML
    private TableColumn PointsColumn;
    @FXML
    private TableColumn PointsDateColumn;
    @FXML
    private TableColumn SpecialPointsColumn;
    @FXML
    private TableView absenceView;
    @FXML
    private TableColumn AbsenceColumn;
    @FXML
    private TableColumn MotivatedColumn;
    @FXML
    private TextField totalAbsences;
    @FXML
    private TextField totalUnmotivatedAbsences;
    @FXML
    private ContextMenu contextMenu;

    Date s1start = Date.valueOf("2019-09-09");
    Date s1finish = Date.valueOf("2019-12-20");
    Date s2start = Date.valueOf("2020-01-13");
    Date s2finish = Date.valueOf("2020-06-12");

    public void initialize(){
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        studentListView.getSelectionModel().selectFirst();
        List<String> classes = SQLServerDriver.getInstance().filterClass();
        classFilter.setItems((ObservableList) classes);

        classFilter.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                List<String> students = SQLServerDriver.getInstance().showStudents(classFilter.getValue().toString());
                studentListView.setItems((ObservableList) students);
                studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                studentListView.getSelectionModel().selectFirst();

                List<String> subjects = SQLServerDriver.getInstance().filterSubject(classFilter.getValue().toString());
                subjectFilter.setItems((ObservableList) subjects);
                subjectFilter.getSelectionModel().selectFirst();

            }
        });

        subjectFilter.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (classFilter.getValue() != null && subjectFilter.getValue() != null) {
                    try {
                        loadDataToTableAbsence();
                        loadDataToTablePoints();
                    } catch (Exception e){ }
                }
            }
        });

        studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if (classFilter.getValue() != null && subjectFilter.getValue() != null) {
                    try {
                        loadDataToTableAbsence();
                        loadDataToTablePoints();
                    } catch (Exception e){ }
                }
            }
        });

        studentListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        showStudentDialog();
                    }
                }
            }
        });

        pointsView.setRowFactory(row -> new TableRow<TablePoints>(){
            @Override
            public void updateItem(TablePoints row, boolean empty){
                super.updateItem(row, empty);
                if (row == null || empty) {
                    setStyle("");
                } else {
                    if (row.getPointsDate().compareTo(s1finish) < 0 ){
                        this.setStyle("-fx-background-color:green");
                    } else {
                        this.setStyle("-fx-background-color:orange");
                    }
                }
            }
        });

        contextMenu = new ContextMenu();
        MenuItem deletePoints = new MenuItem("Delete points");
        contextMenu.getItems().addAll(deletePoints);
        deletePoints.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TablePoints pointsToDelete = (TablePoints) pointsView.getSelectionModel().getSelectedItem();

                int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
                String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
                String subjectName = subjectFilter.getValue().toString();

                try {
                    SQLServerDriver.getInstance().deletePoints(pointsToDelete.getId_points());
                    if (pointsToDelete.getSpecialPoints().equals("Autumn examination")) {
                        try {
                            SQLServerDriver.getInstance().updateAutumnExamination(studentCode, subjectName, 0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                loadDataToTablePoints();

                if (pointsToDelete.getPointsDate().compareTo(s1start) >= 0 && pointsToDelete.getPointsDate().compareTo(s1finish) <=0) {
                    ObservableList<TablePoints> allPoints = pointsView.getItems();
                    float sumDuringTheYear = 0;
                    float countDuringTheYear = 0;
                    int thesis = 0;
                    int autumnExamination = 0;
                    float average = 0;
                    for(int point = 0; point < allPoints.size(); point ++) {
                        if (allPoints.get(point).getPointsDate().compareTo(s1start) >= 0
                                && allPoints.get(point).getPointsDate().compareTo(s1finish) <=0) {
                            if(allPoints.get(point).getSpecialPoints().length() == 0){
                                sumDuringTheYear += allPoints.get(point).getPoints();
                                countDuringTheYear++;
                            } else if (allPoints.get(point).getSpecialPoints().equals("Thesis")) {
                                thesis = allPoints.get(point).getPoints();
                            } else if (allPoints.get(point).getSpecialPoints().equals("Autumn examination")){
                                autumnExamination = allPoints.get(point).getPoints();
                                try {
                                    SQLServerDriver.getInstance().updateAutumnExamination(studentCode, subjectName, autumnExamination);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if (thesis == 0 && autumnExamination == 0) {
                        average = Math.round(sumDuringTheYear / countDuringTheYear);
                        try {
                            SQLServerDriver.getInstance().updateAvgS1(studentCode, subjectName, (int) average);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if ( thesis != 0 && autumnExamination == 0) {
                        average = Math.round(((sumDuringTheYear / countDuringTheYear) * 3 + thesis ) / 4);
                        try {
                            SQLServerDriver.getInstance().updateAvgS1(studentCode, subjectName, (int) average);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (pointsToDelete.getPointsDate().compareTo(s2start) >= 0 && pointsToDelete.getPointsDate().compareTo(s2finish) <=0) {
                    ObservableList<TablePoints> allPoints = pointsView.getItems();
                    float sumDuringTheYear = 0;
                    float countDuringTheYear = 0;
                    int thesis = 0;
                    int autumnExamination = 0;
                    float average = 0;
                    for(int point = 0; point < allPoints.size(); point ++) {
                        if (allPoints.get(point).getPointsDate().compareTo(s2start) >= 0
                                && allPoints.get(point).getPointsDate().compareTo(s2finish) <=0) {
                            if(allPoints.get(point).getSpecialPoints().length() == 0){
                                sumDuringTheYear += allPoints.get(point).getPoints();
                                countDuringTheYear++;
                            } else if (allPoints.get(point).getSpecialPoints().equals("Thesis")) {
                                thesis = allPoints.get(point).getPoints();
                            } else if (allPoints.get(point).getSpecialPoints().equals("Autumn examination")){
                                autumnExamination = allPoints.get(point).getPoints();
                                try {
                                    SQLServerDriver.getInstance().updateAutumnExamination(studentCode, subjectName, autumnExamination);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if (thesis == 0 && autumnExamination == 0) {
                        average = Math.round(sumDuringTheYear / countDuringTheYear);
                        try {
                            SQLServerDriver.getInstance().updateAvgS2(studentCode, subjectName, (int) average);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if ( thesis != 0 && autumnExamination == 0) {
                        average = Math.round(((sumDuringTheYear / countDuringTheYear) * 3 + thesis ) / 4);
                        try {
                            SQLServerDriver.getInstance().updateAvgS2(studentCode, subjectName, (int) average);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        pointsView.setContextMenu(contextMenu);


        absenceView.setRowFactory(row -> new TableRow<TableAbsences>(){
            @Override
            public void updateItem(TableAbsences row, boolean empty){
                super.updateItem(row, empty);
                if (row == null || empty) {
                    setStyle("");
                } else {
                    if (row.getAbsenceDate().compareTo(s1finish) < 0 ){
                        this.setStyle("-fx-background-color:green");
                    } else {
                        this.setStyle("-fx-background-color:orange");
                    }
                }
            }
        });
    }

    public void loadDataToTableAbsence() {
        absenceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
        String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
        int selectedSubject = SQLServerDriver.getInstance().subjectIdWhereName(subjectFilter.getValue().toString());
        int selectedStudent = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
        ObservableList<TableAbsences> showAbsences = SQLServerDriver.getInstance().showAbsencesInMain(selectedStudent, selectedSubject);
        absenceView.setItems(showAbsences);
        AbsenceColumn.setCellValueFactory(new PropertyValueFactory<TableAbsences, java.util.Date>("absenceDate"));
        MotivatedColumn.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("motivatedDialog"));
        try {
            totalAbsences.setText(String.valueOf(SQLServerDriver.getInstance().countAbsences(selectedStudent, selectedSubject)));
            totalUnmotivatedAbsences.setText(String.valueOf(SQLServerDriver.getInstance().countAbsencesUnmotivated(selectedStudent, selectedSubject)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void motivateSelectedAbsence () {
        int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
        String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
        int student = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);

        TableAbsences absence = (TableAbsences) absenceView.getSelectionModel().getSelectedItem();

        int subject = SQLServerDriver.getInstance().subjectIdWhereName((String) subjectFilter.getValue());

        try {
            SQLServerDriver.getInstance().motivateAbsences(student, absence.getAbsenceDate(), subject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadDataToTableAbsence();
    }

    public void loadDataToTablePoints() {
        pointsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
        String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
        int selectedSubject = SQLServerDriver.getInstance().subjectIdWhereName(subjectFilter.getValue().toString());
        int selectedStudent = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
        ObservableList<TablePoints> showPoints = SQLServerDriver.getInstance().showPointsInMain(selectedStudent, selectedSubject);
        pointsView.setItems(showPoints);
        PointsColumn.setCellValueFactory(new PropertyValueFactory<TablePoints, Date>("points"));
        PointsDateColumn.setCellValueFactory(new PropertyValueFactory<TablePoints, String>("pointsDate"));
        SpecialPointsColumn.setCellValueFactory(new PropertyValueFactory<TablePoints, String>("specialPoints"));
    }

    @FXML
    public void showAddPointsDialog () throws SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setHeaderText("Points will be added for "  + studentListView.getSelectionModel().getSelectedItem());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addPointsDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
        String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
        String subjectName = subjectFilter.getValue().toString();

        int studentSelected = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
        int subjectSelected = SQLServerDriver.getInstance().subjectIdWhereName(subjectName);

        AddPointsDialog controller = fxmlLoader.getController();
        controller.showThesisRadio(studentSelected, subjectSelected);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            int points = controller.getPoints();
            String special = new String();
            if (controller.getIsAutumnExamination().isSelected()) {
                special = "Autumn examination";
            } else if (controller.getIsThesis().isSelected()) {
                special = "Thesis";
            }
            Date newPointsDate = controller.getPointsDate();
            if ((controller.getPointsDate().compareTo(Date.valueOf(LocalDate.now())) < 0) ||
                    (controller.getPointsDate().compareTo(Date.valueOf(LocalDate.now())) == 0)){
                SQLServerDriver.getInstance().addPoints(studentCode, subjectName, points, newPointsDate, special);
            } else {
                Component frame = null;
                JOptionPane.showMessageDialog(frame, "Please add points for current or previous date.",
                        "Date must be changed!", JOptionPane.ERROR_MESSAGE);
            }

            loadDataToTablePoints();

            if (newPointsDate.compareTo(s1start) >= 0 && newPointsDate.compareTo(s1finish) <=0) {
                ObservableList<TablePoints> allPoints = pointsView.getItems();
                float sumDuringTheYear = 0;
                float countDuringTheYear = 0;
                int thesis = 0;
                int autumnExamination = 0;
                float average = 0;
                for(int point = 0; point < allPoints.size(); point ++) {
                    if (allPoints.get(point).getPointsDate().compareTo(s1start) >= 0
                            && allPoints.get(point).getPointsDate().compareTo(s1finish) <=0) {
                        if(allPoints.get(point).getSpecialPoints().length() == 0){
                            sumDuringTheYear += allPoints.get(point).getPoints();
                            countDuringTheYear++;
                        } else if (allPoints.get(point).getSpecialPoints().equals("Thesis")) {
                            thesis = allPoints.get(point).getPoints();
                        } else if (allPoints.get(point).getSpecialPoints().equals("Autumn examination")){
                            autumnExamination = allPoints.get(point).getPoints();
                            SQLServerDriver.getInstance().updateAutumnExamination(studentCode, subjectName, autumnExamination);
                        }
                    }
                }

                if (thesis == 0 && autumnExamination == 0) {
                    average = Math.round(sumDuringTheYear / countDuringTheYear);
                    SQLServerDriver.getInstance().updateAvgS1(studentCode, subjectName, (int) average);
                    SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                } else if ( thesis != 0 && autumnExamination == 0) {
                    average = Math.round(((sumDuringTheYear / countDuringTheYear) * 3 + thesis ) / 4);
                    SQLServerDriver.getInstance().updateAvgS1(studentCode, subjectName, (int) average);
                    SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                }

            } else if (newPointsDate.compareTo(s2start) >= 0 && newPointsDate.compareTo(s2finish) <=0) {
                ObservableList<TablePoints> allPoints = pointsView.getItems();
                float sumDuringTheYear = 0;
                float countDuringTheYear = 0;
                int thesis = 0;
                int autumnExamination = 0;
                float average = 0;
                for(int point = 0; point < allPoints.size(); point ++) {
                    if (allPoints.get(point).getPointsDate().compareTo(s2start) >= 0
                            && allPoints.get(point).getPointsDate().compareTo(s2finish) <=0) {
                        if(allPoints.get(point).getSpecialPoints().length() == 0){
                            sumDuringTheYear += allPoints.get(point).getPoints();
                            countDuringTheYear++;
                        } else if (allPoints.get(point).getSpecialPoints().equals("Thesis")) {
                            thesis = allPoints.get(point).getPoints();
                        } else if (allPoints.get(point).getSpecialPoints().equals("Autumn examination")){
                            autumnExamination = allPoints.get(point).getPoints();
                            SQLServerDriver.getInstance().updateAutumnExamination(studentCode, subjectName, autumnExamination);
                        }
                    }
                }

                if (thesis == 0 && autumnExamination == 0) {
                    average = Math.round(sumDuringTheYear / countDuringTheYear);
                    SQLServerDriver.getInstance().updateAvgS2(studentCode, subjectName, (int) average);
                    SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                } else if ( thesis != 0 && autumnExamination == 0) {
                    average = Math.round(((sumDuringTheYear / countDuringTheYear) * 3 + thesis ) / 4);
                    SQLServerDriver.getInstance().updateAvgS2(studentCode, subjectName, (int) average);
                    SQLServerDriver.getInstance().updateFinalAverage(studentCode, subjectName);
                }
            }
        }
    }

    @FXML
    public void showAddAbsenceDialog () throws SQLException {
        int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
        String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
        String subjectName = subjectFilter.getValue().toString();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setHeaderText("An absence will be added for "  + studentListView.getSelectionModel().getSelectedItem());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addAbsenceDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");;
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddAbsenceDialog controller = fxmlLoader.getController();
            if ((controller.getAbsenceDate().compareTo(Date.valueOf(LocalDate.now())) < 0) ||
                    (controller.getAbsenceDate().compareTo(Date.valueOf(LocalDate.now())) == 0)){
                SQLServerDriver.getInstance().addAbsence(studentCode, subjectName, controller.getAbsenceDate());
            } else {
                Component frame = null;
                JOptionPane.showMessageDialog(frame, "Please add absence for current or previous date.",
                        "Date must be changed!", JOptionPane.ERROR_MESSAGE);
            }
            loadDataToTableAbsence();
        }
    }

    @FXML
    public void showMotivateAbsencesDialog () throws InterruptedException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setHeaderText("Absences will be motivated for "  + studentListView.getSelectionModel().getSelectedItem());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("motivateAbsenceDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
            String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
            int selectedStudent = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
            MotivateAbsenceDialog controller = fxmlLoader.getController();
            controller.setSelectedInMainStudent(selectedStudent);
            controller.lodDataToTable();
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            MotivateAbsenceDialog controller = fxmlLoader.getController();
        }
        loadDataToTableAbsence();
    }

    @FXML
    public void showStudentDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("studentDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        studentDialog controller = fxmlLoader.getController();
        int studentCodeUntil = studentListView.getSelectionModel().getSelectedItems().toString().indexOf(":");
        String studentCode = studentListView.getSelectionModel().getSelectedItems().toString().substring(1, studentCodeUntil);
        int selectedStudent = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
        controller.setSelectedInMainStudent(selectedStudent);
        controller.showStudentData(selectedStudent);
        controller.loadParents(selectedStudent);
        controller.loadAverages(selectedStudent);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            System.out.println("ok");
        }
        List<String> students = SQLServerDriver.getInstance().showStudents(classFilter.getValue().toString());
        studentListView.setItems((ObservableList) students);
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        studentListView.getSelectionModel().selectFirst();
    }
}
