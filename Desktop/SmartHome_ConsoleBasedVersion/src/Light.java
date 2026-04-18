// ============================================================
//  Light.java  –  Extends Device
// ============================================================
public class Light extends Device {

    private int brightness;   // 0–100

    // ── Constructors (overloading) ───────────────────────────
    public Light(String name, String deviceId) {
        super(name, deviceId);
        this.brightness = 50;   // default brightness
    }

    public Light(String name, String deviceId, int brightness) {
        super(name, deviceId);
        this.brightness = clamp(brightness);
    }

    // ── Getters & Setters ────────────────────────────────────
    public int  getBrightness()          { return brightness; }
    public void setBrightness(int level) { this.brightness = clamp(level); }

    // ── Business methods ─────────────────────────────────────
    public void setBrightness(int level, boolean verbose) {   // overloaded
        this.brightness = clamp(level);
        if (verbose) System.out.println("[" + name + "] brightness set to " + brightness + "%.");
    }

    public void autoTurnOff() {
        turnOff();
        System.out.println("[" + name + "] auto-turned OFF by schedule.");
    }

    // ── Override ─────────────────────────────────────────────
    @Override
    public void showStatus() {
        System.out.println("  Light        | " + name + " | Status: " + getStatusString()
                + " | Brightness: " + brightness + "%");
    }

    // ── Helper ───────────────────────────────────────────────
    private int clamp(int v) { return Math.max(0, Math.min(100, v)); }
}
