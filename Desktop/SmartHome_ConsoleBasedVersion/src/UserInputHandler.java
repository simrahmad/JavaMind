// ============================================================
//  UserInputHandler.java  –  Login + Role-Based Console I/O
// ============================================================
import java.util.List;
import java.util.Scanner;

public class UserInputHandler {

    private Scanner   scanner;
    private SmartHome smartHome;
    private User      currentUser;   // tracks who is logged in

    // ── Constructor ──────────────────────────────────────────
    public UserInputHandler(SmartHome smartHome) {
        this.scanner     = new Scanner(System.in);
        this.smartHome   = smartHome;
        this.currentUser = null;
    }

    // ── Input helper ─────────────────────────────────────────
    public String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // ════════════════════════════════════════════════════════
    //  LOGIN SCREEN
    // ════════════════════════════════════════════════════════
    public boolean showLoginScreen() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        SMART HOME  --  LOGIN         ║");
        System.out.println("╚══════════════════════════════════════╝");

        String name     = getUserInput("  Username : ");
        String password = getUserInput("  Password : ");

        for (User u : smartHome.getUsers()) {
            if (u.getName().equalsIgnoreCase(name)) {
                boolean ok = u.login(password);
                if (ok) { currentUser = u; return true; }
                else    { System.out.println("\n  X  Wrong password.\n"); return false; }
            }
        }
        System.out.println("\n  X  User '" + name + "' not found.\n");
        return false;
    }

    // ════════════════════════════════════════════════════════
    //  ADMIN MENU
    // ════════════════════════════════════════════════════════
    private void displayAdminMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║    SMART HOME  --  ADMIN PANEL       ║");
        System.out.println("║  Logged in as: " + padRight(currentUser.getName(), 22) + "║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Show ALL devices                 ║");
        System.out.println("║  2. Toggle any device ON/OFF         ║");
        System.out.println("║  3. Set device property              ║");
        System.out.println("║  4. Enable Power Saving Mode         ║");
        System.out.println("║  5. Apply Scene / Mode               ║");
        System.out.println("║  6. Manage automation schedules      ║");
        System.out.println("║  7. Trigger security alert           ║");
        System.out.println("║  8. Show notifications               ║");
        System.out.println("║  9. Add a new device                 ║");
        System.out.println("║  10. Remove a device                 ║");
        System.out.println("║  11. Logout                          ║");
        System.out.println("║  0. Exit application                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Enter choice: ");
    }

    private String parseAdminCommand(String cmd) {
        switch (cmd) {
            case "1" -> smartHome.showAllDevices();

            case "2" -> {
                smartHome.showAllDevices();
                String name = getUserInput("  Device name to toggle: ");
                smartHome.controlDevice(name);
            }

            case "3" -> adminSetProperty();

            case "4" -> smartHome.enablePowerSavingMode();

            case "5" -> {
                System.out.println("  Available modes: Cinema | Sleep | Away");
                String mode = getUserInput("  Enter mode: ");
                smartHome.getContextualController().applyMode(mode, smartHome.getDevices());
            }

            case "6" -> adminManageAutomation();

            case "7" -> {
                boolean found = false;
                for (Device d : smartHome.getDevices()) {
                    if (d instanceof SecuritySystem ss) { ss.triggerAlert(); found = true; break; }
                }
                if (!found) System.out.println("  No security system found.");
            }

            case "8" -> smartHome.getNotificationSystem().showNotifications();

            case "9" -> adminAddDevice();

            case "10" -> adminRemoveDevice();

            case "11" -> { currentUser.logout(); currentUser = null; }

            case "0" -> { return "EXIT"; }

            default  -> System.out.println("  X Unknown option: " + cmd);
        }
        return cmd;
    }

    // ── Admin: set a specific device property ───────────────
    private void adminSetProperty() {
        smartHome.showAllDevices();
        String name   = getUserInput("  Device name: ");
        Device target = findDevice(name);
        if (target == null) { System.out.println("  Device not found."); return; }

        if (target instanceof Light l) {
            String val = getUserInput("  New brightness (0-100): ");
            l.setBrightness(Integer.parseInt(val), true);
        } else if (target instanceof Fan f) {
            String val = getUserInput("  New speed (1-5): ");
            f.setSpeed(Integer.parseInt(val), true);
        } else if (target instanceof AirConditioner ac) {
            String val = getUserInput("  New temperature (16-30): ");
            ac.setTemperature(Integer.parseInt(val), true);
        } else if (target instanceof Door d) {
            String val = getUserInput("  Lock or Unlock? (l/u): ");
            if (val.equalsIgnoreCase("l")) d.lock(); else d.unlock();
        } else if (target instanceof SecuritySystem ss) {
            String val = getUserInput("  Arm or Disarm? (a/d): ");
            if (val.equalsIgnoreCase("a")) ss.arm(); else ss.disarm();
        } else {
            System.out.println("  No configurable property for this device.");
        }
    }

    // ── Admin: automation sub-menu ───────────────────────────
    private void adminManageAutomation() {
        System.out.println("\n  -- Automation Manager --");
        System.out.println("  a. Add schedule");
        System.out.println("  b. Add rule");
        System.out.println("  c. View all schedules & rules");
        System.out.println("  d. Auto turn off all lights");
        String sub = getUserInput("  Choice: ");
        switch (sub.toLowerCase()) {
            case "a" -> {
                String time   = getUserInput("  Time (e.g. 22:00): ");
                String action = getUserInput("  Action: ");
                smartHome.getAutomation().addSchedule(time, action);
            }
            case "b" -> {
                String condition = getUserInput("  Condition: ");
                String action    = getUserInput("  Action: ");
                smartHome.getAutomation().addRule(condition, action);
            }
            case "c" -> {
                smartHome.getAutomation().executeSchedule();
                smartHome.getAutomation().checkConditions();
            }
            case "d" -> smartHome.getAutomation().autoTurnOffLights(smartHome.getDevices());
            default  -> System.out.println("  Unknown option.");
        }
    }

    // ════════════════════════════════════════════════════════
    //  GUEST MENU
    // ════════════════════════════════════════════════════════
    private void displayGuestMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║    SMART HOME  --  GUEST PANEL       ║");
        System.out.println("║  Logged in as: " + padRight(currentUser.getName(), 22) + "║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. View my devices (lights & fans)  ║");
        System.out.println("║  2. Turn ON a light or fan           ║");
        System.out.println("║  3. Turn OFF a light or fan          ║");
        System.out.println("║  4. Show notifications               ║");
        System.out.println("║  5. Logout                           ║");
        System.out.println("║  0. Exit application                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Enter choice: ");
    }

    private String parseGuestCommand(String cmd) {
        List<Device> devices = smartHome.getDevices();

        switch (cmd) {
            case "1" -> {
                System.out.println("\n[Guest View] Visible devices (lights & fans only):");
                for (Device d : devices)
                    if (d instanceof Light || d instanceof Fan) d.showStatus();
            }

            case "2" -> {
                System.out.println("\n[Guest View] Lights & Fans:");
                for (Device d : devices)
                    if (d instanceof Light || d instanceof Fan) d.showStatus();
                String name = getUserInput("  Device name to turn ON: ");
                boolean found = false;
                for (Device d : devices) {
                    if ((d instanceof Light || d instanceof Fan)
                            && d.getName().equalsIgnoreCase(name)) {
                        d.turnOn(); found = true; break;
                    }
                }
                if (!found) System.out.println("  Access denied or device not found: " + name);
            }

            case "3" -> {
                System.out.println("\n[Guest View] Lights & Fans:");
                for (Device d : devices)
                    if (d instanceof Light || d instanceof Fan) d.showStatus();
                String name = getUserInput("  Device name to turn OFF: ");
                boolean found = false;
                for (Device d : devices) {
                    if ((d instanceof Light || d instanceof Fan)
                            && d.getName().equalsIgnoreCase(name)) {
                        d.turnOff(); found = true; break;
                    }
                }
                if (!found) System.out.println("  Access denied or device not found: " + name);
            }

            case "4" -> smartHome.getNotificationSystem().showNotifications();

            case "5" -> { currentUser.logout(); currentUser = null; }

            case "0" -> { return "EXIT"; }

            default  -> System.out.println("  X  That option is not available for Guest users.");
        }
        return cmd;
    }

    // ════════════════════════════════════════════════════════
    //  MAIN LOOP
    // ════════════════════════════════════════════════════════
    public void runLoop() {
        String result = "";

        while (!result.equals("EXIT")) {

            // ── Not logged in → prompt login (max 3 tries) ──
            if (currentUser == null) {
                boolean loggedIn = false;
                int attempts = 0;
                while (!loggedIn && attempts < 3) {
                    loggedIn = showLoginScreen();
                    attempts++;
                    if (!loggedIn && attempts < 3)
                        System.out.println("  Attempts remaining: " + (3 - attempts));
                }
                if (!loggedIn) {
                    System.out.println("\n  X Too many failed attempts. Exiting.");
                    break;
                }
            }

            // ── Route: Admin verified → Admin panel
            //          Everyone else  → Guest panel ──────────
            if (currentUser instanceof Admin) {
                displayAdminMenu();
                result = parseAdminCommand(scanner.nextLine().trim());

            } else {
                // Any non-admin user is treated as a Guest
                System.out.println("  [Access] Logged in with Guest privileges.");
                displayGuestMenu();
                result = parseGuestCommand(scanner.nextLine().trim());
            }
        }

        System.out.println("\n  Goodbye! Smart Home shutting down.");
        scanner.close();
    }

    // ── Admin: add a new device ──────────────────────────────
    private void adminAddDevice() {
        System.out.println("\n  -- Add New Device --");
        System.out.println("  Types: 1=Light  2=Fan  3=AirConditioner  4=Door  5=SecuritySystem");
        String type = getUserInput("  Select type (1-5): ");
        String name = getUserInput("  Device name      : ");
        String id   = getUserInput("  Device ID        : ");

        Device newDevice = switch (type) {
            case "1" -> new Light(name, id);
            case "2" -> new Fan(name, id);
            case "3" -> new AirConditioner(name, id);
            case "4" -> new Door(name, id);
            case "5" -> new SecuritySystem(name, id, smartHome.getNotificationSystem());
            default  -> null;
        };

        if (newDevice != null) {
            smartHome.addDevice(newDevice);
        } else {
            System.out.println("  X  Invalid device type selected.");
        }
    }

    // ── Admin: remove an existing device ─────────────────────
    private void adminRemoveDevice() {
        smartHome.showAllDevices();
        if (smartHome.getDevices().isEmpty()) return;

        String name   = getUserInput("  Enter device name to remove: ");
        Device target = findDevice(name);

        if (target != null) {
            smartHome.removeDevice(target);
        } else {
            System.out.println("  X  Device '" + name + "' not found.");
        }
    }

    // ── Helpers ──────────────────────────────────────────────
    private Device findDevice(String name) {
        for (Device d : smartHome.getDevices())
            if (d.getName().equalsIgnoreCase(name)) return d;
        return null;
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
