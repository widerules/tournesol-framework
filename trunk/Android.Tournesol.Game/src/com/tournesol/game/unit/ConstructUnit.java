package com.tournesol.game.unit;

import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.ShapeArc;
import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Paint.Style;

public class ConstructUnit extends MovingUnit {

	private static final long serialVersionUID = 7197760223934094565L;
	
	public float rotate;
	public float collision_absorbtion = 1f;
	
	public ConstructUnit(){
		color = PaintManager.foreColor;
		style = Style.STROKE;
		manage_rotation = true;
	}
	
	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
	}
	
	@Override
	public boolean canCollide(Unit unit) {
		return true;
	}
	
	@Override
	public RectF getCollideRange()
	{
		float size = width;
		if(size < height)
			size = height;
		
		RecycleBin.collideRangeRectF.left = x - size / 2f;
		RecycleBin.collideRangeRectF.top = y - size / 2f;
		RecycleBin.collideRangeRectF.right = x + size / 2f;
		RecycleBin.collideRangeRectF.bottom = y + size / 2f;
		return RecycleBin.collideRangeRectF;
	}
	
	@Override
	public boolean contains(PointF p){
		RectF r = super.getCollideRange();
		r.inset(-5f / game.world.scale, -5f / game.world.scale);
		return r.contains(p.x, p.y);
	}

	@Override
	public void tick() {
		super.tick();
		if(rotate != 0)
			this.rotate(rotate);
	}

	@Override
	public void draw(Canvas c) {
		drawConstruct(c);
		super.draw(c);
	}

	protected void drawConstruct(Canvas c) {
		Paint paint = getPaint();
		PointF focus = this.getFocusPosition();

		int count = this.shapes.size();
		for(int i = 0; i < count; i++){
			Shape shape = this.shapes.get(i);
			if(!shape.doesDraw)
				continue;
			
			if(shape instanceof ShapeLine){
				ShapeLine line = (ShapeLine)shape;
				c.drawLine(focus.x + line.start.x, 
						   focus.y + line.start.y,
						   focus.x + line.end.x, 
						   focus.y + line.end.y, paint);
			}
			
			if(shape instanceof ShapeArc){
				ShapeArc arc = (ShapeArc)shape;
				
				if(arc.sweepAngle < 360 || arc.width != arc.height){
					RecycleBin.drawRectF.left = focus.x - arc.width / 2 + arc.x;
					RecycleBin.drawRectF.top = focus.y - arc.height / 2 + arc.y;
					RecycleBin.drawRectF.right = focus.x + arc.width / 2 + arc.x;
					RecycleBin.drawRectF.bottom = focus.y + arc.height / 2 + arc.y;
					c.drawArc(RecycleBin.drawRectF, arc.startAngle, arc.sweepAngle, false, paint);
				}
				else{
					c.drawCircle(focus.x, focus.y, arc.width / 2, paint);
				}
				
			}
		}
	}
	
	@Override
	public void collision(CollisionUnit unit, PointF collisionPoint, Shape mine, Shape other)
	{
		super.collision(unit, collisionPoint, mine, other);
		/*
		if(doesMove)
			CollisionConstruct.collision(this, other.unit, mine, other, collisionPoint, collision_absorbtion);	
		*/
	}
	
	/**
	 * Centrer la position x et y au milieu de l'unité selon la largeur et la hauteur des shapes.
	 */
	public void centerFocus(){
		
		float min_x = Float.MAX_VALUE;
		float max_x = -Float.MAX_VALUE;
		
		float min_y = Float.MAX_VALUE;
		float max_y = -Float.MAX_VALUE;
		
		int shapes_size = shapes.size();
		for(int i = 0; i < shapes_size; i++){
			Shape shape = shapes.get(i);
			
			if(shape instanceof ShapeLine){
				ShapeLine line = (ShapeLine)shape;
				
				if(line.start.x < min_x)
					min_x = line.start.x;
				
				if(line.end.x < min_x)
					min_x = line.end.x;
				
				if(line.start.x > max_x)
					max_x = line.start.x;
				
				if(line.end.x > max_x)
					max_x = line.end.x;
				
				if(line.start.y < min_y)
					min_y = line.start.y;
				
				if(line.end.y < min_y)
					min_y = line.end.y;
				
				if(line.start.y > max_y)
					max_y = line.start.y;
				
				if(line.end.y > max_y)
					max_y = line.end.y;
			}
		}
		
		float translate_x = 0;
		float translate_y = 0;
		
		if(min_x != Float.MAX_VALUE && max_x != -Float.MAX_VALUE){
			translate_x = (min_x + max_x)/2;
			width = max_x - min_x;
		}
		
		if(min_y != Float.MAX_VALUE && max_y != -Float.MAX_VALUE){
			translate_y = (min_y + max_y)/2;
			height = max_y - min_y;
		}
		
		for(int i = 0; i < shapes_size; i++){
			Shape shape = shapes.get(i);
			
			if(shape instanceof ShapeLine){
				ShapeLine line = (ShapeLine)shape;
				line.start.x -= translate_x;
				line.start.y -= translate_y;
				line.end.x -= translate_x;
				line.end.y -= translate_y;
			}
		}
		
		x += translate_x;
		y += translate_y;
		
	}

}
