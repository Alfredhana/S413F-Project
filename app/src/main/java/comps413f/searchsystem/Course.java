package comps413f.searchsystem;

import java.io.Serializable;

// Model representing course
public class Course implements Serializable {
    private String courseCode;
    private String courseTile;
    private String date;
    private int time;

    public Course(String courseCode, String courseTile, String date, int time) {
        this.courseCode = courseCode;
        this.courseTile = courseTile;
        this.date = date;
        this.time = time;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTile() {
        return courseTile;
    }

    public String getDate() { return date; }

    public int getTime() {
        return time;
    }
}
