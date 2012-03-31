package com.tournesol.game.shape;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;

public class CollisionCircleCircle {
	
	public static PointF collide(ShapeCircle circle1, ShapeCircle circle2){
		final float a = (circle1.width + circle2.width) /2;
	    final float dx = circle1.unit.x - circle2.unit.x;
	    final float dy = circle1.unit.y - circle2.unit.y;
	    
	    if(a * a > (dx * dx + dy * dy)){
	    	RecycleBin.collisionPointF.x = circle1.unit.x + dx * circle1.width / circle2.width;
	    	RecycleBin.collisionPointF.y = circle1.unit.y + dy * circle1.width / circle2.width;
    		return RecycleBin.collisionPointF;
	    }
	    else
	    	return null;
	}
	
	public static void response(ShapeCircle circle_unit, ShapeArc shape_other, CollisionUnit collision_unit){
		
		//float delta_x = circle_unit.unit.x - collision_unit.x;
		//float delta_y = circle_unit.unit.y - collision_unit.y;
		float delta_x = circle_unit.unit.x - collision_unit.x;
		float delta_y = circle_unit.unit.y - collision_unit.y;
		float degrees = GameMath.degrees(delta_x, delta_y);
		
		float d = GameMath.distance(delta_x, delta_y);
		
	    float arc_distance_x = shape_other.width / 2 * GameMath.cos(degrees);
	    float arc_distance_y = shape_other.height / 2 * GameMath.sin(degrees);
	    float arc_distance = GameMath.distance(arc_distance_x, arc_distance_y);
		
		float multiply = (circle_unit.width / 2 + arc_distance - d) / d;
		float mtd_x = delta_x * multiply;
		float mtd_y = delta_y * multiply;
		
	    float im1 = 1 / circle_unit.unit.mass; 
	    float im2 = 1 / collision_unit.mass;
	    
	    float v_unit_x = 0;
	    float v_unit_y = 0;
	    float v_other_x = 0;
	    float v_other_y = 0;
	    
	    if(circle_unit.unit instanceof MovingUnit){
	    	v_unit_x = ((MovingUnit)circle_unit.unit).vector_x;
	    	v_unit_y = ((MovingUnit)circle_unit.unit).vector_y;
	    }

	    float v_unit_distance = GameMath.distance(v_unit_x, v_unit_y);

	    // push-pull them apart based off their mass
	    
	    if(d < arc_distance){
		    circle_unit.unit.x -= mtd_x * multiply * v_unit_distance * 0.05f;
		    circle_unit.unit.y -= mtd_y * multiply * v_unit_distance * 0.05f;
		    
		    mtd_x = -mtd_x;
		    mtd_y = -mtd_y;
	    }
	    else{
	    	circle_unit.unit.x += mtd_x * multiply;
		    circle_unit.unit.y += mtd_y * multiply;
	    }
	    	
	    
    	v_other_x = collision_unit.vector_x;
    	v_other_y = collision_unit.vector_y;
	    
	    
	    float v_x = v_unit_x - v_other_x;
	    float v_y = v_unit_y - v_other_y;
	    
	    PointF normal = GameMath.direction(mtd_x, mtd_y);
	    float normal_x = normal.x;
	    float normal_y = normal.y;
	    
	    float vn = v_x * normal_x + v_y * normal_y;

	    // sphere intersecting but moving away from each other already
	    if (vn > 0.0f) return;

	    float constants_restitution = -0.5f;
	    
	    // collision impulse
	    float i = (-(1.0f + constants_restitution) * vn) / (im1 + im2);
	    
	    float impulse_x = mtd_x * i;
	    float impulse_y = mtd_y * i;

	    if(circle_unit.unit instanceof MovingUnit){
	    	((MovingUnit)circle_unit.unit).vector_x += impulse_x * im1;
	    	((MovingUnit)circle_unit.unit).vector_y += impulse_y * im1;;
	    }
	}
}
