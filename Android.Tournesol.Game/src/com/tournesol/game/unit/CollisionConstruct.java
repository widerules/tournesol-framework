package com.tournesol.game.unit;

import android.graphics.PointF;
import android.graphics.RectF;

import com.tournesol.game.GameMath;
import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.utility.RecycleBin;

public class CollisionConstruct {

	private static float push_x;
	private static float push_y;
	private static float vector_distance;
	private final static RectF r_current = new RectF();
	private final static RectF r_other = new RectF();
	
	public static void collision(ConstructUnit unit, Unit other, Shape unit_shape, Shape other_shape, PointF collisionPoint, float absorb) {

		//Vérifier si l'autre unité est à l'intérieur de l'unité courante, alors n'appliquer aucune collision
		RectF r = unit.getCollideRange();
		r_current.set(r);
		r_current.inset(-unit.width * 0.1f, -unit.height * 0.1f);
		
		r = other.getCollideRange();
		r_other.set(r);
		r_other.inset(other.width * 0.1f, other.height * 0.1f);
		
		if(r_current.contains(r_other))
			return;

		PointF vector_direction = GameMath.direction(unit.vector_x, unit.vector_y);
		float vector_degrees = GameMath.degrees(unit.vector_x, unit.vector_y);
		float vector_direction_x = vector_direction.x;
		float vector_direction_y = vector_direction.y;
	
		float normal_x;
		float normal_y;
		
		if(other_shape instanceof ShapeLine){
			ShapeLine line = (ShapeLine)other_shape;
			PointF line_direction = GameMath.direction(line.end.x - line.start.x, line.end.y - line.start.y);
			float tangent_x = line_direction.x;
			float tangent_y = line_direction.y;
			
			//Trouver la direction du bon vecteur de repoussement
			float side_1_x = collisionPoint.x -tangent_y;
			float side_1_y = collisionPoint.y + tangent_x;
			float side_2_x = collisionPoint.x + tangent_y;
			float side_2_y = collisionPoint.y - tangent_x;
			float distance_1 = GameMath.distance(unit.x - side_1_x, unit.y - side_1_y);
			float distance_2 = GameMath.distance(unit.x - side_2_x, unit.y - side_2_y);

			if(distance_1 < distance_2){
				normal_x = -tangent_y;
				normal_y = tangent_x;
			}
			else{
				normal_x = tangent_y;
				normal_y = -tangent_x;
			}
		}
		else{
			PointF contact_direction = GameMath.direction(unit.x - collisionPoint.x, unit.y - collisionPoint.y);
			normal_x = contact_direction.x;
			normal_y = contact_direction.y;
		}
		
		float normal_degrees = GameMath.degrees(normal_x, normal_y);
		PointF push_direction = GameMath.direction(normal_degrees * 2 - vector_degrees + 180);
		float push_direction_x = push_direction.x;
		float push_direction_y = push_direction.y;
		
	    float vector_distance = 0; 

		if((other instanceof MovingUnit) && 
			unit.vector_x == 0 &&
			unit.vector_y == 0)
			vector_distance = GameMath.distance(((MovingUnit)other).vector_x, 
											    ((MovingUnit)other).vector_y);
		else
			vector_distance = GameMath.distance(unit.vector_x, unit.vector_y); 
		
	    push_direction_x *= vector_distance * absorb;
	    push_direction_y *= vector_distance * absorb;
		
		unit.vector_x = push_direction_x;
		unit.vector_y = push_direction_y;
		
		PointF acceleration_direction = GameMath.direction(unit.acceleration_x, unit.acceleration_y);
		float acceleration_direction_x = acceleration_direction.x;
		float acceleration_direction_y = acceleration_direction.y;
		float acceleration_distance = GameMath.distance(acceleration_direction_x, acceleration_direction_y);
		
		unit.acceleration_x += push_direction_x * acceleration_distance;
		unit.acceleration_y += push_direction_y * acceleration_distance;
		
		
		PointF newCollisionPoint = RecycleBin.collisionPointF;
		for(int i = 0; i < 5 && newCollisionPoint != null; i++){
			unit.x += push_direction_x;//0.5f;
			unit.y += push_direction_y;//0.5f;
			newCollisionPoint = Shape.collide(unit_shape, other_shape);
		}
		
	}
}
