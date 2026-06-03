package fr.kumiaigorpg.desktop.test;

import java.awt.*;

public class FontTest {
    public static void main(String[] args) {
        Font[] fonts = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAllFonts();

        for (Font f : fonts) {
            if (f.canDisplay('日') && f.canDisplay('語')) {
                System.out.println("Test OK " + f.getName());
            }
        }
    }
}