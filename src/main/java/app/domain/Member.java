package app.domain;

public abstract class Member {
    private String id;
    private String name;

    public Member(String id, String name){
        this.id = id;
        this.name = name;
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void Setname(String name){
        this.name = name;
    }

    public abstract int getMaxBookings();
    public String getType(){
        return this.getClass().getSimpleName();
    }
}