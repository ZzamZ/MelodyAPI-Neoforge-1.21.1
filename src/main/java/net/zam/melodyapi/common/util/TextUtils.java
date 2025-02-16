package net.zam.melodyapi.common.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static void drawCenteredWrappedString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int maxWidth, int color) {
        List<String> wrappedLines = wrapString(font, text, maxWidth);
        int lineHeight = font.lineHeight;

        // Calculate starting Y position to center all lines
        int startY = y - (wrappedLines.size() * lineHeight) / 2;

        for (int i = 0; i < wrappedLines.size(); i++) {
            guiGraphics.drawCenteredString(font, wrappedLines.get(i), x, y + (i * lineHeight), color);
        }
    }

    public static List<String> wrapString(Font font, String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split(" ");

        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (font.width(testLine) > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine.append(currentLine.isEmpty() ? "" : " ").append(word);
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}