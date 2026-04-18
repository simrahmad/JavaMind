// ============================================================
//  Guest.java  –  Extends User (limited access)
// ============================================================
import java.util.List;

public class Guest extends User {

    private final String accessLevel = "limited";

    // ── Constructor ──────────────────────────────────────────
    public Guest(String name, String password) {
        super(name, password, "Guest");
    }

    // ── Getter ───────────────────────────────────────────────
    public String getAccessLevel() { return accessLevel; }

    // ── Guest-only methods ───────────────────────────────────
    public void viewLimitedDevices(List<Device> devices) {
        System.out.println("\n[Guest:" + name + "] Visible devices (lights & fans only):");
        for (Device d : devices) {
            if (d instanceof Light || d instanceof Fan) {
                d.showStatus();
            }
        }
    }

    public void controlAllowedDevices(List<Device> devices, String deviceName, String action) {
        for (Device d : devices) {
            if ((d instanceof Light || d instanceof Fan) && d.getName().equalsIgnoreCase(deviceName)) {
                if (action.equalsIgnoreCase("on"))  d.turnOn();
                else if (action.equalsIgnoreCase("off")) d.turnOff();
                else System.out.println("[Guest] Unknown action: " + action);
                return;
            }
        }
        System.out.println("[Guest:" + name + "] Access denied or device not found: " + deviceName);
    }

    // ── Override toString ────────────────────────────────────
    @Override
    public String toString() {
        return "Guest{name='" + name + "', accessLevel='" + accessLevel + "', loggedIn=" + loggedIn + "}";
    }
}
