package ex2.domain;

import ex2.utils.Constants;

import java.time.LocalDate;

public class Grade {

    private Student student;
    private Assignment assignment;
    private double value;
    private LocalDate date;
    private String teacher;

    public Grade(Student student, Assignment assignment, double value, LocalDate date, String teacher) {
        this.student = student;
        this.assignment = assignment;
        this.value = value;
        this.date = date;
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "student=" + student +
                ", assignment=" + assignment +
                ", value=" + value +
                ", date=" + date.format(Constants.DATE_FORMATTER) +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}