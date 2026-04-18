// ============================================================
//  Main.java  –  Entry point
// ============================================================
public class Main {

    public static void main(String[] args) {

        // ── 1. Create Smart Home ─────────────────────────────
        SmartHome home = new SmartHome();

        // ── 2. Register devices ──────────────────────────────
        Light            livingRoomLight = new Light("Living Room Light", "D001");
        Light            bedroomLight    = new Light("Bedroom Light",     "D002", 80);
        Fan              ceilingFan      = new Fan("Ceiling Fan",         "D003", 3);
        AirConditioner   ac              = new AirConditioner("Main AC", "D004", 24);
        Door             frontDoor       = new Door("Front Door",         "D005");
        SecuritySystem   security        = new SecuritySystem(
                "Security Cam", "D006",
                home.getNotificationSystem());

        home.addDevice(livingRoomLight);
        home.addDevice(bedroomLight);
        home.addDevice(ceilingFan);
        home.addDevice(ac);
        home.addDevice(frontDoor);
        home.addDevice(security);

        // ── 3. Register users ────────────────────────────────
        Admin admin = new Admin("Alice", "admin123");
        Guest guest = new Guest("Bob",   "guest456");

        home.addUser(admin);
        home.addUser(guest);

        // ── 4. Demonstrate login ─────────────────────────────
        System.out.println();
        admin.login("admin123");
        guest.login("guest456");

        // ── 5. Demonstrate device control ───────────────────
        System.out.println();
        livingRoomLight.turnOn();
        livingRoomLight.setBrightness(75, true);
        ceilingFan.turnOn();
        ceilingFan.setSpeed(4, true);
        ac.turnOn();
        ac.setTemperature(22, true);
        frontDoor.lock();

        // ── 6. Admin views all devices ───────────────────────
        System.out.println();
        admin.viewAllDevices(home.getDevices());

        // ── 7. Guest sees limited devices ────────────────────
        guest.viewLimitedDevices(home.getDevices());

        // ── 8. Automation schedules & rules ──────────────────
        System.out.println();
        home.getAutomation().addSchedule("22:00", "Turn off all lights");
        home.getAutomation().addSchedule("06:00", "Turn on Living Room Light");
        home.getAutomation().addRule("motion_detected", "arm security system");
        home.getAutomation().addRule("high_energy", "enable power saving");

        // ── 9. Apply a contextual mode ───────────────────────
        home.getContextualController().applyMode("Cinema", home.getDevices());

        // ── 10. Power saving demonstration ──────────────────
        home.enablePowerSavingMode();

        // ── 11. Security alert demonstration ─────────────────
        System.out.println();
        security.arm();
        security.triggerAlert();

        // ── 12. Show notifications ───────────────────────────
        home.getNotificationSystem().showNotifications();

        // ── 13. Final device snapshot ────────────────────────
        home.showAllDevices();

        // ── 14. Launch interactive console with login ─────────
        System.out.println("\n==========================================");
        System.out.println("  System ready. Please log in.");
        System.out.println("  [Hint] Admin -> Alice / admin123");
        System.out.println("         Guest -> Bob   / guest456");
        System.out.println("==========================================");
        UserInputHandler handler = new UserInputHandler(home);
        handler.runLoop();
    }
}
