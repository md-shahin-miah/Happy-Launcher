package com.example.happylauncher.storage;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;


import com.example.happylauncher.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class InternalFilePNG extends InternalFile
{
	// Constants
	private static final String TAG = "InternalFilePNG" ;


	/**
	 * Constructor (the given filename should include the extension).
	 */
	public InternalFilePNG(String filename)
	{
		super(filename) ;
	}


	/**
	 * Write a Bitmap image to the internal file.
	 */
	public void writeToFile(Bitmap bitmap)
	{
		// Do not continue if the bitmap is empty
		if(bitmap == null) return ;

		try
		{
			// Write the Bitmap in the file
			FileOutputStream output_file = new FileOutputStream(file) ;
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, output_file) ;
			output_file.close() ;
		}
		catch(IOException exception)
		{
			// An error happened
			Utils.logError(TAG, exception.getMessage()) ;
		}
	}


	/**
	 * Return the content of the file as a Bitmap (or <code>null</code> if an error happened).
	 */
	public Bitmap readFromFile()
	{
		if(!exists()) return null ;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options()) ;
	}


	/**
	 * Convert a Bitmap to a Drawable (returns <code>null</code> if an error happened).
	 */
	public Drawable convertBitmapToDrawable(Context context, Bitmap bitmap)
	{
		if(bitmap == null) return null ;
		return new BitmapDrawable(context.getResources(), bitmap) ;
	}


	/**
	 * Return the filename followed by a Base64 String (or an empty string if an error happened).
	 */
	public String prepareForExport()
	{
		// Try to decode the Bitmap
		Bitmap bitmap = readFromFile() ;
		if(bitmap == null) return "" ;

		// Encode the Bitmap as a Base64 String and return the result
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream() ;
		bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArray) ;
		return file.getName() + ": " + Base64.encodeToString(byteArray.toByteArray(), Base64.NO_WRAP) ;
	}


	/**
	 * Decode the line representing a Bitmap in an import file and write it to the internal file.
	 */
	public void loadFromImport(String data)
	{
		// Decode the Base64 String representing the Bitmap
		byte[] bitmap_bytes = Base64.decode(data, Base64.NO_WRAP) ;

		// Create the internal file from the decoded data
		writeToFile(BitmapFactory.decodeByteArray(bitmap_bytes, 0, bitmap_bytes.length)) ;
	}
}
