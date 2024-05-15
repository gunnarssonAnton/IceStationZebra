package org.example.Utility;

import java.awt.*;

public class TerminalMessage {
    private final String message;
    private final Color color;
    public TerminalMessage(String message, Color color){
        this.message = sanitizeOutput(message);
        this.color = color;
    }

    public String getMessage() {
        return message;
    }
    public Color getColor() {
        return color;
    }
    public static String sanitizeOutput(String data) {
        StringBuilder sb = new StringBuilder();
        for (char ch : data.toCharArray()) {
            if (Character.isHighSurrogate(ch) || Character.isLowSurrogate(ch)) {
                // Replace non-UTF-8 surrogates with '?'
                //sb.append('?');
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
