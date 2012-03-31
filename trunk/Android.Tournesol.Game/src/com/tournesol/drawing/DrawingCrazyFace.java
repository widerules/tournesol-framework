package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;

public class DrawingCrazyFace extends Drawing{

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
		c.drawLine(focus.x - radius / 2f, 
				   focus.y + radius / 2f, 
				   focus.x + radius / 2f, 
				   focus.y + radius / 2f, PaintManager.moving_unit);
		
		//Dessiner la bave
		c.drawLine(focus.x + radius * 0.45f, 
				   focus.y + radius * 0.5f, 
				   focus.x + radius * 0.45f, 
				   focus.y + radius * 0.8f, PaintManager.moving_unit);
		
		
		//Dessiner l'oeil gauche
		c.drawCircle(focus.x - radius * 0.7f, 
					 focus.y - radius * 0.1f, 
					 radius * 0.3f, 
					 PaintManager.moving_unit);
		c.drawCircle(focus.x - radius * 0.8f, 
				 focus.y - radius * 0.1f, 
				 radius * 0.05f, 
				 PaintManager.moving_unit);
		
		//Dessiner l'oeil droit
		c.drawCircle(focus.x + radius * 0.3f, 
					 focus.y - radius * 0.3f, 
					 radius * 0.5f, 
					 PaintManager.moving_unit);
		
		c.drawCircle(focus.x + radius * 0.5f, 
				 focus.y - radius * 0.5f, 
				 radius * 0.1f, 
				 PaintManager.moving_unit);
		
		PaintManager.moving_unit.setStyle(Style.FILL_AND_STROKE);
		PaintManager.restore(PaintManager.moving_unit);
	}
	
}
