package models;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String createdAt;
    private String role;

    public User(int id, String username, String email, String password, String createdAt, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCreatedAt() { return createdAt; }
    public String getRole() { return role; }
}
