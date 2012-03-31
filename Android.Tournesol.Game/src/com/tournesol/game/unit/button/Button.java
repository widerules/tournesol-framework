package com.tournesol.game.unit.button;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.tournesol.drawing.Drawing;
import com.tournesol.drawing.DrawingChars;
import com.tournesol.game.unit.Rectangle;
import com.tournesol.game.utility.Chars;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.motion.TouchEvent;

public class Button extends Rectangle{

	private static final long serialVersionUID = 3614401085258619392L;

	public DrawingChars text;
	
	public static final byte TEXT_VALIGN_TOP = 0;
	public static final byte TEXT_VALIGN_MIDDLE = 1;
	public static final byte TEXT_VALIGN_BOTTOM = 2;
	
	public byte text_valign = TEXT_VALIGN_MIDDLE;
	protected float original_stroke_width;
	
	public boolean selected = false;
	public boolean focus = false;
	
	protected TouchEvent touchEvent;
	
	public Button(){
		
		this.doesCollide = false;
		this.shouldCollide = false;
		this.followFocus = false;
		this.scaling = false;
		this.gravity_damper = 0;
		this.manage_rotation = false;
		this.doesMove = false;
		
		text = new DrawingChars();
		text.chars = new Chars();
		text.text_align = Align.CENTER;
	}
	
	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		original_stroke_width = stroke_width;
	}
	
	@Override
	public void touch(TouchEvent e){
		super.touch(e);
		touchEvent = e;
	}
	
	@Override
	public void tick(){
		super.tick();
		
		if(touchEvent != null && touchEvent.type == TouchEvent.TOUCH_UP)
			touchEvent = null;
		
		if(focus){
			stroke_width = original_stroke_width * 2;
		}
		else if(touchEvent != null){
			if(touchEvent.type != TouchEvent.TOUCH_UP)
				stroke_width = original_stroke_width * 2;
			else
				stroke_width = original_stroke_width;
		}
		else
			stroke_width = original_stroke_width;
	}
	
	@Override
	public void draw(Canvas c) {
		drawButton(c);
		super.draw(c);
	}
	
	@Override
	protected void drawConstruct(Canvas c) {	
	
	}
	
	protected void drawButton(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		RecycleBin.drawRectF.left = focus.x - width / 2;
		RecycleBin.drawRectF.top = focus.y - height / 2;
		RecycleBin.drawRectF.right = focus.x + width / 2;
		RecycleBin.drawRectF.bottom = focus.y + height / 2;

		c.drawRoundRect(RecycleBin.drawRectF, 10, 10, paint);
		
		if(stroke_width > 0){
			paint.setStrokeWidth(stroke_width);
			paint.setColor(stroke_color);
			paint.setAlpha(alpha);
			paint.setStyle(Style.STROKE);
			c.drawRoundRect(RecycleBin.drawRectF, 10, 10, paint);
		}
		
		drawText(c);
	}
	
	protected void drawText(Canvas c){
		
		PointF focus = getFocusPosition();
		
		//Appliquer le même alpha pour le texte
		text.alpha = alpha;
		
		switch (text_valign){
			case TEXT_VALIGN_TOP:
				text.draw(c, focus.x, focus.y - height / 2, 0, focus.x, focus.y);
				break;
			case TEXT_VALIGN_MIDDLE:
				text.draw(c, focus.x, focus.y - text.height / 2, 0, focus.x, focus.y);
				break;
			case TEXT_VALIGN_BOTTOM:
				text.draw(c, focus.x, focus.y + height / 2  - text.height, 0, focus.x, focus.y);
				break;
		}
	}
	
	@Override
	protected void drawDrawings(Canvas c){
		c.save();
		RecycleBin.drawRectF.left = x - width / 2f;
		RecycleBin.drawRectF.top = y - height / 2f;
		RecycleBin.drawRectF.right = x + width / 2f;
		RecycleBin.drawRectF.bottom = y + height / 2f;
		c.clipRect(RecycleBin.drawRectF);
		super.drawDrawings(c);
		c.restore();
	}
	
	@Override
	public void copy(Drawing drawing){
		super.copy(drawing);
		if(!(drawing instanceof Button))
			return;
		
		Button copy = (Button)drawing;
		this.text_valign = copy.text_valign;
		this.text.copy(copy.text);
	}

}
