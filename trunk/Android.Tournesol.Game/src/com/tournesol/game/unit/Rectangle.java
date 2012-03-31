package com.tournesol.game.unit;

import android.graphics.PointF;

import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.edge.EdgeVertex;
import com.tournesol.game.edge.PolygoneUnit;
import com.tournesol.game.utility.RecycleBin;

public class Rectangle extends PolygoneUnit {

	private static final long serialVersionUID = -2521410777046613050L;
	
	public Rectangle(){
		EdgeVertex top_left = this.addVertex(-width / 2, -height / 2);
		EdgeVertex top_right = this.addVertex(width / 2, -height / 2);
		EdgeVertex bottom_left = this.addVertex(-width / 2, height / 2);
		EdgeVertex bottom_right = this.addVertex(width / 2, height / 2);
		this.addPolygone(top_left, top_right, bottom_right, bottom_left);
	}
	
	@Override
	public boolean contains(PointF point){
		return containsRectangle(point);
	}
	
	protected boolean containsRectangle(PointF point){
		
		if(vertices.get(0).y - vertices.get(1).y >= 0.1f)
			return super.containsEdge(point);
		
		RecycleBin.collideRangeRectF.left = x + getLeft();
		RecycleBin.collideRangeRectF.top = y + getTop();
		RecycleBin.collideRangeRectF.right = x + getRight();
		RecycleBin.collideRangeRectF.bottom = y + getBottom();
		return RecycleBin.collideRangeRectF.contains(point.x, point.y);
	}
}
