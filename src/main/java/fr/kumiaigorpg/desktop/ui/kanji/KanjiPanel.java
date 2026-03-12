package fr.kumiaigorpg.desktop.ui.kanji;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.model.Kanji;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class KanjiPanel extends JPanel {

    private JTextField searchField;
    private JTextField searchField2;
    private JComboBox<String> niveauCombo;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public KanjiPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Kanjis"), BorderLayout.NORTH);
        add(buildFilters(), BorderLayout.PAGE_START);
        add(buildTable(),   BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);
        loadData("all", null);
    }

    private JPanel buildFilters() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panel.setBackground(Theme.CARD_BG);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        searchField = Theme.styledField("Rechercher un kanji en Japonais...");
        searchField.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(220, 36));
        searchField2= Theme.styledField("Rechercher un kanji en Francais...");
        searchField2.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        searchField2.setPreferredSize(new Dimension(220, 36));
        niveauCombo = new JComboBox<>(new String[]{"Tous", "N5", "N4", "N3", "N2", "N1"});
        niveauCombo.setFont(Theme.FONT_BODY);
        niveauCombo.setPreferredSize(new Dimension(100, 36));
        JButton btnSearch = Theme.primaryButton("Rechercher");
        JButton btnReset  = Theme.secondaryButton("Reinitialiser");
        btnSearch.addActionListener(e -> applyFilter());
        btnReset.addActionListener(e -> { searchField.setText(""); niveauCombo.setSelectedIndex(0); loadData("all", null); });
        searchField.addActionListener(e -> applyFilter());
        panel.add(Theme.label("Rechercher en Japonais : ", Theme.FONT_BODY, Theme.TEXT_MUTED));
        panel.add(searchField);
        panel.add(Theme.label("Rechercher en Français : ", Theme.FONT_BODY, Theme.TEXT_MUTED));
        panel.add(searchField2);
        panel.add(Theme.label("  Niveau : ", Theme.FONT_BODY, Theme.TEXT_MUTED));
        panel.add(niveauCombo);
        panel.add(btnSearch);
        panel.add(btnReset);
        return panel;
    }

    private JScrollPane buildTable() {
        String[] cols = {"Kanji", "Kunyomi", "Onyomi", "Description", "Niveau"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row) && col != 0 && col != 4) {
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

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(CENTER);
                setFont(new Font("MS Gothic", Font.PLAIN, 28));
                setForeground(Theme.RED);
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                return this;
            }
        });

        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(4).setCellRenderer(new NiveauRenderer());

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
        String query2  = searchField2.getText().trim();
        String niveau = (String) niveauCombo.getSelectedItem();
        if (!query.isEmpty()) loadData("search", query);
        if (!query2.isEmpty()) loadData("search2", query2);
        else if (!"Tous".equals(niveau)) loadData("niveau", niveau);
        else loadData("all", null);
    }

    private void loadData(String mode, String param) {
        statusLabel.setText("Chargement...");
        tableModel.setRowCount(0);
        new SwingWorker<List<Kanji>, Void>() {
            @Override protected List<Kanji> doInBackground() throws Exception {
                return switch (mode) {
                    case "search" -> ApiClient.searchKanji(param);
                    case "search2" -> ApiClient.searchKanjiByMeaning(param);
                    case "niveau" -> ApiClient.getKanjiByNiveau(param);
                    default       -> ApiClient.getAllKanji();
                };
            }
            @Override protected void done() {
                try {
                    List<Kanji> list = get();
                    for (Kanji k : list) {
                        tableModel.addRow(new Object[]{ k.getKanjiName(), k.getKunyomi(), k.getOnyomi(), k.getDescription(), k.getJlptLvl() });
                    }
                    statusLabel.setText(list.size() + " kanji(s) trouve(s)");
                } catch (Exception ex) {
                    statusLabel.setText("Erreur : " + ex.getMessage());
                }
            }
        }.execute();
    }

    public static class NiveauRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setOpaque(true);
            String niveau = v != null ? v.toString() : "";
            setBackground(switch (niveau) {
                case "N5" -> new Color(21, 101, 192);
                case "N4" -> new Color(46, 125, 50);
                case "N3" -> new Color(245, 127, 23);
                case "N2" -> new Color(198, 40, 40);
                case "N1" -> new Color(69, 39, 160);
                default   -> Color.GRAY;
            });
            return this;
        }
    }
}
