package sample;

import java.sql.Date;
import java.time.LocalDateTime;

public class TableAbsences {
    private int id_student;
    private int id_subject;
    private int motivated;
    private Date absenceDate;
    private String subject_name;
    private String motivatedDialog;

    public String getMotivatedDialog() {
        return motivatedDialog;
    }

    public void setMotivatedDialog(String motivatedDialog) {
        this.motivatedDialog = motivatedDialog;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public int getId_student() {
        return id_student;
    }

    public void setId_student(int id_student) {
        this.id_student = id_student;
    }

    public int getId_subject() {
        return id_subject;
    }

    public void setId_subject(int id_subject) {
        this.id_subject = id_subject;
    }

    public int getMotivated() {
        return motivated;
    }

    public void setMotivated(int motivated) {
        this.motivated = motivated;
    }

    public Date getAbsenceDate() {
        return absenceDate;
    }

    public void setAbsenceDate(Date absenceDate) {
        this.absenceDate = absenceDate;
    }
}
