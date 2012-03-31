package com.tournesol.game.unit.button;

import java.util.ArrayList;

import com.tournesol.game.unit.CacheUnit;
import com.tournesol.game.utility.Randomizer;

public class DisposableButton extends CircleButton{
	
	public static final int DISPOSABLE_OFF_SCREEN = 150;
	public static final int MAX_CHILDREN = 10;
	public static final int MAX_NEIGHBOURS = 10;
	
	public transient static final CacheUnit<DisposableButton> cache = new CacheUnit<DisposableButton>(DisposableButton.class, 100);
	
	public float goal_x;
	public float goal_y;
	public boolean hide;
	
	public ArrayList<DisposableButton> neighbours;
	public ArrayList<DisposableButton> children;

	public DisposableButton() {
		neighbours = new ArrayList<DisposableButton>();
		children = new ArrayList<DisposableButton>();
	}
	
	public void init(float goal_x, float goal_y, float size){
		
		super.init(x, y, size, size);
		this.hide = false;
		this.goal_x = goal_x;
		this.goal_y = goal_y;
		this.doesTick = false;
		this.shouldTick = false;
		this.doesDraw = false;
		this.shouldDraw = false;

		//Vider les enfants et les voisins
		neighbours.clear();
		children.clear();
	}
	
	
	@Override
	public void tick(){
		
		if(!hide){
			this.x += (goal_x - x) / 20;
			this.y += (goal_y - y) / 20;
		}
		else
		{
			super.tick();
			if(this.alive)
				this.alive = this.x < game.world.focus_width * 2 && this.x > -game.world.focus_width && 
				             this.y < game.world.focus_height * 2 && this.y > -game.world.focus_height;
		}
	}
	
	/**
	 * Cacher le bouton du jeu.
	 */
	public void hide(){
		this.hide = true;
		this.vector_x = (Randomizer.R.nextFloat() - 0.5f) * 10;
		this.vector_y = (Randomizer.R.nextFloat() - 0.5f) * 10;
		this.hideChildren();
	}
	
	/**
	 * Afficher le bouton dans le jeu.
	 */
	public void show(){
		this.show(Randomizer.nextInt(-DISPOSABLE_OFF_SCREEN, 
					  0, 
					  (int)game.world.focus_width, 
					  (int)game.world.focus_width + DISPOSABLE_OFF_SCREEN),
				  Randomizer.nextInt(-DISPOSABLE_OFF_SCREEN, 
					  0, 
					  (int)game.world.focus_width, 
					  (int)game.world.focus_width + DISPOSABLE_OFF_SCREEN));
	}
	
	public void show(float x, float y){
		this.x = x;
		this.y = y;
		this.hide = false;
		this.doesTick = true;
		this.shouldTick = true;
		this.doesDraw = true;
		this.shouldDraw = true;
	}
	
	/**
	 * Effectuer une action de sélection où on affiche les boutons enfants 
	 * et cacher les boutons voisins si le bouton n'était pas sélectionné.
	 * Autrement, cacher les enfants déjà sélectionnés.
	 */
	public void select(){
		
		this.selected = !this.selected;
		
		if(this.selected){
			this.showChildren();
			this.hideNeighboursChildren();
		}
		else{
			this.hideChildren();
		}
	}
	
	/**
	 * Afficher les boutons enfants.
	 */
	public void showChildren(){

		int count = children.size();
		for(int i = 0 ; i < count; i++){
			DisposableButton child = children.get(i);
			
			if(!child.alive){
				game.addUnit(child, this.layer);
				child.show();
			}
			else
				child.show(child.x, child.y);
		}

		
	}
	
	public void hideNeighboursChildren(){
		
		int count = neighbours.size();
		for(int i = 0 ; i < count; i++){
			DisposableButton neighbour = neighbours.get(i);
			neighbour.selected = false;
			neighbour.hideChildren();
		}
	}
	
	/**
	 * Cacher les boutons enfants.
	 */
	public void hideChildren(){

		int count = children.size();
		for(int i = 0 ; i < count; i++){
			DisposableButton child = children.get(i);
			child.selected = false;
			child.hide();
		}
	}
}
