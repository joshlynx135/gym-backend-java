package app.domain;

import java.util.HashMap;
import java.util.Map;

public class GymService {
    private final Map<String, Member> members = new HashMap<>();
    private final Map<String, WorkoutClass> classes = new HashMap<>();
    // Sequential ids: unique by construction (a random 4-character id can collide).
    private long memberCounter = 0;
    private long classCounter = 0;

    public synchronized MemberView createMember(String name, String type) {
        requireNonBlank(name, "name");
        requireNonBlank(type, "type");

        boolean basic = type.equalsIgnoreCase("BASIC");
        boolean premium = type.equalsIgnoreCase("PREMIUM");
        if (!basic && !premium) {
            throw new ValidationError("Field 'type' must be 'BASIC' or 'PREMIUM'");
        }

        String id = "m-" + (++memberCounter);
        Member member = basic ? new BasicMember(id, name) : new PremiumMember(id, name);
        members.put(id, member);
        return toView(member);
    }

    public synchronized MemberView getMember(String memberId) {
        return toView(findMember(memberId));
    }

    public synchronized ClassView createClass(String name, int maxCapacity) {
        requireNonBlank(name, "name");
        if (maxCapacity <= 0) {
            throw new ValidationError("Field 'maxCapacity' must be greater than 0");
        }

        String id = "c-" + (++classCounter);
        WorkoutClass workoutClass = new WorkoutClass(id, name, maxCapacity);
        classes.put(id, workoutClass);
        return toView(workoutClass);
    }

    public synchronized ClassView getWorkoutClass(String classId) {
        return toView(findClass(classId));
    }

    public synchronized ClassView bookClass(String memberId, String classId) {
        Member member = findMember(memberId);
        WorkoutClass workoutClass = findClass(classId);

        // Member-level rule: it needs every class, so it stays in the service.
        // Re-booking a class the member is already in is left for enroll() to report.
        if (!workoutClass.isEnrolled(memberId)
                && countBookings(memberId) >= member.getMaxBookings()) {
            throw new RuleViolationError("Member has reached the maximum number of bookings");
        }

        // Class-level rules (duplicate, full) are enforced by the class itself.
        workoutClass.enroll(memberId);
        return toView(workoutClass);
    }

    // ---- private helpers: they return live objects and are only called while the lock is held ----

    private Member findMember(String memberId) {
        Member member = members.get(memberId);
        if (member == null) {
            throw new NotFoundError("Member '" + memberId + "' not found");
        }
        return member;
    }

    private WorkoutClass findClass(String classId) {
        WorkoutClass workoutClass = classes.get(classId);
        if (workoutClass == null) {
            throw new NotFoundError("Class '" + classId + "' not found");
        }
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

    private static MemberView toView(Member m) {
        return new MemberView(m.getId(), m.getName(), m.getType(), m.getMaxBookings());
    }

    private static ClassView toView(WorkoutClass c) {
        return new ClassView(c.getId(), c.getName(), c.getMaxCapacity(), c.getEnrolledMembers());
    }

    // Rejects null, empty, and whitespace-only strings.
    private static void requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationError("Field '" + field + "' must not be blank");
        }
    }
}