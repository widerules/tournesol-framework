package com.tournesol.game.unit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import com.tournesol.game.Game;
import com.tournesol.game.World;
import com.tournesol.game.utility.RecycleBin;

public class Wallpaper extends Unit {

	private static final long serialVersionUID = 1400698611456098729L;
	public final PaintDrawable paint;
	
	public static int COLOR = Color.rgb(0, 102, 250);
	public static int COLOR2 = Color.rgb(0, 200, 255);

	private Bitmap bmp;
	
	public Wallpaper(){
		this.doesCollide = false;
		this.doesTick = true;
		this.followFocus = false;
		this.scaling = false;
		
		ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
		    @Override
		    public Shader resize(int width, int height) {
		    	
		    	float x = width / 2;
		    	float y = height / 2;
		    	RadialGradient dg = new RadialGradient(x,  y, height * 0.7f, COLOR, COLOR2, Shader.TileMode.CLAMP);
		        return dg;
		    }
		};


		paint = new PaintDrawable();
		
		if(COLOR != COLOR2){
			paint.setShape(new RectShape());
			paint.setShaderFactory(sf);
		}
	}
	
	@Override
	public void setGame(Game game){
		super.setGame(game);
		this.x = 0;
		this.y = 0;
		int bmp_width = 75; 
		int bmp_height = (int)(bmp_width * this.width / this.height); 
		
		if(game.world.orientation == World.ORIENTATION_PORTRAIT)
		{
			this.width = game.world.focus_width;
			this.height = game.world.focus_height;
		}
		else
		{
			this.height = game.world.focus_width;
			this.width = game.world.focus_height;
			int buffer = bmp_width;
			bmp_width = bmp_height;
			bmp_height = buffer;
		}
		

		paint.setBounds(0, 0, bmp_width, bmp_height);
		
		
		if(COLOR != COLOR2){
			bmp = Bitmap.createBitmap(bmp_width, bmp_height, Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			paint.draw(canvas);
		}
	}
	
	@Override
	public boolean canCollide(Unit unit) {
		return false;
	}

	@Override
	public void draw(Canvas c) {
		if(bmp != null){
			RecycleBin.drawRectF.top = 0;
			RecycleBin.drawRectF.left = 0;
			RecycleBin.drawRectF.bottom = height;
			RecycleBin.drawRectF.right = width;
			
			RecycleBin.drawRect.top = 0;
			RecycleBin.drawRect.left = 0;
			RecycleBin.drawRect.bottom = bmp.getHeight();
			RecycleBin.drawRect.right = bmp.getWidth();
			
			c.drawBitmap(bmp, RecycleBin.drawRect, RecycleBin.drawRectF, getPaint());
		}
		else
			c.drawColor(COLOR);
			//c.drawBitmap(bmp, 0, 0, getPaint());
	}
}
