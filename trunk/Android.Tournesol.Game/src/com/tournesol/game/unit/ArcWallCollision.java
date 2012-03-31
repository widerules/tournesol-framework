package com.tournesol.game.unit;

import android.graphics.PointF;
import com.tournesol.game.GameMath;
import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.ShapeArc;

public class ArcWallCollision {

	public static void response(ArcWall wall, MovingUnit unit, PointF collisionPoint) {
		
		ShapeArc arc = (ShapeArc)wall.shapes.get(0);
		
		float delta_x = unit.x - wall.x;
		float delta_y = unit.y - wall.y;
	    
		//Déterminer si la balle est dans l'ellipse 
	    float dist_x = delta_x / (arc.width / 2);
	    float dist_y = delta_y / (arc.height / 2);
	    float dist = dist_x * dist_x + dist_y * dist_y;
	  
	    float v_unit_x = unit.vector_x;
	    float v_unit_y = unit.vector_y;
	    float v_unit_distance = GameMath.distance(v_unit_x, v_unit_y);
	    float v_unit_degrees = GameMath.degrees(v_unit_x, v_unit_y);
	    float absorb = wall.absorb;
	    
	    float a = arc.width / 2;
    	float b = arc.height / 2;
    	float x0 = collisionPoint.x;
    	float y0 = collisionPoint.y;
    	
    	float normal_x = 0;
    	float normal_y = (a*a*(y0 - wall.y))/(b*b*(x0 - wall.x)) * (normal_x  - x0) + y0;
		PointF normal = GameMath.direction(x0 - normal_x, y0 - normal_y);
		normal_x = normal.x;
		normal_y = normal.y;
		float normal_degrees = GameMath.degrees(normal_x, normal_y);
		
		//Vérifier s'il faut corriger la normale
		float distance_origine = GameMath.distance(wall.x + normal_x - unit.x, wall.y + normal_y - unit.y);
		float distance_inverse = GameMath.distance(wall.x - normal_x - unit.x, wall.y - normal_y - unit.y);
		if(distance_inverse < distance_origine){
			normal_x = -normal.x;
			normal_y = -normal.y;
			normal_degrees = (normal_degrees + 180) % 360;
		}
		
		float tangent_x = 0;
		float tangent_y = (b*b)/(y0-wall.y)*(1-(tangent_x-wall.x)*(x0-wall.x)/(a*a)) + wall.y;
    	
    	PointF tangent_direction = GameMath.direction(x0 - tangent_x, y0 - tangent_y);
		tangent_x = tangent_direction.x;
		tangent_y = tangent_direction.y;
		float tangent_degrees = GameMath.degrees(tangent_x, tangent_y);
	    
		float d = GameMath.distance(0, 0, tangent_x, tangent_y, unit.x - x0, unit.y - y0);

	    if(dist > 1){
			PointF push_direction = GameMath.direction(normal_degrees * 2 - v_unit_degrees + 180);
			float push_direction_x = push_direction.x;
			float push_direction_y = push_direction.y;

			unit.x += normal_x * Math.abs(unit.width / 2 - d);
			unit.y += normal_y * Math.abs(unit.width / 2 - d);
		    unit.vector_x = push_direction_x * v_unit_distance * absorb;
		    unit.vector_y = push_direction_y * v_unit_distance * absorb;
	    }
	    else{
			unit.x -= normal_x * Math.abs(unit.width / 2 - d);
			unit.y -= normal_y * Math.abs(unit.width / 2 - d);
			
		    float n_degrees = normal_degrees;
		    float v_degrees = v_unit_degrees;
		    
			if(Math.abs(n_degrees - v_degrees) > 180){
				n_degrees = (n_degrees + 180) % 360;
				v_degrees = (v_degrees + 180) % 360;
				normal_x = -normal_x;
				normal_y = -normal_y;
			}
			
			if(Math.abs(n_degrees - v_degrees) > 90){
				n_degrees = (n_degrees + 180) % 360;
	    		normal_x = -normal_x;
				normal_y = -normal_y;
			}
			
	    	//Vérifier la différence d'angle entre le mur et le vecteur pour déterminer si on glisse l'objet
	    	if(Math.abs(n_degrees - v_degrees) < 52.5f)
	    	{
				PointF push_direction = GameMath.direction(normal_degrees * 2 - v_unit_degrees + 180);
				float push_direction_x = push_direction.x;
				float push_direction_y = push_direction.y;
			    unit.vector_x = push_direction_x * v_unit_distance * absorb;
			    unit.vector_y = push_direction_y * v_unit_distance * absorb;
	    	}
	    	else
	    	{
				if(Math.abs(tangent_degrees - v_unit_degrees) > 180){
					tangent_degrees = (tangent_degrees + 180) % 360;
					v_unit_degrees = (v_unit_degrees + 180) % 360;
				}
					
				if(Math.abs(tangent_degrees - v_unit_degrees) > 90){
					tangent_x = -tangent_x;
					tangent_y = -tangent_y;
				}
				
				tangent_x *= v_unit_distance;
				tangent_y *= v_unit_distance;
				 
			    unit.vector_x = tangent_x;
			    unit.vector_y = tangent_y;
	    	}
	    }
	    	
	    
    	float distance_result = GameMath.distance(unit.vector_x, unit.vector_y);
    	
    	if(Math.abs(distance_result) > 10){
    		PointF direction = GameMath.direction(unit.vector_x, unit.vector_y);
    		unit.vector_x = direction.x * 10;
    		unit.vector_y = direction.y * 10;	
    	}
    	
	}
}
