package app.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GymService {
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, WorkoutClass> classes = new HashMap<>();

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
        Member member = members.get(memberId);
        if (member == null) {
            throw new NotFoundError("Member '" + memberId + "' not found");
        }
        return member;
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

    public WorkoutClass getWorkoutClass(String classId) {
        WorkoutClass workoutClass = classes.get(classId);
        if (workoutClass == null) {
            throw new NotFoundError("Class '" + classId + "' not found");
        }
        return workoutClass;
    }

    public WorkoutClass bookClass(String memberId, String classId) {
        Member member = getMember(memberId);
        WorkoutClass workoutClass = getWorkoutClass(classId);

        // Member-level rule: it needs every class, so it stays in the service.
        // Re-booking a class the member is already in is left for enroll() to report.
        if (!workoutClass.isEnrolled(memberId)
                && countBookings(memberId) >= member.getMaxBookings()) {
            throw new RuleViolationError("Member has reached the maximum number of bookings");
        }

        // Class-level rules (duplicate, full) are enforced by the class itself.
        workoutClass.enroll(memberId);
        return workoutClass;
    }

    private int countBookings(String memberId) {
        int count = 0;
        for (WorkoutClass c : classes.values()) {
            if (c.isEnrolled(memberId)) {
                count++;
            }
        }
        return count;
    }

    // Rejects null, empty, and whitespace-only strings.
    private static void requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationError("Field '" + field + "' must not be blank");
        }
    }
}