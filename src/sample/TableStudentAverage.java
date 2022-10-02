package sample;

public class TableStudentAverage {
    private int id_student;
    private int id_subject;
    private int averageSem1;
    private int averageSem2;
    private int autumnExamination;
    private int requiresThesis;
    private float finalAverage;
    private String subjectName;
    private String thesisRequiredView;

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

    public int getAverageSem1() {
        return averageSem1;
    }

    public void setAverageSem1(int averageSem1) {
        this.averageSem1 = averageSem1;
    }

    public int getAverageSem2() {
        return averageSem2;
    }

    public void setAverageSem2(int averageSem2) {
        this.averageSem2 = averageSem2;
    }

    public int getAutumnExamination() {
        return autumnExamination;
    }

    public void setAutumnExamination(int autumnExamination) {
        this.autumnExamination = autumnExamination;
    }

    public int getRequiresThesis() {
        return requiresThesis;
    }

    public void setRequiresThesis(int requiresThesis) {
        this.requiresThesis = requiresThesis;
    }

    public float getFinalAverage() {
        return finalAverage;
    }

    public void setFinalAverage(float finalAverage) {
        this.finalAverage = finalAverage;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getThesisRequiredView() {
        return thesisRequiredView;
    }

    public void setThesisRequiredView(String thesisRequiredView) {
        this.thesisRequiredView = thesisRequiredView;
    }
}
