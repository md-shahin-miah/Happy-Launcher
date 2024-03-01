package com.example.happylauncher.events;


import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.happylauncher.ActivityMain;
import com.example.happylauncher.Constants;
import com.example.happylauncher.R;
import com.example.happylauncher.Utils;
import com.example.happylauncher.storage.InternalFilePNG;
import com.example.happylauncher.storage.InternalFileTXT;

import java.util.ArrayList;

/**
 * Activity called to add a shortcut (starting with Android Oreo).
 */
public class ShortcutListener extends AppCompatActivity
{
	// Constants
	private static final String TAG = "ShortcutListener" ;


	/**
	 * Constructor.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Let the parent actions be performed
		super.onCreate(savedInstanceState) ;

		// Execute the following code only if the Android version is Oreo or higher
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			{
				// Retrieve the intent which started this activity
				Intent intent = getIntent() ;

				// Check if a new shortcut should be added and accept the request
				LauncherApps.PinItemRequest pinRequest = intent.getParcelableExtra(LauncherApps.EXTRA_PIN_ITEM_REQUEST) ;
				if((pinRequest != null) && (pinRequest.getShortcutInfo() != null) && pinRequest.accept())
					{
						// Check if the request is valid
						ShortcutInfo receivedShortcut = pinRequest.getShortcutInfo() ;
						if((receivedShortcut.getShortLabel() != null))
							{
								// Retrieve the informations of the shortcut
								String display_name = receivedShortcut.getShortLabel().toString() ;
								String user_id = receivedShortcut.getUserHandle().toString() ;
								String shortcut = display_name
										+ Constants.SHORTCUT_SEPARATOR + receivedShortcut.getPackage()
										+ Constants.SHORTCUT_SEPARATOR + receivedShortcut.getId()
										+ Constants.SHORTCUT_SEPARATOR + user_id.replace("UserHandle{", "").replace("}", "") ;

								// Check if the launcher is allowed to retrieve the shortcut icon
								Bitmap icon = null ;
								LauncherApps launcher = (LauncherApps)getSystemService(Context.LAUNCHER_APPS_SERVICE) ;
								if(launcher.hasShortcutHostPermission())
									{
										// If its dimensions are valid, create a Bitmap from the icon
										Drawable shortcutIcon = launcher.getShortcutIconDrawable(receivedShortcut, 0) ;
										if((shortcutIcon.getIntrinsicWidth() > 0) && (shortcutIcon.getIntrinsicHeight() > 0))
											{
												icon = Bitmap.createBitmap(shortcutIcon.getIntrinsicWidth(), shortcutIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888) ;
												shortcutIcon.setBounds(0, 0, shortcutIcon.getIntrinsicWidth(), shortcutIcon.getIntrinsicHeight()) ;
												shortcutIcon.draw(new Canvas(icon)) ;
											}
									}
									else Utils.displayLongToast(this, getString(R.string.error_shortcut_not_default_launcher)) ;

								// Add the shortcut
								addShortcut(display_name, shortcut, icon, false) ;
								ActivityMain.updateList(this) ;
							}
							else
							{
								// Display an error message and quit
								Utils.displayLongToast(this, getString(R.string.error_shortcut_invalid_request)) ;
								Utils.logError(TAG, "unable to add shortcut (" + receivedShortcut + ")") ;
							}

						// Go back to the home screen
						Intent homeIntent = new Intent() ;
						homeIntent.setClass(this, ActivityMain.class) ;
						homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
						startActivity(homeIntent) ;
					}
			}
	}


	/**
	 * Called when a request to add a shortcut has been received.
	 */
	static void addShortcut(String display_name, String shortcut, Bitmap icon, boolean legacy)
	{
		// Check if the shortcut already exists in the file
		InternalFileTXT file = new InternalFileTXT(legacy ? Constants.FILE_SHORTCUTS_LEGACY : Constants.FILE_SHORTCUTS) ;
		if(file.exists())
			{
				// Browse all the saved shortcuts
				String[] saved_shortcut ;
				for(String shortcut_line : file.readAllLines())
				{
					// Do not continue if the shortcut already exists
					saved_shortcut = shortcut_line.split(Constants.SHORTCUT_SEPARATOR) ;
					if(display_name.equals(saved_shortcut[0])) return ;
				}
			}

		// If it was not existing, add the shortcut to the file and save its icon
		InternalFilePNG icon_file = new InternalFilePNG(Constants.FILE_ICON_SHORTCUT_PREFIX + display_name + ".png") ;
		file.writeLine(shortcut) ;
		icon_file.writeToFile(icon) ;
		Utils.logInfo(TAG, "shortcut added") ;
	}


	/**
	 * Remove an entry from the shortcuts file.
	 */
	public static void removeShortcut(Context context, String display_name, String shortcut_apk)
	{
		// Save the current shortcuts list and remove the file
		InternalFileTXT file = new InternalFileTXT(shortcut_apk.equals(Constants.APK_SHORTCUT_LEGACY) ? Constants.FILE_SHORTCUTS_LEGACY : Constants.FILE_SHORTCUTS) ;
		ArrayList<String> currentShortcuts = file.readAllLines() ;
		if(!file.remove())
			{
				Utils.displayLongToast(context, context.getString(R.string.error_shortcut_remove, file.getName())) ;
				return ;
			}

		// Write the new shortcuts list in the file
		String[] shortcut ;
		for(String shortcut_line : currentShortcuts)
		{
			// Extract the display name from the line and check if this is the shortcut to remove
			shortcut = shortcut_line.split(Constants.SHORTCUT_SEPARATOR) ;
			if(shortcut[0].equals(display_name))
				{
					// Remove the shortcut from the favorites if it was there
					new InternalFileTXT(Constants.FILE_FAVORITES).removeLine(shortcut[1]) ;
					continue ;
				}

			// Add all the other shortcuts to the list again
			file.writeLine(shortcut_line) ;
		}

		// Remove the shortcut icon
		InternalFilePNG icon = new InternalFilePNG(Constants.FILE_ICON_SHORTCUT_PREFIX + display_name + ".png") ;
		icon.remove() ;
	}
}
