package fr.kumiaigorpg.desktop.ui;

import fr.kumiaigorpg.desktop.rpg.KumiaiGoRPG;
import fr.kumiaigorpg.desktop.ui.home.HomePanel;
import fr.kumiaigorpg.desktop.ui.kanji.KanjiPanel;
import fr.kumiaigorpg.desktop.ui.quiz.QuizPanel;
import fr.kumiaigorpg.desktop.ui.vocabulaire.VocabulairePanel;
import fr.kumiaigorpg.desktop.ui.abonnement.AbonnementPanel;
import fr.kumiaigorpg.desktop.ui.profil.ProfilPanel;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private HomePanel homePanel;
    private ProfilPanel profilPanel;
    private KumiaiGoRPG rpgPanel;

    public MainFrame() {
        setTitle("Kumiai Go RPG");
        setSize(1300, 900);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(new Color(40, 40, 40));
        tabs.setForeground(Color.BLACK);

        // Onglets
        tabs.addTab("Accueil",       new HomePanel());
        tabs.addTab("Kanjis",         new KanjiPanel());
        tabs.addTab("Vocabulaire",    new VocabulairePanel());
        tabs.addTab("Abonnement",     new AbonnementPanel(this));
        tabs.addTab("Quizz",         new QuizPanel());
        tabs.addTab("Profil",         new ProfilPanel(this));
        rpgPanel = new KumiaiGoRPG();
        tabs.addTab("Kumiai Go RPG", rpgPanel);

        // Style
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        UIManager.put("TabbedPane.selected",           new Color(211, 47, 47));
        UIManager.put("TabbedPane.selectedForeground", Color.BLACK);

        setContentPane(tabs);

        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i == 0 && homePanel != null)   homePanel.refresh();
            if (i == 4 && profilPanel != null) profilPanel.refresh();
        });
    }

    // Appelé depuis ProfilPanel pour recharger après déconnexion
    public void logout() {
        fr.kumiaigorpg.desktop.api.ApiClient.logout();
        new LoginFrame().setVisible(true);
        dispose();
    }
    public void refreshAll() {
        if (homePanel != null)   homePanel.refresh();
        if (profilPanel != null) profilPanel.refresh();
        if (rpgPanel != null)    rpgPanel.refresh();
    }
}
