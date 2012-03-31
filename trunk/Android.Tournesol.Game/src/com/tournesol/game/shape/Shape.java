package com.tournesol.game.shape;

import com.tournesol.game.unit.Unit;
import android.graphics.PointF;

public abstract class Shape {
	
	public transient Unit unit;
	public boolean doesDraw = true;
	public boolean doesCollide = true;
	
	public Shape(){

	}
	
	public Shape(Unit unit){
		this.unit = unit;
	}
	
	public abstract boolean contains(PointF p);
	public abstract void changeSizeRatio(float width, float height);
	public abstract void rotate(float degrees, float x, float y);
	
	private static final PointF p = new PointF();
	public boolean contains(float x, float y){
		p.x = x;
		p.y = y;
		return contains(p);
	}
	
	public static PointF collide(Shape shape1, Shape shape2)
	{
		if(shape1 instanceof ShapeLine){
			
			if(shape2 instanceof ShapeLine)
				return CollisionLineLine.collide((ShapeLine)shape1, (ShapeLine)shape2);
			
			if(shape2 instanceof ShapeArc)
				return CollisionLineArc.collide((ShapeLine)shape1, (ShapeArc)shape2);
			
			if(shape2 instanceof ShapeRectangle)
				return CollisionRectangleLine.collide((ShapeRectangle)shape2, (ShapeLine)shape1);
			
			if(shape2 instanceof ShapeCircle)
				return CollisionLineCircle.collide((ShapeLine)shape1, (ShapeCircle)shape2);
		}
		
		if(shape1 instanceof ShapeCircle){
			
			if(shape2 instanceof ShapeLine)
				return CollisionLineCircle.collide((ShapeLine)shape2, (ShapeCircle)shape1);
			
			if(shape2 instanceof ShapeCircle)
				return CollisionCircleCircle.collide((ShapeCircle)shape1, (ShapeCircle)shape2);
			
			if(shape2 instanceof ShapeArc)
				return CollisionArcCircle.collide((ShapeArc)shape2, (ShapeCircle)shape1);
			
			if(shape2 instanceof ShapeRectangle)
				return CollisionRectangleCircle.collide((ShapeRectangle)shape2, (ShapeCircle)shape1);
		}
		
		if(shape1 instanceof ShapeArc){
			
			if(shape2 instanceof ShapeLine)
				return CollisionLineArc.collide((ShapeLine)shape2, (ShapeArc)shape1);
			
			if(shape2 instanceof ShapeArc)
				return CollisionArcArc.collide((ShapeArc)shape1, (ShapeArc)shape2);
			
			if(shape2 instanceof ShapeRectangle)
				return CollisionRectangleArc.collide((ShapeRectangle)shape2, (ShapeArc)shape1);
			
			if(shape2 instanceof ShapeCircle)
				return CollisionArcCircle.collide((ShapeArc)shape1, (ShapeCircle)shape2);
		}
		
		if(shape1 instanceof ShapeRectangle){
			
			if(shape2 instanceof ShapeLine)
				return CollisionRectangleLine.collide((ShapeRectangle)shape1, (ShapeLine)shape2);
			
			if(shape2 instanceof ShapeArc)
				return CollisionRectangleArc.collide((ShapeRectangle)shape1, (ShapeArc)shape2);
			
			if(shape2 instanceof ShapeRectangle)
				return CollisionRectangleRectangle.collide((ShapeRectangle)shape1, (ShapeRectangle)shape2);
			
			if(shape2 instanceof ShapeCircle)
				return CollisionRectangleCircle.collide((ShapeRectangle)shape1, (ShapeCircle)shape2);
		}
		
		return null;
	}
	

	public static void response(Shape shape_unit, Shape shape_other, PointF collisionPoint, CollisionUnit unit){
		
		if(shape_unit instanceof ShapeCircle){
			
			if(shape_other instanceof ShapeCircle){
				CollisionCircleCircle.response((ShapeCircle)shape_unit, (ShapeCircle)shape_other, unit);
			}
			
			if(shape_other instanceof ShapeArc){
				CollisionCircleCircle.response((ShapeCircle)shape_unit, (ShapeArc)shape_other, unit);
			}
			
			if(shape_other instanceof ShapeLine){
				CollisionLineCircle.response((ShapeCircle)shape_unit, (ShapeLine)shape_other, collisionPoint, unit);
			}
		}
		
	}
	
}
