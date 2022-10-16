import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class Command {
    static boolean LOG = false;
    static boolean selected = false;
    static boolean isAssist = false;
    static course Course;
    static String ID;
    static String CourseID;
    static String searchCourse;
    static ArrayList<course> courseArrayList = new ArrayList<>();
    static ArrayList<Teacher> teacherArrayList = new ArrayList<>();
    static ArrayList<Student> studentArrayList = new ArrayList<>();
    static ArrayList<assist> assistArrayList = new ArrayList<>();

    public static void register(String[] register, int num) {
        if (num != 7) {
            System.out.println("arguments illegal");
        } else {
            if (LOG) {
                System.out.println("already logged in");
            } else {
                String id = register[1];
                String firstName = register[2];
                String lastName = register[3];
                String address = register[4];
                String passwords = register[5];
                String confirmPass = register[6];
                if (database.search(id)) {
                    System.out.println("user id duplication");
                } else {
                    if (idLegal(id) && nameLegal(firstName, lastName) && addressLegal(address) && passLegal(passwords, confirmPass)) {
                        database.store(id, firstName, lastName, address, passwords);
                        if (Integer.parseInt(id) <= 99999) {
                            Teacher x = new Teacher(id, firstName, lastName, address, passwords);
                            teacherArrayList.add(x);
                        } else {
                            Student x = new Student(id, firstName, lastName, address, passwords);
                            studentArrayList.add(x);
                        }
                        System.out.println("register success");
                    }
                }
            }
        }
    }

    public static void login(String[] login, int num) {
        if (num != 3) {
            System.out.println("arguments illegal");
        } else {
            if (LOG) {
                System.out.println("already logged in");
            } else {
                String id = login[1];
                String pass = login[2];
                if (idLegal(id)) {
                    if (!database.search(id)) System.out.println("user id not exist");
                    else {
                        if (database.passSearch(pass)) {
                            if (id.length() == 5) {
                                System.out.println("Hello Professor " + database.lastName[database.idNum] + "~");
                            } else {
                                System.out.println("Hello " + database.firstName[database.idNum] + "~");
                            }
                            ID = id;
                            LOG = true;
                        } else System.out.println("wrong password");
                    }
                }
            }
        }
    }

    public static void addCourse(String[] addCourse, int num) {
        if (num != 3) {
            System.out.println("arguments illegal");
        } else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    String courseID = addCourse[1];
                    String courseName = addCourse[2];
                    if (courseIDLegal(courseID) && !searchCourseInCourse(courseID) && courseNameLegal(courseName)) {
                        System.out.println("add course success");
                        course x = new course(courseID, courseName);
//                        Teacher = findTeacher(ID);
                        courseArrayList.add(x);
//                        courseArrayList.get(findCourseID(courseID)).teacherList.add(ID);
                        courseArrayList.get(findCourseID(courseID)).adminList.add(ID);
                        teacherArrayList.get(searchTeacher(ID)).courseList.add(x);
//                        teacherArrayList.get(searchTeacher(ID)).courseList.get(searchCourseID(courseID)).teacherNum++;
                        courseArrayList.get(findCourseID(courseID)).teacherNum++;
                        database.store(courseID);
                    }
                }
            }
        }
    }//find方法在课程list内部

    public static void removeCourse(String[] removeCourse, int num) {
        if (num != 2) {
            System.out.println("arguments illegal");
        } else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    String courseID = removeCourse[1];
                    if (courseIDLegal(courseID) && database.courseIDSearch(courseID) && searchCourseInTeacher(courseID) != -1) {
                        teacherArrayList.get(searchTeacher(ID)).courseList.remove(searchCourseInTeacher(courseID));
                        System.out.println("remove course success");
                    }
                }
            }
        }
    }//search方法是在教师名下

    public static void listCourse(int num) {
        if (num != 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    if (teacherArrayList.get(searchTeacher(ID)).courseList.size() == 0) {
                        System.out.println("course not exist");
                    } else {
                        int a = searchTeacher(ID);
                        String courseID;
                        String courseName;
                        int teacherNum;
                        int assistNum;
                        int studentNum;
                        teacherArrayList.get(a).courseList.sort(Comparator.comparing(o -> o.courseID));
                        for (int i = 0; i < teacherArrayList.get(a).courseList.size(); i++) {
                            courseID = teacherArrayList.get(a).courseList.get(i).courseID;
                            courseName = teacherArrayList.get(a).courseList.get(i).courseName;
                            teacherNum = courseArrayList.get(findCourseID(courseID)).teacherNum;
                            assistNum = courseArrayList.get(findCourseID(courseID)).assistNum;
                            studentNum = courseArrayList.get(findCourseID(courseID)).studentNum;
                            System.out.println("[ID:" + courseID + "] [Name:" + courseName + "] [TeacherNum:" + teacherNum + "] [AssistantNum:" + assistNum + "] [StudentNum:" + studentNum + "]");
                        }
                    }
                }
            }
        }
    }

    public static void selectCourse(String[] select, int num) {
        if (num != 2) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    String courseID = select[1];
                    if (courseIDLegal(courseID) && database.courseIDSearch(courseID)) {
                        if (ID.length() == 5 && searchCourseInTeacher(courseID) != -1) {
                            System.out.println("select course success");
                            CourseID = courseID;
                            selected = true;
                            Course = (course) findCourse(courseID);//创建课程实例
                        } else if (ID.length() != 5 && searchCourseInAssist(courseID) != -1) {
                            System.out.println("select course success");
                            CourseID = courseID;
                            selected = true;
                            Course = (course) findCourse(courseID);//创建课程实例
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void addAdmin(String[] add, int num) {
        int flag = 0;
        if (num <= 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String id;
                        for (int i = 1; i < num; i++) {
                            id = add[i];
                            if (!idLegal(id)) {
                                flag = 1;
                                break;
                            } else if (idLegal(id) && !database.search(id)) {
                                flag = 1;
                                System.out.println("user id not exist");
                                break;
                            }
                        }
                        if (flag == 0) {
                            for (int i = 1; i < num; i++) {
                                id = add[i];
                                if (idLegal(id) && database.search(id) && !searchAdminInCourse(id)) {
                                    if (id.length() == 5) {
                                        teacherArrayList.get(searchTeacher(id)).courseList.add(Course);
                                        courseArrayList.get(findCourseID(CourseID)).teacherNum++;
                                        courseArrayList.get(findCourseID(CourseID)).adminList.add(id);
                                    } else {
                                        database.search(id);
                                        int t = database.idNum;
                                        assist x = new assist(id, database.firstName[t], database.lastName[t], database.emailAddress[t], database.passwords[t]);
                                        assistArrayList.add(x);
                                        assistArrayList.get(searchAssist(id)).courseList.add(Course);
                                        courseArrayList.get(findCourseID(CourseID)).assistNum++;
                                        courseArrayList.get(findCourseID(CourseID)).adminList.add(id);
                                    }
                                }
                            }
                            System.out.println("add admin success");
                        }
                    }
                }
            }
        }
    }

    public static void removeAdmin(String[] remove, int num) {
        if (num != 2) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    if (teacherArrayList.get(searchTeacher(ID)).courseList.size() == 0)
                        System.out.println("no course selected");
                    else {
                        String id = remove[1];
                        if (idLegal(id)) {
                            if (database.search(id) && searchAdminInCourse(id)) {
                                courseArrayList.get(findCourseID(CourseID)).adminList.remove(searchAdminIDInCourse(id));
                                System.out.println("remove admin success");
                                if (id.length() == 5) {
                                    courseArrayList.get(findCourseID(CourseID)).teacherNum--;
                                    teacherArrayList.get(searchTeacher(id)).courseList.remove(Course);
                                } else {
                                    courseArrayList.get(findCourseID(CourseID)).assistNum--;
                                    assistArrayList.get(searchAssist(id)).courseList.remove(Course);
                                    if (assistArrayList.get(searchAssist(id)).courseList.size() == 0) {
                                        assistArrayList.remove(searchAssist(id));
                                    }
                                }
                            } else {
                                System.out.println("user id not exist");
                            }
                        }
                    }
                }
            }
        }
    }

    public static void listAdmin(int num) {
        if (num != 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        courseArrayList.get(findCourseID(CourseID)).adminList.sort(Comparator.comparing(o -> o));
                        String id;
                        String name;
                        String email;
                        for (int i = 0; i < courseArrayList.get(findCourseID(CourseID)).adminList.size(); i++) {
                            id = courseArrayList.get(findCourseID(CourseID)).adminList.get(i);
                            database.search(id);
                            name = database.lastName[database.idNum] + " " + database.firstName[database.idNum];
                            email = database.emailAddress[database.idNum];
                            System.out.print("[ID:" + id + "] [Name:" + name + "] [Type:");
                            if (id.length() == 5) System.out.print("Professor");
                            else System.out.print("Assistant");
                            System.out.println("] [Email:" + email + "]");
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void changeRole(int num) {
        if (num != 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5 && searchAssist(ID) != -1) {
                    isAssist = !isAssist;
                    if (isAssist) System.out.println("change into Assistant success");
                    else System.out.println("change into Student success");
                } else System.out.println("permission denied");
            }
        }
    }

    public static void addWare(String[] ware, int num) {
        if (num != 3) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String wareID = ware[1];
                        String wareName = ware[2];
                        if (wareIDLegal(wareID)) {
                            if (!searchCourse.equals(CourseID)) System.out.println("ware id illegal");
                            else {
                                if (searchWareInCourse(wareID)) System.out.println("ware id duplication");
                                else {
                                    if (wareNameLegal(wareName)) {
                                        System.out.println("add ware success");
                                        ware x = new ware(wareID, wareName);
                                        database.storeWare(wareID);
                                        courseArrayList.get(findCourseID(CourseID)).wareList.add(x);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeWare(String[] ware, int num) {
        if (num != 2) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() != 5) System.out.println("permission denied");
                else {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String wareID = ware[1];
                        if (wareIDLegal(wareID) && database.wareIDSearch(wareID)) {
                            if (!searchWareInCourse(wareID)) System.out.println("ware id not exist");
                            else {
                                courseArrayList.get(findCourseID(CourseID)).wareList.remove(searchWareIDInCourse(wareID));
                                System.out.println("remove ware success");
                            }
                        }
                    }
                }
            }
        }
    }

    public static void listWare(int num) {
        if (num != 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        courseArrayList.get(findCourseID(CourseID)).wareList.sort(Comparator.comparing(o -> o.wareID));
                        String wareID;
                        String wareName;
                        for (int i = 0; i < courseArrayList.get(findCourseID(CourseID)).wareList.size(); i++) {
                            wareID = courseArrayList.get(findCourseID(CourseID)).wareList.get(i).wareID;
                            wareName = courseArrayList.get(findCourseID(CourseID)).wareList.get(i).wareName;
                            System.out.println("[ID:" + wareID + "] [Name:" + wareName + "]");
                        }
                    }
                } else {
                    System.out.println("permission denied");
                }
            }
        }
    }

    public static void addTask(String[] task, int num) {
        if (num != 5) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String taskID = task[1];
                        String taskName = task[2];
                        String startTime = task[3];
                        String endTime = task[4];
                        if (taskIDLegal(taskID)) {
                            if (!searchCourse.equals(CourseID)) System.out.println("task id illegal");
                            else {
                                if (searchTaskInCourse(taskID)) System.out.println("task id duplication");
                                else {
                                    if (taskNameLegal(taskName) && taskTimeLegal(startTime) && taskTimeLegal(endTime)) {
                                        String time1 = startTime.substring(0, 4) + startTime.substring(5, 7) + startTime.substring(8, 10) + startTime.substring(11, 13) + startTime.substring(14, 16) + startTime.substring(17, 19);
                                        String time2 = endTime.substring(0, 4) + endTime.substring(5, 7) + endTime.substring(8, 10) + endTime.substring(11, 13) + endTime.substring(14, 16) + endTime.substring(17, 19);
                                        if (time2.compareTo(time1) > 0) {
                                            System.out.println("add task success");
                                            task x = new task(taskID, taskName, startTime, endTime);
                                            database.storeTask(taskID);
                                            courseArrayList.get(findCourseID(CourseID)).taskList.add(x);
                                        } else System.out.println("task time illegal");
                                    }
                                }
                            }
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void removeTask(String[] task, int num) {
        if (num != 2) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String taskID = task[1];
                        if (taskIDLegal(taskID) && database.taskIDSearch(taskID)) {
                            if (!searchTaskInCourse(taskID)) System.out.println("task id not exist");
                            else {
                                courseArrayList.get(findCourseID(CourseID)).taskList.remove(searchTaskIDInCourse(taskID));
                                System.out.println("remove task success");
                            }
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void listTask(int num) {
        if (num != 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        courseArrayList.get(findCourseID(CourseID)).taskList.sort(Comparator.comparing(o -> o.taskID));
                        String taskID;
                        String taskName;
                        int receiveNum;
                        String startTime;
                        String endTime;
                        for (int i = 0; i < courseArrayList.get(findCourseID(CourseID)).taskList.size(); i++) {
                            taskID = courseArrayList.get(findCourseID(CourseID)).taskList.get(i).taskID;
                            taskName = courseArrayList.get(findCourseID(CourseID)).taskList.get(i).taskName;
                            receiveNum = courseArrayList.get(findCourseID(CourseID)).taskList.get(i).receiveNum;
                            startTime = courseArrayList.get(findCourseID(CourseID)).taskList.get(i).startTime;
                            endTime = courseArrayList.get(findCourseID(CourseID)).taskList.get(i).endTime;
                            System.out.println("[ID:" + taskID + "] [Name:" + taskName + "] [ReceiveNum:" + receiveNum + "] [StartTime:" + startTime + "] [EndTime:" + endTime + "]");
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void addStudent(String[] addStudent, int num) {
        int flag = 0;
        if (num <= 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String id;
                        for (int i = 1; i < num; i++) {
                            id = addStudent[i];
                            if (!idLegal(id)) {
                                flag = 1;
                                break;
                            } else if (idLegal(id) && !database.search(id)) {
                                flag = 1;
                                System.out.println("user id not exist");
                                break;
                            }
                            if (id.length() == 5) {
                                flag = 1;
                                System.out.println("I'm professor rather than student!");
                                break;
                            }
                        }
                        if (flag == 0) {
                            for (int i = 1; i < num; i++) {
                                id = addStudent[i];
                                if (idLegal(id) && database.search(id) && !searchStudentInCourse(id)) {
                                    database.search(id);
                                    int t = database.idNum;
                                    Student x = new Student(id, database.firstName[t], database.lastName[t], database.emailAddress[t], database.passwords[t]);
                                    studentArrayList.add(x);
//                                    studentArrayList.get(searchStudent(id)).courseList.add(Course);
                                    courseArrayList.get(findCourseID(CourseID)).studentList.add(id);
                                }
                            }
                            System.out.println("add student success");
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void removeStudent(String[] removeStudent, int num) {
        if (num != 2) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        String id = removeStudent[1];
                        if (idLegal(id)) {
                            if (database.search(id) && searchStudentInCourse(id)) {
                                courseArrayList.get(findCourseID(CourseID)).studentList.remove(searchStudentIDInCourse(id));
                                System.out.println("remove student success");
//                                studentArrayList.get(searchStudent(id)).courseList.remove(Course);
                            } else {
                                System.out.println("user id not exist");
                            }
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void listStudent(int num) {
        if (num != 1) System.out.println("arguments illegal");
        else {
            if (!LOG) System.out.println("not logged in");
            else {
                if (ID.length() == 5 || isAssist) {
                    if (!selected) System.out.println("no course selected");
                    else {
                        courseArrayList.get(findCourseID(CourseID)).studentList.sort(Comparator.comparing(o -> o));
                        String id;
                        String name;
                        String email;
                        for (int i = 0; i < courseArrayList.get(findCourseID(CourseID)).studentList.size(); i++) {
                            id = courseArrayList.get(findCourseID(CourseID)).studentList.get(i);
                            database.search(id);
                            name = database.lastName[database.idNum] + " " + database.firstName[database.idNum];
                            email = database.emailAddress[database.idNum];
                            System.out.println("[ID:" + id + "] [Name:" + name + "] [Email:" + email + "]");
                        }
                    }
                } else System.out.println("permission denied");
            }
        }
    }

    public static void logout(int num) {
        if (num != 1) {
            System.out.println("arguments illegal");
        } else {
            if (!LOG) {
                System.out.println("not logged in");
            } else {
                System.out.println("Bye~");
                LOG = false;
                selected = false;
                isAssist = false;
                ID = "";
                CourseID = "";
            }
        }
    }

    public static void printInfo(String[] print, int num) {
        if (!(num == 1 || num == 2)) {
            System.out.println("arguments illegal");
        } else {
            if (!LOG) {
                System.out.println("login first");
            } else {
                if (num == 1) {
                    if (database.search(ID)) print(ID);
                } else {
                    if (ID.length() != 5) System.out.println("permission denied");
                    else {
                        String id = print[1];
                        if (idLegal(id)) {
                            if (!database.search(id)) {
                                System.out.println("user id not exist");
                            } else print(id);
                        }
                    }
                }
            }
        }
    }

    private static void print(String id2) {
        System.out.println("Name: " + database.firstName[database.idNum] + ' ' + database.lastName[database.idNum]);
        System.out.println("ID: " + id2);
        if (id2.length() == 5) {
            System.out.println("Type: Professor");
        } else System.out.println("Type: Student");
        System.out.println("Email: " + database.emailAddress[database.idNum]);
    }

    public static boolean quit(int num) {
        if (num != 1) System.out.println("arguments illegal");
        return num == 1;
    }

    public static boolean idLegal(String id) {
        int idNum;
        if (id.length() == 5) {
            for (int i = 0; i < id.length(); i++) {
                if (!Character.isDigit(id.charAt(i))) {
                    System.out.println("user id illegal");
                    return false;
                }
            }
            idNum = Integer.parseInt(id);
            if (idNum == 0) {
                System.out.println("user id illegal");
                return false;
            }
            return idNum >= 1;
        } else if (id.length() == 8) {
            for (int i = 0; i < id.length(); i++) {
                if (!Character.isDigit(id.charAt(i))) {
                    System.out.println("user id illegal");
                    return false;
                }
            }
            idNum = Integer.parseInt(id);
            int yearNum = idNum / 1000000;
            int acaNum = (idNum / 10000) % 100;
            int classNum = (idNum / 1000) % 10;
            int remainNum = idNum % 1000;
            if (yearNum >= 17 && yearNum <= 22 && acaNum >= 1 && acaNum <= 43 && classNum >= 1 && classNum <= 6 && remainNum >= 1) {
                return true;
            } else {
                System.out.println("user id illegal");
                return false;
            }
        } else if (id.length() == 9) {
            String str = id.substring(0, 2);
            String strNum = id.substring(2, 9);
            if (str.equals("SY") || str.equals("ZY") || str.equals("BY")) {
                for (int i = 0; i < strNum.length(); i++) {
                    if (!Character.isDigit(strNum.charAt(i))) {
                        System.out.println("user id illegal");
                        return false;
                    }
                }
                idNum = Integer.parseInt(strNum);
                int yearNum = idNum / 100000;
                int acaNum = (idNum / 1000) % 100;
                int classNum = (idNum / 100) % 10;
                int remainNum = idNum % 100;
                if (classNum >= 1 && classNum <= 6 && acaNum >= 1 && acaNum <= 43 && remainNum >= 1) {
                    if ((str.equals("SY") || str.equals("ZY")) && yearNum >= 19 && yearNum <= 22) {
                        return true;
                    } else if (str.equals("BY") && yearNum >= 17 && yearNum <= 22) {
                        return true;
                    } else {
                        System.out.println("user id illegal");
                        return false;
                    }
                } else {
                    System.out.println("user id illegal");
                    return false;
                }
            } else {
                System.out.println("user id illegal");
                return false;
            }
        } else {
            System.out.println("user id illegal");
            return false;
        }
    }


    public static boolean nameLegal(String firstName, String lastName) {
        int flag = 0;
        char first = firstName.charAt(0);
        char last = lastName.charAt(0);
        if (nameJudge(firstName, first)) flag++;
        if (nameJudge(lastName, last)) flag++;
        if (flag == 2) return true;
        else {
            System.out.println("user name illegal");
            return false;
        }
    }

    private static boolean nameJudge(String name, char NAME) {
        if (NAME >= 'A' && NAME <= 'Z' && name.length() <= 20) {
            for (int i = 1; i < name.length(); i++) {
                if (!(name.charAt(i) >= 'a' && name.charAt(i) <= 'z')) {
                    return false;
                }
            }
        } else return false;
        return true;
    }

    public static boolean addressLegal(String address) {
        boolean res = address.matches("\\w+@\\w+(\\.\\w+)+");
        if (!res) System.out.println("email address illegal");
        return res;
    }

    public static boolean passLegal(String pass, String passConfirm) {
        boolean res = pass.matches("^[A-Za-z]+\\w{7,15}");
        if (res) {
            if (!pass.equals(passConfirm)) {
                System.out.println("passwords inconsistent");
                res = false;
            }
        } else System.out.println("password illegal");
        return res;
    }

    public static boolean courseIDLegal(String courseID) {
        if (courseID.length() != 5) {
            System.out.println("course id illegal");
            return false;
        } else {
            if (courseID.charAt(0) != 'C') {
                System.out.println("course id illegal");
                return false;
            } else {
                for (int i = 1; i <= 4; i++) {
                    if (!Character.isDigit(courseID.charAt(i))) {
                        System.out.println("course id illegal");
                        return false;
                    }
                }
                int a = Integer.parseInt(courseID.substring(1, 3));
                if (!(a >= 17 && a <= 22)) {
                    System.out.println("course id illegal");
                    return false;
                } else {
                    int b = Integer.parseInt(courseID.substring(3, 5));
                    if (b == 0) {
                        System.out.println("course id illegal");
                        return false;
                    } else return true;
                }
            }
        }
    }

    public static boolean courseNameLegal(String courseName) {
        boolean res = courseName.matches("\\w{6,16}");
        if (!res) System.out.println("course name illegal");
        return res;
    }

    public static boolean wareIDLegal(String wareID) {
        if (wareID.length() != 7) {
            System.out.println("ware id illegal");
            return false;
        } else {
            if (wareID.charAt(0) != 'W') {
                System.out.println("ware id illegal");
                return false;
            } else {
                for (int i = 1; i < wareID.length(); i++) {
                    if (!Character.isDigit(wareID.charAt(i))) {
                        System.out.println("ware id illegal");
                        return false;
                    }
                }
                int a = Integer.parseInt(wareID.substring(1, 5));
                int b, flag = 0;
                String str;
                for (course course : courseArrayList) {
                    str = course.courseID;
                    b = Integer.parseInt(str.substring(1, 5));
                    if (a == b) {
                        flag = 1;
                        searchCourse = str;
                        break;
                    }
                }
                if (flag == 0) {
                    System.out.println("ware id illegal");
                    return false;
                } else {
                    a = Integer.parseInt(wareID.substring(5, 7));
                    if (a == 0) {
                        System.out.println("ware id illegal");
                        return false;
                    } else return true;
                }
            }
        }
    }

    public static boolean wareNameLegal(String wareName) {
        if (wareName.length() <= 16 && wareName.length() >= 6 && wareName.matches("\\w+\\.[A-Za-z0-9]+")) {
            return true;
        }
        System.out.println("ware name illegal");
        return false;
    }

    public static boolean taskIDLegal(String taskID) {
        if (taskID.length() != 7) {
            System.out.println("task id illegal");
            return false;
        } else {
            if (taskID.charAt(0) != 'T') {
                System.out.println("task id illegal");
                return false;
            } else {
                for (int i = 1; i < taskID.length(); i++) {
                    if (!Character.isDigit(taskID.charAt(i))) {
                        System.out.println("task id illegal");
                        return false;
                    }
                }
                int a = Integer.parseInt(taskID.substring(1, 5));
                int b, flag = 0;
                String str;
                for (course course : courseArrayList) {
                    str = course.courseID;
                    b = Integer.parseInt(str.substring(1, 5));
                    if (a == b) {
                        flag = 1;
                        searchCourse = str;
                        break;
                    }
                }
                if (flag == 0) {
                    System.out.println("task id illegal");
                    return false;
                } else {
                    a = Integer.parseInt(taskID.substring(5, 7));
                    if (a == 0) {
                        System.out.println("task id illegal");
                        return false;
                    } else return true;
                }
            }
        }
    }

    public static boolean taskNameLegal(String taskName) {
        if (taskName.length() <= 16 && taskName.length() >= 6 && taskName.matches("\\w+\\.[A-Za-z0-9]+")) {
            return true;
        }
        System.out.println("task name illegal");
        return false;
    }

    public static boolean taskTimeLegal(String taskTime) {
        if (taskTime.length() == 19 && taskTime.matches("\\d{4}-\\d{2}-\\d{2}-\\d{2}:\\d{2}:\\d{2}")) {
            int year = Integer.parseInt(taskTime.substring(0, 4));
//            int month=Integer.parseInt(taskTime.substring(5,7));
//            int day=Integer.parseInt(taskTime.substring(8,10));
            int hour = Integer.parseInt(taskTime.substring(11, 13));
            int minute = Integer.parseInt(taskTime.substring(14, 16));
            int second = Integer.parseInt(taskTime.substring(17, 19));
            String str = taskTime.substring(0, 10);
            if (check(str) && year >= 1900 && year <= 9999) {
                return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59 && second >= 0 && second <= 59;
            } else {
                System.out.println("task time illegal");
                return false;
            }
        }
        System.out.println("task time illegal");
        return false;
    }

    static boolean check(String str) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");//括号内为日期格式，y代表年份，M代表年份中的月份（为避免与小时中的分钟数m冲突，此处用M），d代表月份中的天数
        try {
            sd.setLenient(false);//此处指定日期/时间解析是否不严格，在true是不严格，false时为严格
            sd.parse(str);//从给定字符串的开始解析文本，以生成一个日期
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean searchCourseInCourse(String courseID) {
        for (course course : courseArrayList) {
            if (courseID.equals(course.courseID)) {
                System.out.println("course id duplication");
                return true;
            }
        }
        return false;
    }//在课程list里面寻找课程

    public static int searchCourseInTeacher(String courseID) {
        int teacher = searchTeacher(ID);
        return adminLoop(courseID, teacherArrayList.get(teacher).courseList);
    }//在教师名下的课程list里面寻找课程并返回位置

    public static int searchCourseInAssist(String courseID) {
        int assist = searchAssist(ID);
        return adminLoop(courseID, assistArrayList.get(assist).courseList);
    }

    private static int adminLoop(String courseID, ArrayList<course> courseList) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseID.equals(courseList.get(i).courseID)) {
                return i;
            }
        }
        System.out.println("course id not exist");
        return -1;
    }


    public static boolean searchAdminInCourse(String ID) {
        for (int i = 0; i < Course.adminList.size(); i++) {
            if (ID.equals(Course.adminList.get(i))) return true;
        }
        return false;
    }

    public static int searchAdminIDInCourse(String ID) {
        for (int i = 0; i < Course.adminList.size(); i++) {
            if (ID.equals(Course.adminList.get(i))) return i;
        }
        return -1;
    }

    public static boolean searchWareInCourse(String wareID) {
        for (int i = 0; i < Course.wareList.size(); i++) {
            if (wareID.equals(Course.wareList.get(i).wareID)) {
                return true;
            }
        }
        return false;
    }

    public static int searchWareIDInCourse(String wareID) {
        for (int i = 0; i < Course.wareList.size(); i++) {
            if (wareID.equals(Course.wareList.get(i).wareID)) return i;
        }
        return -1;
    }

    public static boolean searchTaskInCourse(String taskID) {
        for (int i = 0; i < Course.taskList.size(); i++) {
            if (taskID.equals(Course.taskList.get(i).taskID)) {
                return true;
            }
        }
        return false;
    }


    public static int searchTaskIDInCourse(String taskID) {
        for (int i = 0; i < Course.taskList.size(); i++) {
            if (taskID.equals(Course.taskList.get(i).taskID)) return i;
        }
        return -1;
    }

    public static boolean searchStudentInCourse(String studentID) {
        for (int i = 0; i < Course.studentList.size(); i++) {
            if (studentID.equals(Course.studentList.get(i))) return true;
        }
        return false;
    }

    public static int searchStudentIDInCourse(String studentID) {
        for (int i = 0; i < Course.studentList.size(); i++) {
            if (studentID.equals(Course.studentList.get(i))) return i;
        }
        return -1;
    }

    public static Object findCourse(String courseID) {
        for (course course : courseArrayList) {
            if (courseID.equals(course.courseID)) return course;
        }
        return null;
    }//在课程list里面寻找课程并返回课程


    public static int findCourseID(String courseID) {
        for (int i = 0; i < courseArrayList.size(); i++) {
            if (courseID.equals(courseArrayList.get(i).courseID)) return i;
        }
        return -1;
    }//在课程list里面寻找课程id并返回位置


    public static int searchTeacher(String ID) {
        for (int i = 0; i < teacherArrayList.size(); i++) {
            if (ID.equals(teacherArrayList.get(i).ID)) return i;
        }
        return -1;
    }//在教师list里面寻找教师并返回位置

    public static int searchAssist(String ID) {
        for (int i = 0; i < assistArrayList.size(); i++) {
            if (ID.equals(assistArrayList.get(i).ID)) return i;
        }
        return -1;
    }


//    public static int searchStudent(String ID) {
//        for (int i = 0; i < studentArrayList.size(); i++) {
//            if (ID.equals(studentArrayList.get(i).ID)) return i;
//        }
//        return -1;
//    }
}
