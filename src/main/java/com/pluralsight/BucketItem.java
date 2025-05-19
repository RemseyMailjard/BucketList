package com.pluralsight;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable Bucket‑list item using Java 17 <code>record</code> semantics.
 */
public record BucketItem(
        int id,                       // 0 = not yet persisted
        String title,
        String description,
        Category category,
        Priority priority,
        LocalDate createdDate,
        LocalDate targetDate,
        LocalDate completedDate,
        boolean done,
        String notes,
        String imageUrl
) {

    /* ----------------------------- Validation ---------------------------------- */
    public BucketItem {
        title       = requireNonBlank(title, "title");
        description = nullableTrim(description);
        notes       = nullableTrim(notes);
        imageUrl    = nullableTrim(imageUrl);

        createdDate = createdDate == null ? LocalDate.now() : createdDate;
        category    = category    == null ? Category.UNCATEGORISED : category;
        priority    = priority    == null ? Priority.MEDIUM        : priority;

        if (done && completedDate == null)
            completedDate = LocalDate.now();
        if (!done)
            completedDate = null;           // keep model consistent
    }

    /* ----------------------------- Business helpers ---------------------------- */

    /** Returns a copy with <code>done = true</code> and <code>completedDate = now</code>. */
    public BucketItem markDone() {
        return new BucketItem(id, title, description, category, priority,
                createdDate, targetDate, LocalDate.now(),
                true, notes, imageUrl);
    }

    /** Returns a copy with a newly assigned database id. */
    public BucketItem withId(int newId) {
        return new BucketItem(newId, title, description, category, priority,
                createdDate, targetDate, completedDate,
                done, notes, imageUrl);
    }

    /** Returns a copy with an updated target date. */
    public BucketItem withTargetDate(LocalDate newTarget) {
        return new BucketItem(id, title, description, category, priority,
                createdDate, newTarget, completedDate,
                done, notes, imageUrl);
    }

    /* ----------------------------- CSV serialisation --------------------------- */

    private static final String SEP = "|";

    /** Serialises to a <code>|</code>-delimited line. */
    public String toCsv() {
        return String.join(SEP,
                String.valueOf(id),
                esc(title),
                esc(description),
                category.name(),
                String.valueOf(priority.value),
                Boolean.toString(done),
                createdDate.toString(),
                targetDate   != null ? targetDate.toString()   : "",
                completedDate!= null ? completedDate.toString(): "",
                esc(notes),
                esc(imageUrl)
        );
    }

    public static BucketItem fromCsv(String line) {
        String[] p = line.split("\\|", -1);            // -1 keeps trailing empties
        return new BucketItem(
                Integer.parseInt(p[0]),
                unesc(p[1]),
                unesc(p[2]),
                Category.valueOf(p[3]),
                Priority.from(Integer.parseInt(p[4])),
                LocalDate.parse(p[6]),
                p[7].isBlank() ? null : LocalDate.parse(p[7]),
                p[8].isBlank() ? null : LocalDate.parse(p[8]),
                Boolean.parseBoolean(p[5]),
                unesc(p[9]),
                unesc(p[10])
        );
    }

    /* ----------------------------- Nested Enums -------------------------------- */

    public enum Category { TRAVEL, FITNESS, ADVENTURE, CAREER, PERSONAL, LEARNING, OTHER, UNCATEGORISED }

    public enum Priority {
        HIGH(1), MEDIUM(3), LOW(5);
        public final int value;
        Priority(int v){ this.value = v; }
        public static Priority from(int v){
            return v <= 2 ? HIGH : v >= 5 ? LOW : MEDIUM;
        }
    }

    /* ----------------------------- Utility ------------------------------------- */

    private static String requireNonBlank(String s, String field){
        Objects.requireNonNull(s, field+" must not be null");
        s = s.strip();
        if (s.isBlank()) throw new IllegalArgumentException(field+" must not be blank");
        return s;
    }
    private static String nullableTrim(String s){ return s == null ? "" : s.strip(); }
    private static String esc(String s){ return s.replace("|", "\\|"); }
    private static String unesc(String s){ return s.replace("\\|", "|"); }
}
