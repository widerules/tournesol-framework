package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class DrawingPlayPause extends Drawing {

	private static final long serialVersionUID = 3571104675968708432L;
	
	@Override
	public void draw(Canvas c) {
		
		DrawingTriangle.singleton.copy(this);
		DrawingTriangle.singleton.init(height, width * 0.5f);
		DrawingTriangle.singleton.manage_center = false;
		DrawingTriangle.singleton.draw(c, width * 0.2f, height / 2, degrees + 90, 0, 0);
		
		Paint paint = getPaint();
		c.drawLine(width * 0.7f, 0, width * 0.7f, height, paint);
		c.drawLine(width, 0, width, height, paint);
	}
}
