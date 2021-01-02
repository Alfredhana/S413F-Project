package comps413f.searchsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Model representing list of courses
public class CourseList implements Serializable {
    static String COURSE_CODE = "CODE";
    static String COURSE_TITLE = "TITLE";

    private static List<Course> courseList = new ArrayList<>();

    public CourseList() {
        createCourseList();
    }

    // Creates and returns a list of data items
    public static List<Map<String, String>> getCourseList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Course course : courseList) {
            Map<String, String> map = new HashMap<>();
            map.put(COURSE_CODE, course.getCourseCode());
            map.put(COURSE_TITLE, course.getCourseTile());

            list.add(map);
        }
        return list;
    }

    public static Map<String, String> getCourseMap(Course course){
        Map<String, String> map = new HashMap<>();
        map.put(COURSE_CODE, course.getCourseCode());
        map.put(COURSE_TITLE, course.getCourseTile());
        return map;
    }

    // Create list of course
    private static void createCourseList() {
        courseList.clear();

        // Core
        courseList.add(new Course("COMPS102F", "Introduction to Information and Communication Technology I", "Mon", 1600));

        // Project
        courseList.add(new Course("COMPS451F", "Computing Project", "Morning", 2000));
        courseList.add(new Course("COMPS456F", "Software System Development Project", "Mon", 1200));

        // Elective
        courseList.add(new Course("COMPS362F", "Concurrent and Network Programming", "Tue", 1900));

    }

    // Get course with course code
    public static Course getCourse(String courseCode) {
        for (Course course : courseList) {
            if (course.getCourseCode().equals(courseCode))
                return course;
        }
        return null;
    }
}
