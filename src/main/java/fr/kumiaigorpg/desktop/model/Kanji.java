package fr.kumiaigorpg.desktop.model;

public class Kanji {
    private Long id;
    private String kanjiName;
    private String kunyomi;
    private String onyomi;
    private String description;
    private String jlptlvl;

    public Long getId() { return id; }
    public String getKanjiName() { return kanjiName; }
    public String getKunyomi() { return kunyomi; }
    public String getOnyomi() { return onyomi; }
    public String getDescription() { return description; }
    public String getJlptLvl() { return jlptlvl; }
}
