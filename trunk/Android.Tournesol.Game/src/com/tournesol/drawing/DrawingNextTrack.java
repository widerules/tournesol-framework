package com.tournesol.drawing;

import android.graphics.Canvas;

public class DrawingNextTrack extends Drawing {

	private static final long serialVersionUID = 1147187195502649879L;

	public static final DrawingNextTrack singleton = new DrawingNextTrack();
	
	@Override
	public void draw(Canvas c) {
		
		DrawingFastFoward.singleton.copy(this);
		DrawingFastFoward.singleton.draw(c);
		c.drawLine(width, 0, width, height, getPaint());
	}

}
