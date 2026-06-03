package fr.kumiaigorpg.desktop.ui.quiz;

import fr.kumiaigorpg.desktop.api.ApiClient;
import fr.kumiaigorpg.desktop.model.Kanji;
import fr.kumiaigorpg.desktop.model.Vocabulaire;
import fr.kumiaigorpg.desktop.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizPanel extends JPanel {

    private List<QuizQuestion> questions = new ArrayList<>();
    private int indexActuel = 0;
    private int score       = 0;
    private int nbQuestions = 10;
    private String typeQuiz = "Kanjis";

    private JPanel    mainPanel;
    private CardLayout cardLayout;

    public QuizPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        add(Theme.headerPanel("Quiz"), BorderLayout.NORTH);
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG);
        mainPanel.add(buildMenuPanel(),  "menu");
        mainPanel.add(buildQuizPanel(),  "quiz");
        mainPanel.add(buildScorePanel(), "score");
        add(mainPanel, BorderLayout.CENTER);
    }


    //  MENU
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel titre = Theme.label("Configurer le quiz", Theme.FONT_TITLE, Theme.TEXT);
        titre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(32));

        JLabel lblType = Theme.label("Type de quiz", Theme.FONT_H2, Theme.TEXT);
        lblType.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(lblType);
        panel.add(Box.createVerticalStrut(10));

        JPanel typePanel = new JPanel(new GridLayout(1, 2, 12, 0));
        typePanel.setOpaque(false);
        typePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        typePanel.setAlignmentX(LEFT_ALIGNMENT);

        JToggleButton btnKanji = makeToggleBtn("Kanjis", true);
        JToggleButton btnVocab = makeToggleBtn("Vocabulaire", false);

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(btnKanji);
        typeGroup.add(btnVocab);

        btnKanji.addActionListener(e -> typeQuiz = "Kanjis");
        btnVocab.addActionListener(e -> typeQuiz = "Vocabulaire");

        typePanel.add(btnKanji);
        typePanel.add(btnVocab);
        panel.add(typePanel);
        panel.add(Box.createVerticalStrut(24));

        JLabel lblNb = Theme.label("Nombre de questions", Theme.FONT_H2, Theme.TEXT);
        lblNb.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(lblNb);
        panel.add(Box.createVerticalStrut(10));

        JPanel nbPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        nbPanel.setOpaque(false);
        nbPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        nbPanel.setAlignmentX(LEFT_ALIGNMENT);

        JToggleButton btn10 = makeToggleBtn("10 questions", true);
        JToggleButton btn20 = makeToggleBtn("20 questions", false);
        JToggleButton btn40 = makeToggleBtn("40 questions", false);

        ButtonGroup nbGroup = new ButtonGroup();
        nbGroup.add(btn10);
        nbGroup.add(btn20);
        nbGroup.add(btn40);

        btn10.addActionListener(e -> nbQuestions = 10);
        btn20.addActionListener(e -> nbQuestions = 20);
        btn40.addActionListener(e -> nbQuestions = 40);

        nbPanel.add(btn10);
        nbPanel.add(btn20);
        nbPanel.add(btn40);
        panel.add(nbPanel);
        panel.add(Box.createVerticalStrut(32));

        JButton btnDemarrer = Theme.primaryButton("Démarrer le quiz →");
        btnDemarrer.setAlignmentX(CENTER_ALIGNMENT);
        btnDemarrer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnDemarrer.addActionListener(e -> demarrerQuiz());
        panel.add(btnDemarrer);

        return panel;
    }

    private JToggleButton makeToggleBtn(String text, boolean selected) {
        JToggleButton btn = new JToggleButton(text);
        btn.setSelected(selected);
        btn.setFont(Theme.FONT_BODY);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBackground(selected ? Theme.RED : Color.WHITE);
        btn.setForeground(selected ? Color.WHITE : Theme.TEXT);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.RED, 1),
                new EmptyBorder(10, 16, 10, 16)
        ));
        btn.addChangeListener(e -> {
            btn.setBackground(btn.isSelected() ? Theme.RED : Color.WHITE);
            btn.setForeground(btn.isSelected() ? Color.WHITE : Theme.TEXT);
        });
        return btn;
    }


    //  PANEL QUESTION

    private JLabel   lblQuestion, lblProgression, lblFeedback, lblNiveau;
    private JButton[] btnReponses = new JButton[4];
    private JButton  btnSuivant;

    private JPanel buildQuizPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(24, 40, 24, 40));

        lblProgression = Theme.label("Question 1/10", Theme.FONT_SMALL, Theme.TEXT_MUTED);
        lblProgression.setAlignmentX(CENTER_ALIGNMENT);

        lblNiveau = new JLabel("", SwingConstants.CENTER);
        lblNiveau.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNiveau.setAlignmentX(CENTER_ALIGNMENT);
        lblNiveau.setBorder(new EmptyBorder(4, 0, 0, 0));

        lblQuestion = new JLabel("", SwingConstants.CENTER);
        lblQuestion.setFont(new Font("MS Gothic", Font.PLAIN, 56));
        lblQuestion.setForeground(Theme.RED);
        lblQuestion.setAlignmentX(CENTER_ALIGNMENT);
        lblQuestion.setBorder(new EmptyBorder(16, 0, 16, 0));

        JPanel reponsesPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        reponsesPanel.setOpaque(false);
        reponsesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        reponsesPanel.setAlignmentX(LEFT_ALIGNMENT);

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            btnReponses[i] = new JButton();
            btnReponses[i].setFont(Theme.FONT_BODY);
            btnReponses[i].setBackground(Color.WHITE);
            btnReponses[i].setForeground(Theme.TEXT);
            btnReponses[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.BORDER, 1),
                    new EmptyBorder(10, 16, 10, 16)
            ));
            btnReponses[i].setFocusPainted(false);
            btnReponses[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnReponses[i].addActionListener(e -> verifierReponse(idx));
            reponsesPanel.add(btnReponses[i]);
        }

        lblFeedback = Theme.label("", Theme.FONT_BODY, Theme.SUCCESS);
        lblFeedback.setAlignmentX(CENTER_ALIGNMENT);
        lblFeedback.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnSuivant = Theme.primaryButton("Question suivante →");
        btnSuivant.setAlignmentX(CENTER_ALIGNMENT);
        btnSuivant.setVisible(false);
        btnSuivant.addActionListener(e -> questionSuivante());

        panel.add(lblProgression);
        panel.add(lblNiveau);
        panel.add(lblQuestion);
        panel.add(reponsesPanel);
        panel.add(lblFeedback);
        panel.add(Box.createVerticalStrut(16));
        panel.add(btnSuivant);

        return panel;
    }


    //  PANEL SCORE
    private JLabel  lblScoreFinal, lblJustes, lblFausses;
    private JPanel  detailPanel;

    private JPanel buildScorePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(24, 40, 24, 40));

        JLabel emoji = new JLabel("ok", SwingConstants.CENTER);
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 52));
        emoji.setAlignmentX(CENTER_ALIGNMENT);

        lblScoreFinal = Theme.label("", Theme.FONT_TITLE, Theme.RED);
        lblScoreFinal.setAlignmentX(CENTER_ALIGNMENT);
        lblScoreFinal.setBorder(new EmptyBorder(10, 0, 4, 0));

        JPanel compteurs = new JPanel(new GridLayout(1, 2, 20, 0));
        compteurs.setOpaque(false);
        compteurs.setMaximumSize(new Dimension(600, 100));
        compteurs.setAlignmentX(CENTER_ALIGNMENT);

        JPanel carteJuste = makeCompteurCard("Bonnes réponses", "Correct", Theme.SUCCESS);
        JPanel carteFausse = makeCompteurCard("Mauvaises réponses", "Incorrect", Color.RED);
        lblJustes  = (JLabel) carteJuste.getComponent(1);
        lblFausses = (JLabel) carteFausse.getComponent(1);

        compteurs.add(carteJuste);
        compteurs.add(carteFausse);

        JLabel lblDetailTitre = Theme.label(
                "Détail par niveau JLPT", Theme.FONT_H2, Theme.TEXT);
        lblDetailTitre.setAlignmentX(LEFT_ALIGNMENT);
        lblDetailTitre.setBorder(new EmptyBorder(20, 0, 10, 0));

        detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);
        detailPanel.setAlignmentX(LEFT_ALIGNMENT);

        JButton btnRejouer = Theme.primaryButton("Rejouer");
        btnRejouer.setAlignmentX(CENTER_ALIGNMENT);
        btnRejouer.setBorder(new EmptyBorder(10, 40, 10, 40));
        btnRejouer.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        panel.add(emoji);
        panel.add(lblScoreFinal);
        panel.add(Box.createVerticalStrut(12));
        panel.add(compteurs);
        panel.add(lblDetailTitre);
        panel.add(detailPanel);
        panel.add(Box.createVerticalStrut(24));
        panel.add(btnRejouer);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.BG);
        wrapper.add(scroll);
        return wrapper;
    }

    private JPanel makeCompteurCard(String label, String valeur, Color couleur) {
        JPanel card = Theme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel lblVal  = new JLabel(valeur, SwingConstants.CENTER);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblVal.setForeground(couleur);
        lblVal.setAlignmentX(CENTER_ALIGNMENT);
        JLabel lblLbl  = Theme.label(label, Theme.FONT_SMALL, Theme.TEXT_MUTED);
        lblLbl.setAlignmentX(CENTER_ALIGNMENT);
        card.add(lblVal);
        card.add(lblLbl);
        return card;
    }

    //  LOGIQUE QUIZ
    private void demarrerQuiz() {
        cardLayout.show(mainPanel, "quiz");
        lblQuestion.setText("Chargement...");
        for (JButton btn : btnReponses) btn.setEnabled(false);

        new SwingWorker<List<QuizQuestion>, Void>() {
            @Override protected List<QuizQuestion> doInBackground() throws Exception {
                List<QuizQuestion> qs = new ArrayList<>();
                Random rand = new Random();

                if ("Kanjis".equals(typeQuiz)) {
                    List<Kanji> kanjis = ApiClient.getAllKanji();
                    Collections.shuffle(kanjis);
                    for (Kanji k : kanjis) {
                        if (k.getDescription() == null) continue;
                        List<String> fausses = kanjis.stream()
                                .filter(x -> !x.equals(k) && x.getDescription() != null)
                                .map(Kanji::getDescription)
                                .limit(3).toList();
                        if (fausses.size() < 3) continue;
                        qs.add(new QuizQuestion(
                                k.getKanjiName(), k.getDescription(),
                                fausses, k.getJlptLvl().toString()
                        ));
                        if (qs.size() == nbQuestions) break;
                    }
                } else {
                    List<Vocabulaire> mots = ApiClient.getAllVocabulaire();
                    Collections.shuffle(mots);
                    for (Vocabulaire v : mots) {
                        if (v.getTraduction() == null) continue;
                        List<String> fausses = mots.stream()
                                .filter(x -> !x.equals(v) && x.getTraduction() != null)
                                .map(Vocabulaire::getTraduction)
                                .limit(3).toList();
                        if (fausses.size() < 3) continue;
                        qs.add(new QuizQuestion(
                                v.getWord(), v.getTraduction(),
                                fausses, v.getJlptLvl().toString()
                        ));
                        if (qs.size() == nbQuestions) break;
                    }
                }
                return qs;
            }

            @Override protected void done() {
                try {
                    questions   = get();
                    indexActuel = 0;
                    score       = 0;
                    afficherQuestion();
                } catch (Exception ex) {
                    lblQuestion.setText("Erreur : " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void afficherQuestion() {
        if (indexActuel >= questions.size()) {
            afficherScore();
            return;
        }
        QuizQuestion q = questions.get(indexActuel);
        lblProgression.setText("Question " + (indexActuel + 1) + "/" + questions.size());
        lblQuestion.setText(q.question);
        lblFeedback.setText("");
        btnSuivant.setVisible(false);

        lblNiveau.setText("Niveau " + q.niveau);
        lblNiveau.setForeground(couleurNiveau(q.niveau));

        List<String> reponses = new ArrayList<>(q.fausses);
        reponses.add(q.bonneReponse);
        Collections.shuffle(reponses);

        for (int i = 0; i < 4; i++) {
            btnReponses[i].setText(reponses.get(i));
            btnReponses[i].setBackground(Color.WHITE);
            btnReponses[i].setForeground(Theme.TEXT);
            btnReponses[i].setEnabled(true);
        }
    }

    private void verifierReponse(int idx) {
        QuizQuestion q = questions.get(indexActuel);
        String choix   = btnReponses[idx].getText();
        for (JButton btn : btnReponses) btn.setEnabled(false);

        questions.get(indexActuel).reponduJuste = choix.equals(q.bonneReponse);

        if (choix.equals(q.bonneReponse)) {
            score++;
            btnReponses[idx].setBackground(Theme.SUCCESS);
            btnReponses[idx].setForeground(Color.WHITE);
            lblFeedback.setForeground(Theme.SUCCESS);
            lblFeedback.setText("✓ Bonne réponse !");
        } else {
            btnReponses[idx].setBackground(Color.RED);
            btnReponses[idx].setForeground(Color.WHITE);
            lblFeedback.setForeground(Color.RED);
            lblFeedback.setText("✗ Mauvais ! La réponse était : " + q.bonneReponse);
            for (JButton btn : btnReponses)
                if (btn.getText().equals(q.bonneReponse)) {
                    btn.setBackground(Theme.SUCCESS);
                    btn.setForeground(Color.WHITE);
                }
        }
        btnSuivant.setVisible(true);
    }

    private void questionSuivante() {
        indexActuel++;
        afficherQuestion();
    }

    private void afficherScore() {
        int justes  = score;
        int fausses = questions.size() - score;

        lblScoreFinal.setText("Score : " + justes + " / " + questions.size());
        lblJustes.setText(String.valueOf(justes));
        lblFausses.setText(String.valueOf(fausses));

        detailPanel.removeAll();
        Map<String, int[]> statsNiveau = new LinkedHashMap<>();
        for (String n : new String[]{"N5","N4","N3","N2","N1"})
            statsNiveau.put(n, new int[]{0, 0}); // [justes, total]

        for (QuizQuestion q : questions) {
            int[] stat = statsNiveau.get(q.niveau);
            if (stat == null) continue;
            stat[1]++;
            if (q.reponduJuste) stat[0]++;
        }

        for (Map.Entry<String, int[]> entry : statsNiveau.entrySet()) {
            if (entry.getValue()[1] == 0) continue;
            int j = entry.getValue()[0];
            int t = entry.getValue()[1];

            JPanel ligne = new JPanel(new BorderLayout(12, 0));
            ligne.setOpaque(false);
            ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            ligne.setAlignmentX(LEFT_ALIGNMENT);
            ligne.setBorder(new EmptyBorder(4, 0, 4, 0));

            JLabel lblN = new JLabel(entry.getKey());
            lblN.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblN.setForeground(couleurNiveau(entry.getKey()));
            lblN.setPreferredSize(new Dimension(40, 28));

            JProgressBar bar = new JProgressBar(0, t);
            bar.setValue(j);
            bar.setStringPainted(true);
            bar.setString(j + "/" + t);
            bar.setForeground(couleurNiveau(entry.getKey()));
            bar.setBackground(new Color(230, 230, 230));
            bar.setFont(Theme.FONT_SMALL);

            ligne.add(lblN, BorderLayout.WEST);
            ligne.add(bar,  BorderLayout.CENTER);
            detailPanel.add(ligne);
        }

        detailPanel.revalidate();
        detailPanel.repaint();
        cardLayout.show(mainPanel, "score");
    }

    private Color couleurNiveau(String niveau) {
        return switch (niveau) {
            case "N5" -> new Color(21, 101, 192);
            case "N4" -> new Color(46, 125, 50);
            case "N3" -> new Color(245, 127, 23);
            case "N2" -> new Color(198, 40, 40);
            case "N1" -> new Color(69, 39, 160);
            default   -> Theme.TEXT;
        };
    }

    //  MODELE QUESTION
    private static class QuizQuestion {
        String  question, bonneReponse, niveau;
        List<String> fausses;
        boolean reponduJuste = false;

        QuizQuestion(String question, String bonneReponse,
                     List<String> fausses, String niveau) {
            this.question     = question;
            this.bonneReponse = bonneReponse;
            this.fausses      = fausses;
            this.niveau       = niveau;
        }
    }
}