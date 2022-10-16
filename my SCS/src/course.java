import java.util.ArrayList;

public class course {
    String courseID;
    String courseName;
    int teacherNum;
    int assistNum;
    int studentNum;
    //    ArrayList<String> teacherList = new ArrayList<>();
//    ArrayList<String> assistList = new ArrayList<>();
    ArrayList<String> adminList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    ArrayList<ware> wareList = new ArrayList<>();
    ArrayList<task> taskList = new ArrayList<>();

    public course(String courseID, String courseName) {
        this.courseID = courseID;
        this.courseName = courseName;
    }
}