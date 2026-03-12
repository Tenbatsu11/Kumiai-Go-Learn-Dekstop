package fr.kumiaigorpg.desktop.ui;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.utils.SessionManager;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

public class LoginFrame extends JFrame {


    private JPanel cardPanel;
    private CardLayout cardLayout;


    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JLabel loginMessageLabel;
    private JButton loginBtn;


    private JTextField regUsernameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JPasswordField regPasswordConfirmField;
    private JComboBox<String> regNiveauCombo;
    private JLabel regMessageLabel;
    private JButton registerBtn;

    public LoginFrame() {
        if (SessionManager.load()) {
            SwingUtilities.invokeLater(() -> {
                new MainFrame().setVisible(true);
                dispose();
            });
            return;
        }
        setTitle("Kumiai Go RPG — Connexion");
        setSize(720, 840);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);


        JPanel banner = new JPanel();
        banner.setBackground(Theme.RED);
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(new EmptyBorder(24, 0, 24, 0));

        ImageIcon icon = new ImageIcon(getClass().getResource("/logo-kumiai-unique.png"));
        JLabel emoji = new JLabel(new ImageIcon(
                icon.getImage().getScaledInstance(110, 120, Image.SCALE_SMOOTH)
        ));
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Kumiai Go RPG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Apprends le japonais");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(new Color(255, 200, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        banner.add(emoji);
        banner.add(Box.createVerticalStrut(6));
        banner.add(title);
        banner.add(Box.createVerticalStrut(2));
        banner.add(subtitle);

        // Cartes Inscription/Login
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(Theme.BG);
        cardPanel.add(buildLoginPanel(),    "login");
        cardPanel.add(buildRegisterPanel(), "register");

