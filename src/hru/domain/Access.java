package hru.domain;

public enum Access {
    READ("READ"), WRITE("WRITE"), OWN("OWN");
    String desc;

    Access(String own) {
        desc = own;
    }

    public String getDesc() {
        return desc;
    }
}
