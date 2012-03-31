package com.tournesol.drawing;

import android.graphics.Canvas;

public class DrawingPreviousTrack extends Drawing{

	private static final long serialVersionUID = 1147187195502649879L;

	@Override
	public void draw(Canvas c) {
		
		DrawingNextTrack.singleton.copy(this);
		c.save();
		c.rotate(180, width / 2, height / 2);
		DrawingNextTrack.singleton.draw(c);
		c.restore();
	}
}
