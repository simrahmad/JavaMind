// ============================================================
//  NotificationSystem.java
// ============================================================
import java.util.ArrayList;
import java.util.List;

public class NotificationSystem {

    private List<String> alerts;
    private List<String> notificationLog;

    // ── Constructor ──────────────────────────────────────────
    public NotificationSystem() {
        this.alerts          = new ArrayList<>();
        this.notificationLog = new ArrayList<>();
    }

    // ── Getters ──────────────────────────────────────────────
    public List<String> getAlerts()          { return alerts; }
    public List<String> getNotificationLog() { return notificationLog; }

    // ── Business methods ─────────────────────────────────────
    public void sendAlert(String msg) {
        alerts.add(msg);
        notificationLog.add("[ALERT] " + msg);
        System.out.println("[Notification] ALERT: " + msg);
    }

    public void showNotifications() {
        System.out.println("\n── Notification Log (" + notificationLog.size() + " entries) ──");
        if (notificationLog.isEmpty()) {
            System.out.println("  No notifications.");
        } else {
            for (String n : notificationLog) System.out.println("  " + n);
        }
        System.out.println("────────────────────────────────────────");
    }

    public void motionDetected() {
        sendAlert("Motion detected by security system!");
    }

    public void energyUsageHigh() {
        sendAlert("Energy usage is unusually high!");
    }
}
