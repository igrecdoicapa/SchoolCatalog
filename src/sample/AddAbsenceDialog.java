package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.sql.Date;

public class AddAbsenceDialog {
    @FXML
    private DatePicker absenceDate;

    public Date getAbsenceDate() {
        Date returnAbsenceDate = Date.valueOf(absenceDate.getValue());
        return returnAbsenceDate;
    }

    public void setAbsenceDate(DatePicker absenceDate) {
        this.absenceDate = absenceDate;
    }
}
