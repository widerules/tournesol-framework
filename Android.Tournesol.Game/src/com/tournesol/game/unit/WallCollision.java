package com.tournesol.game.unit;

import com.tournesol.game.GameMath;
import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;
import android.util.FloatMath;

public class WallCollision {
	
	public static void response(Wall wall, MovingUnit unit, PointF collisionPoint){
		
		ShapeLine line = (ShapeLine)wall.shapes.get(0);
		float vector_degrees = GameMath.degrees(unit.vector_x, unit.vector_y);
		
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
	    float absorb = wall.absorb;
	    if(vector_distance < 2)
	    	absorb = 1;
	    
	    push_direction_x *= vector_distance * absorb;
	    push_direction_y *= vector_distance * absorb;
		
	    //Distance entre l'unité et le mur
	    float d = GameMath.distance(line.start, line.end, unit.x - wall.x, unit.y - wall.y);
	    
	    //Marche que pour les balles
	    unit.x += normal_x * (unit.width / 2 - d);
		unit.y += normal_y * (unit.width / 2 - d);
	    unit.vector_x = push_direction_x;
	    unit.vector_y = push_direction_y;
	}
}
