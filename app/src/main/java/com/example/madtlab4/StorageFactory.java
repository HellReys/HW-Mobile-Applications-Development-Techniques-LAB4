package com.example.madtlab4;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageFactory {
    private static final String PREFS_NAME = "madt_storage_choice";
    private static final String KEY_CURRENT = "current_storage";
    public static final String STORAGE_PREFS = "prefs";
    public static final String STORAGE_FILES = "files";

    public static Storage getStorage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String current = prefs.getString(KEY_CURRENT, STORAGE_PREFS);
        if (STORAGE_FILES.equals(current)) {
            return new FileStorage(context);
        } else {
            return new PrefsStorage(context);
        }
    }

    public static String getCurrentStorageName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CURRENT, STORAGE_PREFS);
    }

    public static void switchStorage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String current = prefs.getString(KEY_CURRENT, STORAGE_PREFS);
        String next = STORAGE_PREFS.equals(current) ? STORAGE_FILES : STORAGE_PREFS;
        prefs.edit().putString(KEY_CURRENT, next).apply();
    }
}
