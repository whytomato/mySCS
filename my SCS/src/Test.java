import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command;
        int j, k = 0;
        String[] commandLine = new String[15];
        while (true) {
            command = sc.nextLine();
            Arrays.fill(commandLine, "");
            for (int i = 0; i < command.length(); i++) {
                if (command.charAt(i) != ' ') {
                    for (j = i; j < command.length(); j++) {
                        if (command.charAt(j) != ' ') {
                            commandLine[k] += command.charAt(j);
                        } else break;
                    }
                    k++;
                    i = j - 1;
                }
            }
            if (Objects.equals(commandLine[0], "register")) {
                Command.register(commandLine, k);
            } else if (Objects.equals(commandLine[0], "login")) {
                Command.login(commandLine, k);
            } else if (Objects.equals(commandLine[0], "logout")) {
                Command.logout(k);
            } else if (Objects.equals(commandLine[0], "printInfo")) {
                Command.printInfo(commandLine, k);
            } else if (Objects.equals(commandLine[0], "addCourse")) {
                Command.addCourse(commandLine, k);
            } else if (Objects.equals(commandLine[0], "removeCourse")) {
                Command.removeCourse(commandLine, k);
            } else if (Objects.equals(commandLine[0], "listCourse")) {
                Command.listCourse(k);
            } else if (Objects.equals(commandLine[0], "selectCourse")) {
                Command.selectCourse(commandLine, k);
            } else if (Objects.equals(commandLine[0], "addAdmin")) {
                Command.addAdmin(commandLine, k);
            } else if (Objects.equals(commandLine[0], "removeAdmin")) {
                Command.removeAdmin(commandLine, k);
            } else if (Objects.equals(commandLine[0], "listAdmin")) {
                Command.listAdmin(k);
            } else if (Objects.equals(commandLine[0], "changeRole")) {
                Command.changeRole(k);
            } else if (Objects.equals(commandLine[0], "addWare")) {
                Command.addWare(commandLine, k);
            } else if (Objects.equals(commandLine[0], "removeWare")) {
                Command.removeWare(commandLine, k);
            } else if (Objects.equals(commandLine[0], "listWare")) {
                Command.listWare(k);
            } else if (Objects.equals(commandLine[0], "addTask")) {
                Command.addTask(commandLine, k);
            } else if (Objects.equals(commandLine[0], "removeTask")) {
                Command.removeTask(commandLine, k);
            } else if (Objects.equals(commandLine[0], "listTask")) {
                Command.listTask(k);
            } else if (Objects.equals(commandLine[0], "addStudent")) {
                Command.addStudent(commandLine, k);
            } else if (Objects.equals(commandLine[0], "removeStudent")) {
                Command.removeStudent(commandLine, k);
            } else if (Objects.equals(commandLine[0], "listStudent")) {
                Command.listStudent(k);
            } else if (Objects.equals(commandLine[0], "QUIT")) {
                if (Command.quit(k)) {
                    System.out.println("----- Good Bye! -----");
                    System.exit(0);
                }
            } else if (Objects.equals(commandLine[0], "")) {
                continue;
            } else {
                System.out.println("command '" + commandLine[0] + "' not found");
            }
            k = 0;
        }
    }
}