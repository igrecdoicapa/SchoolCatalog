package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.sql.SQLException;

public class AddPointsDialog {
    @FXML
    private DatePicker pointsDate;
    @FXML
    private ComboBox points;
    @FXML
    private RadioButton isThesis;
    @FXML
    private RadioButton isAutumnExamination;
    @FXML
    private Label lblThesis;

    private int selectedInMainSubject;
    private int selectedInMainStudent;


    public void showThesisRadio(int student, int subject) {
        int requiresThesis = 0;
        try {
            requiresThesis = SQLServerDriver.getInstance().isThesis(student, subject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (requiresThesis == 1) {
            lblThesis.setVisible(true);
            isThesis.setVisible(true);
        } else if ( requiresThesis == 0) {
            lblThesis.setVisible(false);
            isThesis.setVisible(false);
        }
    }

    public Date getPointsDate() {
        Date returnPointsDate = Date.valueOf(pointsDate.getValue());
        return returnPointsDate;
    }

    public void setPointsDate(DatePicker pointsDate) {
        this.pointsDate = pointsDate;
    }

    public int getPoints() {
        return Integer.parseInt((String) points.getValue());
    }

    public void setPoints(ComboBox points) {
        this.points = points;
    }

    public RadioButton getIsThesis() {
        return isThesis;
    }

    public void setIsThesis(RadioButton isThesis) {
        this.isThesis = isThesis;
    }

    public RadioButton getIsAutumnExamination() {
        return isAutumnExamination;
    }

    public void setIsAutumnExamination(RadioButton isAutumnExamination) {
        this.isAutumnExamination = isAutumnExamination;
    }

    public int getSelectedInMainSubject() {
        return selectedInMainSubject;
    }

    public void setSelectedInMainSubject(int selectedInMainSubject) {
        this.selectedInMainSubject = selectedInMainSubject;
    }

    public int getSelectedInMainStudent() {
        return selectedInMainStudent;
    }

    public void setSelectedInMainStudent(int selectedInMainStudent) {
        this.selectedInMainStudent = selectedInMainStudent;
    }
}
