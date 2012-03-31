package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawingCircle extends Drawing{

	private static final long serialVersionUID = -1090219246892406435L;

	public static DrawingCircle singleton = new DrawingCircle();
	
	@Override
	protected void draw(Canvas c) {
		
		Paint paint = getPaint();
		c.drawCircle(0, 0, width / 2, paint);
	}

}
