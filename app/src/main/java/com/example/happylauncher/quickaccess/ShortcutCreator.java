package com.example.happylauncher.quickaccess;

// License
/*

Created by SHAHIN
 */

// Imports

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Create a shortcut to display the favorites popup.
 */
public class ShortcutCreator extends AppCompatActivity
{
	/**
	 * Constructor.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Let the parent actions be performed
		super.onCreate(savedInstanceState) ;

		// Create the shortcut and close the activity
		setResult(Activity.RESULT_OK, PopupFavorites.getIntent(this)) ;
		finish() ;
	}
}
