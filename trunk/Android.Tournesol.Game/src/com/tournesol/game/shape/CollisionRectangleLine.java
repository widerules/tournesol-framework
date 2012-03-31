package com.tournesol.game.shape;

import android.graphics.PointF;

public class CollisionRectangleLine {

	public static PointF collide(ShapeRectangle rect, ShapeLine line){
		
		ShapeLine[] lines = rect.getLines();
		PointF point = null;
		
		for(int i = 0 ; i < lines.length; i++){
			point = CollisionLineLine.collide(lines[i], line);
			if(point != null)
				return point;
		}
		
		return point;
	}
	
}
