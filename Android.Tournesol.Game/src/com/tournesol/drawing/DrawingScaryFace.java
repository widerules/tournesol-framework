package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

public class DrawingScaryFace extends Drawing {

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
		c.drawCircle(focus.x - radius /  3, 
				     focus.y + radius /  2, 
				     radius / 20f, 
				     PaintManager.moving_unit);
		

		
		//Dessiner les yeux
		float eye_size = radius / 4;
		c.drawCircle(focus.x - radius * 0.6f, focus.y + radius * 0.1f, 1, PaintManager.moving_unit);
		RecycleBin.drawRectF.left = focus.x - eye_size - radius * 0.75f;
		RecycleBin.drawRectF.top = focus.y - eye_size;
		RecycleBin.drawRectF.right = focus.x + eye_size - radius * 0.75f;
		RecycleBin.drawRectF.bottom = focus.y + eye_size;
		c.drawArc(RecycleBin.drawRectF, 315, 180, true, PaintManager.moving_unit);
		
		
		c.drawCircle(focus.x - radius * 0.1f, focus.y + radius * 0.1f, 1, PaintManager.moving_unit);
		RecycleBin.drawRectF.left = focus.x - eye_size - radius * 0.1f;
		RecycleBin.drawRectF.top = focus.y - eye_size;
		RecycleBin.drawRectF.right = focus.x + eye_size - radius * 0.1f;
		RecycleBin.drawRectF.bottom = focus.y + eye_size;
		c.drawArc(RecycleBin.drawRectF, 45, 180, true, PaintManager.moving_unit);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
	
}
