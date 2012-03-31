package com.tournesol.game.shape;

import android.graphics.PointF;

public class CollisionArcCircle {

	public static PointF collide(ShapeArc arc, ShapeCircle circle){
		return CollisionArcArc.collide(arc, circle);
	}
}
