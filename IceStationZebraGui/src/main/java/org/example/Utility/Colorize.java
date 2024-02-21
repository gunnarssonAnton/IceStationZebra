package org.example.Utility;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colorize {
    public enum Type {
        ALERT,
        WARNING,
        INFO,
        NONE
    }
    public static final Map<String,String> colors;
    static {
        colors = new HashMap<>();
        colors.put("BLACK", "\u001B[30m");
        colors.put("RED", "\u001B[31m");
        colors.put("GREEN", "\u001B[32m");
        colors.put("YELLOW", "\u001B[33m");
        colors.put("BLUE", "\u001B[34m");
        colors.put("PURPLE", "\u001B[35m");
        colors.put("CYAN", "\u001B[36m");
        colors.put("WHITE", "\u001B[37m");
        colors.put("RESET", "\u001B[0m");

    }
    private static String println(String message, Type type){
        return switch (type){
            case ALERT -> printAlert(message);
            case WARNING -> printWarning(message);
            case INFO -> printInfo(message);
            case NONE -> printNone(message);
        };
    }

    public static String printAlert(String message){
        return colors.get("RED") + message + colors.get("RESET");
    }
    public static String printWarning(String message){
        return colors.get("YELLOW") + message + colors.get("RESET");

    }
    public static String printInfo(String message){
        return colors.get("WHITE") + message + colors.get("RESET");

    }
    public static String printNone(String message){
        return colors.get("RESET") + message + colors.get("RESET");

    }
}
