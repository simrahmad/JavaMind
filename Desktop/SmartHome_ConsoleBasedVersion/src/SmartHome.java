// ============================================================
//  SmartHome.java  –  Central hub
// ============================================================
import java.util.ArrayList;
import java.util.List;

public class SmartHome {

    private List<Device>        devices;
    private Automation          automation;
    private ContextualController contextualController;
    private List<User>          users;
    private NotificationSystem  notificationSystem;

    // ── Constructor ──────────────────────────────────────────
    public SmartHome() {
        this.devices              = new ArrayList<>();
        this.users                = new ArrayList<>();
        this.notificationSystem   = new NotificationSystem();
        this.automation           = new Automation();
        this.contextualController = new ContextualController();
    }

    // ── Getters ──────────────────────────────────────────────
    public List<Device>         getDevices()              { return devices; }
    public List<User>           getUsers()                { return users; }
    public Automation           getAutomation()           { return automation; }
    public ContextualController getContextualController() { return contextualController; }
    public NotificationSystem   getNotificationSystem()   { return notificationSystem; }

    // ── Device management ────────────────────────────────────
    public void addDevice(Device d) {
        devices.add(d);
        System.out.println("[SmartHome] Device added: " + d.getName() + " (ID: " + d.getDeviceId() + ")");
    }

    public void removeDevice(Device d) {
        if (devices.remove(d)) {
            System.out.println("[SmartHome] Device removed: " + d.getName());
        } else {
            System.out.println("[SmartHome] Device not found: " + d.getName());
        }
    }

    public void showAllDevices() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  SMART HOME – Device Status (" + devices.size() + " devices)");
        System.out.println("══════════════════════════════════════════");
        if (devices.isEmpty()) {
            System.out.println("  No devices registered.");
        } else {
            for (Device d : devices) d.showStatus();
        }
        System.out.println("══════════════════════════════════════════\n");
    }

    public void controlDevice(String name) {
        for (Device d : devices) {
            if (d.getName().equalsIgnoreCase(name)) {
                System.out.println("[SmartHome] Toggling: " + name);
                if (d.getStatus()) d.turnOff(); else d.turnOn();
                return;
            }
        }
        System.out.println("[SmartHome] Device not found: " + name);
    }

    // ── Power saving ─────────────────────────────────────────
    public void enablePowerSavingMode() {
        System.out.println("\n[SmartHome] ⚡ Power Saving Mode ENABLED");
        for (Device d : devices) {
            if (d instanceof Light) {
                ((Light) d).setBrightness(20, true);
            }
            if (d instanceof AirConditioner) {
                ((AirConditioner) d).setTemperature(26, true);
            }
            if (d instanceof Fan) {
                ((Fan) d).setSpeed(1, true);
            }
        }
        notificationSystem.energyUsageHigh();
    }

    // ── User management ──────────────────────────────────────
    public void addUser(User u) {
        users.add(u);
        System.out.println("[SmartHome] User registered: " + u.getName() + " [" + u.getRole() + "]");
    }
}
