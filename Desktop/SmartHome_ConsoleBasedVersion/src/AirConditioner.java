//  AirConditioner.java  –  Extends Device
public class AirConditioner extends Device {

    private int temperature;   // in Celsius

    // Making the overloaded constructor
    public AirConditioner(String name, String deviceId) {
        super(name, deviceId);
        this.temperature = 24;   // comfortable default
    }

    public AirConditioner(String name, String deviceId, int temperature) {
        super(name, deviceId);
        this.temperature = clamp(temperature);
    }

    // ── Getters & Setters ────────────────────────────────────
    public int  getTemperature()          { return temperature; }
    public void setTemperature(int temp)  { this.temperature = clamp(temp); }

    // ── Overloaded setTemperature ────────────────────────────
    public void setTemperature(int temp, boolean verbose) {
        this.temperature = clamp(temp);
        if (verbose) System.out.println("[" + name + "] temperature set to " + temperature + "°C.");
    }

    // ── Override ─────────────────────────────────────────────
    @Override
    public void showStatus() {
        System.out.println("  AC           | " + name + " | Status: " + getStatusString()
                + " | Temp: " + temperature + "°C");
    }

    private int clamp(int v) { return Math.max(16, Math.min(30, v)); }
}
