package com.example.happylauncher.settings;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


import com.example.happylauncher.ActivityMain;
import com.example.happylauncher.Constants;
import com.example.happylauncher.R;
import com.example.happylauncher.core.Application;
import com.example.happylauncher.core.Folder;

import java.util.ArrayList;

/**
 * Settings and Help activity.
 */
public class ActivitySettingsOperation extends AppCompatActivity
{
	// Attributes
	private static ArrayList<String> applicationsComponentInfos ;
	private static ArrayList<String> applicationsNames ;
	private static ArrayList<String> clockAppsComponentInfos ;
	private static ArrayList<String> clockAppsNames ;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Let the parent actions be performed
		super.onCreate(savedInstanceState) ;

		// Initializations
		if(applicationsComponentInfos == null) applicationsComponentInfos = new ArrayList<>() ;
			else applicationsComponentInfos.clear() ;
		if(applicationsNames == null) applicationsNames = new ArrayList<>() ;
			else applicationsNames.clear() ;
		if(clockAppsComponentInfos == null) clockAppsComponentInfos = new ArrayList<>() ;
			else clockAppsComponentInfos.clear() ;
		if(clockAppsNames == null) clockAppsNames = new ArrayList<>() ;
			else clockAppsNames.clear() ;

		// Prepare the lists of applications
		applicationsComponentInfos.add(Constants.NONE) ;
		applicationsNames.add(getString(R.string.set_no_action)) ;
		clockAppsComponentInfos.add(Constants.NONE) ;
		clockAppsNames.add(getString(R.string.set_no_clock_app)) ;
		loadInstalledApplications() ;

		// Load the general settings layout
		setContentView(R.layout.activity_settings) ;
		getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, new SettingsFragment()).commit() ;
	}


	/**
	 * Build the lists of installed applications.
	 */
	private void loadInstalledApplications()
	{
		// Retrieve the list of all installed applications
		ArrayList<Application> allApplications = ActivityMain.getApplicationsList().getApplications(true) ;

		// Store the retrieved information in the lists
		for(Application application : allApplications)
		{
			// Prepare the lists for the gestures selectors
			applicationsComponentInfos.add(application.getComponentInfo()) ;
			if(application instanceof Folder) applicationsNames.add(((Folder)application).getDisplayNameWithCount()) ;
				else applicationsNames.add(application.getDisplayName()) ;

			// Prepare the lists for the clock app selector
			if(application.getName().contains("clock") || application.getName().contains("alarm"))
				{
					clockAppsComponentInfos.add(application.getComponentInfo()) ;
					clockAppsNames.add(application.getDisplayName()) ;
				}
		}
	}


	/**
	 * Perfom actions when returning to the home screen.
	 */
	@SuppressLint("SourceLockedOrientationActivity")
	@Override
	protected void onDestroy()
	{
		// Fix an Android Oreo 8.1 bug (orientation is sometimes kept from an activity to another)
		if(Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1)
			{
				// Retrieve the selected orientation and apply it
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this) ;
				String forced_orientation = settings.getString(Constants.FORCED_ORIENTATION, Constants.NONE) ;
				switch(forced_orientation)
				{
					case "portrait" :
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) ;
						break ;
					case "landscape" :
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ;
						break ;
					case "reverse_landscape" :
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) ;
						break ;
					default :
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) ;
						break ;
				}
			}

		// Let the parent actions be performed
		super.onDestroy() ;
	}


	// ---------------------------------------------------------------------------------------------

	/**
	 * Load the general settings from the XML file and prepare their values.
	 */
	public static class SettingsFragment extends PreferenceFragmentCompat
	{
		/**
		 * Constructor.
		 */
		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
		{
			// Load the settings from the XML file
			setPreferencesFromResource(R.xml.settings_operation, rootKey) ;

			// Initialize the gestures selectors
			ListPreference doubleTap = findPreference(Constants.DOUBLE_TAP) ;
			if(doubleTap != null)
				{
					doubleTap.setEntries(applicationsNames.toArray(new CharSequence[0])) ;
					doubleTap.setEntryValues(applicationsComponentInfos.toArray(new CharSequence[0])) ;
				}
			ListPreference swipeTowardsLeft = findPreference(Constants.SWIPE_LEFTWARDS) ;
			if(swipeTowardsLeft != null)
				{
					swipeTowardsLeft.setEntries(applicationsNames.toArray(new CharSequence[0])) ;
					swipeTowardsLeft.setEntryValues(applicationsComponentInfos.toArray(new CharSequence[0])) ;
				}
			ListPreference swipeTowardsRight = findPreference(Constants.SWIPE_RIGHTWARDS) ;
			if(swipeTowardsRight != null)
				{
					swipeTowardsRight.setEntries(applicationsNames.toArray(new CharSequence[0])) ;
					swipeTowardsRight.setEntryValues(applicationsComponentInfos.toArray(new CharSequence[0])) ;
				}

			// Initialize the clock app selector
			ListPreference clockApp = findPreference(Constants.CLOCK_APP) ;
			if(clockApp != null)
				{
					clockApp.setEntries(clockAppsNames.toArray(new CharSequence[0])) ;
					clockApp.setEntryValues(clockAppsComponentInfos.toArray(new CharSequence[0])) ;
				}
		}
	}
}
