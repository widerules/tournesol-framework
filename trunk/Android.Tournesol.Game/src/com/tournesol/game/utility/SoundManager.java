package com.tournesol.game.utility;

import java.util.Hashtable;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager {

	public static int MAX_STREAMS = 50;
	
	private static SoundPool[] soundPools = new SoundPool[5];
	
	private static MediaPlayer mediaPlayer;
	private static Context context;

	public static boolean music = true;
	public static boolean sound = true;
	
	public static Hashtable<Integer, int[]> mapping_sound;
	
	public static void init(Context context){
		SoundManager.context = context;
		
		mapping_sound = new Hashtable<Integer, int[]>();
		mediaPlayer = new MediaPlayer();
	}
	
	public static void playSound(int id){
		SoundManager.playSound(id, 0, 1, 1);
	}
	
	public static void playSound(int id, int loop){
		SoundManager.playSound(id, loop, 1, 1);
	}
	
	public static void playSound(int id, int loop, float rate){
		SoundManager.playSound(id, loop, rate, 1);
	}
	
	public static void playSound(int id, int loop, float rate, float volume){

		if(!sound)
			return;
		
		int stream = mapping_sound.get(id)[0];
		int soundPool = mapping_sound.get(id)[1];
		soundPools[soundPool].play(stream, volume, volume, 0, loop, rate);
	}
	
	
	public static void loadSound(int id){
		loadSound(id, 0);
	}
	
	public static void loadSound(int id, int soundPool){
		
		if(soundPools[soundPool] == null)
			soundPools[soundPool] = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);

		mapping_sound.put(id, new int[]{soundPools[soundPool].load(context, id, 1), soundPool});
	}
	
	
	public static void playMusic(int id){
		
		if(!music)
			return;
		
		AssetFileDescriptor assetFileDescriptor = context.getResources().openRawResourceFd(id);

	    try
	    {   
	        mediaPlayer.reset();
	        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
	        mediaPlayer.setLooping(true);
	        mediaPlayer.prepare();
	        mediaPlayer.start();
	        assetFileDescriptor.close();
	    }
	    catch (Exception e)
	    {
	        Log.e("SoundManager", e.getMessage(), e);
	    }
	}
	
	public static void stopMusic(){
		mediaPlayer.stop();
	}
	
	public static void pause(){
		
		mediaPlayer.pause();
		for(int[] stream_soundpool : mapping_sound.values())
			soundPools[stream_soundpool[1]].pause(stream_soundpool[0]);
	}
	
	public static void resume(){
		
		mediaPlayer.start();
		for(int[] stream_soundpool : mapping_sound.values())
			soundPools[stream_soundpool[1]].resume(stream_soundpool[0]);
	}
	
	public static void clearSound(){
		for(int i = 0; i < soundPools.length; i++)
			if(soundPools[i] != null){
				soundPools[i].release();
				soundPools[i] = null;
			}
				
		
		mapping_sound.clear();
	}
	
	public static void resetSound(){
		for(int i = 0; i < soundPools.length; i++)
			resetSound(i);
	}
	
	public static void resetSound(int soundPool){
		
		if(soundPools[soundPool] != null)
			soundPools[soundPool].release();
		
		soundPools[soundPool] = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
		for(int key : mapping_sound.keySet())
			if(mapping_sound.get(key)[1] == soundPool)
				loadSound(key, soundPool);
	}
}
