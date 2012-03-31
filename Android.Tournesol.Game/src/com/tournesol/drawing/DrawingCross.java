package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawingCross extends Drawing{

	private static final long serialVersionUID = -1090219246892406435L;

	@Override
	protected void draw(Canvas c) {
		
		Paint paint = getPaint();
		c.drawLine(-width / 2, -height / 2, width / 2, height / 2, paint);
		c.drawLine(-width / 2, height / 2, width / 2, -height / 2, paint);
	}

}
