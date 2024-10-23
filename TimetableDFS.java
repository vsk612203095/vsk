import java.util.;

public class TimetableDFS {
    private static final String[] SUBJECTS = {"DAA", "CN", "AI", "DS", "OS"};
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String[] SLOTS = {
            "9:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30",
            "1:30 - 2:30", "2:30 - 3:30", "3:30 - 4:30", "4:30 - 5:30"
    };
    private static final int MAX_LECTURES_PER_DAY = 3;
    private static final int LECTURES_PER_SUBJECT = 3;  // Each subject has 3 lectures per week

    // Variables to track subjects allocation
    private static Map<String, Integer> subjectCounts = new HashMap<>();
    private static Map<String, String[]> timetableA = new HashMap<>();
    private static Map<String, String[]> timetableB = new HashMap<>();

    public static void main(String[] args) {
        // Initialize subject counts
        for (String subject : SUBJECTS) {
            subjectCounts.put(subject, 0);
        }

        // Initialize timetable maps
        for (String day : DAYS) {
            timetableA.put(day, new String[SLOTS.length]);
            timetableB.put(day, new String[SLOTS.length]);
        }

        // Perform DFS to generate the timetable
        if (dfs(0, 0, "Div A")) {
            System.out.println("Division A Timetable:");
            printTimetable(timetableA);

            System.out.println("\nDivision B Timetable:");
            printTimetable(timetableB);
        } else {
            System.out.println("No valid timetable found.");
        }
    }

    // DFS Backtracking function
    private static boolean dfs(int dayIndex, int slotIndex, String division) {
        if (dayIndex == DAYS.length) return true;  // Timetable filled successfully

        String day = DAYS[dayIndex];
        String[] timetable = division.equals("Div A") ? timetableA.get(day) : timetableB.get(day);

        // If the day has max lectures or all slots are filled, move to the next day
        if (slotIndex >= SLOTS.length) {
            return dfs(dayIndex + 1, 0, division);
        }

        // Try assigning subjects to current slot
        for (String subject : SUBJECTS) {
            if (subjectCounts.get(subject) < LECTURES_PER_SUBJECT && isSafeToPlace(timetable, timetableA.get(day), timetableB.get(day), slotIndex, subject)) {
                timetable[slotIndex] = subject;  // Place the subject in this slot
                subjectCounts.put(subject, subjectCounts.get(subject) + 1);

                // Try the next slot
                if (dfs(dayIndex, slotIndex + 1, division)) {
                    return true;  // Solution found
                }

                // Backtrack: remove the subject and try another
                timetable[slotIndex] = null;
                subjectCounts.put(subject, subjectCounts.get(subject) - 1);
            }
        }

        // If we're in Div A, try Div B for the same slot (no overlapping allowed)
        if (division.equals("Div A")) {
            return dfs(dayIndex, slotIndex, "Div B");
        }

        return false;  // No valid timetable found for this configuration
    }

    // Check if it's safe to place a subject in a slot
    private static boolean isSafeToPlace(String[] timetable, String[] timetableA, String[] timetableB, int slotIndex, String subject) {
        return timetable[slotIndex] == null && timetableA[slotIndex] == null && timetableB[slotIndex] == null;
    }

    // Print timetable
    private static void printTimetable(Map<String, String[]> timetable) {
        System.out.format("%-20s", "Slots");
        for (String day : DAYS) {
            System.out.format("%-12s", day);
        }
        System.out.println();

        for (int i = 0; i < SLOTS.length; i++) {
            System.out.format("%-20s", SLOTS[i]);
            for (String day : DAYS) {
                String subject = timetable.get(day)[i];
                System.out.format("%-12s", subject != null ? subject : "");
            }
            System.out.println();
        }
    }
}
