package com.tournesol.game.shape;

import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.unit.Unit;

public class CollisionUnit {

	public float x;
	public float y;
	public float degrees;
	public float mass;
	public float vector_x;
	public float vector_y;
	public float acceleration_x;
	public float acceleration_y;
	
	public void init(Unit unit){
		x = unit.x;
		y = unit.y;
		degrees = unit.degrees;
		mass = unit.mass;
		if(unit instanceof MovingUnit){
			MovingUnit movingUnit = (MovingUnit)unit;
			vector_x = movingUnit.vector_x;
			vector_y = movingUnit.vector_y;
			acceleration_x = movingUnit.acceleration_x;
			acceleration_y = movingUnit.acceleration_y;
		}
		else{
			vector_x = 0;
			vector_y = 0;
			acceleration_x = 0;
			acceleration_y = 0;
		}
	}
}
