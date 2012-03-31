package com.tournesol.game.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.motion.TouchEvent;

public class DirectionalPad extends Control<DirectionalPad> {

	private static final long serialVersionUID = 6891519047469859567L;
	
	public static final int DIRECTION_NONE = -1;
	public static final int DIRECTION_UP = 270;
	public static final int DIRECTION_LEFT = 180;
	public static final int DIRECTION_RIGHT = 0;
	public static final int DIRECTION_DOWN = 90;
	public static final int DIRECTION_UP_LEFT = 225;
	public static final int DIRECTION_UP_RIGHT = 315;
	public static final int DIRECTION_DOWN_LEFT = 135;
	public static final int DIRECTION_DOWN_RIGHT = 45;

	private TouchEvent touch_event;
	public int direction = DIRECTION_NONE;
	
	public float directional_degrees = 60;
	public float diagonal_degrees = 90 - directional_degrees; 
	
	
	@Override
	public boolean canCollide(Unit unit) {
		return false;
	}

	@Override
	public void draw(Canvas c) {
		super.draw(c);
		drawDirectionalPad(c);
	}
	
	protected void drawDirectionalPad(Canvas c){
		PointF focus = this.getFocusPosition();
		Paint paint = this.getPaint();
		c.drawCircle(focus.x, focus.y, this.width / 2, paint);
		
		RecycleBin.drawRectF.left = focus.x - width / 2;
		RecycleBin.drawRectF.top = focus.y - height / 2;
		RecycleBin.drawRectF.right = focus.x + width / 2;
		RecycleBin.drawRectF.bottom = focus.y + height / 2;
		
		paint.setStyle(Style.FILL);
		paint.setAlpha(125);
		
		if(direction != DIRECTION_NONE)
			if(direction == DIRECTION_UP || direction == DIRECTION_DOWN || direction == DIRECTION_RIGHT || direction == DIRECTION_LEFT )
				c.drawArc(RecycleBin.drawRectF, direction - 22.5f + degrees, directional_degrees, true, paint);
			else
				c.drawArc(RecycleBin.drawRectF, direction - 22.5f + degrees, diagonal_degrees, true, paint);
	}
	
	@Override
	public void touch(TouchEvent e){
		super.touch(e);
		touch_event = e;
	}
	
	@Override
	public void tick(){
		super.tick();
		
		int old_direction = direction;
		
		if(touch_event != null && touch_event.type != TouchEvent.TOUCH_UP){
			
			float degrees = GameMath.degrees(touch_event.x - this.x, touch_event.y - this.y) - this.degrees;
			if(degrees < 0)
				degrees += 360;
				
			degrees %= 360;
			
			if(degrees >= (360 - (directional_degrees / 2)) || degrees < directional_degrees / 2)
				direction = DIRECTION_RIGHT;
			else if(degrees >= directional_degrees / 2 && degrees < (directional_degrees / 2 + diagonal_degrees))
				direction = DIRECTION_DOWN_RIGHT;
			else if(degrees >= (90 - directional_degrees / 2) && degrees < (90 + directional_degrees / 2))
				direction = DIRECTION_DOWN;
			else if(degrees >= (90 + directional_degrees / 2) && degrees < (90 + directional_degrees / 2 + diagonal_degrees))
				direction = DIRECTION_DOWN_LEFT;
			else if(degrees >= (180 - directional_degrees / 2) && degrees < (180 + directional_degrees / 2))
				direction = DIRECTION_LEFT;
			else if(degrees >= (180 + directional_degrees / 2) && degrees < (180 + directional_degrees / 2 + diagonal_degrees))
				direction = DIRECTION_UP_LEFT;
			else if(degrees >= (270 - directional_degrees / 2) && degrees < (270 + directional_degrees / 2))
				direction = DIRECTION_UP;
			else if(degrees >= (270 + directional_degrees / 2) && degrees < (270 + directional_degrees / 2 + diagonal_degrees))
				direction = DIRECTION_UP_RIGHT;
		}
		else
			direction = DIRECTION_NONE;
		
		if(old_direction != direction)
			this.controlChanged(direction, old_direction);
	}
}
