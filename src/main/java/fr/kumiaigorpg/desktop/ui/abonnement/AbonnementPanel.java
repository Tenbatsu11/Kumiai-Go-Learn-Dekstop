package fr.kumiaigorpg.desktop.ui.abonnement;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.ui.MainFrame;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AbonnementPanel extends JPanel {

    private JLabel lblActuel;
    private JButton btnGratuit, btnPremium;
    private final MainFrame mainFrame;

    public AbonnementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Abonnement"), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG);
        content.setBorder(new EmptyBorder(24, 40, 24, 40));

        // Abonnement actuel
        lblActuel = Theme.label(
            "Abonnement actuel : " + ApiClient.getAbonnement(),
            new Font("Segoe UI", Font.BOLD, 15), Theme.RED
        );
        lblActuel.setAlignmentX(LEFT_ALIGNMENT);
        lblActuel.setBorder(new EmptyBorder(0, 0, 24, 0));

        content.add(lblActuel);

        // Side Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(800, 380));
        cardsPanel.setAlignmentX(LEFT_ALIGNMENT);

        cardsPanel.add(buildGratuitCard());
        cardsPanel.add(buildPremiumCard());

        content.add(cardsPanel);

        updateButtonStates();

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel buildGratuitCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 2),
            new EmptyBorder(24, 24, 24, 24)
        ));

        addLine(card, "Plan Gratuit",     new Font("Segoe UI", Font.BOLD, 20), Theme.TEXT);
        addLine(card, "0€ / mois",            Theme.FONT_H2, Theme.TEXT_MUTED);
        card.add(Box.createVerticalStrut(16));
        addLine(card, "Kanjis N5 et N4",  Theme.FONT_BODY, Theme.SUCCESS);
        addLine(card, "Vocabulaire de base", Theme.FONT_BODY, Theme.SUCCESS);
        addLine(card, "Kanjis N3, N2, N1",Theme.FONT_BODY, new Color(189, 189, 189));
        addLine(card, "Contenu exclusif", Theme.FONT_BODY, new Color(189, 189, 189));
        card.add(Box.createVerticalStrut(20));

        btnGratuit = Theme.secondaryButton("Choisir Gratuit");
        btnGratuit.setAlignmentX(LEFT_ALIGNMENT);
        btnGratuit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnGratuit.addActionListener(e -> selectPlan("GRATUIT"));
        card.add(btnGratuit);

        return card;
    }

    private JPanel buildPremiumCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.RED);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        addLine(card, "Plan Premium",          new Font("Segoe UI", Font.BOLD, 20), Color.WHITE);
        addLine(card, "4,99€ / mois",              Theme.FONT_H2, new Color(255, 200, 200));
        card.add(Box.createVerticalStrut(16));
        addLine(card, "Tous les kanjis N5→N1", Theme.FONT_BODY, Color.WHITE);
        addLine(card, "Tout le vocabulaire",   Theme.FONT_BODY, Color.WHITE);
        addLine(card, "Contenu exclusif",       Theme.FONT_BODY, Color.WHITE);
        addLine(card, "Mises à jour en avant-première", Theme.FONT_BODY, Color.WHITE);
        addLine(card, "Accès au jeu Kumiai GO RPG",  Theme.FONT_BODY, Color.WHITE);
        card.add(Box.createVerticalStrut(20));

        btnPremium = new JButton("Passer Premium");
        btnPremium.setBackground(Color.WHITE);
        btnPremium.setForeground(Theme.RED);
        btnPremium.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPremium.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnPremium.setFocusPainted(false);
        btnPremium.setOpaque(true);
        btnPremium.setAlignmentX(LEFT_ALIGNMENT);
        btnPremium.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnPremium.addActionListener(e -> selectPlan("PREMIUM"));
        card.add(btnPremium);

        return card;
    }

    private void addLine(JPanel panel, String text, Font font, Color color) {
        JLabel lbl = Theme.label(text, font, color);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(2, 0, 2, 0));
        panel.add(lbl);
    }

    private void selectPlan(String plan) {
        btnGratuit.setEnabled(false);
        btnPremium.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() throws Exception {
                return ApiClient.updateAbonnement(plan); // ← appel API vers la DB
            }
            @Override protected void done() {
                try {
                    if (get()) {
                        lblActuel.setText("Abonnement actuel : " + plan);
                        updateButtonStates();
                        mainFrame.refreshAll();
                        String msg = plan.equals("PREMIUM")
                                ? "Bienvenue en Premium ! Tout le contenu est maintenant débloqué."
                                : "Plan Gratuit activé.";
                        JOptionPane.showMessageDialog(
                                AbonnementPanel.this, msg, "Abonnement",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                AbonnementPanel.this,
                                "Erreur lors de la mise à jour.",
                                "Erreur", JOptionPane.ERROR_MESSAGE
                        );
                        updateButtonStates(); // ← remet les boutons dans leur état initial
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            AbonnementPanel.this,
                            "Erreur réseau : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE
                    );
                    updateButtonStates();
                }
            }
        }.execute();
    }

    private void updateButtonStates() {
        boolean isPremium = "PREMIUM".equals(ApiClient.getAbonnement());
        btnPremium.setEnabled(!isPremium);
        btnGratuit.setEnabled(isPremium);
    }
}
