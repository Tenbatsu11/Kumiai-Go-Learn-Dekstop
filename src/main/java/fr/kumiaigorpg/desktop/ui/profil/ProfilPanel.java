package fr.kumiaigorpg.desktop.ui.profil;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.ui.MainFrame;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilPanel extends JPanel {

    private JTextField aboField;
    private final MainFrame mainFrame;
    private JTextField usernameField, emailField;
    private JLabel statusLabel;

    public ProfilPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Profil"), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG);
        content.setBorder(new EmptyBorder(24, 40, 24, 40));

        // ── Avatar ────────────────────────────────────────────
        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("SansSerif", Font.PLAIN, 64));
        avatar.setAlignmentX(LEFT_ALIGNMENT);

        JLabel nameLabel = Theme.label(ApiClient.getUsername(), Theme.FONT_TITLE, Theme.TEXT);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel emailLabel = Theme.label(ApiClient.getEmail(), Theme.FONT_BODY, Theme.TEXT_MUTED);
        emailLabel.setAlignmentX(LEFT_ALIGNMENT);

        content.add(avatar);
        content.add(Box.createVerticalStrut(8));
        content.add(nameLabel);
        content.add(emailLabel);
        content.add(Box.createVerticalStrut(24));

        // ── Formulaire ────────────────────────────────────────
        JPanel formCard = Theme.card();
        formCard.setLayout(new GridBagLayout());
        formCard.setMaximumSize(new Dimension(600, 300));
        formCard.setAlignmentX(LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = Theme.styledField("Nom d'utilisateur");
        usernameField.setText(ApiClient.getUsername());

        emailField = Theme.styledField("Email");
        emailField.setText(ApiClient.getEmail());

        JTextField niveauField = Theme.styledField("");
        niveauField.setText(ApiClient.getUserlvl());
        niveauField.setEditable(false);
        niveauField.setBackground(new Color(245, 245, 245));

        JTextField aboField = Theme.styledField("");
        aboField.setText(ApiClient.getAbonnement());
        aboField.setEditable(false);
        aboField.setBackground(new Color(245, 245, 245));

        addFormRow(formCard, gbc, 0, "Nom d'utilisateur", usernameField);
        addFormRow(formCard, gbc, 1, "Email",              emailField);
        addFormRow(formCard, gbc, 2, "Niveau JLPT",        niveauField);
        addFormRow(formCard, gbc, 3, "Abonnement",         aboField);

        content.add(formCard);
        content.add(Box.createVerticalStrut(16));

        // ── Status ────────────────────────────────────────────
        statusLabel = Theme.label("", Theme.FONT_SMALL, Theme.SUCCESS);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(statusLabel);
        content.add(Box.createVerticalStrut(16));

        // ── Boutons ───────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(LEFT_ALIGNMENT);

        JButton btnSave   = Theme.primaryButton("Sauvegarder");
        JButton btnLogout = Theme.secondaryButton("Déconnexion");

        btnSave.addActionListener(e -> saveProfile());
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, "Voulez-vous vous déconnecter ?", "Déconnexion",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) mainFrame.logout();
        });

        btnPanel.add(btnSave);
        btnPanel.add(btnLogout);
        content.add(btnPanel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        return scroll;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        panel.add(Theme.label(labelText, Theme.FONT_BODY, Theme.TEXT_MUTED), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        field.setPreferredSize(new Dimension(280, 36));
        panel.add(field, gbc);
    }

    private void saveProfile() {
        String username = usernameField.getText().trim();
        String email    = emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                return ApiClient.updateUser(username, email);
            }
            @Override protected void done() {
                try {
                    if (get()) {
                        statusLabel.setForeground(Theme.SUCCESS);
                        statusLabel.setText("Profil mis à jour avec succès !");
                    } else {
                        statusLabel.setForeground(Color.RED);
                        statusLabel.setText("Erreur lors de la mise à jour.");
                    }
                } catch (Exception ex) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Erreur réseau : " + ex.getMessage());
                }
            }
        }.execute();
    }
    public void refresh() {
        aboField.setText(ApiClient.getAbonnement());
    }
}
