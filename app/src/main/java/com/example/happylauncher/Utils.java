package com.example.happylauncher;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.happylauncher.core.Application;
import com.example.happylauncher.settings.ColorPickerDialog;


public abstract class Utils {
    // Constants
    private static final String LOG_PREFIX = "[LogDL] ";


    /**
     * Display an R.string message in a Toast for a short duration.
     */
    public static void displayToast(Context context, int message) {
        if (context == null) return;
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    /**
     * Display a message in a Toast for a long duration.
     */
    public static void displayLongToast(Context context, String message) {
        if (context == null) return;
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    /**
     * Retrieve the currently selected color for the given preference key.
     */
    public static int getColor(SharedPreferences settings, String key, String fallback) {
        // Try to load the color at the given key, or use the provided fallback
        String hexadecimal = settings.getString(key, Constants.NONE);
        if (hexadecimal.equals(Constants.NONE)) hexadecimal = fallback;

        // Convert the hexadecimal color to an "int" color
        return ColorPickerDialog.convertHexadecimalColorToInt(hexadecimal);
    }


    /**
     * Retrieve the currently defined icon size in pixels.
     */
    public static int getIconSize(Context context, SharedPreferences settings) {
        // Compute the icon size in dp based on current settings (Android default is 48dp)
        int icon_size_dp = 12 * settings.getInt(Constants.ICON_SIZE, 4);

        // Convert the icon size from dp to pixels
        return Math.round(icon_size_dp * context.getResources().getDisplayMetrics().density);
    }


    /**
     * Try to start an app from the list using the ComponentInfo in the given setting key.
     *
     * @return <code>true</code> if something was done, <code>false</code> otherwise
     */
    public static boolean searchAndStartApplication(View view, SharedPreferences settings, String setting_key) {
        // Retrieve the app to launch (if any) based on the given setting key
        String component_info = settings.getString(setting_key, Constants.NONE);
        if (component_info.equals(Constants.NONE)) return false;

        // Search the application in the list
        for (Application application : ActivityMain.getApplicationsList().getApplications(true))
            if (application.getComponentInfo().equals(component_info)) {
                // Start the application
                application.start(view);
                return true;
            }

        // The application was not found, display an error message and reset the setting value
        Context context = view.getContext();
        Utils.displayLongToast(context, context.getString(R.string.error_app_not_found, component_info));
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(setting_key, Constants.NONE).apply();
        return true;
    }


    /**
     * Write an ERROR message to Android logcat (used in <code>catch</code> statements).
     */
    public static void logError(String tag, String message) {
//        if (BuildConfig.DEBUG)
//            Log.e(LOG_PREFIX + tag, message);
    }

    /**
     * Write an INFO message to Android logcat (used to report milestones or successes).
     */
    public static void logInfo(String tag, String message) {
//        if (BuildConfig.debug)
//            Log.i(LOG_PREFIX + tag, message);
    }

    /**
     * Write a DEBUG message to Android logcat (used to follow the behavior while debugging).
     */
    public static void logDebug(String tag, String message) {
//        if (BuildConfig.DEBUG)
//            Log.d(LOG_PREFIX + tag, message);
    }
}
