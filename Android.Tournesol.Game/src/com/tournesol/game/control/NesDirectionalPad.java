package com.tournesol.game.control;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class NesDirectionalPad extends DirectionalPad {

	private static final long serialVersionUID = 9184783873024341493L;

	
	@Override
	public void draw(Canvas c){
		
		Paint paint = getPaint();
		PointF focus = getFocusPosition();
		
		Path path = RecycleBin.drawPath;
		path.reset();
		path.moveTo(focus.x + width * -3 / 6, focus.y + height * -1 / 6);
		path.lineTo(focus.x + width * -3 / 6, focus.y + height * 1 / 6);
		path.lineTo(focus.x + width * -1 / 6, focus.y + height * 1 / 6);
		path.lineTo(focus.x + width * -1 / 6, focus.y + height * 3 / 6);
		path.lineTo(focus.x + width * 1 / 6, focus.y + height * 3 / 6);
		path.lineTo(focus.x + width * 1 / 6, focus.y + height * 1 / 6);
		path.lineTo(focus.x + width * 3 / 6, focus.y + height * 1 / 6);
		path.lineTo(focus.x + width * 3 / 6, focus.y + height * -1 / 6);
		path.lineTo(focus.x + width * 1 / 6, focus.y + height * -1 / 6);
		path.lineTo(focus.x + width * 1 / 6, focus.y + height * -3 / 6);
		path.lineTo(focus.x + width * -1 / 6, focus.y + height * -3 / 6);
		path.lineTo(focus.x + width * -1 / 6, focus.y + height * -1 / 6);
		path.close();
		
		c.drawPath(path, paint);
	}
}
