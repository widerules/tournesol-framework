package com.tournesol.drawing;

import com.tournesol.game.GameMath;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawingSearch extends Drawing {

	private static final long serialVersionUID = -2063978767848454803L;

	@Override
	public void draw(Canvas c) {
		
		Paint paint = getPaint();
		c.drawCircle(width / 2.5f, height / 2.5f, width / 2.5f, paint);
		c.drawLine(width / 2.5f * (1 + GameMath.cos(45)), height / 2.5f * (1 + GameMath.sin(45)), width, height, paint);
	}
	
}
