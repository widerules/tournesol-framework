package com.tournesol.game.utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class PaintManager {

	public static int foreColor = Color.WHITE;
	public static int backColor = Color.BLACK;
	public static int specialColor = Color.rgb(105, 120, 220);
	
	public static Typeface pixel_typeface;
	
	public final static Paint pixel_text = new Paint();
	public final static Paint button = new Paint();
	public final static Paint wall = new Paint();
	public final static Paint moving_unit = new Paint();
	
	private static float savedTextSize;
	private static float savedStrokeWidth;
	private static int savedColor;
	private static Style savedStyle;
	private static Align savedTextAlign;
	private static Typeface savedTypeface;
	
	public static void init(Context context){
		initPixelText();
		initButtonRectangle();
		initWall();
		initMovingUnit();
	}
	
	public static void save(Paint paint){
		savedTextSize = paint.getTextSize();
		savedColor = paint.getColor();
		savedStyle = paint.getStyle();
		savedTextAlign = paint.getTextAlign();
		savedStrokeWidth = paint.getStrokeWidth();
		savedTypeface = paint.getTypeface();
	}
	
	public static void restore(Paint paint){
		paint.setTextSize(savedTextSize);
		paint.setColor(savedColor);
		paint.setStyle(savedStyle);
		paint.setTextAlign(savedTextAlign);
		paint.setStrokeWidth(savedStrokeWidth);
		paint.setTypeface(savedTypeface);
	}  
	
	private static void initPixelText(){
		pixel_text.setColor(foreColor);
		pixel_text.setTypeface(pixel_typeface);
		pixel_text.setTextAlign(Align.CENTER);
		pixel_text.setAntiAlias(true);
	}
	
	private static void initButtonRectangle(){
		button.setColor(foreColor);
		button.setStyle(Style.FILL_AND_STROKE);
		button.setAntiAlias(true); 
	}
	 
	private static void initWall(){
		wall.setColor(foreColor);
		wall.setStyle(Style.STROKE);
		wall.setAntiAlias(true);
		wall.setStrokeWidth(2);
	} 
	
	private static void initMovingUnit(){
		moving_unit.setColor(foreColor);
		moving_unit.setStrokeWidth(2);
		moving_unit.setStyle(Style.STROKE);
		moving_unit.setAntiAlias(true);
	}
}
