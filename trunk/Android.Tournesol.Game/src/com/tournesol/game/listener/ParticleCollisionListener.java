package com.tournesol.game.listener;

import android.graphics.PointF;

import com.tournesol.drawing.Drawing;
import com.tournesol.game.GameMath;
import com.tournesol.game.listener.ICollisionListener;
import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.unit.Particles;
import com.tournesol.game.unit.Unit;

public class ParticleCollisionListener implements ICollisionListener{
	
	private static final long serialVersionUID = 4091527464559630570L;
	
	//Couche où la particule sera ajoutée
	public int layer;
	
	//Couleur de la particule
	public int color;
	
	//Dessin de la particule
	public Drawing drawing;
	
	public float min_vector_distance = 1f;
	
	@Override
	public void collision(Unit unit1, Unit unit2, PointF collisionPoint) {
		
		if(!(unit1 instanceof MovingUnit))
			return;
		
		MovingUnit movingUnit = (MovingUnit)unit1;

		float distance = GameMath.distance(movingUnit.vector_x, movingUnit.vector_y);
		if(distance > min_vector_distance){
			Particles particle = Particles.cache.get();
			particle.color = color;
			particle.init(collisionPoint.x, collisionPoint.y, (int)distance, drawing);
			unit1.game.addUnit(particle, layer);
		}
	}

}
