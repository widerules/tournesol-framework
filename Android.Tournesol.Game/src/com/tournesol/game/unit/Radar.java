package com.tournesol.game.unit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.utility.RecycleBin;

public class Radar extends Unit {

	private static final long serialVersionUID = -2605251300985843582L;

	public int circle_count = 3;
	public int line_count = 6;
	
	public float tick_degrees = 270;
	
	public void tick(){
		
		tick_degrees++;
		if(tick_degrees >= 360)
			tick_degrees = tick_degrees - 360;
	}
	
	public void draw(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		float radius = width / 2;
		float radius_increment = -radius  / circle_count;
		float degrees_increment = 180f / line_count;
		float degrees = 0;
		 
		for(int line = 0; line < line_count; line++){
			
			float startX = radius * GameMath.cos(degrees);
			float startY = radius * GameMath.sin(degrees);
			float stopX = radius * GameMath.cos(degrees + 180);
			float stopY =  radius * GameMath.sin(degrees + 180);
			c.drawLine(startX, startY, stopX, stopY, paint);
			degrees += degrees_increment;
		}
		
		for(int circle = 0; circle < circle_count; circle++){
			c.drawCircle(focus.x, focus.y, radius, paint);
			radius += radius_increment;
		}
		
		if(doesTick){
			float alpha = 25;
			paint.setStyle(Style.FILL_AND_STROKE);
			int degrees_count = 10;
			for(degrees = 0; degrees < degrees_count; degrees++){
				RecycleBin.drawRectF.left = focus.x - this.width / 2;
				RecycleBin.drawRectF.top = focus.y - this.height / 2;
				RecycleBin.drawRectF.right = focus.x + this.width / 2;
				RecycleBin.drawRectF.bottom = focus.y + this.height / 2;
				paint.setAlpha((int)alpha);
				c.drawArc(RecycleBin.drawRectF, tick_degrees - (degrees_count - degrees) * 4, (degrees_count - degrees) * 4, true, paint);
				alpha += 255f / degrees_count;
			}
		}
	}
	
}
