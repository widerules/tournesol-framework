package com.tournesol.drawing;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawingCheck extends Drawing {

	private static final long serialVersionUID = 3021732250585470094L;

	@Override
	public void draw(Canvas c) {
		
		Paint paint = this.getPaint();
		Path path = RecycleBin.drawPath;
		path.reset();

		path.moveTo(0, 0);
		path.lineTo(width * -0.25f, height * -0.4f);
		path.lineTo(width * -0.05f, height * -0.6f);
		path.lineTo(0, height * -0.35f);
		path.lineTo(width * 0.5f, -height);
		path.lineTo(width * 0.75f, height * -0.75f);
		path.close();
		
		c.drawPath(path, paint);
	}
	
}
