package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.TimeScoreManager;

public class DrawingFinishLapScore extends Drawing{
	public static final float TEXT_SIZE = 16;
	public static final float TEXT_GAP = 5;
	
	private String[] laps_format;
	    
	public DrawingFinishLapScore(){}
	
	public DrawingFinishLapScore(String[] lap_names, int... laps){
		init(lap_names, laps);
	}
	
	public void init(String[] lap_names, int... laps){
		
		laps_format = new String[laps.length + 1];
		Rect bounds = new Rect();
		height = 0;
		width = 0;
		int total = 0;
		
		Paint p = new Paint(PaintManager.pixel_text);
		p.setTextSize(TEXT_SIZE);
		
		for(int lap_id = 0 ; lap_id < laps.length ; lap_id++){
			
			total += laps[lap_id];
			laps_format[lap_id] = lap_names[lap_id] + " " +
				TimeScoreManager.formatHighScore(laps[lap_id]);
			
			p.getTextBounds(laps_format[lap_id], 0, laps_format[lap_id].length(), bounds);
			height += bounds.height();
			if(width < bounds.width())
				width = bounds.width();
		}
		
		laps_format[laps_format.length - 1] = "total " +
			TimeScoreManager.formatHighScore(total);
		
		p.getTextBounds(laps_format[laps_format.length - 1], 0, 
											  laps_format[laps_format.length - 1].length(), bounds);
		height += bounds.height();
		if(width < bounds.width())
			width = bounds.width();
	}
	
	@Override
	public void draw(Canvas c) {

		PaintManager.save(PaintManager.pixel_text);
		PaintManager.pixel_text.setTextAlign(Align.LEFT);
		PaintManager.pixel_text.setTextSize(TEXT_SIZE);
		
		for(int i = 0; i < laps_format.length; i++){
			if(laps_format[i] != null)
				c.drawText(laps_format[i], -width / 2, (i - 1.5f) * height /laps_format.length + TEXT_GAP * i, PaintManager.pixel_text);
		}
		
		PaintManager.restore(PaintManager.pixel_text);
	}
}
