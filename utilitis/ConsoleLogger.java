package utilitis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Custom Console Logger
 *
 * A simple, configurable console logger with color-coded output for different
 * log levels (INFO, WARNING, ERROR, DEBUG). Behavior is controlled via
 * environment variables and runtime methods.
 *
 * Environment Variables:
 * - LOG_INFO: 'true' or 'false' (default: 'true')
 * - LOG_WARNING: 'true' or 'false' (default: 'true')
 * - LOG_ERROR: 'true' or 'false' (default: 'true')
 * - LOG_DEBUG: 'true' or 'false' (default: 'false')
 * - LOG_COLOR: 'true' or 'false' (default: 'true')
 *
 * Author: Ramazan Seçilmiş
 * Version: 1.0.0
 */
public final class ConsoleLogger {
    private ConsoleLogger() {}

    // ANSI color codes
    private static final String GREEN = "\033[92m";
    private static final String YELLOW = "\033[93m";
    private static final String RED = "\033[91m";
    private static final String BLUE = "\033[94m";
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";

    // Configuration from environment variables
    private static volatile boolean infoEnabled = parseEnv("LOG_INFO", true);
    private static volatile boolean warningEnabled = parseEnv("LOG_WARNING", true);
    private static volatile boolean errorEnabled = parseEnv("LOG_ERROR", true);
    private static volatile boolean debugEnabled = parseEnv("LOG_DEBUG", false);
    private static volatile boolean colorEnabled = parseEnv("LOG_COLOR", true);

    private enum Level {
        INFO, WARNING, ERROR, DEBUG
    }

    private static boolean parseEnv(String name, boolean defaultValue) {
        String value = System.getenv(name);
        if (value == null) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value);
    }

    private static String colorFor(Level level) {
        switch (level) {
            case INFO:
                return GREEN;
            case WARNING:
                return YELLOW;
            case ERROR:
                return RED;
            case DEBUG:
                return BLUE;
            default:
                return "";
        }
    }

    private static boolean isEnabled(Level level) {
        switch (level) {
            case INFO:
                return infoEnabled;
            case WARNING:
                return warningEnabled;
            case ERROR:
                return errorEnabled;
            case DEBUG:
                return debugEnabled;
            default:
                return true;
        }
    }

    private static String formatMessage(Level level, String message, String prefix) {
        String header;
        if (colorEnabled) {
            header = colorFor(level) + BOLD + "[" + level.name() + "]" + RESET;
        } else {
            header = "[" + level.name() + "]";
        }
        if (prefix != null && !prefix.isEmpty()) {
            return header + " " + prefix + " " + message;
        }
        return header + " " + message;
    }

    private static void log(Level level, String message, String prefix) {
        if (!isEnabled(level)) {
            return;
        }
        String formatted = formatMessage(level, message, prefix);
        if (level == Level.ERROR) {
            System.err.println(formatted);
        } else {
            System.out.println(formatted);
        }
    }

    // Public logging APIs
    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    public static void info(String message, String prefix) {
        log(Level.INFO, message, prefix);
    }

    public static void warning(String message) {
        log(Level.WARNING, message, null);
    }

    public static void warning(String message, String prefix) {
        log(Level.WARNING, message, prefix);
    }

    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    public static void error(String message, String prefix) {
        log(Level.ERROR, message, prefix);
    }

    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    public static void debug(String message, String prefix) {
        log(Level.DEBUG, message, prefix);
    }

    // Configuration utilities
    public static void enableLogLevel(String level) {
        setLevelEnabled(level, true);
    }

    public static void disableLogLevel(String level) {
        setLevelEnabled(level, false);
    }

    private static void setLevelEnabled(String level, boolean enabled) {
        if (level == null) {
            return;
        }
        String key = level.trim().toLowerCase(Locale.ROOT);
        switch (key) {
            case "info":
                infoEnabled = enabled;
                break;
            case "warning":
                warningEnabled = enabled;
                break;
            case "error":
                errorEnabled = enabled;
                break;
            case "debug":
                debugEnabled = enabled;
                break;
            default:
                // ignore unknown level
        }
    }

    public static void enableColors() {
        colorEnabled = true;
    }

    public static void disableColors() {
        colorEnabled = false;
    }

    public static Map<String, Boolean> getConfig() {
        Map<String, Boolean> cfg = new HashMap<>();
        cfg.put("info_enabled", infoEnabled);
        cfg.put("warning_enabled", warningEnabled);
        cfg.put("error_enabled", errorEnabled);
        cfg.put("debug_enabled", debugEnabled);
        cfg.put("color_enabled", colorEnabled);
        return Collections.unmodifiableMap(cfg);
    }

    public static void setConfig(Map<String, ?> options) {
        if (options == null) {
            return;
        }
        for (Map.Entry<String, ?> entry : options.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            boolean boolVal = (value instanceof Boolean)
                ? (Boolean) value
                : Boolean.parseBoolean(value.toString());
            switch (key) {
                case "info_enabled":
                    infoEnabled = boolVal;
                    break;
                case "warning_enabled":
                    warningEnabled = boolVal;
                    break;
                case "error_enabled":
                    errorEnabled = boolVal;
                    break;
                case "debug_enabled":
                    debugEnabled = boolVal;
                    break;
                case "color_enabled":
                    colorEnabled = boolVal;
                    break;
                default:
                    // ignore unknown keys
            }
        }
    }
}


