package com.tournesol.drawing;

import com.tournesol.game.utility.PaintManager;

import android.graphics.Canvas;

public class DrawingPoint extends Drawing {

	public static DrawingPoint singleton = new DrawingPoint();
	
	public DrawingPoint(){
		this.height = 1;
		this.width = 1;
	}
	
	@Override
	public void draw(Canvas c) {
		c.drawPoint(0, 0, PaintManager.moving_unit);
	}

}
