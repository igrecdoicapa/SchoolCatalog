package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class MotivateAbsenceDialog implements Initializable {
    @FXML
    private TableView allStudentAbsencesTableView ;
    @FXML
    private TableColumn subjectColumn;
    @FXML
    private TableColumn dateColumn;
    @FXML
    private TableColumn motivatedColumn;
    @FXML
    private Button motivateSelected;

    private int selectedInMainSubject;
    private int selectedInMainStudent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allStudentAbsencesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void lodDataToTable() {
        ObservableList<TableAbsences> showAbsences = SQLServerDriver.getInstance().showAbsencesInDialog(selectedInMainStudent);
        allStudentAbsencesTableView.setItems(showAbsences);
        subjectColumn.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("subject_name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<TableAbsences, Date>("absenceDate"));
        motivatedColumn.setCellValueFactory(new PropertyValueFactory<TableAbsences, String>("motivatedDialog"));
    }

    public int getSelectedInMainSubject() {
        return selectedInMainSubject;
    }

    public void setSelectedInMainSubject(int selectedSubject) {
        this.selectedInMainSubject = selectedSubject;
    }

    public int getSelectedInMainStudent() {
        return selectedInMainStudent;
    }

    public void setSelectedInMainStudent(int selectedStudent) {
        this.selectedInMainStudent = selectedStudent;
    }

    public void motivateSelectedAbsences() throws SQLException {
        ObservableList<TableAbsences> selected = allStudentAbsencesTableView.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selected.size(); i++){
            Date dateOfAbsence = selected.get(i).getAbsenceDate();
            String stringSubject = selected.get(i).getSubject_name();
            int intSubject = SQLServerDriver.getInstance().subjectIdWhereName(stringSubject);
            SQLServerDriver.getInstance().motivateAbsences(selectedInMainStudent, (java.sql.Date) dateOfAbsence, intSubject);
        }
        lodDataToTable(); //todo: to heavy, should refresh
    }

    public void unmotivateSelectedAbsences() throws SQLException {
        ObservableList<TableAbsences> selected = allStudentAbsencesTableView.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selected.size(); i++){
            Date dateOfAbsence = selected.get(i).getAbsenceDate();
            String stringSubject = selected.get(i).getSubject_name();
            int intSubject = SQLServerDriver.getInstance().subjectIdWhereName(stringSubject);
            SQLServerDriver.getInstance().unmotivateAbsences(selectedInMainStudent, (java.sql.Date) dateOfAbsence, intSubject);
        }
        lodDataToTable(); //todo: to heavy, should refresh
    }
}
