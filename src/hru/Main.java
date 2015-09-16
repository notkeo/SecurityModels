package hru;

import hru.domain.Access;
import hru.domain.HSubject;

import java.util.Scanner;

public class Main {

    public static HSubject currentSubject;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while (true) {
            String action = s.next();
            switch (action) {
                case "create_subj": {
                    createSubject(s);
                    break;
                }
                case "auth": {
                    auth(s);
                    break;
                }
                case "create_obj": {
                    createSubject(s);
                    break;
                }
                case "destroy_obj": {
                    destroyObject(s);
                    break;
                }
                case "destroy_subj": {
                    destroySubject(s);
                    break;
                }
                case "remove_access": {
                    removeAccess(s);
                    break;
                }
                case "set_access": {
                    setAccess(s);
                    break;
                }
            }
        }
    }

    private static void removeAccess(Scanner s) {
        String subject = s.next();
        String object = s.next();
        if (currentSubject != null) currentSubject.removeAccess(subject, object);
        else System.out.println("Доступ запрещен. Выполните авторизацию");
    }

    private static void destroyObject(Scanner s) {
        String objectName = s.next();
        if (currentSubject != null) currentSubject.destroyObject(objectName);
        else System.out.println("Доступ запрещен. Выполните авторизацию");
    }

    private static void destroySubject(Scanner s) {
        String name = s.next();
        if (currentSubject != null) currentSubject.destroyObject(name);
        else System.out.println("Доступ запрещен. Выполните авторизацию");
    }

    private static void auth(Scanner s) {
        String login = s.next();
        String password = s.next();
        HSubject subject = AccessMap.getInstance().findSubject(login);
        if (null != subject && subject.getName().equals(login) && subject.getPassword().equals(password)) {
            System.out.println("Доброе утро " + login);
            currentSubject = subject;
        } else System.out.println("Ошибка авторизации");
    }

    private static void createSubject(Scanner scanner) {
        String login = scanner.next();
        String password = scanner.next();
        AccessMap.getInstance().createSubject(currentSubject, login, password);
    }

    public static void setAccess(Scanner s) {
        String subjectName = s.next();
        String objectName = s.next();
        String accessType = s.next();
        Access access = null;
        if (accessType.equals("read")) access = Access.READ;
        else if (accessType.equals("write")) access = Access.WRITE;
        else if (accessType.equals("own")) access = Access.OWN;
        else {
            System.out.println("Неверный тип доступа");
            return;
        }
        if (currentSubject != null) currentSubject.setAccess(subjectName, objectName, access);
        else System.out.println("Доступ запрещен. Выполните авторизацию");
    }
}
