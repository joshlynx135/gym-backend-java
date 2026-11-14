package app.domain;

import java.util.ArrayList;
import java.util.List;

public class WorkoutClass {
    private final String id;
    private String name;
    private final int maxCapacity;
    // Private and never returned directly: only this class can change who is enrolled.
    private final List<String> enrolledMembers = new ArrayList<>();

    public WorkoutClass(String id, String name, int maxCapacity) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
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

    // Returns an immutable copy, so callers cannot modify the internal list.
    public List<String> getEnrolledMembers() {
        return List.copyOf(enrolledMembers);
    }

    public boolean isEnrolled(String memberId) {
        return enrolledMembers.contains(memberId);
    }

    // The class enforces its own rules: no duplicates, never over capacity.
    public void enroll(String memberId) {
        if (isEnrolled(memberId)) {
            throw new RuleViolationError("Member already booked this class");
        }
        if (enrolledMembers.size() >= maxCapacity) {
            throw new RuleViolationError("Class is full");
        }
        enrolledMembers.add(memberId);
    }

    public void cancel(String memberId) {
        // remove(Object) returns false if the element was not in the list.
        if (!enrolledMembers.remove(memberId)) {
            throw new NotFoundError("Member is not booked in this class");
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}