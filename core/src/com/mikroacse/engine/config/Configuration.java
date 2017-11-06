package com.mikroacse.engine.config;

import com.badlogic.gdx.utils.JsonValue;
import com.mikroacse.engine.utils.JSONLoader;

/**
 * Created by MikroAcse on 08.07.2016.
 */
public class Configuration {
    protected static JsonValue config;

    public static void load(String path) {
        config = JSONLoader.load(path);
    }

    public static String getString(String key, String defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asString();
    }

    public static String getString(String key) {
        return getString(key, null);
    }

    public static int getInt(String key, int defaultValue) {
        JsonValue node = getNode(key);
        if (node == null) {
            return defaultValue;
        }
        return node.asInt();
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0);
    }

    public static int getColor(String key, int defaultValue) {
        String value = getString(key, null);
        if (value == null) {
            return defaultValue;
        }
        value = value.substring(2);
        return Integer.parseInt(value, 16);
    }

    public static int getColor(String key) {
        return getColor(key, 0);
    }

    protected static JsonValue getNode(String nodePath) {
        String[] keys = nodePath.split("\\.");

        JsonValue node = config;

        for (String key : keys) {
            if (node == null) {
                return null;
            }
            node = node.get(key);
        }

        return node;
    }
}
