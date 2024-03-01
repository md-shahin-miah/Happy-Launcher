package com.example.happylauncher.events;




import static com.example.happylauncher.ActivityMain.updateList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.happylauncher.Utils;


/**
 * Listen for packages additions and deletions.
 */
public class PackagesListener extends BroadcastReceiver
{
	// Constants
	private static final String TAG = "PackagesListener" ;


	/**
	 * Provide the filter to use when registering this receiver.
	 */
	public IntentFilter getFilter()
	{
		IntentFilter filter = new IntentFilter() ;
		filter.addAction(Intent.ACTION_PACKAGE_ADDED) ;
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED) ;
		filter.addDataScheme("package") ;
		return filter ;
	}


	/**
	 * Called when a broadcast message is received.
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Check if the intent is valid
		if(intent == null) return ;
		Utils.logDebug(TAG, "received " + intent) ;

		// Do not react to applications updates
		if(intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) return ;

		// If a package has been added or removed, update the list of applications
		String action = intent.getAction() ;
		if(Intent.ACTION_PACKAGE_ADDED.equals(action) || Intent.ACTION_PACKAGE_REMOVED.equals(action))
			updateList(context) ;
	}
}
