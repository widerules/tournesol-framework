package com.tournesol.game;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.tournesol.game.listener.ShowHideUnitTickListener;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.PaintManager;

public class Layer {
	
	private static final long serialVersionUID = 3955750618779023529L;
	
	private transient Game game;
	
	public static final int LAYER_LICENSE = 97;
	public static final int LAYER_GAME_DESIGN = 98;
	public static final int LAYER_BACKGROUND = 99;
	
	
	
	public transient ArrayList<Unit> units;
	
	//Ordre de cette couche dans l'ensemble de couches du jeu
	public int order;

	public void init(Game game){
		this.game = game;
		this.units = new ArrayList<Unit>();
		
		//Ajouter toutes les unités du jeu dans la couche
		int count = game.unitsMoving.size();
		for(int i = 0; i < count; i++)
			if(((Unit)game.unitsMoving.get(i)).layer == order)
				this.addUnit((Unit)game.unitsMoving.get(i));
		
		count = game.unitsStatic.size();
		for(int i = 0; i < count; i++)
			if(game.unitsStatic.get(i).layer == order)
				this.addUnit(game.unitsStatic.get(i));
	}
	
	public void addUnit(Unit unit){
		units.add(unit);
	}
	
	public void removeUnit(Unit unit){
		
		if(unit == null)
			return;
		
		synchronized (units) {
			unit.alive = false;
			units.remove(unit);
		}
	}
	
	/**
	 * Vider la liste des unités et s'assurer que ceux-ci soit mort!
	 */
	public void clearUnits(){
		
		synchronized (units) {
			int count = units.size();
			for(int i = 0; i < count; i++)
				units.get(i).alive = false;
			
			units.clear();
		}
	}
	
	public void draw(Canvas c){
		synchronized (units) {
			for(int k = 0 ; k < units.size(); k++)
				drawUnit(c, units.get(k));
		}
	}
	
	private void drawUnit(Canvas c, Unit unit){
		
		if(!unit.doesDraw)
			return;
		
		if(unit.scaling){
			c.save();
			float wall = PaintManager.wall.getStrokeWidth();
			float moving_unit = PaintManager.moving_unit.getStrokeWidth();
			PaintManager.wall.setStrokeWidth(wall / game.world.scale);
			PaintManager.moving_unit.setStrokeWidth(moving_unit / game.world.scale);
			
			c.scale(game.world.scale, game.world.scale, game.world.focus_width / 2, game.world.focus_height / 2);
			
			PointF focus = unit.getFocusPosition();
			unit.draw(c, 0, 0, unit.degrees, focus.x, focus.y);
			
			PaintManager.wall.setStrokeWidth(wall);
			PaintManager.moving_unit.setStrokeWidth(moving_unit);
			c.restore();
		}
		else{
			
			if(unit.followFocus)
				unit.draw(c, (game.world.focus_x - unit.x) * (1 - game.world.scale), 
							 (game.world.focus_y - unit.y) * (1 - game.world.scale), 0, unit.x, unit.y);
			else
				unit.draw(c, 0, 0, unit.degrees, unit.x, unit.y);
		}
	}

	public void show(){

		synchronized (units) {
			int count = units.size();
			for(int i = 0; i < count; i++){
				Unit unit = units.get(i);
				int count_listeners = unit.tick_listeners.size();
				for(int listener = 0; listener < count_listeners; listener++)
					if(unit.tick_listeners.get(listener) instanceof ShowHideUnitTickListener)
						((ShowHideUnitTickListener)unit.tick_listeners.get(listener)).show(unit);
			}
		}
	}
	
	public void hide(){
		synchronized (units) {
			int count = units.size();
			for(int i = 0; i < count; i++){
				Unit unit = units.get(i);
	
				int count_listeners = unit.tick_listeners.size();
				for(int listener = 0; listener < count_listeners; listener++)
					if(unit.tick_listeners.get(listener) instanceof ShowHideUnitTickListener)
						((ShowHideUnitTickListener)unit.tick_listeners.get(listener)).hide(unit);
			}
		}
	}
	
	public void hideSmoothly(){
		hideSmoothly(false);
	}
	
	public void hideSmoothly(boolean stop_alive){
		synchronized (units) {
			int count = units.size();
			for(int i = 0; i < count; i++){
				Unit unit = units.get(i);
	
				int count_listeners = unit.tick_listeners.size();
				for(int listener = 0; listener < count_listeners; listener++)
					if(unit.tick_listeners.get(listener) instanceof ShowHideUnitTickListener){
						((ShowHideUnitTickListener)unit.tick_listeners.get(listener)).stop_alive = stop_alive;
						((ShowHideUnitTickListener)unit.tick_listeners.get(listener)).hideSmoothly(unit);
					}
			}
		}
	}
}
