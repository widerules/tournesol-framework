package com.tournesol.game.listener;

import com.tournesol.game.Game;
import com.tournesol.game.GameMath;
import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.Randomizer;

public class ShowHideUnitTickListener implements IUnitTickListener{

	private static final long serialVersionUID = 7468521966651150314L;
	public static boolean DEFAULT_CHANGE_ALPHA = true;
	public static UnitTickListenerHelper<ShowHideUnitTickListener> helper = new UnitTickListenerHelper<ShowHideUnitTickListener>(ShowHideUnitTickListener.class);
	
	public float original_x;
	public float original_y;

	public float goal_x;
	public float goal_y;
	
	public float start_range_x = 0;
	public float start_hole_x = 0;
	public float end_hole_x = 0;
	public float end_range_x = 0;
	public float start_range_y = 0;
	public float start_hole_y = 0;
	public float end_hole_y = 0;
	public float end_range_y = 0;
	
	public boolean hide = false;
	
	//Forcer que l'unité se cache même si on veut le réafficher
	public boolean force_hide = false;
	
	//Action à faire quand l'objet est vraiment caché
	public boolean stop_tick = true;
	public boolean stop_draw = true;
	public boolean stop_alive = false;
	public boolean stop_move = true;
	
	//Détermine si l'unité est à son but
	public boolean at_goal = false;
	
	//Détermine que l'unité ne bouge plus
	public boolean cancel_move = false;
	public boolean does_cancel_move = true;
	
	public boolean change_alpha_on_show = DEFAULT_CHANGE_ALPHA;
	public boolean change_alpha_on_hide = DEFAULT_CHANGE_ALPHA;
	public float max_distance_alpha = 300;
	
	public float ratio_move_hide = 0.01f;
	public float ratio_move_show = 0.03f;

	public ShowHideUnitTickListener(Unit unit){
		this(unit, unit.game);
	}
	
	public ShowHideUnitTickListener(Unit unit, Game game){
		this.init(unit, game);
	}
	
	public ShowHideUnitTickListener(Unit unit, float hide_distance){
		original_x = unit.x;
		original_y = unit.y;
		goal_x = original_x;
		goal_y = original_y;
		hide = false;
		
		init(hide_distance);
	}
	
	public void init(Unit unit, Game game){
		original_x = unit.x;
		original_y = unit.y;
		goal_x = original_x;
		goal_y = original_y;
		hide = false;
		
		if(game != null){
			float width = game.world.focus_width;
			float height = game.world.focus_height;
			if(unit.scaling){
				width *= game.world.scale;
				height *= game.world.scale;
			}
			start_range_y = game.world.focus_y - height * 2f;
			start_hole_y = game.world.focus_y - height;
			end_hole_y = game.world.focus_y + height;
			end_range_y = game.world.focus_y + height * 2f;
			start_range_x = game.world.focus_x - width * 2f;
			start_hole_x = game.world.focus_x - width;
			end_hole_x = game.world.focus_x + width;
			end_range_x = game.world.focus_x + width * 2f;
		}
	}
	
	public void init(float hide_distance){
		init(original_x, original_y, hide_distance);
	}
	
	public void init(float x, float y, float hide_distance){
		start_range_y = y - hide_distance * 2f;
		start_hole_y = y - hide_distance * 1f;
		end_hole_y = y + hide_distance * 1f;
		end_range_y = y + hide_distance * 2f;
		
		start_range_x = x - hide_distance * 2f;
		start_hole_x = x - hide_distance * 1f;
		end_hole_x = x + hide_distance * 1f;
		end_range_x = x + hide_distance * 2f;
	}
	
	public void show(Unit unit){
		
		if(force_hide)
			return;
		
		goal_x = original_x;
		goal_y = original_y;
		
		hide = false;
		at_goal = false;
		cancel_move = false;
		
		unit.doesCollide = unit.shouldCollide;
		unit.doesTick = unit.shouldTick;
		unit.doesDraw = unit.shouldDraw;
		unit.doesTouch = unit.shouldTouch;
	}
	
	public void hideSmoothly(Unit unit){
		
		if(hide)
			return;
		
		goal_x = Randomizer.nextFloat(start_range_x, start_hole_x, end_hole_x, end_range_x);
		goal_y = Randomizer.nextFloat(start_range_y, start_hole_y, end_hole_y, end_range_y);
		
		hide = true;
		at_goal = false;
		cancel_move = false;
		
		unit.doesCollide = false;
		unit.doesTouch = false;
		
		if(unit instanceof MovingUnit){
			((MovingUnit)unit).vector_x = 0;
			((MovingUnit)unit).vector_y = 0;
			((MovingUnit)unit).acceleration_x = 0;
			((MovingUnit)unit).acceleration_y = 0;
		}
	}
	
	public void hide(Unit unit){
		this.hideSmoothly(unit);
		unit.x = goal_x;
		unit.y = goal_y;
	}
	
	
	@Override
	public void tick(Unit unit) {
	
		if(cancel_move && stop_move)
			return;
		
		this.changeAlpha(unit);

		if(!hide){	
			unit.x += (goal_x - unit.x) * ratio_move_show;
			unit.y += (goal_y - unit.y) * ratio_move_show;
		}
		else
		{
			unit.x += (goal_x - unit.x) * ratio_move_hide;
			unit.y += (goal_y - unit.y) * ratio_move_hide;
		}
		
		if(Math.abs(unit.x - goal_x) < 2 && Math.abs(unit.y - goal_y) < 2){
			
			at_goal = true;
			
			if(hide){
				if(stop_tick)
					unit.doesTick = false;
				
				if(stop_draw)
					unit.doesDraw = false;
				
				if(stop_alive)
					unit.alive = false;
			}
		}
		else
			at_goal = false;
		
		if(Math.abs(unit.x - goal_x) < 0.5f && Math.abs(unit.y - goal_y) < 0.5f && !hide && does_cancel_move){
			cancel_move = true;
		}
	}
	
	protected void changeAlpha(Unit unit){

		if((!change_alpha_on_show && !hide) || (!change_alpha_on_hide && hide)){
			unit.alpha = 255;
			return;
		}
		
		float distance = GameMath.distance(unit.x - goal_x/*original_x*/, unit.y - goal_y/*original_y*/);
		
		if(hide)
			if(distance < max_distance_alpha)
				unit.alpha = 255 - (int)(255 * (max_distance_alpha - distance) / max_distance_alpha);
			else
				unit.alpha = 255;
		else
			if(distance > max_distance_alpha)
				unit.alpha = 0;
			else
				unit.alpha = (int)(255 * (max_distance_alpha - distance) / max_distance_alpha);
	}
}
