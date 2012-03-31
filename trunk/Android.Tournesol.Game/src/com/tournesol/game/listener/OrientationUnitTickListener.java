package com.tournesol.game.listener;

import android.graphics.PointF;

import com.tournesol.game.Game;
import com.tournesol.game.GameMath;
import com.tournesol.game.World;
import com.tournesol.game.unit.Unit;

public class OrientationUnitTickListener extends ShowHideUnitTickListener{


	private static final long serialVersionUID = 2055494145778763360L;
	public static UnitTickListenerHelper<OrientationUnitTickListener> helper = new UnitTickListenerHelper<OrientationUnitTickListener>(OrientationUnitTickListener.class);
	
	public float original_degrees;
	public float goal_degrees;
	public float original_landscape_x;
	public float original_landscape_y;
	
	public OrientationUnitTickListener(Unit unit, Game game, float original_landscape_x, float original_landscape_y) {
		super(unit, game);
		this.original_degrees = unit.degrees;
		this.goal_degrees = game.world.orientation;
		this.original_landscape_x = original_landscape_x;
		this.original_landscape_y = original_landscape_y;
	}
	
	public void init(Unit unit, Game game, float original_landscape_x, float original_landscape_y){
		this.init(unit, game);
		this.original_degrees = unit.degrees;
		this.goal_degrees = game.world.orientation;
		this.original_landscape_x = original_landscape_x;
		this.original_landscape_y = original_landscape_y;
	}
	
	@Override
	public void show(Unit unit){
		
		super.show(unit);
		
		if(goal_degrees == World.ORIENTATION_PORTRAIT || 
		   goal_degrees == World.ORIENTATION_REVERSE_PORTRAIT){
			goal_x = original_x;
			goal_y = original_y;
		}
		
		if(goal_degrees == World.ORIENTATION_LANDSCAPE || 
		   goal_degrees == World.ORIENTATION_REVERSE_LANDSCAPE){
			goal_x = original_landscape_x;
			goal_y = original_landscape_y;
		}
		
		if(goal_degrees == World.ORIENTATION_REVERSE_PORTRAIT || 
		   goal_degrees == World.ORIENTATION_REVERSE_LANDSCAPE){
			PointF point = GameMath.rotate(180, goal_x - unit.game.world.focus_width / 2, goal_y - unit.game.world.focus_height / 2);
			goal_x = point.x + unit.game.world.focus_width / 2;
			goal_y = point.y + unit.game.world.focus_height / 2;
		}
	}
	
	@Override
	public void tick(Unit unit) {
		
		super.tick(unit);
		int world_orientation = unit.game.world.orientation;
		if(goal_degrees != world_orientation){
			goal_degrees = world_orientation;
			if(!hide)
				this.show(unit);
		}

		unit.rotate((90 - goal_degrees - unit.degrees + original_degrees));
	}
	
	@Override
	protected void changeAlpha(Unit unit){
		
		if((!change_alpha_on_show && !hide) || (!change_alpha_on_hide && hide)){
			unit.alpha = 255;
			return;
		}
		
		if(unit.game.world.orientation == World.ORIENTATION_PORTRAIT)
			super.changeAlpha(unit);
		else
		{
			float distance = GameMath.distance(unit.x - original_landscape_x, unit.y - original_landscape_y);
			if(distance > max_distance_alpha)
				unit.alpha = 0;
			else
				unit.alpha = (int)(255 * (max_distance_alpha - distance) / max_distance_alpha);
		}
	}
}
