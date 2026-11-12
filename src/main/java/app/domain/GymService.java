package app.domain;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class GymService {
    private Map<String, Member> members = new HashMap<>();
    private Map<String, WorkoutClass> classes = new HashMap<>();

    public Member createMember(String name, String type) {
        requireNonBlank(name, "name");
        requireNonBlank(type, "type");

        String id = "m-" + UUID.randomUUID().toString().substring(0, 4);
        Member member;

        if (type.equalsIgnoreCase("BASIC")) {
            member = new BasicMember(id, name);
        } else if (type.equalsIgnoreCase("PREMIUM")) {
            member = new PremiumMember(id, name);
        } else {
            throw new ValidationError("Field 'type' must be 'BASIC' or 'PREMIUM'");
        }

        members.put(id, member);
        return member;
    }

    public Member getMember(String memberId) {
        if (!members.containsKey(memberId)) {
            throw new NotFoundError("Member '" + memberId + "' not Found");
        }
        return members.get(memberId);
    }

    public WorkoutClass createClass(String name, int maxCapacity) {
        requireNonBlank(name, "name");
        if (maxCapacity <= 0) {
            throw new ValidationError("Field 'maxCapacity' must be greater than 0");
        }
        String id = "c-" + UUID.randomUUID().toString().substring(0, 4);
        WorkoutClass workoutClass = new WorkoutClass(id, name, maxCapacity);
        classes.put(id, workoutClass);
        return workoutClass;
    }

    public WorkoutClass getClass(String classId) {
        if (!classes.containsKey(classId)) {
            throw new NotFoundError("Class '" + classId + "' not Found");
        }
        return classes.get(classId);
    }

    public WorkoutClass bookClass(String memberId, String classId) {
        Member member = getMember(memberId);
        WorkoutClass class_ = getClass(classId);
        if (class_.getEnrolledMembers().size() >= class_.getMaxCapacity()) {
            throw new RuleViolationError("Class is full");
        }
        if (class_.getEnrolledMembers().contains(memberId)) {
            throw new RuleViolationError("Member already booked this class");
        }
        int currentBookings = 0;
        for (WorkoutClass c : classes.values()) {
            if (c.getEnrolledMembers().contains(memberId)) {
                currentBookings++;
            }
        }

        if (currentBookings >= member.getMaxBookings()) {
            throw new RuleViolationError("Member has reached the maximum number of bookings");
        }
        class_.getEnrolledMembers().add(memberId);
        return class_;
    }

    private static void requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationError("Field '" + field + "' must not be blank");
        }
    }
}
