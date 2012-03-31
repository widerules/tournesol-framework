package com.tournesol.game.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GamePreference{

	public SharedPreferences preferences;
	public void init(Context context){
		this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
}
