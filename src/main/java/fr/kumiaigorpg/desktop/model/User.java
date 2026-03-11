package fr.kumiaigorpg.desktop.model;

public class User {
    private String id;
    private String email;
    private String username;
    private String password;
    private String userlvl;
    private String abonnement;

    public User() {}

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getUserlvl() { return userlvl; }
    public String getAbonnement() { return abonnement; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setUserlvl(String userlvl) { this.userlvl = userlvl; }
    public void setAbonnement(String abonnement) { this.abonnement = abonnement; }

}
