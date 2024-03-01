package com.example.happylauncher.core;

// Imports

import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.happylauncher.menu.DialogMenu;


public class Menu extends Application
{
	/**
	 * Constructor.
	 */
	public Menu(String display_name, String name, String apk, Drawable icon)
	{
		super(display_name, name, apk, icon, null) ;
	}


	/**
	 * Display the main menu.
	 */
	public boolean start(View view)
	{
		new DialogMenu(view.getContext()).show() ;
		return true ;
	}
}
