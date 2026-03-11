package fr.kumiaigorpg.desktop.model;

public class LoginResponse {
    private String token;
    public String id;
    private String username;
    private String userlvl;
    private String abonnement;

    public String getToken() { return token; }
    public String getId() { return id; };
    public String getUsername() { return username; }
    public String getUserlvl() { return userlvl; }
    public String getAbonnement() { return abonnement; }
}
