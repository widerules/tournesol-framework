package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Style;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

public class DrawingFunnyFace extends Drawing{

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
		RecycleBin.drawRectF.top = focus.y - smile_size;
		RecycleBin.drawRectF.right = focus.x + smile_size;
		RecycleBin.drawRectF.bottom = focus.y + smile_size;
		c.drawArc(RecycleBin.drawRectF, 0, 180, true, PaintManager.moving_unit);
		
		//Dessiner les yeux
		float eye_size = radius / 6;
		RecycleBin.drawRectF.left = focus.x - eye_size - radius / 3;
		RecycleBin.drawRectF.top = focus.y - eye_size - radius / 3;
		RecycleBin.drawRectF.right = focus.x + eye_size - radius / 3;
		RecycleBin.drawRectF.bottom = focus.y + eye_size - radius / 3;
		c.drawArc(RecycleBin.drawRectF, 180, 180, false, PaintManager.moving_unit);
		
		RecycleBin.drawRectF.left = focus.x - eye_size + radius / 3;
		RecycleBin.drawRectF.top = focus.y - eye_size - radius / 3;
		RecycleBin.drawRectF.right = focus.x + eye_size + radius / 3;
		RecycleBin.drawRectF.bottom = focus.y + eye_size - radius / 3;
		c.drawArc(RecycleBin.drawRectF, 180, 180, false, PaintManager.moving_unit);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
	
}
