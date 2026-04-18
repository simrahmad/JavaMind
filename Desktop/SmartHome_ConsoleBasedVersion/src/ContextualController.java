// ============================================================
//  ContextualController.java
// ============================================================
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextualController {

    private String              currentMode;
    private Map<String, String> scenes;   // scene name → description

    // ── Constructor ──────────────────────────────────────────
    public ContextualController() {
        this.currentMode = "Normal";
        this.scenes      = new HashMap<>();
        configureScene(); // load default scenes
    }

    // ── Getters & Setters ────────────────────────────────────
    public String getCurrentMode()        { return currentMode; }
    public void   setCurrentMode(String m){ this.currentMode = m; }
    public Map<String, String> getScenes(){ return scenes; }

    // ── Business methods ─────────────────────────────────────
    public void configureScene() {
        scenes.put("Cinema",  "Lights OFF, AC 22°C, Fan speed 2");
        scenes.put("Sleep",   "Lights OFF, AC 20°C, Fan speed 1, Door LOCKED");
        scenes.put("Away",    "All lights OFF, Security ARMED, Door LOCKED");
        scenes.put("Normal",  "Lights 70%, AC 24°C, Fan speed 3");
    }

    public void applyMode(String mode, List<Device> devices) {
        this.currentMode = mode;
        System.out.println("\n[ContextualController] Applying mode: " + mode);
        switch (mode.toLowerCase()) {
            case "cinema" -> setCinemaMode(devices);
            case "sleep"  -> setSleepMode(devices);
            case "away"   -> setAwayMode(devices);
            default       -> System.out.println("  Unknown mode '" + mode + "'. No changes applied.");
        }
    }

    public void setCinemaMode(List<Device> devices) {
        System.out.println("  [Cinema Mode] Dimming lights, cooling room...");
        for (Device d : devices) {
            if (d instanceof Light) { d.turnOff(); }
            if (d instanceof AirConditioner) { ((AirConditioner) d).setTemperature(22, true); d.turnOn(); }
            if (d instanceof Fan) { ((Fan) d).setSpeed(2, true); d.turnOn(); }
        }
    }

    public void setSleepMode(List<Device> devices) {
        System.out.println("  [Sleep Mode] Turning off lights, locking doors, cooling room...");
        for (Device d : devices) {
            if (d instanceof Light) { d.turnOff(); }
            if (d instanceof AirConditioner) { ((AirConditioner) d).setTemperature(20, true); d.turnOn(); }
            if (d instanceof Fan) { ((Fan) d).setSpeed(1, true); d.turnOn(); }
            if (d instanceof Door) { ((Door) d).lock(); }
        }
    }

    public void setAwayMode(List<Device> devices) {
        System.out.println("  [Away Mode] Securing home...");
        for (Device d : devices) {
            if (d instanceof Light)          { d.turnOff(); }
            if (d instanceof Door)           { ((Door) d).lock(); }
            if (d instanceof SecuritySystem) { ((SecuritySystem) d).arm(); }
            if (d instanceof AirConditioner || d instanceof Fan) { d.turnOff(); }
        }
    }
}
