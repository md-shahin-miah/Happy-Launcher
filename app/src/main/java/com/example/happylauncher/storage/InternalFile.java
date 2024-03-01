package com.example.happylauncher.storage;



import static com.example.happylauncher.ActivityMain.getInternalFolder;

import android.content.Context;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Manage the storage of an internal file.
 */
public class InternalFile
{
	// Attributes
	final File file ;


	/**
	 * Constructor.
	 */
	InternalFile(String filename)
	{
		file = new File(getInternalFolder(), filename) ;
	}


	/**
	 * Check if the internal file exists on the system.
	 */
	public boolean exists()
	{
		return file.exists() ;
	}


	/**
	 * Try to remove the internal file (considered as successful if not existing).
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	public boolean remove()
	{
		if(!exists()) return true ;
		return file.delete() ;
	}


	/**
	 * Return the name of the internal file without the path.
	 */
	public String getName()
	{
		return file.getName() ;
	}


	/**
	 * Try to rename the internal file.
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	public boolean rename(String new_filename)
	{
		return file.renameTo(new File(getInternalFolder(), new_filename)) ;
	}


	/**
	 * Search internal files starting with a prefix (returns <code>null</code> if none was found).
	 */
	public static String[] searchFilesStartingWith(Context context, final String prefix)
	{
		FilenameFilter filter = (directory, name) -> name.startsWith(prefix) ;
		return context.getFilesDir().list(filter) ;
	}
}
