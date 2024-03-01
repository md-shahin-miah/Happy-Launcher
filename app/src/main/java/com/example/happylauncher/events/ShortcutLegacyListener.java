package com.example.happylauncher.events;




import static com.example.happylauncher.ActivityMain.updateList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;

import com.example.happylauncher.Constants;
import com.example.happylauncher.R;
import com.example.happylauncher.Utils;


/**
 * Listen for legacy shortcut creation requests (before Android Oreo).
 */
public class ShortcutLegacyListener extends BroadcastReceiver
{
	// Constants
	private static final String TAG = "ShortcutLegacyListener" ;


	/**
	 * Provide the filter to use when registering this receiver.
	 */
	public IntentFilter getFilter()
	{
		return new IntentFilter("com.android.launcher.action.INSTALL_SHORTCUT") ;
	}


	/**
	 * Called when a broadcast message is received.
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Execute the following code only if the Android version is before Oreo
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
			{
				// Check if the intent is valid
				if(intent == null) return ;
				Utils.logDebug(TAG, "received Intent{" + intent.getAction() + "}") ;

				// Check if a request to add a shortcut has been received
				if("com.android.launcher.action.INSTALL_SHORTCUT".equals(intent.getAction()))
					{
						// Retrive the name, icon and intent of the shortcut
						String display_name = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME) ;
						Bitmap icon = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON) ;
						Intent shortcutIntent = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT) ;

						// If the request is invalid, display a message and quit
						if((display_name == null) || (shortcutIntent == null))
							{
								Utils.displayLongToast(context, context.getString(R.string.error_shortcut_invalid_request)) ;
								Utils.logError(TAG, "unable to add shortcut (" + display_name + ", " + shortcutIntent + ")") ;
								return ;
							}

						// Serialize the intent and ignore the request if it is an app install (fix bug with Android 7)
						String shortcut_intent = shortcutIntent.toUri(0) ;
						if(shortcut_intent.contains("android.intent.action.MAIN") &&
							shortcut_intent.contains("android.intent.category.LAUNCHER")) return ;

						// Add the shortcut and update the applications list
						String shortcut = display_name + Constants.SHORTCUT_SEPARATOR + shortcut_intent ;
						ShortcutListener.addShortcut(display_name, shortcut, icon, true) ;
						updateList(context) ;
					}
			}
	}
}
