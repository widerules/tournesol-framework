package com.tournesol.game.utility;

import java.util.Random;

public class Randomizer {

	public static Random R = new Random();
	
	public static int nextInt(int start_range, int start_hole, int end_hole, int end_range){
		
		int r = R.nextInt((start_hole - start_range) + (end_range - end_hole)) + start_range;
		if(r >= start_hole)
			r += end_hole;
		
		return r;
	}
	
	public static int nextInt(int start, int end){
		return R.nextInt(end - start  + 1) + start;
	}
	
	public static float nextFloat(float start, float end){
		return R.nextFloat() * (end - start) + start;
	}
	
	public static float nextFloat(float start_range, float start_hole, float end_hole, float end_range){
		
		float r = nextFloat(start_range, end_range);
		if(r > start_hole && r < end_hole)
			return nextFloat(start_range, start_hole, end_hole, end_range);
		
		return r;
	}
}
