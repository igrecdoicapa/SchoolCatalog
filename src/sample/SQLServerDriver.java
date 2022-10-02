package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class SQLServerDriver {
    String connectionUrl =
            "jdbc:sqlserver://localhost;"
                    + "database=SchoolCatalog2020;"
                    + "user=aly;"
                    + "password=123qwe;";

    private Connection conn;

    public static final String table_class = "class";
    public static final String column_class_id_class = "id_class";
    public static final String column_class_grade = "class_grade";
    public static final String column_class_code = "class_code";
    public static final String column_class_gradeCode = "class_gradeCode";
    public static final String column_class_id_professor = "id_professor";
    public static final String select_filterClass = "SELECT " + column_class_gradeCode + " FROM " + table_class;
    private PreparedStatement select_filterClass_ps;

    public static final String table_classSubject = "classSubject";
    public static final String column_classSubject_id_classSubject= "id_classSubject";
    public static final String column_classSubject_id_class = "id_class";
    public static final String column_classSubject_id_subject = "id_subject";
    public static final String column_classSubject_id_professor = "id_professor";

    public static final String table_subject = "subject";
    public static final String column_subject_id_subject = "id_subject";
    public static final String column_subject_subjectName = "subjectName";
    public static final String select_filterSubject = "SELECT s." + column_subject_subjectName + " FROM " + table_subject + " s " +
            " JOIN " + table_classSubject + " cs ON s." + column_subject_id_subject + " = cs." + column_classSubject_id_subject +
            " JOIN " + table_class + " c ON c." + column_class_id_class + " = cs." + column_classSubject_id_class +
            " WHERE c." + column_class_gradeCode + " = ?";
    private PreparedStatement select_filterSubject_ps;
    public static final String select_id_subject = "SELECT " + column_subject_id_subject + " FROM " + table_subject
            + " WHERE " + column_subject_subjectName + " = ?";
    private PreparedStatement select_id_subject_ps;

    public static final String table_contact = "contact";
    public static final String column_contact_id_contact = "id_contact";
    public static final String column_contact_first_name = "contact_first_name";
    public static final String column_contact_last_name = "contact_last_name";
    public static final String column_contact_address = "contact_address";
    public static final String column_contact_phone1 = "contact_phone1";
    public static final String column_contact_email = "contact_email";

    public static final String  table_studentParent = "studentParent";
    public static final String column_studentParent_student = "id_student";
    public static final String column_studentParent_parent = "id_parent";

    public static final String table_student = "student";
    public static final String column_student_id_student = "id_student";
    public static final String column_student_id_contact = "id_contact";
    public static final String column_student_id_class = "id_class";
    public static final String column_studentCode = "studentCode";
    public static final String select_listStudents = "SELECT " + column_studentCode + "," +
            column_contact_first_name + "," +
            column_contact_last_name +
            " FROM " + table_student + " s " +
            " JOIN " + table_contact + " co on co." + column_contact_id_contact + " = s." + column_student_id_contact +
            " JOIN " + table_class + " cl on cl." + column_class_id_class + " = s." + column_student_id_class +
            " WHERE cl." + column_class_gradeCode + " = ?" +
            " ORDER BY " + column_contact_last_name;
    private PreparedStatement select_listStudents_ps;
    public static final String select_student_whereStudentCode = "SELECT " + column_student_id_student + " FROM " + table_student +
            " WHERE " + column_studentCode + " = ?";
    private PreparedStatement select_student_whereStudentCode_ps;
    public static final String select_studentCard = "SELECT " + column_contact_first_name + "," +
            column_contact_last_name + "," +
            column_contact_phone1 + "," +
            column_contact_email +
            " FROM " + table_student + " s " +
            " JOIN " + table_contact + " c ON c." + column_contact_id_contact + " = s." + column_student_id_contact +
            " WHERE s." + column_student_id_student + " = ?";
    private PreparedStatement select_studentCard_ps;
    public static final String select_studentCard_parents = "SELECT " + " c." + column_contact_id_contact + " , " +
            column_contact_first_name + "," +
            column_contact_last_name + "," +
            column_contact_phone1 + "," +
            column_contact_email +
            " FROM " + table_student + " s " +
            " JOIN " + table_studentParent + " sp ON sp." + column_studentParent_student + " = s." + column_student_id_student +
            " JOIN " + table_contact + " c ON c." + column_contact_id_contact + " = sp." + column_studentParent_parent +
            " WHERE sp." + column_studentParent_student + " = ?";
    private PreparedStatement select_studentCard_parents_ps;
    public static final String update_contact_firstName = "UPDATE c " +
            " SET c." + column_contact_first_name + " = ? " +
            " FROM " + table_student + " s " +
            " JOIN " + table_contact + " c ON c." + column_contact_id_contact + " = s." + column_student_id_contact +
            " WHERE s." + column_student_id_student + " = ?";
    private PreparedStatement update_contact_firstName_ps;
    public static final String update_contact_lastName = "UPDATE c " +
            " SET c." + column_contact_last_name + " = ? " +
            " FROM " + table_student + " s " +
            " JOIN " + table_contact + " c ON c." + column_contact_id_contact + " = s." + column_student_id_contact +
            " WHERE s." + column_student_id_student + " = ?";
    private PreparedStatement update_contact_lastName_ps;
    public static final String update_contact_email = "UPDATE c " +
            " SET c." + column_contact_email + " = ? " +
            " FROM " + table_student + " s " +
            " JOIN " + table_contact + " c ON c." + column_contact_id_contact + " = s." + column_student_id_contact +
            " WHERE s." + column_student_id_student + " = ?";
    private PreparedStatement update_contact_email_ps;
    public static final String update_contact_phone = "UPDATE c " +
            " SET c." + column_contact_phone1 + " = ? " +
            " FROM " + table_student + " s " +
            " JOIN " + table_contact + " c ON c." + column_contact_id_contact + " = s." + column_student_id_contact +
            " WHERE s." + column_student_id_student + " = ?";
    private PreparedStatement update_contact_phone_ps;
    public static final String delete_studentCard_Parents = "DELETE FROM " + table_studentParent +
            " WHERE " + column_studentParent_parent + " = ? " +
            " AND " + column_student_id_student+ " = ?";
    private PreparedStatement delete_studentCard_Parents_ps;

    public static final String table_studentAverage = "studentAverage";
    public static final String column_studentAverage_student = "id_student";
    public static final String column_studentAverage_subject = "id_subject";
    public static final String column_studentAverage_avg1= "averageSem1";
    public static final String column_studentAverage_avg2 = "averageSem2";
    public static final String column_studentAverage_autumnExamination = "autumnExamination";
    public static final String column_studentAverage_requiresThesis = "requiresThesis";
    public static final String column_studentAverage_finalAvg = "finalAverage";
    public static final String select_studentAverage_studentDialog = "SELECT " +
            " sa."  + column_studentAverage_student + " , " + //used for marking subject as requiring thesis, it will not be displayed
            " sa." + column_studentAverage_subject + " , " + //used for marking subject as requiring thesis, it will not be displayed
            column_subject_subjectName + " , " +
            column_studentAverage_avg1 + " , " +
            column_studentAverage_avg2 + " , " +
            column_studentAverage_autumnExamination + " , " +
            column_studentAverage_finalAvg + " , " +
            "CASE WHEN " + column_studentAverage_requiresThesis + " = 1 THEN 'Yes' ELSE 'No' END AS thesisRequired " +
            " FROM " + table_studentAverage + " sa " +
            " JOIN " + table_subject + " s ON sa." + column_studentAverage_subject + " = s." + column_subject_id_subject +
            " WHERE " + column_studentAverage_student + " = ?";
    private PreparedStatement select_studentAverage_studentDialog_ps;
    public static final String update_studentAverage_makeThesis = "UPDATE " + table_studentAverage +
            " SET " + column_studentAverage_requiresThesis + " = 1  " +
            "WHERE " + column_studentAverage_subject + " = ? " +
            " AND " + column_studentAverage_student + " = ? ";
    private PreparedStatement update_studentAverage_makeThesis_ps;
    public static final String update_studentAverage_removeThesis = "UPDATE " + table_studentAverage +
            " SET " + column_studentAverage_requiresThesis + " = 0  " +
            "WHERE " + column_studentAverage_subject + " = ? " +
            " AND " + column_studentAverage_student + " = ? ";
    private PreparedStatement update_studentAverage_removeThesis_ps;
    public static final String update_studentAverage_s1 = "UPDATE " + table_studentAverage +
            " SET " + column_studentAverage_avg1 + " = ? " +
            " WHERE " + column_studentAverage_student + " = ? " +
            " AND " + column_studentAverage_subject + " = ?";
    private PreparedStatement update_studentAverage_s1_ps;
    public static final String update_studentAverage_s2 = "UPDATE " + table_studentAverage +
            " SET " + column_studentAverage_avg2 + " = ? " +
            " WHERE " + column_studentAverage_student + " = ? " +
            " AND " + column_studentAverage_subject + " = ?";
    private PreparedStatement update_studentAverage_s2_ps;
    public static final String update_studentAverage_autumnExamination = "UPDATE " + table_studentAverage +
            " SET " + column_studentAverage_autumnExamination + " = ? " +
            ", " + column_studentAverage_finalAvg + " = ? " +
            " WHERE " + column_studentAverage_student + " = ? " +
            " AND " + column_studentAverage_subject + " = ?";
    private PreparedStatement update_studentAverage_autumnExamination_ps;
    public static final String update_studentAverage_finalAverage = "UPDATE " + table_studentAverage +
            " SET " + column_studentAverage_finalAvg + " = " +
                "( SELECT ( " + column_studentAverage_avg1 +
                    " + CAST(" + column_studentAverage_avg2 + " AS DECIMAL(3,1))) / 2 " +
                    " FROM " + table_studentAverage +
                    " WHERE " + column_studentAverage_student + " = ? " +
                    " AND " + column_studentAverage_subject + " = ? )" +
            " WHERE " + column_studentAverage_student + " = ? " +
            " AND " + column_studentAverage_subject + " = ?" +
            " AND (" + column_studentAverage_autumnExamination + " = 0 " +
                " OR " + column_studentAverage_autumnExamination + " IS null)";
    private PreparedStatement update_studentAverage_finalAverage_ps;
    public static final String select_studentAverage_thesis = "SELECT " + column_studentAverage_requiresThesis +
            " FROM " + table_studentAverage +
            " WHERE " + column_studentAverage_student + " = ? " +
            " AND " + column_studentAverage_subject + " = ?";
    private PreparedStatement select_studentAverage_thesis_ps;


    public static final String table_absence = "absences";
    public static final String column_absence_id_absence = "id_absences";
    public static final String column_absence_id_student = "id_student";
    public static final String column_absence_id_subject = "id_subject";
    public static final String column_absence_motivated = "motivated";
    public static final String column_absence_ts_absence = "ts_absence";
    public static final String column_absence_ts_motivated = "ts_motivated";
    public static final String column_absenceDate = "absenceDate";
    public static final String insert_absence = "INSERT INTO " + table_absence + " (" +
            column_absence_id_student + " , " +
            column_absence_id_subject + " , " +
            column_absence_motivated + " , " +
            column_absence_ts_absence + " , " +
            column_absenceDate + " ) VALUES (?, ?, ?, ?, ?)";
    private PreparedStatement insert_absence_ps;
    public static final String select_absence_dialog = "SELECT " +
            column_subject_subjectName + " , " +
            column_absenceDate + " , " +
            " CASE WHEN " + column_absence_motivated + " = 0 THEN 'Not motivated' ELSE 'Motivated' END " +
            " FROM " + table_absence + " a " +
            " JOIN " + table_subject + " su ON a." + column_absence_id_subject + " = su." + column_subject_id_subject +
            " WHERE a." + column_absence_id_student + " = ?";
    private PreparedStatement select_absence_dialog_ps;
    public static final String update_absence_motivate = "UPDATE " + table_absence + " SET " +
            column_absence_motivated + " = 1 , " +
            column_absence_ts_motivated + " =  CURRENT_TIMESTAMP " +
            " WHERE " + column_absence_id_student + " = ? " +
            " AND " + column_absenceDate + " = ? " +
            " AND " + column_absence_id_subject + " = ? " +
            " AND " + column_absence_motivated + " = 0";
    private PreparedStatement update_absence_motivate_ps;
    public static final String update_absence_unmotivate = "UPDATE " + table_absence + " SET " +
            column_absence_motivated + " = 0 , " +
            column_absence_ts_motivated + " =  null " +
            " WHERE " + column_absence_id_student + " = ? " +
            " AND " + column_absenceDate + " = ? " +
            " AND " +  column_absence_id_subject + " = ? " +
            " AND " + column_absence_motivated + " = 1";
    private PreparedStatement update_absence_unmotivate_ps;
    public static final String select_absence_main = "SELECT " +
            column_absenceDate + " , " +
            " CASE WHEN " + column_absence_motivated + " = 0 THEN 'Not motivated' ELSE 'Motivated' END " +
            " FROM " + table_absence +
            " WHERE " + column_absence_id_student + " = ?" +
            " AND " + column_absence_id_subject + " = ?";
    private PreparedStatement select_absence_main_ps;
    public static final String select_unmotivated_absences = "SELECT COUNT(*) FROM " + table_absence +
            " WHERE " + column_absence_motivated + " = 0 " +
            " AND " + column_absence_id_student + " = ? " +
            " AND " + column_absence_id_subject + " = ?";
    private PreparedStatement select_unmotivated_absences_ps;
    public static final String select_absences = "SELECT COUNT(*) FROM " + table_absence +
            " WHERE " + column_absence_id_student + " = ? " +
            " AND " + column_absence_id_subject + " = ?";
    private PreparedStatement select_absences_ps;
    public static final String select_checkAbsences = "SELECT COUNT(*) FROM " + table_absence +
            " WHERE " + column_absence_id_student + " = ?" +
            " AND " + column_absence_id_subject + " = ?" +
            " AND " + column_absenceDate + " = ?";
    private PreparedStatement select_checkAbsences_ps;

    public static final String table_points = "points";
    public static final String column_points_id = "id_points";
    public static final String column_points_student = "id_student";
    public static final String column_points_subject = "id_subject";
    public static final String column_points_points = "points";
    public static final String column_points_ts = "ts";
    public static final String column_points_pointsDate = "pointsDate";
    public static final String column_points_special = "specialPoints";
    public static final String insert_points = "INSERT INTO " + table_points + " ( " +
            column_points_student + " , " +
            column_points_subject + " , " +
            column_points_points + " , " +
            column_points_ts + " , " +
            column_points_pointsDate  + " , " +
            column_points_special+ " ) VALUES ( ?, ?, ?, ?, ?, ?)";
    private PreparedStatement insert_points_ps;
    public static final String select_points_main = "SELECT " +
            column_points_id + " , " +
            column_points_points + " , " +
            column_points_pointsDate + " , " +
            column_points_special +
            " FROM " + table_points +
            " WHERE " + column_points_student + " = ?" +
            " AND " + column_points_subject + " = ?";
    private PreparedStatement select_points_main_ps;
    public static final String select_checkPoints = "SELECT COUNT(*) FROM " + table_points +
            " WHERE " + column_points_student + " = ?" +
            " AND " + column_points_subject + " = ? " +
            " AND " + column_points_pointsDate + " = ?";
    private PreparedStatement select_checkPoints_ps;
    public static final String delete_points = "DELETE FROM " + table_points +
            " WHERE " + column_points_id + " = ?";
    private PreparedStatement delete_points_ps;



    private static SQLServerDriver instance = new SQLServerDriver();
    SQLServerDriver() {

    }
    public static SQLServerDriver getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(connectionUrl);
            select_filterClass_ps = conn.prepareStatement(select_filterClass);
            select_filterSubject_ps = conn.prepareStatement(select_filterSubject);
            select_listStudents_ps = conn.prepareStatement(select_listStudents);
            select_student_whereStudentCode_ps = conn.prepareStatement(select_student_whereStudentCode);
            select_id_subject_ps = conn.prepareStatement(select_id_subject);
            insert_absence_ps = conn.prepareStatement(insert_absence);
            select_absence_dialog_ps = conn.prepareStatement(select_absence_dialog);
            update_absence_motivate_ps = conn.prepareStatement(update_absence_motivate);
            update_absence_unmotivate_ps = conn.prepareStatement(update_absence_unmotivate);
            select_absence_main_ps = conn.prepareStatement(select_absence_main);
            select_unmotivated_absences_ps = conn.prepareStatement(select_unmotivated_absences);
            select_absences_ps = conn.prepareStatement(select_absences);
            insert_points_ps = conn.prepareStatement(insert_points);
            select_points_main_ps = conn.prepareStatement(select_points_main);
            select_checkPoints_ps = conn.prepareStatement(select_checkPoints);
            select_checkAbsences_ps = conn.prepareStatement(select_checkAbsences);
            select_studentCard_ps = conn.prepareStatement(select_studentCard);
            select_studentCard_parents_ps = conn.prepareStatement(select_studentCard_parents);
            select_studentAverage_studentDialog_ps = conn.prepareStatement(select_studentAverage_studentDialog);
            update_studentAverage_makeThesis_ps = conn.prepareStatement(update_studentAverage_makeThesis);
            update_studentAverage_removeThesis_ps = conn.prepareStatement(update_studentAverage_removeThesis);
            update_studentAverage_s1_ps = conn.prepareStatement(update_studentAverage_s1);
            update_studentAverage_s2_ps = conn.prepareStatement(update_studentAverage_s2);
            update_studentAverage_autumnExamination_ps = conn.prepareStatement(update_studentAverage_autumnExamination);
            update_studentAverage_finalAverage_ps = conn.prepareStatement(update_studentAverage_finalAverage);
            update_contact_firstName_ps = conn.prepareStatement(update_contact_firstName);
            update_contact_lastName_ps = conn.prepareStatement(update_contact_lastName);
            update_contact_email_ps = conn.prepareStatement(update_contact_email);
            update_contact_phone_ps = conn.prepareStatement(update_contact_phone);
            delete_points_ps = conn.prepareStatement(delete_points);
            delete_studentCard_Parents_ps = conn.prepareStatement(delete_studentCard_Parents);
            select_studentAverage_thesis_ps = conn.prepareStatement(select_studentAverage_thesis);
            System.out.println("Database opened");
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to db: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public ObservableList<String> filterClass() {
        try {
            ResultSet results = select_filterClass_ps.executeQuery();
            ObservableList<String> classes = FXCollections.observableArrayList();
            while (results.next()) {
                TableClass classGradeCode = new TableClass();
                classGradeCode.setClass_gradeCode(results.getString(column_class_gradeCode));
                classes.add(classGradeCode.getClass_gradeCode());
            }
            return classes;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public ObservableList<String> filterSubject(String classGradeCode) {
        try {
            select_filterSubject_ps.setString(1, classGradeCode);
            ResultSet results = select_filterSubject_ps.executeQuery();
            ObservableList<String> subjects = FXCollections.observableArrayList();
            while (results.next()){
                TableSubject subjectName = new TableSubject();
                subjectName.setSubjectName(results.getString(column_subject_subjectName));
                subjects.add(subjectName.getSubjectName());
            }
            return subjects;
        } catch (SQLException e) {

            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public ObservableList<String> showStudents(String classGradeCode) {
        try {
            select_listStudents_ps.setString(1,classGradeCode);
            ResultSet results = select_listStudents_ps.executeQuery();
            ObservableList<String> students = FXCollections.observableArrayList();
            while (results.next()){
                TableContact studentName = new TableContact();
                studentName.setStudentCode(results.getString(column_studentCode));
                studentName.setContact_first_name(results.getString(column_contact_first_name));
                studentName.setContact_last_name(results.getString(column_contact_last_name));
                students.add(
                        studentName.getStudentCode() + ": " +
                        studentName.getContact_first_name() + " " +
                        studentName.getContact_last_name());
            }
            return students;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public int studentIdWhereCode(String studentCode){
        try {
            select_student_whereStudentCode_ps.setString(1, studentCode);
            ResultSet results = select_student_whereStudentCode_ps.executeQuery();
            int id_student = 0;
            while (results.next()) {
                TableStudent student = new TableStudent();
                student.setId_student(results.getInt(column_student_id_student));
                id_student = student.getId_student();
            }
            return id_student;
        } catch (SQLException e){
            System.out.println("Query failed studentIdWhereCode" + e.getMessage());
            return -1;
        }
    }

    public int subjectIdWhereName (String subjectName) {
        try {
            select_id_subject_ps.setString(1, subjectName);
            ResultSet results = select_id_subject_ps.executeQuery();
            int id_subject = 0;
            while (results.next()) {
                TableSubject subject = new TableSubject();
                subject.setId_subject(results.getInt(column_subject_id_subject));
                id_subject = subject.getId_subject();
            }
            return id_subject;
        } catch (SQLException e) {
            System.out.println("Query failed subjectIdWhereName" + e.getMessage());
            return -1;
        }
    }

    public void addAbsence(String studentCode, String subjectCode, Date absenceDate) throws SQLException {
        int id_student = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
        int id_subject = SQLServerDriver.getInstance().subjectIdWhereName(subjectCode);
        select_checkPoints_ps.setInt(1, id_student);
        select_checkPoints_ps.setInt(2, id_subject);
        select_checkPoints_ps.setDate(3, absenceDate);
        ResultSet results = select_checkPoints_ps.executeQuery();
        int points = 0;
        while (results.next()) {
            points = results.getInt(1);
        }

        if (points == 0) {
            insert_absence_ps.setInt(1, id_student);
            insert_absence_ps.setInt(2, id_subject);
            insert_absence_ps.setInt(3, 0);
            insert_absence_ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            insert_absence_ps.setDate(5, absenceDate);
            int affectedRows = insert_absence_ps.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Please check absence insert");
            }
        } else {
            Component frame = null;
            JOptionPane.showMessageDialog(frame, "Points exist on this date.",
                    "Date must be changed!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public ObservableList<TableAbsences> showAbsencesInDialog (int id_student) {
        try {
            select_absence_dialog_ps.setInt(1,id_student);
            ResultSet results = select_absence_dialog_ps.executeQuery();
            ObservableList<TableAbsences> absences = FXCollections.observableArrayList();
            while (results.next()){
                TableAbsences absence = new TableAbsences();
                absence.setSubject_name(results.getString(column_subject_subjectName));
                absence.setAbsenceDate(results.getDate(column_absenceDate));
                absence.setMotivatedDialog(results.getString(3));
                absences.add(absence);

            }
            return absences;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public void motivateAbsences (int Student, Date absenceDate, int Subject) throws SQLException {
        update_absence_motivate_ps.setInt(1, Student);
        update_absence_motivate_ps.setDate(2, absenceDate);
        update_absence_motivate_ps.setInt(3, Subject);
        update_absence_motivate_ps.executeUpdate();
    }

    public void unmotivateAbsences (int Student, Date absenceDate, int Subject) throws SQLException {
        update_absence_unmotivate_ps.setInt(1, Student);
        update_absence_unmotivate_ps.setDate(2, absenceDate);
        update_absence_unmotivate_ps.setInt(3, Subject);
        int affectedRows = update_absence_unmotivate_ps.executeUpdate();

        if (affectedRows < 1){
            System.out.println("Check update");
        }
    }

    public int countAbsences (int Student, int Subject) throws SQLException {
        try {
            select_absences_ps.setInt(1, Student);
            select_absences_ps.setInt(2, Subject);
            ResultSet results = select_absences_ps.executeQuery();
            int absences = -1;
            while (results.next()){
                absences = results.getInt(1);
            }
            return absences;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return -1;
        }
    }

    public int countAbsencesUnmotivated (int Student, int Subject) throws SQLException {
        try {
            select_unmotivated_absences_ps.setInt(1, Student);
            select_unmotivated_absences_ps.setInt(2, Subject);
            ResultSet results = select_unmotivated_absences_ps.executeQuery();
            int absences = -1;
            while (results.next()){
                absences = results.getInt(1);
            }
            return absences;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return -1;
        }
    }

    public ObservableList<TableAbsences> showAbsencesInMain (int id_student, int id_subject) {
        try {
            select_absence_main_ps.setInt(1,id_student);
            select_absence_main_ps.setInt(2, id_subject);
            ResultSet results = select_absence_main_ps.executeQuery();
            ObservableList<TableAbsences> absences = FXCollections.observableArrayList();
            while (results.next()){
                TableAbsences absence = new TableAbsences();
                absence.setAbsenceDate(results.getDate(column_absenceDate));
                absence.setMotivatedDialog(results.getString(2));
                absences.add(absence);
            }
            return absences;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public void addPoints(String studentCode, String subjectCode, int points, Date pointsDate, String special ) throws SQLException {
        int id_student = SQLServerDriver.getInstance().studentIdWhereCode(studentCode);
        int id_subject = SQLServerDriver.getInstance().subjectIdWhereName(subjectCode);
        select_checkAbsences_ps.setInt(1, id_student);
        select_checkAbsences_ps.setInt(2, id_subject);
        select_checkAbsences_ps.setDate(3, pointsDate);
        ResultSet results = select_checkAbsences_ps.executeQuery();
        int absences = -1;
        while (results.next()){
            absences = results.getInt(1);
        }

        if(absences == 0){
            insert_points_ps.setInt(1, id_student);
            insert_points_ps.setInt(2, id_subject);
            insert_points_ps.setInt(3, points);
            insert_points_ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            insert_points_ps.setDate(5, pointsDate);
            insert_points_ps.setString(6, special);
            int affectedRows = insert_points_ps.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Please check absence insert");
            }
        } else {
            Component frame = null;
            JOptionPane.showMessageDialog(frame, "Absence exists on this date.",
                    "Date must be changed!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public ObservableList<TablePoints> showPointsInMain (int id_student, int id_subject) {
        try {
            select_points_main_ps.setInt(1,id_student);
            select_points_main_ps.setInt(2, id_subject);
            ResultSet results = select_points_main_ps.executeQuery();
            ObservableList<TablePoints> points = FXCollections.observableArrayList();
            while (results.next()){
                TablePoints point = new TablePoints();
                point.setId_points(results.getInt(column_points_id));
                point.setPoints(results.getInt(column_points_points));
                point.setPointsDate(results.getDate(column_points_pointsDate));
                point.setSpecialPoints(results.getString(column_points_special));
                points.add(point);
            }
            return points;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public TableContact showStudentCard (int student) {
        try {
            select_studentCard_ps.setInt(1,student);
            ResultSet results = select_studentCard_ps.executeQuery();
            TableContact contact = new TableContact();
            while (results.next()){
                contact.setContact_first_name(results.getString(column_contact_first_name));
                contact.setContact_last_name(results.getString(column_contact_last_name));
                contact.setContact_email(results.getString(column_contact_email));
                contact.setContact_phone1(results.getString(column_contact_phone1));
            }
            return contact;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public ObservableList<TableContact> showStudentCardParents(int student) {
        try {
            select_studentCard_parents_ps.setInt(1,student);
            ResultSet results = select_studentCard_parents_ps.executeQuery();
            ObservableList<TableContact> parents = FXCollections.observableArrayList();
            while (results.next()){
                TableContact parent = new TableContact();
                parent.setId_contact(results.getInt(column_contact_id_contact));
                parent.setContact_first_name(results.getString(column_contact_first_name));
                parent.setContact_last_name(results.getString(column_contact_last_name));
                parent.setContact_email(results.getString(column_contact_email));
                parent.setContact_phone1(results.getString(column_contact_phone1));
                parents.add(parent);
            }
            return parents;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public ObservableList<TableStudentAverage> showStudentCardAverages(int student) {
        try {
            select_studentAverage_studentDialog_ps.setInt(1,student);
            ResultSet results = select_studentAverage_studentDialog_ps.executeQuery();
            ObservableList<TableStudentAverage> averages = FXCollections.observableArrayList();
            while (results.next()){
                TableStudentAverage average = new TableStudentAverage();
                average.setId_student(results.getInt(column_studentAverage_student));
                average.setId_subject(results.getInt(column_studentAverage_subject));
                average.setSubjectName(results.getString(3));
                average.setAverageSem1(results.getInt(column_studentAverage_avg1));
                average.setAverageSem2(results.getInt(column_studentAverage_avg2));
                average.setAutumnExamination(results.getInt(column_studentAverage_autumnExamination));
                average.setFinalAverage(results.getFloat(column_studentAverage_finalAvg));
                average.setThesisRequiredView(results.getString(8));
                averages.add(average);
            }
            return averages;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return null;
        }
    }

    public void makeThesis (int subject, int student) {
        try {
            update_studentAverage_makeThesis_ps.setInt(1, subject);
            update_studentAverage_makeThesis_ps.setInt(2, student);
            update_studentAverage_makeThesis_ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
        }
    }

    public void removeThesis (int subject, int student) {
        try {
            update_studentAverage_removeThesis_ps.setInt(1, subject);
            update_studentAverage_removeThesis_ps.setInt(2, student);
            update_studentAverage_removeThesis_ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
        }
    }

    public void updateAvgS1 (String student, String subject, int average) throws SQLException {
        int studentId = SQLServerDriver.getInstance().studentIdWhereCode(student);
        int subjectId = SQLServerDriver.getInstance().subjectIdWhereName(subject);
        update_studentAverage_s1_ps.setInt(1, average);
        update_studentAverage_s1_ps.setInt(2, studentId);
        update_studentAverage_s1_ps.setInt(3, subjectId);
        update_studentAverage_s1_ps.executeUpdate();
    }

    public void updateAvgS2 (String student, String subject, int average) throws SQLException {
        int studentId = SQLServerDriver.getInstance().studentIdWhereCode(student);
        int subjectId = SQLServerDriver.getInstance().subjectIdWhereName(subject);
        update_studentAverage_s2_ps.setInt(1, average);
        update_studentAverage_s2_ps.setInt(2, studentId);
        update_studentAverage_s2_ps.setInt(3, subjectId);
        update_studentAverage_s2_ps.executeUpdate();
    }

    public void updateAutumnExamination (String student, String subject, int autumnExamination) throws SQLException {
        int studentId = SQLServerDriver.getInstance().studentIdWhereCode(student);
        int subjectId = SQLServerDriver.getInstance().subjectIdWhereName(subject);
        float autumnExaminationD = autumnExamination;
        update_studentAverage_autumnExamination_ps.setInt(1, autumnExamination);
        update_studentAverage_autumnExamination_ps.setFloat(2, autumnExaminationD);
        update_studentAverage_autumnExamination_ps.setInt(3, studentId);
        update_studentAverage_autumnExamination_ps.setInt(4, subjectId);
        update_studentAverage_autumnExamination_ps.executeUpdate();
    }

    public void updateFinalAverage (String student, String subject) throws SQLException {
        int studentId = SQLServerDriver.getInstance().studentIdWhereCode(student);
        int subjectId = SQLServerDriver.getInstance().subjectIdWhereName(subject);
        update_studentAverage_finalAverage_ps.setInt(1, studentId);
        update_studentAverage_finalAverage_ps.setInt(2, subjectId);
        update_studentAverage_finalAverage_ps.setInt(3, studentId);
        update_studentAverage_finalAverage_ps.setInt(4, subjectId);
        update_studentAverage_finalAverage_ps.executeUpdate();
    }

    public void updateFirstNameStudent (int student, String firstName) throws SQLException {
        update_contact_firstName_ps.setString(1, firstName);
        update_contact_firstName_ps.setInt(2, student);
        update_contact_firstName_ps.executeUpdate();

    }

    public void updateLastNameStudent (int student, String lastName) throws SQLException {
        update_contact_lastName_ps.setString(1, lastName);
        update_contact_lastName_ps.setInt(2, student);
        update_contact_lastName_ps.executeUpdate();
    }

    public void updateEmailStudent (int student, String email) throws SQLException {
        update_contact_email_ps.setString(1, email);
        update_contact_email_ps.setInt(2, student);
        update_contact_email_ps.executeUpdate();
    }

    public void deleteParent (int parent, int student) throws SQLException {
        delete_studentCard_Parents_ps.setInt(1,parent);
        delete_studentCard_Parents_ps.setInt(2,student);
        delete_studentCard_Parents_ps.executeUpdate();
    }

    public void updatePhoneStudent (int student, String phone) throws SQLException {
        update_contact_phone_ps.setString(1, phone);
        update_contact_phone_ps.setInt(2, student);
        update_contact_phone_ps.executeUpdate();
    }

    public void deletePoints (int points) throws SQLException {
        delete_points_ps.setInt(1,points);
        delete_points_ps.executeUpdate();
    }

    public int isThesis(int student, int subject) throws SQLException {
        try {
            select_studentAverage_thesis_ps.setInt(1, student);
            select_studentAverage_thesis_ps.setInt(2, subject);
            ResultSet results =  select_studentAverage_thesis_ps.executeQuery();
            int isThesis = -1;
            while(results.next()){
                isThesis = results.getInt(1);
            }
            return isThesis;
        } catch (SQLException e) {
            System.out.println("Query failed " + e.getMessage());
            return -1;
        }
    }

    public void close() {
        try {
            if (select_filterClass_ps != null) {
                select_filterClass_ps.close();
            }
            if (select_filterSubject_ps != null) {
                select_filterSubject_ps.close();
            }
            if (select_listStudents_ps != null) {
                select_listStudents_ps.close();
            }
            if (select_student_whereStudentCode_ps != null) {
                select_student_whereStudentCode_ps.close();
            }
            if (select_id_subject_ps != null) {
                select_id_subject_ps.close();
            }
            if (insert_absence_ps != null) {
                insert_absence_ps.close();
            }
            if (select_absence_dialog_ps != null) {
                select_absence_dialog_ps.close();
            }
            if (update_absence_motivate_ps != null) {
                update_absence_motivate_ps.close();
            }
            if (update_absence_unmotivate_ps != null) {
                update_absence_unmotivate_ps.close();
            }
            if (select_absence_main_ps != null) {
                select_absence_main_ps.close();
            }
            if (select_unmotivated_absences_ps != null) {
                select_unmotivated_absences_ps.close();
            }
            if (select_absences_ps != null) {
                select_absences_ps.close();
            }
            if (insert_points_ps != null) {
                insert_points_ps.close();
            }
            if (select_points_main_ps != null) {
                select_points_main_ps.close();
            }
            if (select_checkPoints_ps != null) {
                select_checkPoints_ps.close();
            }
            if (select_checkAbsences_ps != null) {
                select_checkAbsences_ps.close();
            }
            if (select_studentCard_ps != null) {
                select_studentCard_ps.close();
            }
            if (select_studentCard_parents_ps != null) {
                select_studentCard_parents_ps.close();
            }
            if (select_studentAverage_studentDialog_ps != null) {
                select_studentAverage_studentDialog_ps.close();
            }
            if (update_studentAverage_makeThesis_ps != null) {
                update_studentAverage_makeThesis_ps.close();
            }
            if (update_studentAverage_removeThesis_ps != null) {
                update_studentAverage_removeThesis_ps.close();
            }
            if (update_studentAverage_s1_ps != null) {
                update_studentAverage_s1_ps.close();
            }
            if (update_studentAverage_s2_ps != null) {
                update_studentAverage_s2_ps.close();
            }
            if (update_studentAverage_autumnExamination_ps != null) {
                update_studentAverage_autumnExamination_ps.close();
            }
            if (update_studentAverage_finalAverage_ps != null) {
                update_studentAverage_finalAverage_ps.close();
            }
            if (update_contact_firstName_ps != null) {
                update_contact_firstName_ps.close();
            }
            if (update_contact_lastName_ps != null) {
                update_contact_lastName_ps.close();
            }
            if (update_contact_email_ps != null) {
                update_contact_email_ps.close();
            }
            if (update_contact_phone_ps != null) {
                update_contact_phone_ps.close();
            }
            if (delete_points_ps != null) {
                delete_points_ps.close();
            }
            if (delete_studentCard_Parents_ps != null) {
                delete_studentCard_Parents_ps.close();
            }
            if (select_studentAverage_thesis_ps != null) {
                select_studentAverage_thesis_ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
        System.out.println("Database closed");
    }
}
