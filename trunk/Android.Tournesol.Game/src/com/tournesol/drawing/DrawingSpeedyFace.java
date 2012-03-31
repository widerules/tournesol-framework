package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;

public class DrawingSpeedyFace extends Drawing{

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
		c.drawLine(focus.x - radius / 5, 
				   focus.y + radius / 2, 
				   focus.x + radius / 5, 
				   focus.y + radius / 2, PaintManager.moving_unit);
		  
		//Dessiner les yeux
		c.drawCircle(focus.x - radius /  3, focus.y - radius /  4, radius / 20f, PaintManager.moving_unit);
		c.drawCircle(focus.x + radius /  3, focus.y - radius /  4, radius / 20f, PaintManager.moving_unit);
		
		//Dessiner les sourcils
		c.drawLine(focus.x - radius * 0.6f, 
				   focus.y - radius * 0.6f, 
				   focus.x - radius * 0.2f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		
		c.drawLine(focus.x + radius * 0.6f, 
				   focus.y - radius * 0.6f, 
				   focus.x + radius * 0.2f, 
				   focus.y - radius * 0.2f, PaintManager.moving_unit);
		

		PaintManager.restore(PaintManager.moving_unit);
	}
	
}
