package com.tournesol.game.control;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.motion.TouchEvent;

public class GameAnalog extends HeroControl {

	private static final long serialVersionUID = -9038683154641044132L;
	
	private TouchEvent touch_event;
	public float vector_x = 0;
	public float vector_y = 0;
	
	public GameAnalog(){
		this.doesCollide = false;
		this.shouldCollide = false;
		this.followFocus = false;
		this.scaling = false;
	}
	
	@Override
	public boolean canCollide(Unit unit) {
		return false;
	}

	@Override
	public void draw(Canvas c) {
		
		PointF focus = this.getFocusPosition();
		c.drawCircle(focus.x, focus.y, this.width / 2, PaintManager.wall);
		
		PaintManager.save(PaintManager.wall);
		PaintManager.wall.setStyle(Style.FILL); 
		PaintManager.wall.setColor(PaintManager.backColor);
		c.drawCircle(focus.x + vector_x, focus.y + vector_y, this.width / 4, PaintManager.wall);
		
		PaintManager.wall.setStyle(Style.STROKE);
		PaintManager.wall.setColor(PaintManager.foreColor);
		c.drawCircle(focus.x + vector_x, focus.y + vector_y, this.width / 4, PaintManager.wall);
		
		PaintManager.restore(PaintManager.wall);
	}
	
	@Override
	public void touch(TouchEvent e){
		super.touch(e);
		touch_event = e;
	}
	
	@Override
	public void tick(){
		super.tick();
		if(touch_event != null && touch_event.type != TouchEvent.TOUCH_UP){
			vector_x = touch_event.x - this.x;
			vector_y = touch_event.y - this.y;
			
			float distance = GameMath.distance(vector_x, vector_y);
			if(distance > width / 2){
				PointF p = GameMath.direction(vector_x, vector_y);
				vector_x = p.x * width / 2;
				vector_y = p.y * width / 2;
			}
		}
	}
}
