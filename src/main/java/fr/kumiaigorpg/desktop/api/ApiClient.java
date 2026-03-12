package fr.kumiaigorpg.desktop.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.kumiaigorpg.desktop.model.*;
import fr.kumiaigorpg.desktop.utils.SessionManager;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/";
    private static final OkHttpClient http = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static final MediaType JSON = MediaType.parse("application/json");

    // Session
    private static String token;
    private static String userId;
    private static String username;
    private static String email;
    private static String userlvl;
    private static String abonnement;

    // Auth
    public static boolean login(String email, String password) throws IOException {
        String body = gson.toJson(Map.of("email", email, "password", password));
        Request req = new Request.Builder()
                .url(BASE_URL + "users/login")
                .post(RequestBody.create(body, JSON))
                .build();

        try (Response res = http.newCall(req).execute()) {
            if (res.isSuccessful() && res.body() != null) {
                LoginResponse lr = gson.fromJson(res.body().string(), LoginResponse.class);
                token    = lr.getToken();
                userId = lr.getId();
                username = lr.getUsername();
                userlvl  = lr.getUserlvl();
                abonnement  = lr.getAbonnement();
                ApiClient.email = email;
                return true;
            }
            return false;
        }
    }

    public static boolean register(String username, String email,
                                   String password, String niveau, String abonnement) throws IOException {
        String body = gson.toJson(Map.of(
                "username", username,
                "email",    email,
                "password", password,
                "userlvl",  niveau,
                "abonnement", abonnement
        ));
        Request req = new Request.Builder()
                .url(BASE_URL + "users")
                .post(RequestBody.create(body, JSON))
                .build();

        try (Response res = http.newCall(req).execute()) {
            return res.isSuccessful();
        }
    }

    public static void logout() {
        token = null; userId = null; username = null;
        email = null; userlvl = null; abonnement = null;
        SessionManager.clear();
    }

    public static boolean isLoggedIn() { return token != null; }

    // Kanji
    public static List<Kanji> getAllKanji() throws IOException {
        return getList("kanji", new TypeToken<List<Kanji>>(){}.getType());
    }

    public static List<Kanji> getKanjiByNiveau(String niveau) throws IOException {
        return getList("kanji/jlptlvl/" + niveau, new TypeToken<List<Kanji>>(){}.getType());
    }

    public static List<Kanji> searchKanji(String kanjiName) throws IOException {
        return getList("kanji/search?kanjiName=" + kanjiName, new TypeToken<List<Kanji>>(){}.getType());
    }

    // Vocabulaire
    public static List<Vocabulaire> getAllVocabulaire() throws IOException {
        return getList("vocabulaire", new TypeToken<List<Vocabulaire>>(){}.getType());
    }

    public static List<Vocabulaire> getVocabulaireByNiveau(String niveau) throws IOException {
        return getList("vocabulaire/jlptlvl/" + niveau, new TypeToken<List<Vocabulaire>>(){}.getType());
    }

    public static List<Vocabulaire> searchVocabulaire(String word) throws IOException {
        return getList("vocabulaire/search?word=" + word, new TypeToken<List<Vocabulaire>>(){}.getType());
    }

    // Users
    public static boolean updateUser(String newUsername, String newEmail) throws IOException {
        User u = new User();
        u.setUsername(newUsername);
        u.setEmail(newEmail);
        u.setUserlvl(userlvl);
        u.setAbonnement(abonnement);

        String body = gson.toJson(u);
        Request req = new Request.Builder()
                .url(BASE_URL + "users/" + userId)
                .header("Authorization", "Bearer " + token)
                .put(RequestBody.create(body, JSON))
                .build();

        try (Response res = http.newCall(req).execute()) {
            if (res.isSuccessful()) {
                username = newUsername;
                email    = newEmail;
                return true;
            }
            return false;
        }
    }

    public static boolean updateAbonnement(String abonnement) throws IOException {
        System.out.println("userId : " + userId);
        System.out.println("token  : " + token);
        System.out.println("url    : " + BASE_URL + "users/" + userId + "/abonnement");


        String body = gson.toJson(Map.of("abonnement", abonnement));
        Request request = new Request.Builder()
            .url(BASE_URL + "users/" + userId + "/abonnement")
                .header("Authorization", "Bearer " + token)
                .put(RequestBody.create(body, JSON))
                .build();

        try (Response res = http.newCall(request).execute()) {
            System.out.println("Code HTTP : " + res.code()); // ← vérifie le code retour
            if (res.isSuccessful()) {
                ApiClient.abonnement = abonnement;
                return true;
            }
            return false;
        }
    }

    public static void setSession(String tok, String id, String user,
                                  String mail, String lvl, String abo) {
        token      = tok;
        userId     = id;
        username   = user;
        email      = mail;
        userlvl    = lvl;
        abonnement = abo;
    }

    // Getters et Setters
    public static String getToken() { return token; }
    public static String getUserId() { return userId; }
    public static String getUsername()    { return username; }
    public static String getEmail()       { return email; }
    public static String getUserlvl()     { return userlvl; }
    public static String getAbonnement()  { return abonnement; }
    public static void   setAbonnement(String a) { abonnement = a; }


    private static <T> T getList(String endpoint, Type type) throws IOException {
        Request req = new Request.Builder()
                .url(BASE_URL + endpoint)
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        try (Response res = http.newCall(req).execute()) {
            if (res.isSuccessful() && res.body() != null) {
                return gson.fromJson(res.body().string(), type);
            }
            throw new IOException("Erreur API : " + res.code());
        }
    }
}
