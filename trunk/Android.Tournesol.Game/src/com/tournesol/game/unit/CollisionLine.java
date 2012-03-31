package com.tournesol.game.unit;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import com.tournesol.game.edge.Edge;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;

public class CollisionLine extends EdgeUnit{

	private static final long serialVersionUID = -2579724875312439338L;
	
	public final ArrayList<PointF> collisionPoints;
	public final ArrayList<Unit> collisionUnits;
	
	public CollisionLine(){
		
		this.doesDraw = false;
		this.shouldDraw = false;
		this.doesTick = false;
		this.shouldTick = false;
		this.collisionPoints = new ArrayList<PointF>();
		this.collisionUnits = new ArrayList<Unit>();
		addEdges(addVertex(0, 0), addVertex(width, height));
	}
	
	@Override
	public void init(float x, float y, float width, float height){

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		Edge edge = (Edge)this.shapes.get(0);
		edge.start.x = 0;
		edge.start.y = 0;
		edge.end.x = width;
		edge.end.y = height;
	}

	@Override
	public boolean canCollide(Unit unit) {
		return true;
	}
	
	@Override
	public void collision(CollisionUnit unit, PointF collisionPoint, Shape mine, Shape other){
		
		collisionPoints.add(new PointF(collisionPoint.x, collisionPoint.y));
		collisionUnits.add(other.unit);
		
	}
	
	@Override
	public RectF getCollideRange()
	{
		float left;
		float right;
		
		if(width > 0){
			left = x ; 
			right = x + width;
		}
		else{
			left = x + width; 
			right = x;
		}
		
		float top;
		float bottom;
		
		if(height > 0){
			top = y; 
			bottom = y + height;
		}
		else{
			top = y + height; 
			bottom = y;
		}
		
		RecycleBin.collideRangeRectF.left = left;
		RecycleBin.collideRangeRectF.top = top;
		RecycleBin.collideRangeRectF.right = right;
		RecycleBin.collideRangeRectF.bottom = bottom;

		return RecycleBin.collideRangeRectF;
	}
}
