package com.tournesol.file;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class FileManager {

	private static Context context;
	
	public static void saveObject(String file, Object obj){
		
		try {
			ObjectOutputStream objectStream = new ObjectOutputStream(context.openFileOutput(file, Context.MODE_WORLD_READABLE));
			objectStream.writeObject(obj);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Object loadObject(String file){
		try {
			
			ObjectInputStream objectStream = new ObjectInputStream(context.openFileInput(file));
			Log.i("com.tournesol.game.FileManager", "Load object from " + file + "(" + context.getFileStreamPath(file).length() / 1024 + "kb)");
			return objectStream.readObject();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void init(Context context){
		FileManager.context = context;
	}
}
