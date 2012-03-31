package com.tournesol.scoreloop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.preference.PreferenceManager;

import com.scoreloop.client.android.core.model.Achievement;
import com.scoreloop.client.android.core.model.Continuation;
import com.scoreloop.client.android.core.model.Score;
import com.scoreloop.client.android.ui.EntryScreenActivity;
import com.scoreloop.client.android.ui.LeaderboardsScreenActivity;
import com.scoreloop.client.android.ui.OnScoreSubmitObserver;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;
import com.scoreloop.client.android.ui.ShowResultOverlayActivity;

public class ScoreloopHelper {

	private final static String KEY_HAS_ACCEPTED_SCORELOOP_TOS = "KEY_HAS_ACCEPTED_SCORELOOP_TOS";
	
	private static Activity activity;
	private static Handler handler_submit_score;
	
	private static Handler handler_increment_award;
	private static Handler handler_achieve_award;
	private static Handler handler_submit_awards;
	
	public static void init(Context context, String secret){
		ScoreloopManagerSingleton.init(context, secret);
	}
	
	public static boolean getHasAcceptedScoreLoopTOS(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		return preferences.getBoolean(KEY_HAS_ACCEPTED_SCORELOOP_TOS, false);
	}
	
	public static void setHasAcceptedScoreLoopTOS(boolean value){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		Editor edit = preferences.edit();
		edit.putBoolean(KEY_HAS_ACCEPTED_SCORELOOP_TOS, value);
		edit.commit();
	}
	
	public static void initActivity(Activity activity){
		ScoreloopHelper.activity = activity;
		handler_submit_score = new Handler(callback_submit_score);
		handler_increment_award = new Handler(callback_increment_award);
		handler_achieve_award = new Handler(callback_achieve_award);
		handler_submit_awards = new Handler(callback_submit_awards);
	}
	
	public static void destroy(){
		ScoreloopManagerSingleton.destroy();
	}
	
	
	public static void submitScore(float result, float minor_result, int mode){
		
		if(!getHasAcceptedScoreLoopTOS())
			return;
		
		if(!ScoreloopManagerSingleton.get().hasLoadedAchievements()){
			ScoreloopManagerSingleton.get().loadAchievements(null);
			return;
		}
		
		Message msg = new Message();
		msg.getData().putFloat("RESULT", result);
		msg.getData().putFloat("MINOR_RESULT", minor_result);
		msg.getData().putInt("MODE", mode);
		handler_submit_score.sendMessage(msg);
	}
	
	public static void incrementAward(String awardId, int increment){
		
		if(!getHasAcceptedScoreLoopTOS())
			return;
		
		if(!ScoreloopManagerSingleton.get().hasLoadedAchievements()){
			ScoreloopManagerSingleton.get().loadAchievements(null);
			return;
		}
		
		Message msg = new Message();
		msg.getData().putString("AWARD_ID", awardId);
		msg.getData().putInt("INCREMENT", increment);
		handler_increment_award.sendMessage(msg);
	}
	
	public static void achieveAward(String awardId){
		
		if(!getHasAcceptedScoreLoopTOS())
			return;
		
		if(!ScoreloopManagerSingleton.get().hasLoadedAchievements()){
			ScoreloopManagerSingleton.get().loadAchievements(null);
			return;
		}
		
		Message msg = new Message();
		msg.getData().putString("AWARD_ID", awardId);
		handler_achieve_award.sendMessage(msg);
	}
	
	public static void submitAwards(){
		
		if(!getHasAcceptedScoreLoopTOS())
			return;
		
		if(!ScoreloopManagerSingleton.get().hasLoadedAchievements()){
			ScoreloopManagerSingleton.get().loadAchievements(null);
			return;
		}
		
		handler_submit_awards.sendMessage(new Message());
	}
	
	public static void showTOS(final Continuation<Boolean> continuation){
		
		if(!getHasAcceptedScoreLoopTOS())
			ScoreloopManagerSingleton.get().askUserToAcceptTermsOfService(activity, new Continuation<Boolean>() {
				
				@Override
				public void withValue(Boolean value, Exception arg1) {
					if(value == null)
						value = false;
					
					setHasAcceptedScoreLoopTOS(value);
					
					if(value)
						ScoreloopManagerSingleton.get().loadAchievements(null);
					
					if(continuation != null)
						continuation.withValue(value, arg1);
				}
			});
		else
			ScoreloopManagerSingleton.get().loadAchievements(null);
	}
	
	public static void showScoreLoopMenu(){
		if(getHasAcceptedScoreLoopTOS()){
			final Intent intent = new Intent(activity, EntryScreenActivity .class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        activity.startActivity(intent);
		}
		else{
			showTOS(new Continuation<Boolean>() {
				public void withValue(Boolean value, Exception arg1) {
					if(value){
						final Intent intent = new Intent(activity, EntryScreenActivity .class);
						if(intent == null)
							return;
						
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        activity.startActivity(intent);
					}
				};
			});
		}
	}
	
	public static void showScoreLoopLeaderboard(final int leaderboard, final int mode){
		
		if(getHasAcceptedScoreLoopTOS()){
			final Intent intent = new Intent(activity, LeaderboardsScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.putExtra(LeaderboardsScreenActivity.LEADERBOARD, leaderboard);
	        intent.putExtra(LeaderboardsScreenActivity.MODE, mode);
	        activity.startActivity(intent);
		}
		else{
			showTOS(new Continuation<Boolean>() {
				public void withValue(Boolean value, Exception arg1) {
					if(value){
						final Intent intent = new Intent(activity, LeaderboardsScreenActivity.class);
						if(intent == null)
							return;
						
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        intent.putExtra(LeaderboardsScreenActivity.LEADERBOARD, leaderboard);
				        intent.putExtra(LeaderboardsScreenActivity.MODE, mode);
				        activity.startActivity(intent);
					}
				};
			});
		}
	}
	
	private static final Handler.Callback callback_submit_score = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {

			float result = msg.getData().getFloat("RESULT");
			float minor_result = msg.getData().getFloat("MINOR_RESULT");
			int mode = msg.getData().getInt("MODE");
			
			Score score = new Score((double) result, null);
			score.setMinorResult((double) minor_result);
			score.setMode(mode);
			ScoreloopManagerSingleton.get().onGamePlayEnded(score, false);
			ScoreloopManagerSingleton.get().submitLocalScores(null);
			return true;
		}
	};

	private static final Handler.Callback callback_increment_award = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {

			String awardId = msg.getData().getString("AWARD_ID");
			int increment = msg.getData().getInt("INCREMENT");
			Achievement achievement = ScoreloopManagerSingleton.get().getAchievement(awardId);
			
			if(!achievement.isAchieved()){
				int newValue = achievement.getValue() + increment;
				if(newValue >= achievement.getAward().getAchievingValue())
					ScoreloopManagerSingleton.get().achieveAward(awardId, true, true);
				else
					achievement.setValue(newValue);
			}

			return true;
		}
	};
	
	
	private static final Handler.Callback callback_achieve_award = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {

			String awardId = msg.getData().getString("AWARD_ID");
			ScoreloopManagerSingleton.get().achieveAward(awardId, true, true);
			
			return true;
		}
	};
	
	private static final Handler.Callback callback_submit_awards = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {

			ScoreloopManagerSingleton.get().submitAchievements(null);
			return true;
		}
	};
}
