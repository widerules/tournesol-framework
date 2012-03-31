package com.tournesol.drawing;

import android.graphics.Canvas;

public class DrawingRewind extends Drawing{

	private static final long serialVersionUID = -5030795016408815735L;

	@Override
	public void draw(Canvas c) {
		
		DrawingFastFoward.singleton.copy(this);
		c.save();
		c.rotate(180, width / 2, height / 2);
		DrawingFastFoward.singleton.draw(c);
		c.restore();
	}
}
