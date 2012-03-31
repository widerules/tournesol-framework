package com.tournesol.drawing;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class DrawingBack extends Drawing{

	private static final long serialVersionUID = -3709955133108007411L;

	@Override
	public void draw(Canvas c) {
		DrawingTriangle.singleton.copy(this);
		DrawingTriangle.singleton.style = Style.FILL;
		DrawingTriangle.singleton.init(width * 0.4f, 0);
		DrawingTriangle.singleton.manage_center = true;
		DrawingTriangle.singleton.draw(c, width * 0.4f, 0, -90, 0, 0);
		
		Paint paint = getPaint();
		c.drawLine(0, 0, width * 2 / 3, 0, paint);
		c.drawLine(width / 6, height, width * 2 / 3, height, paint);
		
		float center_x = this.width * 2 / 3;
	    float center_y = this.height / 2;
	    
	    RecycleBin.drawRectF.left = center_x - this.width / 3;
	    RecycleBin.drawRectF.top = center_y - this.height / 2;
	    RecycleBin.drawRectF.right = center_x + this.width / 3;
	    RecycleBin.drawRectF.bottom = center_y + this.height / 2;
	    
	    PaintManager.save(paint);
	    paint.setStyle(Style.STROKE);
	    c.drawArc(RecycleBin.drawRectF, 270, 180, false, paint);
	    PaintManager.restore(paint);
	}

}
