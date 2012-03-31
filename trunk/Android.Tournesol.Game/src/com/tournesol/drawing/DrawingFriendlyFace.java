package com.tournesol.drawing;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Paint.Style;

public class DrawingFriendlyFace extends Drawing {
	
	@Override
	public void draw(Canvas c) {

		PaintManager.save(PaintManager.moving_unit);
		PaintManager.moving_unit.setStyle(Style.STROKE);
		PaintManager.moving_unit.setStrokeWidth(5);
		
		//Dessiner le cercle
		c.drawCircle(width / 2, height / 2, width / 2, PaintManager.moving_unit);
		
		//Dessiner le sourire
		RecycleBin.drawRectF.left = width / 4;
		RecycleBin.drawRectF.top = height / 4;
		RecycleBin.drawRectF.right = width * 3 / 4;
		RecycleBin.drawRectF.bottom = height * 3 / 4;
		c.drawArc(RecycleBin.drawRectF, 0, 180, false, PaintManager.moving_unit);
		
		PaintManager.moving_unit.setStyle(Style.FILL_AND_STROKE);
		
		//Dessiner les yeux
		c.drawCircle(width / 3, width / 3, width / 20f, PaintManager.moving_unit);
		c.drawCircle(width * 2 / 3, width / 3, width / 20f, PaintManager.moving_unit);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
}
