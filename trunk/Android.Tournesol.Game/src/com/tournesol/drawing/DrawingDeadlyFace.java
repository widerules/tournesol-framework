package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

public class DrawingDeadlyFace extends Drawing{

	@Override
	public void draw(Canvas c) {
		
		PointF focus = new PointF(width / 2, height / 2);
		float radius = width / 2;
		
		PaintManager.save(PaintManager.moving_unit);
		PaintManager.moving_unit.setStyle(Style.STROKE);
		PaintManager.moving_unit.setStrokeWidth(5);
		
		//Dessiner le cercle
		c.drawCircle(focus.x, focus.y, radius, PaintManager.moving_unit);
		
		//Dessiner le sourire
		float smile_size = radius * 0.2f;
		float x = focus.x - radius * 0.6f;
		float y = focus.y + radius * 0.3f;
		for(int i = 0 ; i < 6 ; i++){
		 
			RecycleBin.drawRectF.left = x + (smile_size * i);
			RecycleBin.drawRectF.top = y - smile_size / 2;
			RecycleBin.drawRectF.right = x + (smile_size * (i + 1));
			RecycleBin.drawRectF.bottom = y + smile_size / 2;
			c.drawArc(RecycleBin.drawRectF, 180 * (i % 2), 180, false, PaintManager.moving_unit);
			
		}

		//Dessiner les yeux
		c.drawLine(focus.x - radius * 0.6f, 
				   focus.y - radius * 0.6f, 
				   focus.x - radius * 0.2f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		
		c.drawLine(focus.x - radius * 0.2f, 
				   focus.y - radius * 0.6f, 
				   focus.x - radius * 0.6f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		
		c.drawLine(focus.x + radius * 0.6f, 
				   focus.y - radius * 0.6f, 
				   focus.x + radius * 0.2f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		 
		c.drawLine(focus.x + radius * 0.2f, 
				   focus.y - radius * 0.6f, 
				   focus.x + radius * 0.6f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
	
}
