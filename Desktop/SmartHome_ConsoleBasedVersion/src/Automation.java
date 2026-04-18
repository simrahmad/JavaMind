// ============================================================
//  Automation.java
// ============================================================
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automation {

    // ── Inner class to hold a schedule entry ─────────────────
    public static class Schedule {
        String time;
        String action;
        Schedule(String time, String action) {
            this.time   = time;
            this.action = action;
        }
        @Override public String toString() {
            return "  Schedule[time=" + time + ", action=" + action + "]";
        }
    }

    private List<Schedule>         schedules;
    private Map<String, String>    rules;      // condition → action

    // ── Constructor ──────────────────────────────────────────
    public Automation() {
        this.schedules = new ArrayList<>();
        this.rules     = new HashMap<>();
    }

    // ── Getters ──────────────────────────────────────────────
    public List<Schedule>      getSchedules() { return schedules; }
    public Map<String, String> getRules()     { return rules; }

    // ── Business methods ─────────────────────────────────────
    public void addSchedule(String time, String action) {
        schedules.add(new Schedule(time, action));
        System.out.println("[Automation] Schedule added: '" + action + "' at " + time);
    }

    public void addRule(String condition, String action) {
        rules.put(condition, action);
        System.out.println("[Automation] Rule added: IF '" + condition + "' THEN '" + action + "'");
    }

    public void checkConditions() {
        System.out.println("[Automation] Checking " + rules.size() + " rule(s)...");
        for (Map.Entry<String, String> e : rules.entrySet()) {
            System.out.println("  Rule: IF '" + e.getKey() + "' → '" + e.getValue() + "'");
        }
    }

    public void executeSchedule() {
        System.out.println("[Automation] Executing " + schedules.size() + " schedule(s):");
        for (Schedule s : schedules) System.out.println(s);
    }

    public void autoTurnOffLights(List<Device> devices) {
        System.out.println("[Automation] Auto-turning off all lights...");
        for (Device d : devices) {
            if (d instanceof Light) {
                ((Light) d).autoTurnOff();
            }
        }
    }
}
