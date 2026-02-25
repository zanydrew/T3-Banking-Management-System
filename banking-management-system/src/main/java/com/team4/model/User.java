package com.team4.model;

public abstract class User {
    private String id;
    private String username;
    private String password;
    private String fullname;
    private String phone;
    private String dOB;
    private UserRole role;

    // Constructor

    protected User(String id, String username, String password, String fullname, String phone, String dOB,
            UserRole role) {

        setId(id);
        setUsername(username);
        setPassword(password);
        setFullname(fullname);
        setPhone(phone);
        setdOB(dOB);
        setRole(role);
    }

    // Getters

    public String getId() {
        return id;
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

    protected void setId(String id) {
        if (id.isEmpty() || id.isBlank()) {
            throw new IllegalArgumentException("Id must be filled.");
        }
        this.id = id.trim();
    }

    protected void setPhone(String phone) {
        String p = (phone == null) ? "" : phone.trim();
        if (phone.isEmpty() || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number must be filled.");
        } else if (!isDigits(p) || p.length() < 8 || p.length() > 12) {
            throw new IllegalArgumentException("Phone number must be valid.");
        }
        this.phone = p;
    }

    // Stragy: recieve the full dOB in format dd-mm-yyyy and parse it to dd, mm,
    // yyyy separately.
    // then use isValidDate to validate

    protected void setdOB(String dOB) {
        String d = (dOB == null) ? "" : dOB.trim();

        if (dOB.isEmpty()) {
            throw new IllegalArgumentException("Date of birth must be filled.");
        } else if (d.length() != 10 || !isValidDate(d)) {
            throw new IllegalArgumentException("Invalid Date of birth.");
        }

        this.dOB = d;
    }

    // protected void setdOB(String dd, String mm, String yyyy) {
    // if (dd == null || mm == null || yyyy == null) {
    // dd = "";
    // mm = "";
    // yyyy = "";
    // }
    // if (dd.isEmpty() || mm.isEmpty() || yyyy.isEmpty()) {
    // throw new IllegalArgumentException("Date of birth must be filled.");
    // }
    // if (!isValidDate(dd, mm, yyyy))
    // dOB = dd + "-" + mm + "-" + yyyy;

    // dOB = dd + "-" + mm + "-" + yyyy;
    // }

    protected void setUsername(String username) {
        if (username.isEmpty() || username.isBlank()) {
            throw new IllegalArgumentException("username must be filled.");
        }
        this.username = username.trim().toLowerCase();
    }

    protected void setPassword(String password) {
        if (password.isEmpty() || password.isBlank()) {
            throw new IllegalArgumentException("Password must be filled.");
        } else if (!(isValidPassword(password))) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters with at least one upper and lower case and special character and no white space.");
        }
        this.password = password.trim();
    }

    protected void setFullname(String fullname) {
        String n = (fullname == null) ? "" : fullname.trim();
        if (fullname.isEmpty() || fullname.isBlank()) {
            throw new IllegalArgumentException("Full name must be filled.");
        }

        this.fullname = n;
    }

    protected void setRole(UserRole role) {
        this.role = role;
    }

}
