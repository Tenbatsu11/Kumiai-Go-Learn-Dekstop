package fr.kumiaigorpg.desktop;

import fr.kumiaigorpg.desktop.ui.LoginFrame;
import fr.kumiaigorpg.desktop.ui.home.HomePanel;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Theme.applyGlobalTheme();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
