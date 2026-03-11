package fr.kumiaigorpg.desktop.rpg;

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
        add(buildContent(), BorderLayout.CENTER);
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
}

