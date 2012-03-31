package com.tournesol.game.control;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.motion.TouchEvent;

public class GameButton extends HeroControl{

	private static final long serialVersionUID = 832067122514521853L;

	private TouchEvent touch_event;
	public long hold_tick;
	
	public GameButton(){
		this.doesCollide = false;
		this.shouldCollide = false;
		this.followFocus = false;
		this.scaling = false;
		this.hold_tick = 0;
	}

	@Override
	public boolean canCollide(Unit unit) {
		return false;
	}

	@Override
	public void draw(Canvas c) {
		PointF focus = this.getFocusPosition();
		c.drawCircle(focus.x, focus.y, this.width / 2, PaintManager.wall);
	}
	
	@Override
	public void touch(TouchEvent e){
		super.touch(e);
		touch_event = e;
	}
	
	@Override
	public void tick(){
		super.tick();
		
		if(touch_event != null && touch_event.type != TouchEvent.TOUCH_UP)
			hold_tick++;
		else
			hold_tick = 0;
	}
	
}
