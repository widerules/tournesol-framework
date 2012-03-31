package com.tournesol.network.wifi;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;

import com.tournesol.drawing.DrawingChars;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.network.ConnectionUnit;

public class WifiConnectionUnit extends ConnectionUnit {

	private static final long serialVersionUID = 4011790906529820166L;
	
	private final DrawingChars drawingWI = new DrawingChars();
	private final DrawingChars drawingFI = new DrawingChars();
	
	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		drawingWI.chars.set("wi");
		drawingWI.manage_center = true;
		drawingWI.text_size = height * 0.5f;
		drawingFI.copy(drawingWI);
		drawingFI.chars.set("fi");
	}
	
	@Override
	public void draw(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		paint.setStyle(Style.STROKE);
		paint.setColor(stroke_color);
	
		//Côté gauche
		Path path = RecycleBin.drawPath;
		path.reset();
		path.moveTo(focus.x - distance + width * 0.1f, focus.y - height * 0.5f);
		path.lineTo(focus.x - distance - width * 0.3f, focus.y - height * 0.5f);
		path.lineTo(focus.x - distance - width * 0.3f, focus.y + height * 0.5f);
		path.lineTo(focus.x - distance - width * 0.1f, focus.y + height * 0.5f);
		path.close();
		c.drawPath(path, paint);
		
		drawingWI.copy(paint);
		drawingWI.draw(c, focus.x - distance - width * 0.15f, focus.y, 0);
		
		paint = getPaint();
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(stroke_color);
		
		//Côté droit
		path.reset();
		path.moveTo(focus.x + distance + width * 0.1f, focus.y - height * 0.5f);
		path.lineTo(focus.x + distance + width * 0.3f, focus.y - height * 0.5f);
		path.lineTo(focus.x + distance + width * 0.3f, focus.y + height * 0.5f);
		path.lineTo(focus.x + distance - width * 0.1f, focus.y + height * 0.5f);
		path.close();
		c.drawPath(path, paint);
		
		paint.setColor(color);
		paint.setStyle(Style.STROKE);
		drawingFI.copy(paint);
		drawingFI.draw(c, focus.x + distance + width * 0.15f, focus.y, 0);
	}
}
