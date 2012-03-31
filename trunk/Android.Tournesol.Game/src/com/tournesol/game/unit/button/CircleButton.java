package com.tournesol.game.unit.button;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.game.shape.ShapeEllipse;

public class CircleButton extends Button{

	private static final long serialVersionUID = -2363050218906418955L;
	private static final float text_offset = 5;

	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		
		this.shapes.clear();
		ShapeEllipse ellipse = new ShapeEllipse(this);
		this.shapes.add(ellipse);
	}
	

	@Override
	protected void drawButton(Canvas c) {
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		
		c.drawCircle(focus.x, focus.y, this.width / 2, paint);
		
		if(stroke_width > 0){
			PaintManager.save(paint);
			paint.setStrokeWidth(stroke_width);
			paint.setColor(stroke_color);
			paint.setColor(alpha);
			paint.setStyle(Style.STROKE);
			c.drawCircle(focus.x, focus.y, this.width / 2, paint);
			PaintManager.restore(paint);
		}

		PaintManager.save(PaintManager.pixel_text);
		PaintManager.pixel_text.setColor(text.color);
		PaintManager.pixel_text.setAlpha(alpha);
		PaintManager.pixel_text.setTextSize(text.text_size);
		PaintManager.pixel_text.setTextAlign(text.text_align);
		
		if(text_valign == TEXT_VALIGN_TOP){
			RecycleBin.drawRectF.left = focus.x - this.width / 2 - text_offset;
			RecycleBin.drawRectF.top = focus.y - this.height / 2 - text_offset;
			RecycleBin.drawRectF.right = focus.x + this.width / 2 + text_offset;
			RecycleBin.drawRectF.bottom = focus.y + this.height / 2 + text_offset;
			
			Path path = RecycleBin.drawPath;
			path.reset();
			path.moveTo(focus.x - this.width / 2, y);
			path.addArc(RecycleBin.drawRectF, 180, 180);
			c.drawTextOnPath(text.chars.chars, 0, text.chars.length, path, 0, 0, PaintManager.pixel_text);
		}
		
		if(text_valign == TEXT_VALIGN_MIDDLE)
			c.drawText(text.chars.chars, 0, text.chars.length, focus.x, focus.y, PaintManager.pixel_text);
		 
		PaintManager.restore(PaintManager.pixel_text);
	}
}
