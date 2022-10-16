import java.util.ArrayList;

public class Student {
//    ArrayList<course> courseList = new ArrayList<>();
    String ID;
    String firstName;
    String lastName;
    String emailAddress;
    String passwords;

    public Student(String ID, String firstName, String lastName, String emailAddress, String passwords) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.passwords = passwords;
    }
}
