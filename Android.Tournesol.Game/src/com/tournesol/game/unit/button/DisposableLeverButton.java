package com.tournesol.game.unit.button;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.tournesol.game.GameMath;
import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.unit.button.DisposableButton;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.motion.TouchEvent;

public class DisposableLeverButton extends DisposableButton {

	private static final long serialVersionUID = 1045581419420244615L;
	
	public static float MAX_DISTANCE = 300;
	public static float MIN_DISTANCE = 65;
	
	//Coordonnées pour ajuster la valeur
	public float fix_x;
	public float fix_y;
	
	public ShapeLine line;

	private TouchEvent old_touch_event;
	public ILeverListener lever_listener;
	
	public DisposableLeverButton(){
		
		line = new ShapeLine(this);
		line.start.x = 0;
		line.start.y = 0;
		this.shapes.add(line);
	}
	
	public void init(float fix_x, float fix_y, ILeverListener lever_listener){
		
		this.lever_listener = lever_listener;
		this.fix_x = fix_x;
		this.fix_y = fix_y;
	}
	
	@Override
	public void show(){

		PointF p = GameMath.direction(degrees);
		float distance = getDistanceFromValuePct(this.lever_listener.getValuePct());
		this.init(fix_x + distance * p.x, fix_y - distance * p.y, 40);
		line.end.x = fix_x - this.x;
		line.end.y = fix_y - this.y;
		old_touch_event = null;
		
		this.show(fix_x, fix_y);
	}
	
	@Override
	public void draw(Canvas c){
		
		PointF focus = getFocusPosition();
		
		c.drawLine(focus.x + line.start.x, 
				   focus.y + line.start.y,
				   focus.x + line.end.x, 
				   focus.y + line.end.y, PaintManager.moving_unit);
		
		super.draw(c);
	}
	
	@Override
	public void tick(){
		
		if(old_touch_event != null && old_touch_event.type != TouchEvent.TOUCH_UP)
			this.notifyLeverListener();
		
		if(old_touch_event != null){
			this.x = old_touch_event.x;
			this.y = old_touch_event.y;
		}
		else{
			this.x += (goal_x - x) / 20;
			this.y += (goal_y - y) / 20;
		}
		
		if(hide && this.alive){
			this.alive = Math.abs(this.x - this.goal_x) > 5 || Math.abs(this.y - this.goal_y) > 5;
		}

		line.end.x = fix_x - this.x;
		line.end.y = fix_y - this.y;
	}
	
	@Override
	public void hide(){
		hide = true;	
		old_touch_event = null;
		this.goal_x = fix_x;
		this.goal_y = fix_y;
		this.line.end.x = this.line.start.x;
		this.line.end.y = this.line.start.y;
	}
	
	@Override
	public void touch(TouchEvent e){
		
		super.touch(e);
		old_touch_event = e;
	}
	
	public float getDistanceFromValuePct(float value_pct){
		
		float distance =  (MAX_DISTANCE - MIN_DISTANCE) * value_pct;
		distance += MIN_DISTANCE;
		
		if(distance > MAX_DISTANCE)
			distance = MAX_DISTANCE;
		else if(distance < MIN_DISTANCE)
			distance = MIN_DISTANCE;

		return distance;
	}
	
	public void notifyLeverListener(){
		
		float distance = GameMath.distance(this.x - fix_x, this.y - fix_y);
		distance -= MIN_DISTANCE;
		float distance_pct = distance / (MAX_DISTANCE - MIN_DISTANCE);
		PointF direction = GameMath.direction(this.x - fix_x, this.y - fix_y);
		
		if(distance_pct > 1)
			distance_pct = 1;
		else if(distance_pct < 0)
			distance_pct = 0;
		
		lever_listener.move(distance_pct, direction.x, direction.y);
	}
}
