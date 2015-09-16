package hru.domain;

import hru.AccessMap;

public class HSubject extends HObject {
    private String password;

    public HSubject(String name, String password) {
        super(name);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean createSubject(String name, String password) {
        return AccessMap.getInstance().createSubject(this, name, password);
    }

    public boolean createObject(String name) throws IllegalAccessException {
        return AccessMap.getInstance().createObject(this, new HObject(name));
    }

    public boolean destroySubject(String name) throws IllegalStateException {
        return AccessMap.getInstance().destroySubject(this, name);
    }

    public boolean destroyObject(String name) throws IllegalStateException {
        return AccessMap.getInstance().destroyObject(this, name);
    }


    public boolean setAccess(String subject, String object, Access access) {
        return AccessMap.getInstance().setAccess(this, subject, object, access);
    }

    public boolean removeAccess(String subject, String object) {
        return AccessMap.getInstance().removeAccess(this, subject, object);
    }

    public void printDocs(){
        AccessMap.getInstance().printFor(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HObject hObject = (HObject) o;
        return name.equals(hObject.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode() + 31 * password.hashCode();
    }

}
