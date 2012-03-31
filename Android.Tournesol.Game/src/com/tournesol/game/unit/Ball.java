package com.tournesol.game.unit;


import android.graphics.RectF;
import android.graphics.PointF;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.shape.ShapeCircle;
import com.tournesol.game.utility.RecycleBin;

public class Ball extends EdgeUnit{

	private static final long serialVersionUID = 8958762810900193935L;
	
	public Ball(){
		
		ShapeCircle circle = new ShapeCircle(this);
		circle.width = width;
		circle.height = height;
		this.shapes.add(circle);
		this.doesMove = false;
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
