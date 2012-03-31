package com.tournesol.game.unit;

import java.util.ArrayList;

import com.tournesol.game.utility.Chars;

import android.graphics.Canvas;
import android.graphics.PointF;

public class AnimatedCharsUnit extends CharsUnit {

	private static final long serialVersionUID = -7061410577652094716L;
	
	public int current_tick = 0;
	public int modifier_tick = 1;
	public int max_tick = 200;
	
	public ArrayList<Chars> list_chars = new ArrayList<Chars>();
	private int index = 0;
	
	@Override
	public void tick(){
		
		current_tick += modifier_tick;
		if(current_tick > max_tick || current_tick < 0){
			modifier_tick *= -1;
		}
		
		if(current_tick == max_tick / 2){
			index++;
			if(index >= list_chars.size())
				index = 0;
				
			drawingChars.chars = list_chars.get(index);
		}
	}
	
	@Override
	public void draw(Canvas c){
		
		float middle_tick = max_tick / 2f;
		
		if(current_tick == middle_tick)
			return;
		
		c.save();
		
		if(current_tick < middle_tick)
			//Retract
			c.scale(/*focus.x, focus.y, */(middle_tick - current_tick) / middle_tick, 1);
		else
			//Expand
			c.scale(/*focus.x, focus.y, */(current_tick - middle_tick) / middle_tick, 1);
		
		super.draw(c);
		c.restore();
	}
}
