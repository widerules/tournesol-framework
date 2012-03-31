package com.tournesol.drawing;

import java.io.Serializable;
import java.util.HashMap;

import com.tournesol.game.Game;
import com.tournesol.game.utility.PaintManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;

public abstract class Drawing implements Serializable {
	
	private static final long serialVersionUID = 7580596850333437562L;
	
	private static final int offset = 4;
	
	private static final HashMap<Class, Bitmap> cache_class_bitmap = new HashMap<Class, Bitmap>();
	private static final HashMap<Integer, Bitmap> cache_res_bitmap = new HashMap<Integer, Bitmap>();
	private static Context context;
	
	private static final Paint paint = initPaint();
	
	public float width = 100;
	public float height = 100;
	public float degrees = 0;
	public float stroke_width = 1;
	public Style style = Style.STROKE;
	public int color = PaintManager.foreColor;//Color.BLACK;
	public int stroke_color = PaintManager.foreColor;//Color.BLACK;
	public int alpha = 255;
	public PathEffect pathEffect = null;
	
	//Indiquer si on doit centrer le dessin en fonction du width et du height
	public boolean manage_center = false;
	
	//Indiquer si le dessin gère lui-même la rotation des objets
	public boolean manage_rotation = false;
	
	protected abstract void draw(Canvas c);
	
	public void draw(Canvas c, float translate_x, float translate_y, float degrees) {
		this.draw(c, translate_x, translate_y, degrees, translate_x, translate_y);
	}
	
	public void draw(Canvas c, float translate_x, float translate_y, float degrees, float rotate_x, float rotate_y) {
		c.save();
		
		if(!manage_center)
			c.translate(translate_x, translate_y);
		else
			c.translate(translate_x-width / 2, translate_y-height / 2);
		
		if(!manage_rotation)
			c.rotate(degrees, rotate_x, rotate_y);
		
		if(style == Style.FILL_AND_STROKE){
			
			style = Style.STROKE;
			draw(c);

			style = Style.FILL;
			draw(c);
			
			style = Style.FILL_AND_STROKE;
		}
		else
			draw(c);
		
		c.restore();
	}
	
	public void init(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public Paint getPaint(){
		
		paint.setStrokeWidth(stroke_width);
		paint.setStyle(style);
		
		if(style == Style.FILL)
			paint.setColor(color);
		else if(style == Style.STROKE)
			paint.setColor(stroke_color);
		
		paint.setAlpha(alpha);
		paint.setPathEffect(pathEffect);
		return paint;
	}
	
	public void rotate(float degrees){
		this.degrees += degrees;
		this.degrees %= 360;
	}
	
	private static Paint initPaint(){
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		return p;
	}
	
	public void copy(Drawing drawing){
		this.init(drawing.width, drawing.height);
		degrees = drawing.degrees;
		stroke_width = drawing.stroke_width;
		stroke_color = drawing.stroke_color;
		style = drawing.style;
		color = drawing.color;
		alpha = drawing.alpha;
		manage_center = drawing.manage_center;
		manage_rotation= drawing.manage_rotation;
	}
	
	public void copy(Paint paint){
		stroke_width = paint.getStrokeWidth();
		style = paint.getStyle();
		color = paint.getColor();
		alpha = paint.getAlpha();
	}
	
	public Bitmap getBitmap(){
		return getBitmap(this.getClass());
	}
	
	public static Bitmap getBitmap(Class c){
		
		if(cache_class_bitmap.containsKey(c))
			return cache_class_bitmap.get(c);
		
		Bitmap bmp = null;
		
		try {
			bmp = createBitmap(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cache_class_bitmap.put(c, bmp);
	
		return bmp;
	}
	
	public static Bitmap getBitmap(Class c, float width, float height){
		
		if(cache_class_bitmap.containsKey(c))
			return cache_class_bitmap.get(c);
		
		Bitmap bmp = null;
		
		try {
			bmp = createBitmap(c, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cache_class_bitmap.put(c, bmp);
	
		return bmp;
	}
	
	private static Bitmap createBitmap(Class<Drawing> c) throws Exception {
		return createBitmap(c.newInstance());
	}
	
	private static Bitmap createBitmap(Class<Drawing> c, float width, float height) throws Exception {
		Drawing drawing = c.newInstance();
		drawing.init(width, height);
		return createBitmap(drawing);
	}
	
	public static Bitmap createBitmap(Drawing drawing){
		Bitmap bmp = Bitmap.createBitmap((int)drawing.width + offset, (int)drawing.height + offset, Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.translate(offset / 2, offset / 2);
		drawing.draw(canvas);
		return bmp;
	}
	
	public static Bitmap createBitmap(int resId){
	
		if(cache_res_bitmap.containsKey(resId))
			return cache_res_bitmap.get(resId);
		
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
		cache_res_bitmap.put(resId, bmp);
		return bmp;
	}
	
	public static void init(Context context){
		Drawing.context = context;
	}
	
}
