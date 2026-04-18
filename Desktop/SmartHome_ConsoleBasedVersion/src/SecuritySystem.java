// ============================================================
//  SecuritySystem.java  –  Extends Device
// ============================================================
public class SecuritySystem extends Device {

    private boolean isArmed;
    private NotificationSystem notificationSystem;

    // ── Constructors (overloading) ───────────────────────────
    public SecuritySystem(String name, String deviceId, NotificationSystem ns) {
        super(name, deviceId);
        this.isArmed            = false;
        this.notificationSystem = ns;
    }

    public SecuritySystem(String name, String deviceId) {
        super(name, deviceId);
        this.isArmed            = false;
        this.notificationSystem = null;
    }

    // ── Getters & Setters ────────────────────────────────────
    public boolean isArmed()           { return isArmed; }
    public void    setArmed(boolean a) { this.isArmed = a; }

    // ── Business methods ─────────────────────────────────────
    public void arm() {
        this.isArmed = true;
        System.out.println("[" + name + "] Security system ARMED.");
    }

    public void disarm() {
        this.isArmed = false;
        System.out.println("[" + name + "] Security system DISARMED.");
    }

    public void triggerAlert() {
        System.out.println("[" + name + "] *** ALERT TRIGGERED! ***");
        if (notificationSystem != null) {
            notificationSystem.motionDetected();
        }
    }

    // ── Override ─────────────────────────────────────────────
    @Override
    public void showStatus() {
        System.out.println("  Security Sys | " + name + " | Status: " + getStatusString()
                + " | Armed: " + (isArmed ? "YES" : "NO"));
    }
}
