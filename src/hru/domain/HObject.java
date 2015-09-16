package hru.domain;

public class HObject {
    protected String name;

    public HObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
        return name.hashCode();
    }
}
