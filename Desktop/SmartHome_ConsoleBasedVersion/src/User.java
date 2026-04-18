// ============================================================
//  User.java  –  Base class for all users
// ============================================================
public class User {

    // ── Fields ───────────────────────────────────────────────
    protected String name;
    protected String password;
    protected String role;
    protected boolean loggedIn;

    // ── Constructors (overloading) ───────────────────────────
    public User(String name, String password, String role) {
        this.name     = name;
        this.password = password;
        this.role     = role;
        this.loggedIn = false;
    }

    public User(String name, String password) {
        this(name, password, "Guest");
    }

    // ── Getters & Setters ────────────────────────────────────
    public String getName()             { return name; }
    public void   setName(String n)     { this.name = n; }

    public String getRole()             { return role; }
    public void   setRole(String r)     { this.role = r; }

    public boolean isLoggedIn()         { return loggedIn; }

    // ── Business methods ─────────────────────────────────────
    public boolean login(String inputPassword) {
        if (this.password.equals(inputPassword)) {
            this.loggedIn = true;
            System.out.println("[Auth] " + name + " logged in as " + role + ".");
            return true;
        }
        System.out.println("[Auth] Incorrect password for " + name + ".");
        return false;
    }

    public void logout() {
        this.loggedIn = false;
        System.out.println("[Auth] " + name + " logged out.");
    }

    // ── Override toString ────────────────────────────────────
    @Override
    public String toString() {
        return "User{name='" + name + "', role='" + role + "', loggedIn=" + loggedIn + "}";
    }
}
