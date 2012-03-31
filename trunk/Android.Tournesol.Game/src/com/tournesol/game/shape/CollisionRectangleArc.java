package com.tournesol.game.shape;

import android.graphics.PointF;

public class CollisionRectangleArc {

	public static PointF collide(ShapeRectangle rect, ShapeArc arc){
		
		ShapeLine[] lines = rect.getLines();
		PointF point = null;
		
		for(int i = 0 ; i < lines.length; i++){
			point = CollisionLineArc.collide(lines[i], arc);
			if(point != null)
				return point;
		}
		
		return point;
	}
}
