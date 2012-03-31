package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

public class DrawingCruelFace extends Drawing{

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
		float smile_size = radius / 2;
		RecycleBin.drawRectF.left = focus.x - smile_size;
		RecycleBin.drawRectF.top = focus.y - smile_size + radius / 2;
		RecycleBin.drawRectF.right = focus.x + smile_size;
		RecycleBin.drawRectF.bottom = focus.y + smile_size + radius / 2;
		c.drawArc(RecycleBin.drawRectF, 180, 180, true, PaintManager.moving_unit);
		
		PaintManager.moving_unit.setStyle(Style.FILL_AND_STROKE);
		
		//Dessiner les yeux
		c.drawCircle(focus.x - radius /  3, focus.y - radius /  4, radius / 20f, PaintManager.moving_unit);
		c.drawCircle(focus.x + radius /  3, focus.y - radius /  4, radius / 20f, PaintManager.moving_unit);
		
		c.drawLine(focus.x - radius * 0.2f, 
				   focus.y - radius * 0.6f, 
				   focus.x - radius * 0.6f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		
		c.drawLine(focus.x + radius * 0.2f, 
				   focus.y - radius * 0.6f, 
				   focus.x + radius * 0.6f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
	
}
