package com.tournesol.game.shape;

import android.graphics.PointF;

public class CollisionRectangleRectangle {

	public static PointF collide(ShapeRectangle rect1, ShapeRectangle rect2){
		
		ShapeLine[] lines1 = rect1.getLines();
		ShapeLine[] lines2 = rect2.getLines();
		PointF point = null;
		
		for(int i = 0 ; i < lines1.length; i++){
			for(int j = 0 ; j < lines2.length; j++){
				point = CollisionLineLine.collide(lines1[i], lines2[j]);
				if(point != null)
					return point;
			}
		}
		
		return point;
	}
	
}
