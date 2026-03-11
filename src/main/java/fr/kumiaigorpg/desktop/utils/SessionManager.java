package fr.kumiaigorpg.desktop.utils;

import fr.kumiaigorpg.desktop.api.ApiClient;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class SessionManager {

    private static final String SESSION_FILE = System.getProperty("user.home") + "/.kumiai_session";

    public static void save() {
        Properties props = new Properties();
        props.setProperty("token",      ApiClient.getToken()       != null ? ApiClient.getToken()       : "");
        props.setProperty("userId",     ApiClient.getUserId()      != null ? ApiClient.getUserId()      : "");
        props.setProperty("username",   ApiClient.getUsername()    != null ? ApiClient.getUsername()    : "");
        props.setProperty("email",      ApiClient.getEmail()       != null ? ApiClient.getEmail()       : "");
        props.setProperty("userlvl",    ApiClient.getUserlvl()     != null ? ApiClient.getUserlvl()     : "");
        props.setProperty("abonnement", ApiClient.getAbonnement()  != null ? ApiClient.getAbonnement()  : "");

        try (OutputStream out = new FileOutputStream(SESSION_FILE)) {
            props.store(out, "Kumiai Session");
        } catch (IOException e) {
            System.err.println("Impossible de sauvegarder la session : " + e.getMessage());
        }
    }

    public static boolean load() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) return false;

        Properties props = new Properties();
        try (InputStream in = new FileInputStream(file)) {
            props.load(in);
            String token = props.getProperty("token", "");
            if (token.isEmpty()) return false;

            ApiClient.setSession(
                    token,
                    props.getProperty("userId",     ""),
                    props.getProperty("username",   ""),
                    props.getProperty("email",      ""),
                    props.getProperty("userlvl",    ""),
                    props.getProperty("abonnement", "")
            );
            return true;
        } catch (IOException e) {
            System.err.println("Impossible de charger la session : " + e.getMessage());
            return false;
        }
    }

    public static void clear() {
        new File(SESSION_FILE).delete();
    }
}