        root.add(banner,    BorderLayout.NORTH);
        root.add(cardPanel, BorderLayout.CENTER);
        setContentPane(root);
    }

    // Panel Login
    private JPanel buildLoginPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Theme.BG);
        form.setBorder(new EmptyBorder(28, 40, 28, 40));

        loginEmailField    = Theme.styledField("Email");
        loginPasswordField = Theme.styledPasswordField();

        addRow(form, "Email",        loginEmailField);
        form.add(Box.createVerticalStrut(14));
        addRow(form, "Mot de passe", loginPasswordField);
        form.add(Box.createVerticalStrut(22));

        loginBtn = Theme.primaryButton("Se connecter");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        form.add(loginBtn);

        form.add(Box.createVerticalStrut(10));


        form.add(buildSeparator("ou"));
        form.add(Box.createVerticalStrut(10));

        // Bouton vers Inscription
        JButton goRegisterBtn = Theme.secondaryButton("Créer un compte");
        goRegisterBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        goRegisterBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        form.add(goRegisterBtn);

        form.add(Box.createVerticalStrut(10));
        loginMessageLabel = Theme.label("", Theme.FONT_SMALL, Color.RED);
        loginMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(loginMessageLabel);


        loginBtn.addActionListener(e -> handleLogin());
        loginPasswordField.addActionListener(e -> handleLogin());
        goRegisterBtn.addActionListener(e -> {
            loginMessageLabel.setText("");
            cardLayout.show(cardPanel, "register");
            setTitle("Kumiai Go RPG — Inscription");
        });

        return form;
    }

    // Panel Inscription
    private JPanel buildRegisterPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Theme.BG);
        form.setBorder(new EmptyBorder(20, 40, 20, 40));

        regUsernameField      = Theme.styledField("Nom d'utilisateur");
        regEmailField         = Theme.styledField("Email");
        regPasswordField      = Theme.styledPasswordField();
        regPasswordConfirmField = Theme.styledPasswordField();
        regNiveauCombo = new JComboBox<>(new String[]{"N5", "N4", "N3", "N2", "N1"});
        regNiveauCombo.setFont(Theme.FONT_BODY);
        regNiveauCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        regNiveauCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        addRow(form, "Nom d'utilisateur",      regUsernameField);
        form.add(Box.createVerticalStrut(10));
        addRow(form, "Email",                  regEmailField);
        form.add(Box.createVerticalStrut(10));
        addRow(form, "Mot de passe",           regPasswordField);
        form.add(Box.createVerticalStrut(10));
        addRow(form, "Confirmer mot de passe", regPasswordConfirmField);
        form.add(Box.createVerticalStrut(10));

        JLabel lblNiveau = Theme.label("Niveau JLPT", Theme.FONT_SMALL, Theme.TEXT_MUTED);
        lblNiveau.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblNiveau);
        form.add(Box.createVerticalStrut(4));
        form.add(regNiveauCombo);
        form.add(Box.createVerticalStrut(18));

        registerBtn = Theme.primaryButton("S'inscrire");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        form.add(registerBtn);

        form.add(Box.createVerticalStrut(10));

        // Bouton retour
        JButton goLoginBtn = Theme.secondaryButton("← Déjà un compte ? Se connecter");
        goLoginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        goLoginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        form.add(goLoginBtn);

        form.add(Box.createVerticalStrut(8));
        regMessageLabel = Theme.label("", Theme.FONT_SMALL, Color.RED);
        regMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(regMessageLabel);


        registerBtn.addActionListener(e -> handleRegister());
        goLoginBtn.addActionListener(e -> {
            regMessageLabel.setText("");
            cardLayout.show(cardPanel, "login");
            setTitle("Kumiai Go RPG — Connexion");
        });

        return form;
    }

    // Login
    private void handleLogin() {
        String email    = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            loginMessageLabel.setText("Remplis tous les champs.");
            return;
        }

        loginBtn.setEnabled(false);
        loginBtn.setText("Connexion...");

        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                return ApiClient.login(email, password);
            }
            @Override protected void done() {
                try {
                    if (get()) {
                        SessionManager.save();
                        new MainFrame().setVisible(true);
                        dispose();
                    } else {
                        loginMessageLabel.setText("Email ou mot de passe incorrect.");
                        loginBtn.setEnabled(true);
                        loginBtn.setText("Se connecter");
                    }
                } catch (Exception ex) {
                    loginMessageLabel.setText("Impossible de contacter le serveur.");
                    loginBtn.setEnabled(true);
                    loginBtn.setText("Se connecter");
                }
            }
        }.execute();
    }

    // Inscription
    private void handleRegister() {
        String username  = regUsernameField.getText().trim();
        String email     = regEmailField.getText().trim();
        String password  = new String(regPasswordField.getPassword());
        String confirm   = new String(regPasswordConfirmField.getPassword());
        String niveau    = (String) regNiveauCombo.getSelectedItem();
        String abonnement= "GRATUIT";

        // ── Validations ───────────────────────────────────────
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            regMessageLabel.setForeground(Color.RED);
            regMessageLabel.setText("Remplis tous les champs.");
            return;
        }
        if (!email.contains("@")) {
            regMessageLabel.setForeground(Color.RED);
            regMessageLabel.setText("Email invalide.");
            return;
        }
        if (password.length() < 6) {
            regMessageLabel.setForeground(Color.RED);
            regMessageLabel.setText("Mot de passe trop court (6 caractères min).");
            return;
        }
        if (!password.equals(confirm)) {
            regMessageLabel.setForeground(Color.RED);
            regMessageLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        registerBtn.setEnabled(false);
        registerBtn.setText("Inscription...");

        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                return ApiClient.register(username, email, password, niveau, abonnement);
            }
            @Override protected void done() {
                try {
                    if (get()) {
                        // ── Inscription OK → connexion automatique ────
                        boolean logged = ApiClient.login(email, password);
                        if (logged) {
                            SessionManager.save();
                            new MainFrame().setVisible(true);
                            dispose();
                        } else {
                            // Inscription OK mais login raté → retour login
                            regMessageLabel.setForeground(Theme.SUCCESS);
                            regMessageLabel.setText("Compte créé ! Connecte-toi.");
                            cardLayout.show(cardPanel, "login");
                            setTitle("Kumiai Go RPG — Connexion");
                        }
                    } else {
                        regMessageLabel.setForeground(Color.RED);
                        regMessageLabel.setText("Erreur lors de l'inscription.");
                        registerBtn.setEnabled(true);
                        registerBtn.setText("S'inscrire");
                    }
                } catch (Exception ex) {
                    regMessageLabel.setForeground(Color.RED);
                    regMessageLabel.setText("Impossible de contacter le serveur.");
                    registerBtn.setEnabled(true);
                    registerBtn.setText("S'inscrire");
                }
            }
        }.execute();
    }

    // UI
    private void addRow(JPanel panel, String labelText, JComponent field) {
        JLabel lbl = Theme.label(labelText, Theme.FONT_SMALL, Theme.TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
    }

    private JPanel buildSeparator(String text) {
        JPanel sep = new JPanel(new BorderLayout(8, 0));
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSeparator left  = new JSeparator();
        JSeparator right = new JSeparator();
        JLabel lbl = Theme.label(text, Theme.FONT_SMALL, Theme.TEXT_MUTED);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        sep.add(left,  BorderLayout.WEST);
        sep.add(lbl,   BorderLayout.CENTER);
        sep.add(right, BorderLayout.EAST);
        return sep;
    }
}
