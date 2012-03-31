package com.tournesol.game.unit;

import android.graphics.PointF;
import android.graphics.RectF;

import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.shape.ShapeArc;
import com.tournesol.game.utility.RecycleBin;

public class ArcWall extends EdgeUnit {

	private static final long serialVersionUID = -3080244077517479851L;

	public float absorb = 0.9f;

	public ArcWall(float x, float y, float width, float height, float startAngle, float sweepAngle){
		
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		
		ShapeArc arc = new ShapeArc(this);
		arc.x = 0;
		arc.y = 0;
		arc.height = height;
		arc.width = width;
		arc.startAngle = startAngle;
		arc.sweepAngle = sweepAngle;
		this.shapes.add(arc);
	}
	
	@Override
	public boolean contains(PointF point){
		return getCollideRange().contains(point.x, point.y);
	}
	
	@Override
	public RectF getCollideRange()
	{
		RecycleBin.collideRangeRectF.left = x - width / 2;
		RecycleBin.collideRangeRectF.top = y - height / 2;
		RecycleBin.collideRangeRectF.right = x + width / 2;
		RecycleBin.collideRangeRectF.bottom = y + height / 2;
		return RecycleBin.collideRangeRectF;
	}
}
