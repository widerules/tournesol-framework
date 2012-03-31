package com.tournesol.drawing;

import java.io.Serializable;

import com.tournesol.game.utility.Chars;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class DrawingChars extends Drawing implements Serializable{

	private static final long serialVersionUID = -8679017731018259275L;
	public static final int V_ALIGN_TOP = 0;
	public static final int V_ALIGN_CENTER = 1;
	public static final int V_ALIGN_BOTTOM = 2;
	
	protected IGetChars getChars;
	public Chars chars = new Chars();
	public float text_size = 12f;
	public Align text_align = Align.LEFT;
	public int text_valign = V_ALIGN_TOP;

	public interface IGetChars extends Serializable{
		Chars getChars();
	}
	
	public void init(IGetChars getChars){
		this.getChars = getChars;
	}
	
	private final static Chars empty = new Chars("abc");
	public void calculateDimension(){

		Paint paint = getPaint();
		paint.setTypeface(PaintManager.pixel_typeface);
		paint.setTextSize(text_size);
		paint.setTextAlign(text_align);
		paint.setStyle(Style.FILL);
		
		Chars[] split_chars = chars.split(Chars.LINE_BREAK);
		int count = chars.split_length + 1;
		float total_height = 0;
		float max_width = 0;
		for(int i = 0 ; i < count; i++){

			if(split_chars[i].length != 0 && ((int)split_chars[i].chars[0]) != 32)
				paint.getTextBounds(split_chars[i].chars, 0, split_chars[i].length, RecycleBin.drawRect);
			else
				paint.getTextBounds(empty.chars, 0, empty.length, RecycleBin.drawRect);
			
			total_height += Math.abs(RecycleBin.drawRect.top) * 1.25f;
			if(RecycleBin.drawRect.right - RecycleBin.drawRect.left > max_width)
				max_width = RecycleBin.drawRect.right - RecycleBin.drawRect.left;
		}
		
		this.init(max_width, total_height);
	}
	
	@Override
	public void draw(Canvas c) {

		if(getChars != null)
			chars = getChars.getChars();
		
		calculateDimension();
		
		Paint paint = getPaint();
		paint.setTypeface(PaintManager.pixel_typeface);
		paint.setTextSize(text_size);
		paint.setTextAlign(text_align);
		paint.setStyle(Style.FILL);
		
		if(text_valign == V_ALIGN_CENTER)
			c.translate(0, height * -0.5f);
		
		if(text_valign == V_ALIGN_BOTTOM)
			c.translate(0, height * -1f);
		
		Chars[] split_chars = chars.split(Chars.LINE_BREAK);
		int count = chars.split_length + 1;
		
		for(int i = 0 ; i < count; i++){
			float current_height = /*(i + 1 - count / 2f) **/(i + 1) *  height / count;
			c.drawText(split_chars[i].chars, 0, split_chars[i].length, 0, current_height, paint);
		}
	}

	@Override
	public void copy(Drawing drawing){
		
		super.copy(drawing);
		if(!(drawing instanceof DrawingChars))
			return;
		
		DrawingChars copy = (DrawingChars)drawing;
		this.text_size = copy.text_size;
		this.text_align = copy.text_align;
		this.text_valign = copy.text_valign;
	}
}
