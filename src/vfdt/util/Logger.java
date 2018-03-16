package vfdt.util;

import java.text.DecimalFormat;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 16
 */
public abstract class Logger {
    private static final StringBuilder log   = new StringBuilder();
    private static       boolean       debug = false;
    public static        DecimalFormat df    = new DecimalFormat("#.0000");

    public static void append(String s) {
        if (isDebug())
            log.append(s);
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static String getLog() {
        return log.toString();
    }
}
