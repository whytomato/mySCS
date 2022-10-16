import java.util.stream.StreamSupport;

public class database {
    static String[] ID = new String[200];
    static String[] firstName = new String[200];
    static String[] lastName = new String[200];
    static String[] emailAddress = new String[200];
    static String[] passwords = new String[200];
    static String[] courseID = new String[200];
    static String[] wareID = new String[200];
    static String[] taskID =new String[200];
    //    static String[] wareName=new String[200];
    static int num = 0;
    static int courseNum = 0;
    static int wareNum=0;
    static int taskNUM=0;
    static int idNum;

    public static void store(String id, String f, String l, String e, String p) {
        ID[num] = id;
        firstName[num] = f;
        lastName[num] = l;
        emailAddress[num] = e;
        passwords[num] = p;
        num++;
    }

    public static void store(String ID) {
        courseID[courseNum] = ID;
        courseNum++;
    }
    public static void storeWare(String ID){
        wareID[wareNum]=ID;
        wareNum++;
    }
    public static void storeTask(String ID){
        taskID[taskNUM]=ID;
        taskNUM++;
    }

    public static boolean search(String id) {
        for (int i = 0; i < ID.length; i++) {
            if (id.equals(ID[i])) {
                idNum = i;
                return true;
            }
        }
        return false;
    }

    public static boolean passSearch(String pass) {
        return passwords[idNum].equals(pass);
    }

    public static boolean courseIDSearch(String id) {
        for (String s : courseID) {
            if (id.equals(s)) return true;
        }
        System.out.println("course id not exist");
        return false;
    }

    public static boolean wareIDSearch(String id) {
        for (String s : wareID) {
            if (id.equals(s))
                return true;
        }
        System.out.println("ware id not exist");
        return false;
    }
    public static boolean taskIDSearch(String id){
        for (String s : taskID) {
            if (id.equals(s))
                return true;
        }
        System.out.println("task id not exist");
        return false;
    }
}