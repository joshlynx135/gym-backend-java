package app.domain;

public abstract class Member {
    private final String id;
    private String name;

    // protected: only subclasses can call it (an abstract class can't be instantiated anyway)
    protected Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Each subclass states its own rules and type name.
    public abstract int getMaxBookings();

    public abstract String getType();
}