package com.tournesol.game.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.tournesol.drawing.Drawing;
import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.motion.TouchEvent;

public class QuadraButton extends Control<QuadraButton>  {

	private static final long serialVersionUID = 6891519047469859567L;
	
	public static final byte BUTTON_NONE = 0;
	public static final byte BUTTON_UP = 1;
	public static final byte BUTTON_RIGHT = 2;
	public static final byte BUTTON_DOWN = 4;
	public static final byte BUTTON_LEFT = 8;
	
	private TouchEvent touch_event;
	private byte first_touch_button = BUTTON_NONE;
	
	//Flag pour indiquer quels boutons sont pressés
	public byte button = BUTTON_NONE;
	
	public Drawing drawing_up;
	public Drawing drawing_down;
	public Drawing drawing_right;
	public Drawing drawing_left;
	
	@Override
	public boolean canCollide(Unit unit) {
		return false;
	}

	@Override
	public void draw(Canvas c) {
		super.draw(c);
		PointF focus = this.getFocusPosition();
		float focus_x = focus.x;
		float focus_y = focus.y;
		
		Paint paint = this.getPaint();
		
		float distance = this.width / 2 * GameMath.cos(45);
		
		if((button & BUTTON_LEFT) == BUTTON_LEFT){
			PaintManager.save(paint);
			paint.setStrokeWidth(stroke_width * 2);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setAlpha(125);
			c.drawCircle(focus_x - distance, focus_y, this.width / 5, paint);
			PaintManager.restore(paint);
		}
		
		c.drawCircle(focus_x - distance, focus_y, this.width / 5, paint);
		if(drawing_left != null){
			drawing_left.alpha = alpha;
			drawing_left.draw(c, focus_x - distance, focus_y, 0);
		}
		
		paint = this.getPaint();
		if((button & BUTTON_RIGHT) == BUTTON_RIGHT){
			PaintManager.save(paint);
			paint.setStrokeWidth(stroke_width * 2);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setAlpha(125);
			c.drawCircle(focus_x + distance, focus_y, this.width / 5, paint);
			PaintManager.restore(paint);
		}
		
		c.drawCircle(focus_x + distance, focus_y, this.width / 5, paint);
		if(drawing_right != null){
			drawing_right.alpha = alpha;
			drawing_right.draw(c, focus_x + distance, focus_y, 0);
		}
		
		paint = this.getPaint();
		if((button & BUTTON_UP) == BUTTON_UP){
			PaintManager.save(paint);
			paint.setStrokeWidth(stroke_width * 2);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setAlpha(125);
			c.drawCircle(focus_x, focus_y - distance, this.width / 5, paint);
			PaintManager.restore(paint);
		}
		
		c.drawCircle(focus_x, focus_y - distance, this.width / 5, paint);
		if(drawing_up != null){
			drawing_up.alpha = alpha;
			drawing_up.draw(c, focus_x, focus_y - distance, 0);
		}
		
		paint = this.getPaint();
		if((button & BUTTON_DOWN) == BUTTON_DOWN){
			PaintManager.save(paint);
			paint.setStrokeWidth(stroke_width * 2);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setAlpha(125);
			c.drawCircle(focus_x , focus_y  + distance, this.width / 5, paint);
			PaintManager.restore(paint);
		}
		
		c.drawCircle(focus_x , focus_y  + distance, this.width / 5, paint);
		if(drawing_down != null){
			drawing_down.alpha = alpha;
			drawing_down.draw(c, focus_x, focus_y + distance, 0);
		}
	}
	
	@Override
	public void touch(TouchEvent e){
		super.touch(e);
		touch_event = e;
		first_touch_button = getTouchButton(touch_event.x - this.x, touch_event.y - this.y);
	}
	
	@Override
	public void tick(){
		super.tick();
		
		byte old_button = button;
		
		if(touch_event != null && touch_event.type != TouchEvent.TOUCH_UP)
			button = (byte)(first_touch_button | getTouchButton(touch_event.x - this.x, touch_event.y - this.y));
		else
			button = BUTTON_NONE;
		
		if(old_button != button)
			this.controlChanged(button, old_button);
	}
	
	private byte getTouchButton(float x, float y){
		
		float degrees = GameMath.degrees(x, y) - this.degrees;
		if(degrees < 0)
			degrees += 360;
		
		degrees %= 360;
		byte touch_button = BUTTON_NONE;
		
		if(degrees >= 315 || degrees < 45)
			touch_button = BUTTON_RIGHT;
		else if(degrees >= 45 && degrees < 135)
			touch_button = BUTTON_DOWN;
		else if(degrees >= 135 && degrees < 225)
			touch_button = BUTTON_LEFT;
		else if(degrees >= 225 && degrees < 315)
			touch_button = BUTTON_UP;
		
		return touch_button;
	}
}
