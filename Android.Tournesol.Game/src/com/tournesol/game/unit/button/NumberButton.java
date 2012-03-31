package com.tournesol.game.unit.button;

import com.tournesol.drawing.DrawingChars;
import com.tournesol.drawing.DrawingTriangle;
import com.tournesol.game.GameMath;
import com.tournesol.motion.TouchEvent;

import android.graphics.Canvas;
import android.graphics.PointF;

public class NumberButton extends Button{

	private static final long serialVersionUID = 6658747695192703049L;

	public int number = 0;
	public int min_number = 0;
	public int max_number = 10;
	
	public NumberButton(){
		text.text_valign = DrawingChars.V_ALIGN_CENTER;
	}
	
	@Override
	public void drawButton(Canvas c){
		
		PointF focus = getFocusPosition();
		
		
		DrawingTriangle.singleton.copy(this);
		DrawingTriangle.singleton.init(width * 0.3f, width * 0.3f);
		
		this.text.draw(c, focus.x, focus.y, 0);
		
		boolean doesIncrease = false;
		if(touchEvent != null)
			doesIncrease = doesTouchIncrease(touchEvent.x, touchEvent.y);
		
		if(number > min_number){
			if(touchEvent != null && !doesIncrease)
				DrawingTriangle.singleton.stroke_width = original_stroke_width * 2;
			else
				DrawingTriangle.singleton.stroke_width = original_stroke_width;
			
			DrawingTriangle.singleton.draw(c, focus.x + width * -0.25f, focus.y, 270, 0, 0);
		}
		
		if(number < max_number){
			if(touchEvent != null && doesIncrease)
				DrawingTriangle.singleton.stroke_width = original_stroke_width * 2;
			else
				DrawingTriangle.singleton.stroke_width = original_stroke_width;
			
			DrawingTriangle.singleton.draw(c, focus.x + width * 0.25f, focus.y, 90, 0, 0);
		}
	}
	
	@Override
	public void touch(TouchEvent e){
		if(doesTouchIncrease(e.x, e.y))
			setNumber(number + 1);
		else
			setNumber(number - 1);
		
		super.touch(e);
	}
	
	
	public void setNumber(int number){
		if(number > max_number || number < min_number)
			return;
		
		this.number = number;
		this.text.chars.reset();
		this.text.chars.add(number);
	}
	
	private boolean doesTouchIncrease(float touch_x, float touch_y){
		
		float degrees = GameMath.degrees(touch_x - x , touch_y - y) - this.degrees;
		degrees %= 360;
		return degrees > 270 || degrees < 90;
	}
}
