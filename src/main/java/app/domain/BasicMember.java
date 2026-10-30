package app.domain;

public class BasicMember extends Member {
    public BasicMember(String id, String name) {
        super(id, name);
    }

    @Override 
    public int getMaxBookings() {
        return 1;
    }
}
