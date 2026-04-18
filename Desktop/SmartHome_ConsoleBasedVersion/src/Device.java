// ============================================================
//  Device.java  –  Abstract base class for all smart devices
// ============================================================
public abstract class Device {

    // ── Fields ───────────────────────────────────────────────
    protected String name;
    protected boolean status;      // true = ON, false = OFF
    protected String deviceId;

    // ── Constructor ──────────────────────────────────────────
    public Device(String name, String deviceId) {
        this.name     = name;
        this.deviceId = deviceId;
        this.status   = false;     // devices start OFF by default
    }

    // ── Getters & Setters ────────────────────────────────────
    public String getName()            { return name; }
    public void   setName(String n)    { this.name = n; }

    public boolean getStatus()         { return status; }
    public void    setStatus(boolean s){ this.status = s; }

    public String getDeviceId()        { return deviceId; }
    public void   setDeviceId(String id){ this.deviceId = id; }

    // ── Concrete shared behaviours ───────────────────────────
    public void turnOn() {
        this.status = true;
        System.out.println("[" + name + "] turned ON.");
    }

    public void turnOff() {
        this.status = false;
        System.out.println("[" + name + "] turned OFF.");
    }

    public String getStatusString() {
        return status ? "ON" : "OFF";
    }

    // ── Abstract method – every subclass must override ───────
    public abstract void showStatus();
}
