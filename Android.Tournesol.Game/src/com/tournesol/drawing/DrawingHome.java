package com.tournesol.drawing;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;

public class DrawingHome extends Drawing {

	private static final long serialVersionUID = -5272974784459192507L;

	@Override
	public void draw(Canvas c) {
		
		Paint paint = getPaint();
		paint.setStyle(Style.STROKE);
		
	    Path path = RecycleBin.drawPath;
	    path.reset();
	    path.moveTo(0, height / 2);
	    path.lineTo(width / 2, 0);
	    path.lineTo(width, height / 2);
	    c.drawPath(path, paint);
	   
	    path.reset();
	    
	    path.moveTo(width / 4, height / 2);
	    path.lineTo(width / 4, height);
	    path.lineTo(width * 3 / 4, height);
	    path.lineTo(width * 3 / 4, height / 2);
	    c.drawPath(path, paint);
	    c.drawLine(width * 3 / 4, 0, width * 3 / 4, height / 10, paint);
	}

}
