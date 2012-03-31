package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

public class DrawingDizzyFace extends Drawing {

	@Override
	public void draw(Canvas c) {
		
		PointF focus = new PointF(width / 2, height / 2);
		float radius = width / 2;
		
		
		PaintManager.save(PaintManager.moving_unit);
		PaintManager.moving_unit.setStyle(Style.STROKE);
		PaintManager.moving_unit.setStrokeWidth(5);
		
		//Dessiner le cercle
		c.drawCircle(focus.x, focus.y, radius, PaintManager.moving_unit);
		
		PaintManager.moving_unit.setStrokeWidth(5);
		
		//Dessiner le sourire
		float smile_size = radius / 3;
		RecycleBin.drawRectF.left = focus.x - smile_size;
		RecycleBin.drawRectF.top = focus.y - smile_size + radius * 0.7f;
		RecycleBin.drawRectF.right = focus.x + smile_size;
		RecycleBin.drawRectF.bottom = focus.y + smile_size + radius * 0.5f;
		c.drawArc(RecycleBin.drawRectF, 180, 180, true, PaintManager.moving_unit);
		
		
		
		//Dessiner les yeux
		PaintManager.moving_unit.setStrokeWidth(5);
		drawDizzyEye(c, focus.x - radius * 0.4f, focus.y - radius * 0.4f);
		drawDizzyEye(c, focus.x + radius / 3, focus.y - radius / 3);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
	
	private void drawDizzyEye(Canvas c, float x, float y){
		float radius = width / 2;
		float size = radius / 6f;
		float step = radius / 6f;
		for(int i = 0; i < 4 ; i++){
			
			RecycleBin.drawRectF.left = x - size / 2;
			RecycleBin.drawRectF.top = y - size / 2 + (step * (i % 2)) / 2;
			RecycleBin.drawRectF.right = x + size / 2;
			RecycleBin.drawRectF.bottom = y + size / 2 + (step * (i % 2)) / 2;
			
			if(i % 2 == 0)
				c.drawArc(RecycleBin.drawRectF, 90, 180, false, PaintManager.moving_unit);
			else
				c.drawArc(RecycleBin.drawRectF, 270, 180, false, PaintManager.moving_unit);	

			size += step;
		}
	}
	
}
