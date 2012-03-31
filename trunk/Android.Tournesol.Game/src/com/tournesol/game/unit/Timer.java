package com.tournesol.game.unit;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Timer extends Unit {

	private static final long serialVersionUID = 3076904408298687876L;
	
	public int tick = 0;
	public int max_tick = 100;
	
	@Override
	public void tick(){
		super.tick();
	
		if(tick < max_tick)
			tick++;		
	}
	
	@Override
	public void draw(Canvas c){
		super.draw(c);
		
		Paint paint = getPaint();
		PointF focus = getFocusPosition();
		
		float radius = width / 2;
		RecycleBin.drawRectF.left = focus.x - radius;
		RecycleBin.drawRectF.top = focus.y - radius;
		RecycleBin.drawRectF.right = focus.x + radius;
		RecycleBin.drawRectF.bottom = focus.y + radius;
		
		float sweepAngle = tick * 360f / max_tick ; 
		if(sweepAngle > 0)
			c.drawArc(RecycleBin.drawRectF, 270, sweepAngle, true, paint);
	}
	
}
