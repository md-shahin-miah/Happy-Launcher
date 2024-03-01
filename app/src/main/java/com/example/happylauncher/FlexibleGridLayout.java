package com.example.happylauncher;

// License
/*

	Created by SHAHIN
 */

// Imports

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Provide a GridLayoutManager with a dynamic width adapted to the parent available width.
 */
public class FlexibleGridLayout extends GridLayoutManager
{
	// Attributes
	private final int item_width ;


	/**
	 * Constructor with an item width given in pixels.
	 */
	public FlexibleGridLayout(Context context, int item_width)
	{
		// Create the GridLayout starting with one column
		super(context, 1, RecyclerView.VERTICAL, false) ;

		// Retrieve the width of an item in pixels
		this.item_width = item_width ;
	}


	/**
	 * Called to lay out the elements of the RecyclerView.
	 */
	@Override
	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
	{
		// Update the number of columns according to the available width
		setSpanCount(Math.max(1, getWidth() / item_width)) ;

		// Let the parent actions be performed
		super.onLayoutChildren(recycler, state) ;
	}
}
