package app.domain;

import java.util.ArrayList;
import java.util.List;

public class WorkoutClass {
    private String id;
    private String name;
    private int maxCapacity;
    private List<String> enrolledMembers;


    public WorkoutClass(String id, String name, int maxCapacity){
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.enrolledMembers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public List<String> getEnrolledMembers() {
        return enrolledMembers;
    }

    public void setName(String name) {
        this.name = name;
    }
}
