// ============================================================
//  Fan.java  –  Extends Device
// ============================================================
public class Fan extends Device {

    private int speed;   // 1–5

    // ── Constructors (overloading) ───────────────────────────
    public Fan(String name, String deviceId) {
        super(name, deviceId);
        this.speed = 1;
    }

    public Fan(String name, String deviceId, int speed) {
        super(name, deviceId);
        this.speed = clamp(speed);
    }

    // ── Getters & Setters ────────────────────────────────────
    public int  getSpeed()          { return speed; }
    public void setSpeed(int level) { this.speed = clamp(level); }

    // ── Overloaded setSpeed ──────────────────────────────────
    public void setSpeed(int level, boolean verbose) {
        this.speed = clamp(level);
        if (verbose) System.out.println("[" + name + "] speed set to " + speed + ".");
    }

    // ── Override ─────────────────────────────────────────────
    @Override
    public void showStatus() {
        System.out.println("  Fan          | " + name + " | Status: " + getStatusString()
                + " | Speed: " + speed + "/5");
    }

    private int clamp(int v) { return Math.max(1, Math.min(5, v)); }
}
