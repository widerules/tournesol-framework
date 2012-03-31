package com.tournesol.game.shape;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;
import android.graphics.RectF;

public class CollisionLineCircle {

	public static PointF collide(ShapeLine line, ShapeCircle circle){
		
		float A_x = line.start.x + line.unit.x;
		float A_y = line.start.y + line.unit.y;
		float B_x = line.end.x + line.unit.x;
		float B_y = line.end.y + line.unit.y;
		float C_x = circle.x + circle.unit.x;
		float C_y = circle.y + circle.unit.y;
		
		float AC_x = C_x;
		float AC_y = C_y;
		
	    AC_x -= A_x;
	    AC_y -= A_y;
	    
	    float AB_x = B_x;
	    float AB_y = B_y;
	    
	    AB_x -= A_x;
	    AB_y -= A_y;
	    
	    float ab2 = AB_x * AB_x + AB_y * AB_y;
	    float acab = AC_x * AB_x + AC_y * AB_y;
	    float t = acab / ab2;

	    if (t < 0.0) 
	    	t = 0.0f;
	    else if (t > 1.0) 
	    	t = 1.0f;

	    //P = A + t * AB;
	    float P_x = AB_x * t + A_x;
	    float P_y = AB_y * t + A_y;
	    
	    float H_x = P_x - C_x;
	    float H_y = P_y - C_y;
	    
	    float h2 = H_x * H_x + H_y * H_y;
	    float r2 = (circle.width / 2) * (circle.width / 2);

	    if(h2 > r2) 
	    	return null;
	    else{
	    	RecycleBin.collisionPointF.x = P_x;
	    	RecycleBin.collisionPointF.y = P_y;
	    	return RecycleBin.collisionPointF;
	    }
	}
	
	
	public static void response(ShapeCircle circle_unit, ShapeLine line_other, PointF collisionPoint, CollisionUnit collision_unit){

		if(!(circle_unit.unit instanceof MovingUnit))
			return;
		
		MovingUnit unit = (MovingUnit)circle_unit.unit;

		float unit_x = circle_unit.x + circle_unit.unit.x;
		float unit_y = circle_unit.y + circle_unit.unit.y;
		float vector_degrees = GameMath.degrees(unit.vector_x, unit.vector_y);
		
		PointF line_direction = GameMath.direction(line_other.end.x - line_other.start.x, line_other.end.y - line_other.start.y);
		float tangent_x = line_direction.x;
		float tangent_y = line_direction.y;
		
		//Trouver la direction du bon vecteur de repoussement
		float side_1_x = collisionPoint.x -tangent_y;
		float side_1_y = collisionPoint.y + tangent_x;
		float side_2_x = collisionPoint.x + tangent_y;
		float side_2_y = collisionPoint.y - tangent_x;
		float distance_1 = GameMath.distance(unit_x - side_1_x, unit_y - side_1_y);
		float distance_2 = GameMath.distance(unit_x - side_2_x, unit_y - side_2_y);

		float normal_x;
		float normal_y;
		
		if(distance_1 < distance_2){
			normal_x = -tangent_y;
			normal_y = tangent_x;
		}
		else{
			normal_x = tangent_y;
			normal_y = -tangent_x;
		}

		float normal_degrees = GameMath.degrees(normal_x, normal_y);
		PointF push_direction = GameMath.direction(normal_degrees * 2 - vector_degrees + 180);
		float push_direction_x = push_direction.x;
		float push_direction_y = push_direction.y;
		
	    float vector_distance = GameMath.distance(unit.vector_x, unit.vector_y); 
	    float absorb = unit.mass / (collision_unit.mass + unit.mass);
	    push_direction_x *= vector_distance * absorb;
	    push_direction_y *= vector_distance * absorb;
		
	    unit.vector_x = push_direction_x;
	    unit.vector_y = push_direction_y;
		

		PointF newCollisionPoint = RecycleBin.collisionPointF;
		for(int i = 0; i < 5 && newCollisionPoint != null; i++){
			unit.x += push_direction_x * 0.5f;
			unit.y += push_direction_y * 0.5f;
			newCollisionPoint = Shape.collide(circle_unit, line_other);
		}
		
	}
	
}
