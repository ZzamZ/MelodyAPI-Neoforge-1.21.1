package net.zam.melodyapi.common.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static void drawCenteredWrappedString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int maxWidth, int color) {
        List<String> wrappedLines = wrapString(font, text, maxWidth);
        int lineHeight = font.lineHeight;

        for (int i = 0; i < wrappedLines.size(); i++) {
            int width = font.width(wrappedLines.get(i));
            guiGraphics.drawString(font, wrappedLines.get(i), x - width / 2, y + (i * lineHeight), color, false);
        }
    }

    public static void drawCenteredVerticallyWrappedString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int maxWidth, int color) {
        List<String> wrappedLines = wrapString(font, text, maxWidth);
        int lineHeight = font.lineHeight;

        int startY = y - (wrappedLines.size() * lineHeight) / 2;

        for (int i = 0; i < wrappedLines.size(); i++) {
            int width = font.width(wrappedLines.get(i));
            guiGraphics.drawString(font, wrappedLines.get(i), x - width / 2, startY + (i * lineHeight), color, false);
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