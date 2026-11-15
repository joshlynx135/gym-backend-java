package app.domain;

/** Immutable snapshot of a member. Safe to return outside the service. */
public record MemberView(String id, String name, String type, int maxBookings) {
}