package com.tournesol.drawing;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class DrawingAgain extends Drawing {

	private static final long serialVersionUID = 645795182881798717L;

	@Override
	protected void draw(Canvas c) {
		
		Paint paint = getPaint();
		RecycleBin.drawRectF.left = -width / 2;
		RecycleBin.drawRectF.top = -height / 2;
		RecycleBin.drawRectF.right = width / 2;
		RecycleBin.drawRectF.bottom = height / 2;

		c.drawArc(RecycleBin.drawRectF, 295, 310, false, paint);
		
		DrawingTriangle.singleton.copy(paint);
		DrawingTriangle.singleton.copy(this);
		DrawingTriangle.singleton.manage_center = false;
		DrawingTriangle.singleton.init(width * 0.5f, height * 0.5f);
		DrawingTriangle.singleton.style = Style.FILL;
		DrawingTriangle.singleton.draw(c, width * 0f, height * -0.35f, 45);
	}

}
