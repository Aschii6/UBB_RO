package ex2.dto;

public class GradeDTO {
    private String studentName;
    private String assignmentId;
    private double grade;
    private String teacher;

    public GradeDTO(String studentName, String assignmentId, double grade, String teacher) {
        this.studentName = studentName;
        this.assignmentId = assignmentId;
        this.grade = grade;
        this.teacher = teacher;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "GradeDTO{" +
                "studentName='" + studentName + '\'' +
                ", assignmentId='" + assignmentId + '\'' +
                ", grade=" + grade +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
