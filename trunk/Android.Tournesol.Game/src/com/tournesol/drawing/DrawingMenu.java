package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawingMenu extends Drawing{

	private static final long serialVersionUID = -7111229527435642090L;

	@Override
	public void draw(Canvas c) {
		
		Paint paint = getPaint();
		c.drawLine(0, 0, width, 0, paint);
		c.drawLine(width * 0.2f, height / 3, width, height / 3, paint);
		c.drawLine(width * 0.2f, height * 2 / 3, width, height * 2 / 3, paint);
		c.drawLine(width * 0.2f, height, width, height, paint);
	}

}
