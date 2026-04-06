package com.team4.model.user;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Abstract class for all users in the system.
 *
 * Design decisions:
 * - ONE constructor that always validates.
 *   Another constructor for loading trusted data in the database (no validation needed)
 * - All validation logic lives HERE.
 * - Setters are protected only subclasses and same-package code
 *   can mutate. The service layer calls setters through public
 *   update methods, keeping mutation strict.
 */

public abstract class User {
    private int userId;
    private String username;
    private String password;
    private String fullname;
    private String phone;
    private String dOB;
    private UserRole role;

    // Constructor

    protected User(int userId, String username, String password, String fullname, String phone, String dOB,
            UserRole role) {

        setUserId(userId);
        setUsername(username);
        setPassword(password);
        setFullname(fullname);
        setPhone(phone);
        setdOB(dOB);
        setRole(role);
    }

    /**
     * Another constructor for load User from trusted DB data, no validation.
     * Use ONLY inside DAO(Data Access Object) implementations.
     */
    protected User(int id, String username, String password,
                   String fullname, String phone, String dOB,
                   UserRole role, boolean trusted) {
        this.userId   = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone    = phone;
        this.dOB      = dOB;
        this.role     = role;
    }

    // Getters

    public int getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getdOB() {
        return dOB;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    public UserRole getRole() {
        return role;
    }

    // Setters

    protected void setUserId(int userId) {
        if (userId < 0) throw new IllegalArgumentException("User ID cannot be negative.");
        this.userId = userId;
    }

    protected void setPhone(String phone) {
        String p = (phone == null) ? "" : phone.trim();
        if (phone.isEmpty() || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number must be filled.");
        }
        if (!isDigits(p) || p.length() < 8 || p.length() > 12) {
            throw new IllegalArgumentException("Phone number must be 9–11 digits.");
        }
        this.phone = p;
    }


    protected void setdOB(String dOB) {
        String d = dOB.trim();
        try {
            LocalDate parsed = LocalDate.parse(d); // expects yyyy-MM-dd
            if (parsed.isAfter(LocalDate.now()))
                throw new IllegalArgumentException("Date of birth cannot be in the future.");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Date of birth must be in format YYYY-MM-DD (e.g. 2000-05-25).");
        }
        this.dOB = d;
    }

    protected void setUsername(String username) {
        if (username.isEmpty() || username.isBlank()) {
            throw new IllegalArgumentException("username must be filled.");
        }
        this.username = username.trim().toLowerCase();
    }

    protected void setPassword(String password) {
        if (password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("Password must be filled.");
        }
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException(
                    "Invalid password. " + PASSWORD_HINT);
        }
        this.password = password;
    }

    protected void setFullname(String fullname) {
        String n = (fullname == null) ? "" : fullname.trim();
        if (fullname.isEmpty() || fullname.isBlank()) {
            throw new IllegalArgumentException("Full name must be filled.");
        }

        this.fullname = n;
    }

    protected void setRole(UserRole role) {
        if (role == null) throw new IllegalArgumentException("Role is required.");
        this.role = role;
    }

    // ==== Helpers ===

    public boolean isDigits(String s) {
        if (s.isBlank())
            return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

    public static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*_])[A-Za-z\\d!@#$%&*_]{8,20}$";


    public static final String PASSWORD_HINT  =
            "8–20 chars, at least one uppercase, lowercase, digit, and special character (!@#$%&*_). No spaces.";

    /*
     * 1. check string length
     * 2 check if it's digit
     * 3. parse from month to month in number
     * 4. check if:
     * -dd between 01-31
     * -mm between 01-12
     * -yyyy <= current year
     * -dd <=29 on feb
     */

//    public boolean isValidDate(String date) {
//        try {
//            LocalDate parsedDate = LocalDate.parse(date); // ISO format yyyy-MM-dd
//            return !parsedDate.isAfter(LocalDate.now());
//        } catch (DateTimeParseException e) {
//            System.out.println("\nDate must be in format: yyyy-MM-dd.");
//            return false;
//        }
//    }

//    public boolean isValidDate(String date) {
//
//        String[] parts = date.split("-");
//        String dd = parts[0].trim();
//        String mm = parts[1].trim();
//        String yyyy = parts[2].trim();
//
//        LocalDate nowDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
//        String currentYear = nowDate.format(formatter);
//
//        if (dd.isBlank() || mm.isBlank() || yyyy.isBlank()
//                || !isDigits(dd) || !isDigits(mm) || !isDigits(yyyy)
//                || dd.length() != 2 || mm.length() != 2 || yyyy.length() != 4) {
//
//            System.out.println("\n" + "Date must enter in this format: dd-mm-yyyy.");
//            return false;
//        }
//
//        // check valid date
//        else if (Integer.parseInt(dd) > 31 || Integer.parseInt(dd) < 1
//                || Integer.parseInt(mm) > 12 || Integer.parseInt(mm) < 1
//                || Integer.parseInt(yyyy) > Integer.parseInt(currentYear)
//                || (Integer.parseInt(mm) == 2 && Integer.parseInt(dd) > 29)) {
//
//            System.out.println("\n" + "Please enter a accurate date (day: from 01 to 31, month: from 01 to 12).");
//            return false;
//        }
//
//        return true;
//    }

    /*
     * Password must be from 8-20 characters.
     * must contain at lease 1 uppercase and lower case
     * at least 1 speacial character
     * no white space.
     */



//    private boolean isValidPassword(String pw) {
//        final String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*_])[A-Za-z\\d!@#$%&*_]{8,20}$";
//        Pattern pattern = Pattern.compile(regex);
//
//        if (pw == null || !(pattern.matcher(pw).matches())) {
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public String toString() {
        return "User{" +
                "Id='" + userId + '\'' +
                ", fullName='" + fullname + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", position='" + role + '\'' +
                ", date of birth=" + dOB +
                '}';
    }

}
