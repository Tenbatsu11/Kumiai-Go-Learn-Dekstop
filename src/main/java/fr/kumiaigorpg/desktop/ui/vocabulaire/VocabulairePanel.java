package fr.kumiaigorpg.desktop.ui.vocabulaire;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.model.Vocabulaire;
import fr.kumiaigorpg.desktop.ui.kanji.KanjiPanel;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class VocabulairePanel extends JPanel {

    private JTextField searchField;
    private JComboBox<String> niveauCombo;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public VocabulairePanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Vocabulaire"), BorderLayout.NORTH);
        add(buildFilters(), BorderLayout.PAGE_START);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);
        loadData("all", null);
    }

    private JPanel buildFilters() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panel.setBackground(Theme.CARD_BG);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        searchField = Theme.styledField("Rechercher un mot...");
        searchField.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(220, 36));
        niveauCombo = new JComboBox<>(new String[]{"Tous", "N5", "N4", "N3", "N2", "N1"});
        niveauCombo.setFont(Theme.FONT_BODY);
        niveauCombo.setPreferredSize(new Dimension(100, 36));
        JButton btnSearch = Theme.primaryButton("Rechercher");
        JButton btnReset  = Theme.secondaryButton("Reinitialiser");
        btnSearch.addActionListener(e -> applyFilter());
        btnReset.addActionListener(e -> { searchField.setText(""); niveauCombo.setSelectedIndex(0); loadData("all", null); });
        searchField.addActionListener(e -> applyFilter());
        panel.add(Theme.label("Recherche : ", Theme.FONT_BODY, Theme.TEXT_MUTED));
        panel.add(searchField);
        panel.add(Theme.label("  Niveau : ", Theme.FONT_BODY, Theme.TEXT_MUTED));
        panel.add(niveauCombo);
        panel.add(btnSearch);
        panel.add(btnReset);
        return panel;
    }

    private JScrollPane buildTable() {
        String[] cols = {"Mot", "Furigana", "Traduction", "Niveau"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row) && col != 0 && col != 3) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                return c;
            }
        };

        table.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.setSelectionBackground(Theme.RED_LIGHT);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setFont(new Font("MS Gothic", Font.PLAIN, 18));
                setForeground(Theme.RED);
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                return this;
            }
        });

        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setCellRenderer(new KanjiPanel.NiveauRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel buildStatus() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(Theme.CARD_BG);
        statusLabel = Theme.label("", Theme.FONT_SMALL, Theme.TEXT_MUTED);
        p.add(statusLabel);
        return p;
    }

    private void applyFilter() {
        String query  = searchField.getText().trim();
        String niveau = (String) niveauCombo.getSelectedItem();
        if (!query.isEmpty()) loadData("search", query);
        else if (!"Tous".equals(niveau)) loadData("niveau", niveau);
        else loadData("all", null);
    }

    private void loadData(String mode, String param) {
        statusLabel.setText("Chargement...");
        tableModel.setRowCount(0);
        new SwingWorker<List<Vocabulaire>, Void>() {
            @Override protected List<Vocabulaire> doInBackground() throws Exception {
                return switch (mode) {
                    case "search" -> ApiClient.searchVocabulaire(param);
                    case "niveau" -> ApiClient.getVocabulaireByNiveau(param);
                    default       -> ApiClient.getAllVocabulaire();
                };
            }
            @Override protected void done() {
                try {
                    List<Vocabulaire> list = get();
                    for (Vocabulaire v : list) {
                        tableModel.addRow(new Object[]{ v.getWord(), v.getFurigana(), v.getTraduction(), v.getJlptLvl() });
                    }
                    statusLabel.setText(list.size() + " mot(s) trouve(s)");
                } catch (Exception ex) {
                    statusLabel.setText("Erreur : " + ex.getMessage());
                }
            }
        }.execute();
    }
}
