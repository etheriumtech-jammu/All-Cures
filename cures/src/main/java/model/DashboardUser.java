package model;

public enum DashboardUser {
   MARKETING("marketing.etheriumtech@gmail.com", "Marketing@123"),
	    ADMIN("admin@etheriumtech.com", "Admin@1234"); 
	

    private final String email;
    private final String password;

    DashboardUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // helper method to validate incoming credentials
    public static boolean isValid(String email, String password) {
        for (DashboardUser user : DashboardUser.values()) {
            if (user.email.equalsIgnoreCase(email) &&
                user.password.equals(password)) {
                return true;
            }
        }
        return false;
    }
}
