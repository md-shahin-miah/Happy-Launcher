package com.example.happylauncher;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.happylauncher.core.Application;

import java.text.Collator;
import java.util.ArrayList;


public class SearchAdapter extends RecyclerAdapter implements Filterable
{
	// Attributes
	private final ArrayList<Application> initialApplicationsList ;
	private final Collator collator ;


	/**
	 * Constructor to fill a RecyclerView with the applications list.
	 */
	public SearchAdapter(Context context, ArrayList<Application> applicationsList)
	{
		// Let the parent actions be performed
		super(context, applicationsList, Constants.SEARCH) ;

		// Initializations
		initialApplicationsList = applicationsList ;
		collator = Collator.getInstance() ;
		collator.setStrength(Collator.PRIMARY) ;
	}


	/**
	 * Create the filter which will be used to search in the list.
	 */
	@Override
	public Filter getFilter()
	{
		return new Filter()
		{
			/**
			 * Retrieve the results after the filter is applied.
			 */
			@Override
			protected FilterResults performFiltering(CharSequence filter)
			{
				// Check if there is a search pattern
				String search = filter.toString() ;
				if(search.isEmpty()) applicationsList = initialApplicationsList ;
					else
					{
						// Filter results based on the search pattern ignoring case and accents
						applicationsList = new ArrayList<>() ;
						int search_length = search.length() ;
						for(Application application : initialApplicationsList)
						{
							String app_name = application.getDisplayName() ;
							if(collator.equals(app_name.substring(0, Math.min(search_length, app_name.length())), search))
								applicationsList.add(application) ;
						}
					}

				// Prepare the filter results
				FilterResults filterResults = new FilterResults() ;
				filterResults.values = applicationsList ;
				return filterResults ;
			}


			/**
			 * Display the search results.
			 */
			@SuppressLint("NotifyDataSetChanged")
			@Override
			protected void publishResults(CharSequence filter, FilterResults results)
			{
				//noinspection unchecked
				applicationsList = (ArrayList<Application>)results.values ;
				notifyDataSetChanged() ;
			}
		} ;
	}


	/**
	 * Launch the first app currently displayed in the adapter (if any).
	 */
	public void launchFirstApp(View view)
	{
		if(getItemCount() >= 1)
			applicationsList.get(0).start(view) ;
	}
}
