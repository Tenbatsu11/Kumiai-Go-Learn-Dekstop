package fr.kumiaigorpg.desktop.ui.home;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomePanel extends JPanel {
    private JLabel lblAbo;

    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Accueil"), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG);
        content.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Welcome page
        JPanel welcomeCard = new JPanel();
        welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));
        welcomeCard.setBackground(Theme.RED);
        welcomeCard.setBorder(new EmptyBorder(24, 24, 24, 24));
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JLabel lblWelcome = new JLabel("Bienvenue, " + ApiClient.getUsername() + " !");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblNiveau = new JLabel("Niveau JLPT : " + ApiClient.getUserlvl());
        lblNiveau.setFont(Theme.FONT_BODY);
        lblNiveau.setForeground(new Color(255, 200, 200));
        lblNiveau.setAlignmentX(LEFT_ALIGNMENT);

        lblAbo = new JLabel("Abonnement : " + ApiClient.getAbonnement());
        lblAbo.setFont(Theme.FONT_BODY);
        lblAbo.setForeground(new Color(255, 200, 200));
        lblAbo.setAlignmentX(LEFT_ALIGNMENT);

        welcomeCard.add(lblWelcome);
        welcomeCard.add(Box.createVerticalStrut(6));
        welcomeCard.add(lblNiveau);
        welcomeCard.add(Box.createVerticalStrut(2));
        welcomeCard.add(lblAbo);

        // Présentation
        JPanel aboutCard = Theme.card();
        aboutCard.setLayout(new BoxLayout(aboutCard, BoxLayout.Y_AXIS));
        aboutCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel aboutTitle = Theme.label("À propos de l'application", Theme.FONT_H2, Theme.TEXT);
        aboutTitle.setAlignmentX(LEFT_ALIGNMENT);
        aboutTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        JTextArea aboutText = new JTextArea(
            "Kumiai Go RPG est une application d'apprentissage du japonais.\n\n" +
            "Explore des centaines de kanjis et de mots de vocabulaire classés par niveau " +
            "JLPT (N5 à N1). Suis ta progression et passe en Premium pour accéder à " +
            "tout le contenu sans limite !"
        );
        aboutText.setFont(Theme.FONT_BODY);
        aboutText.setForeground(Theme.TEXT_MUTED);
        aboutText.setEditable(false);
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setOpaque(false);
        aboutText.setAlignmentX(LEFT_ALIGNMENT);

        aboutCard.add(aboutTitle);
        aboutCard.add(aboutText);

        // Niveau JLPT
        JPanel niveauxTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        niveauxTitle.setOpaque(false);
        niveauxTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        niveauxTitle.add(Theme.label("Niveaux disponibles", Theme.FONT_H2, Theme.TEXT));

        JPanel niveauxPanel = new JPanel(new GridLayout(1, 5, 8, 0));
        niveauxPanel.setOpaque(false);
        niveauxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        String[][] levels = {
            {"N5", "#E3F2FD", "#1565C0"},
            {"N4", "#E8F5E9", "#2E7D32"},
            {"N3", "#FFF9C4", "#F57F17"},
            {"N2", "#FCE4EC", "#C62828"},
            {"N1", "#EDE7F6", "#4527A0"},
        };

        for (String[] lvl : levels) {
            JLabel badge = new JLabel(lvl[0], SwingConstants.CENTER);
            badge.setFont(new Font("Segoe UI", Font.BOLD, 18));
            badge.setForeground(Color.decode(lvl[2]));
            badge.setOpaque(true);
            badge.setBackground(Color.decode(lvl[1]));
            badge.setBorder(BorderFactory.createLineBorder(Color.decode(lvl[2]), 1));
            niveauxPanel.add(badge);
        }

        content.add(welcomeCard);
        content.add(Box.createVerticalStrut(20));
        content.add(aboutCard);
        content.add(Box.createVerticalStrut(20));
        content.add(niveauxTitle);
        content.add(Box.createVerticalStrut(8));
        content.add(niveauxPanel);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }
    public void refresh() {
        lblAbo.setText("Abonnement : " + ApiClient.getAbonnement());
    }
}
