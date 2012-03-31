package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint.Style;

public class DrawingFastFoward extends Drawing {
	
	private static final long serialVersionUID = 6347848176160066066L;
	
	public static final DrawingFastFoward singleton = new DrawingFastFoward();
	
	@Override
	public void draw(Canvas c) {
		
		DrawingTriangle.singleton.copy(this);
		DrawingTriangle.singleton.init(height, width * 0.5f);
		DrawingTriangle.singleton.manage_center = false;
		DrawingTriangle.singleton.draw(c, width * 0.225f, height / 2, degrees + 90, 0, 0);
		DrawingTriangle.singleton.draw(c, width * 0.7f, height / 2, degrees + 90, 0, 0);
	}
}
