package com.tournesol.game.utility;

public class TimeScoreManager {

	private final static StringBuilder sb = new StringBuilder(8);
	
	private static int minutes_mask;
	private static int seconds_mask;
	private static int minutes;
	private static int seconds;
	private static int ms;
	
	
	private static final Chars highscore = new Chars();
	private static final Chars time = new Chars();
	
	public static Chars format(int milliseconds){
		
		minutes_mask = ((int)(milliseconds / 60000)) * 60000 ;
		seconds_mask = (((int)(milliseconds - minutes_mask) / 1000)) * 1000;
		minutes = minutes_mask / 60000;
		seconds = seconds_mask / 1000;
		ms = (milliseconds - seconds_mask - minutes_mask) / 10;

		time.reset();
		
		if(minutes > 0){
			time.add(minutes);
			time.add(':');
		}

		if(seconds > 0){
			if(minutes_mask > 0 && seconds < 10)
				time.add(Chars.DIGITS[0]);
			
			time.add(seconds);
		}
		else{
			if(minutes > 0){
				time.add(Chars.DIGITS[0]);
				time.add(Chars.DIGITS[0]);
			}
			else
				time.add(Chars.DIGITS[0]);
		}
		
		time.add('.');

		if(ms > 0){
			if(ms < 10)
				time.add(Chars.DIGITS[0]);
			time.add(ms);
		}
		else{
			time.add(Chars.DIGITS[0]);
			time.add(Chars.DIGITS[0]);
		}
			
		return time;
	}
	
	public static Chars formatHighScore(int milliseconds){
		
		minutes_mask = ((int)(milliseconds / 60000)) * 60000 ;
		seconds_mask = (((int)(milliseconds - minutes_mask) / 1000)) * 1000;
		minutes = minutes_mask / 60000;
		seconds = seconds_mask / 1000;
		ms = (milliseconds - seconds_mask - minutes_mask) / 10;

		highscore.reset();
		
		if(minutes > 0){
			highscore.add(minutes);
		}
		else{
			highscore.add(Chars.DIGITS[0]);
			highscore.add(Chars.DIGITS[0]);
		}
		
		highscore.add(':');

		if(seconds > 0){
			if(minutes_mask > 0 && seconds < 10)
				highscore.add(Chars.DIGITS[0]);
			
			highscore.add(seconds);
		}
		else{
			highscore.add(Chars.DIGITS[0]);
			highscore.add(Chars.DIGITS[0]);
		}
		
		highscore.add('.');
		
		if(ms > 0){
			if(ms < 10)
				highscore.add(Chars.DIGITS[0]);
			highscore.add(ms);
		}
		else{
			highscore.add(Chars.DIGITS[0]);
			highscore.add(Chars.DIGITS[0]);
		}

		return highscore;
	}
}

