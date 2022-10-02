package sample;

import java.sql.Date;

public class TablePoints {
    private int id_points;
    private int id_student;
    private int id_subject;
    private int points;
    private int thesis;
    private int autumn_examination;
    private Date pointsDate;
    private String specialPoints;

    public String getSpecialPoints() {
        return specialPoints;
    }

    public void setSpecialPoints(String specialPoints) {
        this.specialPoints = specialPoints;
    }

    public int getId_points() {
        return id_points;
    }

    public void setId_points(int id_points) {
        this.id_points = id_points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getThesis() {
        return thesis;
    }

    public void setThesis(int thesis) {
        this.thesis = thesis;
    }

    public int getAutumn_examination() {
        return autumn_examination;
    }

    public void setAutumn_examination(int autumn_examination) {
        this.autumn_examination = autumn_examination;
    }

    public Date getPointsDate() {
        return pointsDate;
    }

    public void setPointsDate(Date pointsDate) {
        this.pointsDate = pointsDate;
    }

}
