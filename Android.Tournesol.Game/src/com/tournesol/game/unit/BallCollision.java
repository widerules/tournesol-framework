package com.tournesol.game.unit;

import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.ShapeCircle;

public class BallCollision {

	public static void response(MovingUnit ball, ShapeCircle other_circle, CollisionUnit collisionUnit, PointF collisionPoint) {
		
		ShapeCircle circle_unit = (ShapeCircle)ball.shapes.get(0);

		float delta_x = ball.x - collisionUnit.x;
		float delta_y = ball.y - collisionUnit.y;

		float d = GameMath.distance(delta_x, delta_y);

		float multiply = ((circle_unit.width + other_circle.width) / 2 - d) / d;
		float mtd_x = delta_x * multiply;
		float mtd_y = delta_y * multiply;
		
	    float im1 = 1 / circle_unit.unit.mass; 
	    float im2 = 1 / collisionUnit.mass;
	    
	    float v_unit_x = 0;
	    float v_unit_y = 0;
	    float v_other_x = 0;
	    float v_other_y = 0;
	    
    	v_unit_x = ball.vector_x;
    	v_unit_y = ball.vector_y;
	    
	    // push-pull them apart based off their size
    	ball.x += mtd_x * multiply;
    	ball.y += mtd_y * multiply;

    	v_other_x = collisionUnit.vector_x;
    	v_other_y = collisionUnit.vector_y;
	    
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

    	ball.vector_x += impulse_x * im1;
    	ball.vector_y += impulse_y * im1;	
    	
    	float distance_result = GameMath.distance(ball.vector_x, ball.vector_y);
    	
    	if(Math.abs(distance_result) > 10){
        	ball.vector_x = 5 / distance_result;
        	ball.vector_y = 5 / distance_result;	
    	}
    		
	}
}
