package fr.kumiaigorpg.desktop.rpg;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class KumiaiGoRPG extends JPanel {

    public KumiaiGoRPG() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Kumiai GO RPG"), BorderLayout.NORTH);
        if ("PREMIUM".equals(ApiClient.getAbonnement())) {
            add(buildContent(), BorderLayout.CENTER);
        } else {
            add(buildLockedContent(), BorderLayout.CENTER);
        }
    }
    private JScrollPane buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG);
        content.setBorder(new EmptyBorder(24,32,24,32));

        JPanel aboutCard = Theme.card();
        aboutCard.setLayout(new BoxLayout(aboutCard, BoxLayout.Y_AXIS));
        aboutCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JTextArea info = new JTextArea(
                "Kumiai GO RPG est un RPG où vous allez pouvoir profiter d'une toute nouvelle aventure en lien av le jeu Kumiai GO que vous connaissez déjà!\n"+
                "Explorez un nouveau monde créé uniquement pour Kumiai GO Learn et fait par notre team de développeurs." +
                "Affrontez des monstres où seul votre connaissance du Japonais pourra triompher de cette quête qui vous attend. \n\n" +
                "Si vous aussi vous souhaitez accéder à ce nouvel univers, n'attendez plus et passé à l'offre Premium de Kumiai GO Learn !"
                );
        info.setEditable(false);
        info.setFont(Theme.FONT_BODY);
        info.setForeground(Theme.TEXT_MUTED);
        info.setLineWrap(true);
        info.setOpaque(false);
        info.setAlignmentX(CENTER_ALIGNMENT);

        JButton button = new JButton("Lancer Kumiai GO RPG");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("notepad.exe");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        content.add(aboutCard);
        content.add(info);
        content.add(button);
        content.setSize(300, 200);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel buildLockedContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(60, 40, 40, 40));

        JLabel icon = new JLabel("🔒", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 64));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titre = Theme.label("Contenu réservé aux membres Premium",
                Theme.FONT_TITLE, Theme.TEXT);
        titre.setAlignmentX(CENTER_ALIGNMENT);
        titre.setBorder(new EmptyBorder(16, 0, 12, 0));

        JLabel desc = Theme.label("Passe à l'offre Premium pour accéder à Kumiai GO RPG.",
                Theme.FONT_BODY, Theme.TEXT_MUTED);
        desc.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(titre);
        panel.add(desc);

        return panel;
    }

    public void refresh() {
        removeAll();
        add(Theme.headerPanel("Kumiai GO RPG"), BorderLayout.NORTH);
        if ("PREMIUM".equals(ApiClient.getAbonnement())) {
            add(buildContent(), BorderLayout.CENTER);
        } else {
            add(buildLockedContent(), BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }
}

