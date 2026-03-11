package fr.kumiaigorpg.desktop.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Theme {

    public static final Color RED       = new Color(211, 47, 47);
    public static final Color RED_DARK  = new Color(183, 28, 28);
    public static final Color RED_LIGHT = new Color(255, 235, 238);
    public static final Color BG        = new Color(250, 250, 250);
    public static final Color CARD_BG   = Color.WHITE;
    public static final Color TEXT      = new Color(33, 33, 33);
    public static final Color TEXT_MUTED= new Color(117, 117, 117);
    public static final Color BORDER    = new Color(224, 224, 224);
    public static final Color SUCCESS   = new Color(56, 142, 60);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_H2     = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);

    // ── Composants stylés ──────────────────────────────────────

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(RED);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(RED_DARK); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(RED); }
        });
        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(158, 158, 158));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    public static JTextField styledField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setToolTipText(placeholder);
        return field;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    public static JLabel label(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    public static JPanel headerPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.decode("#FD7E14"));
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(Color.WHITE);
        panel.add(lbl);
        return panel;
    }

    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("TabbedPane.foreground", Theme.TEXT);
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
        UIManager.put("Panel.background", BG);
        UIManager.put("OptionPane.background", BG);
    }

    public static Font getJapaneseFont(int size) {
        // Teste ces noms exacts un par un jusqu'à ce que ça marche
        String[] candidates = {
                "MS Gothic",
                "MS Mincho",
                "Yu Gothic",
                "Yu Gothic UI",
                "Meiryo",
                "Meiryo UI"
        };

        for (String name : candidates) {
            Font f = new Font(name, Font.PLAIN, size);
            if (f.canDisplay('日')) {
                System.out.println("Font utilisée : " + name); // log pour vérifier
                return f;
            }
        }

        System.out.println("⚠ Aucune font japonaise trouvée !");
        return new Font("SansSerif", Font.PLAIN, size);
    }
}
