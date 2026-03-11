package fr.kumiaigorpg.desktop.model;

public class Vocabulaire {
    private Long id;
    private String word;
    private String furigana;
    private String traduction;
    private String jlptlvl;

    public Long getId() { return id; }
    public String getWord() { return word; }
    public String getFurigana() { return furigana; }
    public String getTraduction() { return traduction; }
    public String getJlptLvl() { return jlptlvl; }
}
