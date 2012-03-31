package com.tournesol.game.unit;

import android.graphics.RectF;

import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.utility.RecycleBin;

public class Wall extends EdgeUnit{

	private static final long serialVersionUID = 7210493991636274171L;
	public float absorb = 0.5f;

	public Wall(float x1, float y1, float x2, float y2)
	{
		this.x = x1;
		this.y = y1;
		this.width = x2 - x1;
		this.height = y2 - y1;
		addEdges(addVertex(0, 0), addVertex(x2 - x1, y2 - y1));
	}
	
	@Override
	public RectF getCollideRange()
	{
		/*
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
		*/
		RecycleBin.collideRangeRectF.left = x + getLeft();
		RecycleBin.collideRangeRectF.top = y + getTop();
		RecycleBin.collideRangeRectF.right = x + getRight();
		RecycleBin.collideRangeRectF.bottom = y + getBottom();
		return RecycleBin.collideRangeRectF;
	}
}
