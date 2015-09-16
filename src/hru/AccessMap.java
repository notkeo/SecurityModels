package hru;

import hru.domain.*;
import hru.domain.HObject;

import java.util.HashMap;

public class AccessMap {

    private static AccessMap accessMapInstance = new AccessMap();

    private HashMap<HSubject, HashMap<HObject, Access>> smap;
    private HashMap<HObject, HashMap<HSubject, Access>> omap;

    private AccessMap() {
        smap = new HashMap<>();
        omap = new HashMap<>();
    }

    public boolean createObject(HSubject actor, HObject object) throws IllegalAccessException {
        HashMap<HObject, Access> ownedObjects = smap.get(actor);
        if (ownedObjects != null) {
            Access access = ownedObjects.get(object);
            if (access == null) {
                ownedObjects.put(object, Access.OWN);
                omap.put(object, new HashMap<>());
                omap.get(object).put(actor, Access.OWN);
                return true;
            } else throw new IllegalStateException("Объект уже зарегистрирован в системе");
        } else throw new IllegalAccessException("Субъект не зарегистрирован в системе");
    }

    public boolean createSubject(HSubject actor, String username, String password) throws IllegalStateException {
        HSubject subject = new HSubject(username, password);
        if (!smap.containsKey(subject)) {
            smap.put(subject, new HashMap<>());
            smap.get(subject).put(subject, Access.OWN);
            if (actor != null) smap.get(actor).put(subject, Access.OWN);

            omap.put(subject, new HashMap<>());
            omap.get(subject).put(subject, Access.OWN);
            if (actor != null) omap.get(subject).put(actor, Access.OWN);
            return true;
        } else throw new IllegalStateException("Субъект уже зарегистрирован в системе");
    }

    public int checkAccess(HSubject s, HObject o, Access access) throws IllegalAccessException {
        HashMap<HObject, Access> owned = smap.get(s);
        if (owned != null) {
            Access s_access = o == null ? null : owned.get(o);
            if (s_access == null) throw new IllegalStateException("Доступ невозможен. Объек не сущесвует в системе.");
            if (access.compareTo(s_access) < 0) System.out.println("Доступ запрещен.");
            return access.compareTo(s_access);
        } else throw new IllegalAccessException("Субъект не зарегистрирован в системе.");
    }

    public boolean destroySubject(HSubject actor, String subjectName) throws IllegalStateException {
        try {
            HSubject subject = findSubject(subjectName);
            if (checkAccess(actor, subject, Access.OWN) == 0) {
                if (smap.containsKey(subject)) {
                    if (smap.get(actor).get(subject) != null)
                        smap.get(subject).remove(subject);
                    smap.remove(subject);
                    omap.remove(subject);
                    return true;
                } else throw new IllegalArgumentException("Попытка удаления несуществующего субъекта");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean destroyObject(HSubject actor, String objectName) throws IllegalStateException {
        try {
            HObject object = findObject(objectName);
            if (checkAccess(actor, object, Access.OWN) == 0) {
                if (omap.containsKey(object)) {
                    HashMap<HSubject, Access> owners = omap.get(object);
                    for (HSubject s : owners.keySet()) {
                        HashMap<HObject, Access> owned = smap.get(s);
                        owned.remove(object);
                    }
                    omap.remove(object);
                    return true;
                } else throw new IllegalStateException("Попытка удаления не существующего объекта");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean setAccess(HSubject actor, String subjectName, String objectName, Access access) {
        try {
            HObject object = findObject(objectName);
            HSubject subject = findSubject(subjectName);
            if (checkAccess(actor, object, Access.OWN) == 0) {
                HashMap<HObject, Access> accessHashMap = subject == null ? null : smap.get(subject);
                if (accessHashMap != null) {
                    accessHashMap.put(object, access);
                    omap.get(object).put(subject, access);
                    return true;
                } else
                    throw new IllegalArgumentException("Невозможно назначить права для несуществующего пользователя");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeAccess(HSubject actor, String subjectName, String objectName) {
        try {
            HSubject subject = findSubject(subjectName);
            HObject object = findSubject(objectName);
            if (checkAccess(actor, object, Access.OWN) == 0) {
                HashMap<HObject, Access> ownedObjects = subject == null ? null : smap.get(subject);
                if (ownedObjects != null) {
                    ownedObjects.remove(object);
                    omap.get(object).remove(subject);
                    return true;
                } else throw new IllegalArgumentException("Невозможно удалить право у несуществующего пользователя");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static AccessMap getInstance() {
        return accessMapInstance;
    }

    public HSubject findSubject(String username) {
        for (HSubject s : smap.keySet()) {
            if (s.getName().equals(username)) {
                return s;
            }
        }
        return null;
    }

    public HObject findObject(String objectName) {
        for (HObject object : omap.keySet()) {
            if (object.getName().equals(objectName)) {
                return object;
            }
        }
        return null;
    }
}
