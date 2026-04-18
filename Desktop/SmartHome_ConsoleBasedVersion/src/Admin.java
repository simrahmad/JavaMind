// ============================================================
//  Admin.java  –  Extends User (full access)
// ============================================================
import java.util.List;

public class Admin extends User {

    private final String accessLevel = "full";

  //Making the constructor for Admin
    public Admin(String name, String password) {
        super(name, password, "Admin");
    }

    // ── Getter ───────────────────────────────────────────────
    public String getAccessLevel() { return accessLevel; }

    // ── Admin-only methods ───────────────────────────────────
    public void manageDevices(List<Device> devices) {
        System.out.println("\n[Admin:" + name + "] Managing all devices (" + devices.size() + " total):");
        for (Device d : devices) d.showStatus();
    }

    public void viewAllDevices(List<Device> devices) {
        System.out.println("\n[Admin:" + name + "] Full device list:");
        for (Device d : devices) d.showStatus();
    }

    public void configureAutomation(Automation automation, String time, String action) {
        System.out.println("[Admin:" + name + "] Configuring automation: " + action + " at " + time);
        automation.addSchedule(time, action);
    }

    // ── Override toString ────────────────────────────────────
    @Override
    public String toString() {
        return "Admin{name='" + name + "', accessLevel='" + accessLevel + "', loggedIn=" + loggedIn + "}";
    }
}
