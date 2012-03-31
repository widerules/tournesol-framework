package com.tournesol.game.listener;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;

public class AnimationUnitTickListener implements IUnitTickListener {

	private static final long serialVersionUID = 4029586138018728054L;
	public static UnitTickListenerHelper<AnimationUnitTickListener> helper = new UnitTickListenerHelper<AnimationUnitTickListener>(AnimationUnitTickListener.class);
	
	public float tick = 0;
	public float tick_lasting = 1000;
	
	//Détermine si on utilise la position de rotation comme relative(false) ou absolue(true)
	public boolean rotate_position_absolute = true;
	
	//Détermine s'il faut appliquer la même position de rotation à la prochaîne animation
	public boolean apply_same_rotate_position = false;
	
	public float rotate_x = Float.MIN_VALUE;
	public float rotate_y = Float.MIN_VALUE;
	
	public float degrees = Float.MIN_VALUE;
	
	public float vector_x = 0;
	public float vector_y = 0;
	
	public boolean animated = true;
	

	public AnimationUnitTickListener nextAnimation;
	
	@Override
	public void tick(Unit unit) {
	
		if(!animated)
			return;
		
		if(tick_lasting >= 0){
			tick++;
			if(tick > tick_lasting){
				unit.tick_listeners.remove(this);
				if(nextAnimation != null){
					
					if(apply_same_rotate_position){
						nextAnimation.rotate_x = rotate_x;
						nextAnimation.rotate_y = rotate_y;
					}
						
					
					nextAnimation.tick = 0;
					unit.tick_listeners.add(nextAnimation);
				}
				return;
			}
		}

		if(degrees != Float.MIN_VALUE){
			float rotate_x;
			float rotate_y;
			
			if(this.rotate_x != Float.MIN_VALUE)
				rotate_x = this.rotate_x;
			else
				rotate_x = unit.x;
			
			if(this.rotate_y != Float.MIN_VALUE)
				rotate_y = this.rotate_y;
			else
				rotate_y = unit.y;

			if(!rotate_position_absolute){
				rotate_x += unit.x;
				rotate_y += unit.y;
			}
			
			float old_x = unit.x;
			float old_y = unit.y;
			unit.rotate(degrees, rotate_x, rotate_y);
			
			if(!rotate_position_absolute){
				this.rotate_x += old_x - unit.x;
				this.rotate_y += old_y - unit.y;
			}
		}
		
		unit.x += vector_x;
		unit.y += vector_y;
		
	}

	public static void pulse(Unit[] units, float center_x, float center_y, float factor, float tick_lasting, boolean animated, boolean useOriginalPosition){

		for(int i = 0; i < units.length; i++){
			
			Unit unit = units[i];
			if(unit == null)
				continue;
			
			float unit_x;
			float unit_y;
			
			if(useOriginalPosition){
				unit_x = ((ShowHideUnitTickListener)unit.tick_listeners.get(0)).original_x;
				unit_y = ((ShowHideUnitTickListener)unit.tick_listeners.get(0)).original_y;
			}
			else{
				unit_x = unit.x;
				unit_y = unit.y;
			}
			
			float distance = GameMath.distance(unit_x - center_x, unit_y - center_y);
			float tick_distance = distance * factor / (tick_lasting / 2);
			float degrees = GameMath.degrees(unit_x - center_x, unit_y - center_y);
			
			AnimationUnitTickListener animExpand = new AnimationUnitTickListener();
			animExpand.animated = animated;
			animExpand.tick_lasting = tick_lasting / 2;
			animExpand.vector_x = tick_distance * GameMath.cos(degrees);
			animExpand.vector_y = tick_distance * GameMath.sin(degrees);
			
			AnimationUnitTickListener animRetract = new AnimationUnitTickListener();
			animRetract.tick_lasting = tick_lasting / 2;
			animRetract.vector_x = -animExpand.vector_x;
			animRetract.vector_y = -animExpand.vector_y;
			
			animExpand.nextAnimation = animRetract;
			animRetract.nextAnimation = animExpand;
			
			unit.tick_listeners.add(animExpand);
		}
		
	}
}
