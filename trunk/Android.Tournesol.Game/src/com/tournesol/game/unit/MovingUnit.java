package com.tournesol.game.unit;

import com.tournesol.drawing.Drawing;

public abstract class MovingUnit extends Unit implements IMovingUnit{

	public static final float DEFAULT_GRAVITY_DAMPER = 0.01f;
	
	public float vector_x = 0;
	public float vector_y = 0;
	public float acceleration_x = 0;
	public float acceleration_y = 0;
	
	public float gravity_damper = DEFAULT_GRAVITY_DAMPER;
	
	//Détermine si l'unité doit et peut être bougé
	public boolean doesMove = true;
	public boolean shouldMove = true;
	
	//Déterminer si l'unité doit changer de vitesse et de position lors de la collision avec un mur
	public boolean wall_rebound = true;

	@Override
	public void tick()
	{
		super.tick();
		this.move();
	}
	
	public void move()
	{
		if(this.doesMove && this.game != null){
			vector_x += acceleration_x + game.world.gravity_y * gravity_damper;
			vector_y += acceleration_y + game.world.gravity_x * gravity_damper;
			x += vector_x;
			y += vector_y;
		}
	}
	
	@Override
	public boolean doesMove() {
		return doesMove;
	}
	
	@Override
	public void copy(Drawing drawing){
		super.copy(drawing);
		if(!(drawing instanceof MovingUnit))
			return;
		
		MovingUnit copy = (MovingUnit)drawing;

		this.doesMove = copy.doesMove;
		this.shouldMove = copy.shouldMove;
	}
}
