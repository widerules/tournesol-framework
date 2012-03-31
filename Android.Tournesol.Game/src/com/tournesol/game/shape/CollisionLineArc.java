package com.tournesol.game.shape;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;
import android.util.FloatMath;

public class CollisionLineArc {
	
	public static PointF collide(ShapeLine l, ShapeArc arc){
		
		float dir_x = l.end.x - l.start.x;
		float dir_y = l.end.y - l.start.y;
		float origin_x = 0; 
		float origin_y = 0; 
		float center_x = l.unit.x + l.start.x - (arc.x + arc.unit.x); //l.getAbsoluteStart().x - arc.getAbsolutePoint().x; 
		float center_y = l.unit.y + l.start.y - (arc.y + arc.unit.y);//l.getAbsoluteStart().y - arc.getAbsolutePoint().y; 
		
		float rx = arc.width / 2;
		float ry = arc.height / 2;
		
		float diff_x = center_x - origin_x;
		float diff_y = center_y - origin_y;
		float mDir_x   = dir_x/(rx*rx);
		float mDir_y = dir_y/(ry*ry);
		float mDiff_x  = diff_x/(rx*rx);
		float mDiff_y  = diff_y/(ry*ry);

		float a = dir_x * mDir_x + dir_y * mDir_y;
		float b = dir_x * mDiff_x + dir_y * mDiff_y;
		float c = diff_x * mDiff_x + diff_y * mDiff_y - 1;
		float d = b*b - a*c;
		
		PointF collisionPoint = null;

		if ( d < 0 ) {
			
		} else if ( d > 0 ) {
			float root = FloatMath.sqrt(d);
			float t_a  = (-b - root) / a;
			float t_b  = (-b + root) / a;

			if ( (t_a < 0 || 1 < t_a) && (t_b < 0 || 1 < t_b) ) {

			} else {
				
				if ( 0 <= t_a && t_a <= 1 ){
					collisionPoint = lerp(arc, l, t_a);
					if(collisionPoint != null) return collisionPoint;
				}
				if ( 0 <= t_b && t_b <= 1 ){
					collisionPoint = lerp(arc, l, t_b);
					if(collisionPoint != null) return collisionPoint;
				}
			}
		} else {
			float t = -b/a;
			if ( 0 <= t && t <= 1 ) {
				return lerp(arc, l, t);
			} else {
			}
		}

		return null;
	}
	
	private static PointF lerp(ShapeArc arc, ShapeLine line, float t) {
		
		RecycleBin.collisionPointF.x = (line.end.x - line.start.x) * t;
		RecycleBin.collisionPointF.y = (line.end.y - line.start.y) * t;
		
		
		PointF p = arc.getAbsolutePoint();
		float arc_x = p.x;
		float arc_y = p.y;
		
		p = line.getAbsoluteStart();
		float line_x = p.x;
		float line_y = p.y;
		
		
		if(arc.containsCollisionAngle(RecycleBin.collisionPointF.x - (arc_x - line_x), 
									  RecycleBin.collisionPointF.y - (arc_y - line_y))){
			RecycleBin.collisionPointF.x += line_x;
			RecycleBin.collisionPointF.y += line_y;
			return RecycleBin.collisionPointF;
		}
		
	    return null;
	};
}
