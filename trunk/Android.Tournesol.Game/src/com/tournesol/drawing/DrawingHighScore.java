package com.tournesol.drawing;

import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.TimeScoreManager;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

public class DrawingHighScore extends Drawing {

	private transient static Paint paint = new Paint(PaintManager.pixel_text);
	
	public static final float TEXT_SIZE = 16;
	public static final float TEXT_GAP = 5;
	
	private String[] scores_format;
	private int highlight;
	   
	public DrawingHighScore(){}
	
	public void init(String[] scores, String[] ranks, byte course_id, int highlight){
		
		this.highlight = highlight;
		
		//Score[] scores = CourseScoreManager.getBestScore(course_id);
		scores_format = new String[scores.length];
		Rect bounds = new Rect();
		height = 0;
		width = 0;
		
		paint.setTextSize(TEXT_SIZE);
		
		for(int rank_id = 0 ; rank_id < ranks.length ; rank_id++){
			String score_format = null;
			if(scores[rank_id] != null)
				score_format = scores[rank_id];
			else
				score_format = ranks[rank_id] + " 00:00.00 none";
				
			scores_format[rank_id] = score_format;
			paint.getTextBounds(scores_format[rank_id], 0, scores_format[rank_id].length(), bounds);
			height += bounds.height();
			
			if(width < bounds.width())
				width = bounds.width();
		}
	}
	
	@Override
	public void draw(Canvas c) {

		PaintManager.save(PaintManager.pixel_text);
		PaintManager.pixel_text.setTextAlign(Align.LEFT);
		PaintManager.pixel_text.setTextSize(TEXT_SIZE);
		
		for(int i = 0; i < scores_format.length; i++){
			
			if(i == highlight)
				PaintManager.pixel_text.setColor(PaintManager.specialColor);
			else
				PaintManager.pixel_text.setColor(PaintManager.foreColor);
			
			if(scores_format[i] != null)
				c.drawText(scores_format[i], -width / 2, (i - 2f) * height / scores_format.length + TEXT_GAP * i, PaintManager.pixel_text);
			
		}
		
		PaintManager.restore(PaintManager.pixel_text);
	}

}
