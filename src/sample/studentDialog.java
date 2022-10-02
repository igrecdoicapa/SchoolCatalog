package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Date;

public class studentDialog {
    @FXML
    private TextField studentFirstName;
    @FXML
    private TextField studentLastName;
    @FXML
    private TextField studentEmail;
    @FXML
    private TextField studentPhone;
    @FXML
    private TableView contactsView;
    @FXML
    private TableColumn contactFirstName;
    @FXML
    private TableColumn contactLastName;
    @FXML
    private TableColumn contactEmail;
    @FXML
    private TableColumn contactPhone;
    @FXML
    private TableView averageView;
    @FXML
    private TableColumn subject;
    @FXML
    private TableColumn sem1;
    @FXML
    private TableColumn sem2;
    @FXML
    private TableColumn autumnExamination;
    @FXML
    private TableColumn finalAverage;
    @FXML
    private TableColumn requiresThesis;
    @FXML
    private ContextMenu contextMenu;


    private int selectedInMainStudent;

    public void initialize(){
        contextMenu = new ContextMenu();
        MenuItem deleteParent = new MenuItem("Delete parent");
        contextMenu.getItems().addAll(deleteParent);
        deleteParent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TableContact parent = (TableContact) contactsView.getSelectionModel().getSelectedItem();
                try {
                    SQLServerDriver.getInstance().deleteParent(parent.getId_contact(), selectedInMainStudent);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                loadParents(selectedInMainStudent);
            }
        });
        contactsView.setContextMenu(contextMenu);

        studentFirstName.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (oldPropertyValue) {
                    try {
                        SQLServerDriver.getInstance().updateFirstNameStudent(selectedInMainStudent, studentFirstName.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } }
            }
        });

        studentLastName.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (oldPropertyValue) {
                    try {
                        SQLServerDriver.getInstance().updateLastNameStudent(selectedInMainStudent, studentLastName.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } }
            }
        });

        studentPhone.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (oldPropertyValue) {
                    try {
                        SQLServerDriver.getInstance().updatePhoneStudent(selectedInMainStudent, studentPhone.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } }
            }
        });

        studentEmail.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (oldPropertyValue) {
                    try {
                        SQLServerDriver.getInstance().updateEmailStudent(selectedInMainStudent, studentEmail.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } }
            }
        });
    }

    public void showStudentData(int selectedInMainStudent) {
        TableContact student = new TableContact();
        student = SQLServerDriver.getInstance().showStudentCard(selectedInMainStudent);
        studentFirstName.setText(student.getContact_first_name());
        studentLastName.setText(student.getContact_last_name());
        studentEmail.setText(student.getContact_email());
        studentPhone.setText(student.getContact_phone1());
    }

    public void loadParents (int selectedInMainStudent) {
        ObservableList<TableContact> parents = SQLServerDriver.getInstance().showStudentCardParents(selectedInMainStudent);
        contactsView.setItems(parents);
        contactFirstName.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("contact_first_name"));
        contactLastName.setCellValueFactory(new PropertyValueFactory<TableAbsences, Date>("contact_last_name"));
        contactEmail.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("contact_email"));
        contactPhone.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("contact_phone1"));
    }

    public void loadAverages (int selectedInMainStudent) {
        ObservableList<TableStudentAverage> averages = SQLServerDriver.getInstance().showStudentCardAverages(selectedInMainStudent);
        averageView.setItems(averages);
        subject.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("subjectName"));
        sem1.setCellValueFactory(new PropertyValueFactory<TableAbsences, Date>("averageSem1"));
        sem2.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("averageSem2"));
        autumnExamination.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("autumnExamination"));
        finalAverage.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("finalAverage"));
        requiresThesis.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("thesisRequiredView"));
        averageView.getSelectionModel().selectFirst();
    }

    public void makeSelectedThesis () {
        TableStudentAverage selected = (TableStudentAverage) averageView.getSelectionModel().getSelectedItem();
        SQLServerDriver.getInstance().makeThesis(selected.getId_subject(), selected.getId_student());
        loadAverages(selected.getId_student());
    }

    public void removeSelectedThesis () {
        TableStudentAverage selected = (TableStudentAverage) averageView.getSelectionModel().getSelectedItem();
        SQLServerDriver.getInstance().removeThesis(selected.getId_subject(), selected.getId_student());
        loadAverages(selected.getId_student());
    }

    public int getSelectedInMainStudent() {
        return selectedInMainStudent;
    }

    public void setSelectedInMainStudent(int selectedInMainStudent) {
        this.selectedInMainStudent = selectedInMainStudent;
    }

    public TextField getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(TextField studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public TextField getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(TextField studentLastName) {
        this.studentLastName = studentLastName;
    }

    public TextField getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(TextField studentEmail) {
        this.studentEmail = studentEmail;
    }

    public TextField getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(TextField studentPhone) {
        this.studentPhone = studentPhone;
    }

    public TableView getContactsView() {
        return contactsView;
    }

    public void setContactsView(TableView contactsView) {
        this.contactsView = contactsView;
    }

    public TableColumn getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(TableColumn contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public TableColumn getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(TableColumn contactLastName) {
        this.contactLastName = contactLastName;
    }

    public TableColumn getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(TableColumn contactEmail) {
        this.contactEmail = contactEmail;
    }

    public TableColumn getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(TableColumn contactPhone) {
        this.contactPhone = contactPhone;
    }

    public TableView getAverageView() {
        return averageView;
    }

    public void setAverageView(TableView averageView) {
        this.averageView = averageView;
    }

    public TableColumn getSubject() {
        return subject;
    }

    public void setSubject(TableColumn subject) {
        this.subject = subject;
    }

    public TableColumn getSem1() {
        return sem1;
    }

    public void setSem1(TableColumn sem1) {
        this.sem1 = sem1;
    }

    public TableColumn getSem2() {
        return sem2;
    }

    public void setSem2(TableColumn sem2) {
        this.sem2 = sem2;
    }

    public TableColumn getAutumnExamination() {
        return autumnExamination;
    }

    public void setAutumnExamination(TableColumn autumnExamination) {
        this.autumnExamination = autumnExamination;
    }

    public TableColumn getFinalAverage() {
        return finalAverage;
    }

    public void setFinalAverage(TableColumn finalAverage) {
        this.finalAverage = finalAverage;
    }

    public TableColumn getRequiresThesis() {
        return requiresThesis;
    }

    public void setRequiresThesis(TableColumn requiresThesis) {
        this.requiresThesis = requiresThesis;
    }
}
