package app.domain;

import java.util.List;

/** Immutable snapshot of a workout class. Safe to return outside the service. */
public record ClassView(String id, String name, int maxCapacity, List<String> enrolledMembers) {

    // Compact constructor: runs before the fields are assigned.
    public ClassView {
        // A record only makes the field itself final, not the list's contents,
        // so we store an immutable copy.
        enrolledMembers = List.copyOf(enrolledMembers);
    }
}