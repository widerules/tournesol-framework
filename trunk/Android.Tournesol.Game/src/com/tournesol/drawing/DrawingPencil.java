package com.tournesol.drawing;

import android.graphics.Canvas;

import com.tournesol.game.utility.PaintManager;

public class DrawingPencil extends Drawing{

	@Override
	public void draw(Canvas c) {

		c.drawLine(0, height, width * 0.125f, height * 0.75f, PaintManager.wall);
		c.drawLine(0, height, width * 0.25f, height * 0.875f, PaintManager.wall);
		c.drawLine(width * 0.25f, height * 0.875f, width * 0.125f, height * 0.75f, PaintManager.wall);
		
		c.drawLine(width * 0.125f, height * 0.75f, width * 0.75f, height * 0.125f, PaintManager.wall);
		c.drawLine(width * 0.25f, height * 0.875f, width * 0.875f, height * 0.25f, PaintManager.wall);
		c.drawLine(width * 0.75f, height * 0.125f, width * 0.875f, height * 0.25f, PaintManager.wall);
	}
}
