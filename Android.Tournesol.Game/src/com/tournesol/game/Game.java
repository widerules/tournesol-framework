package com.tournesol.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.tournesol.game.listener.IGameTickListener;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.unit.IMovingUnit;
import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.PointF;

public class Game implements Serializable{

	private static final long serialVersionUID = -1170648744760754346L;

	public transient Object lock;
	
	public final List<Unit> unitsStatic = new ArrayList<Unit>();
	public final List<IMovingUnit> unitsMoving = new ArrayList<IMovingUnit>();
	
	public final Layer[] layers = new Layer[100];
	public World world;
	
	public final List<IGameTickListener> tick_listeners = new ArrayList<IGameTickListener>();

	protected transient Thread gameThread;
	protected transient boolean running;
	public transient GameView gameView;
	
	//Délai en milliseconde
	public final static int NORMAL_DELAY = 10;
	public final static int ANNOYING_DELAY = 100;
	public int delay = NORMAL_DELAY; 
	
	//Tick per second
	public float tps;
	
	//Séquence pour les id des units
	public AtomicLong sequence_unit_id = new AtomicLong();
	
	private long start_tick = 0;
	private long stop_tick = 0;
	private long current_delay = 0;
	
	public void init(){
		lock = new Object();
		this.initWorld();
		this.initLayers();
	}

	public void addUnit(Unit unit){
		this.addUnit(unit, 0);
	}
	
	public void addUnit(Unit unit, int layer){
		unit.setGame(this);

		unit.doesDraw = unit.shouldDraw;
		unit.doesTick = unit.shouldTick;
		
		synchronized(lock)
		{
			if(unit instanceof IMovingUnit)
				unitsMoving.add((IMovingUnit)unit);
				
			else
				unitsStatic.add(unit);
	
	
			layers[layer].addUnit(unit);
			unit.layer = layer;
	
			world.addUnit(unit);
		}
	}

	public void removeUnit(Unit unit){

		if(unit == null) return;
		
		//S'assurer que l'unité n'est plus vivante et ne se dessine plus
		unit.alive = false;
		unit.doesDraw = false;
		unit.doesTick = false;
		
		synchronized(lock)
		{
			if(unit instanceof IMovingUnit)
				unitsMoving.remove(unit);
				
			else
				unitsStatic.remove(unit);
				
			for(int i = 0 ; i < layers.length ; i++)
				layers[i].removeUnit(unit);
		
			world.removeUnit(unit);
		}
		
		
	}

	public void clearUnits(){
		
		synchronized(lock)
		{
			for(int i = 0 ; i < layers.length ; i++)
				layers[i].clearUnits();
		
			int units_moving = unitsMoving.size();
			for(int i = 0; i < units_moving; i++)
				((Unit)unitsMoving.get(i)).alive = false;
				
			unitsMoving.clear();
	
			int units_static = unitsStatic.size();
			for(int i = 0; i < units_static; i++)
				((Unit)unitsStatic.get(i)).alive = false;
			
			unitsStatic.clear();
		
			world.clearUnits();
		}
	}
	
	public void clearUnits(int layer){
		
		synchronized(lock)
		{
			int count = layers[layer].units.size();
			for(int i = 0 ; i < count ; i++){
				removeUnit(layers[layer].units.get(0));
			}
		}
	}

	public int countUnits(){
		return unitsMoving.size() + unitsStatic.size();
	}
	
	public Unit getUnit(long id){
		
		synchronized(lock)
		{
			for(int layer = 0; layer < layers.length; layer++){
				int count = layers[layer].units.size();
				for(int i = 0 ; i < count ; i++){
					if(layers[layer].units.get(i).id == id)
						return layers[layer].units.get(i);
				}
			}
			
			return null;
		}
	}

	public void start(){
		initGameThread();
		gameThread.start();
	}

	public void stop(){
		running = false;
	}

	private void initWorld(){
		world = new World();
		world.width = 0;
		world.height = 0;
		world.init(this);
		world.gravity_x = 0;
		world.gravity_y = 0;
	}
	
	private void initLayers(){
		for(int i = 0 ; i < layers.length ; i++){
			layers[i] = new Layer();
			layers[i].init(this);
			layers[i].order = i;
		}
	}

	private void initGameThread(){

		gameThread = new Thread(new Runnable() {

			@Override
			public void run() {
 
				running = true;
				while(running)  {
					
					start_tick = System.currentTimeMillis();

					//Enlever les cadavres, remettre l'état des objets à aucune collision et les chatouiller
					synchronized(lock)
					{
						//Chatouillers les curieux
						tick();
						
						RecycleBin.removedUnits.clear();

						int count = unitsMoving.size();
						for(int i = 0 ; i < count ; i++){
							final Unit unit = (Unit)unitsMoving.get(i);
							if(!(unit.alive)){
								RecycleBin.removedUnits.add(unit);
								unit.killed();
								continue;
							}
	
							if(unit.doesTick)
								unit.tick();
						}
						

						count = unitsStatic.size();
						for(int i = 0 ; i < count ; i++){
							final Unit unit = unitsStatic.get(i);
							if(!(unit.alive)){
								RecycleBin.removedUnits.add(unit);
								unit.killed();
								continue;
							}
	
							if(unit.doesTick)
								unit.tick();
						}
						
						//Rénitialiser l'état de collision 
						count = unitsMoving.size();
						for(int i = 0 ; i < count ; i++)
							((Unit)unitsMoving.get(i)).inCollision = false;
						
						count = unitsStatic.size();
						for(int i = 0 ; i < count ; i++)
							unitsStatic.get(i).inCollision = false;
					}
					
					
					
					for(int i = 0 ; i < RecycleBin.removedUnits.size() ; i++)
						removeUnit(RecycleBin.removedUnits.get(i));

					synchronized(lock)
					{
					
						//Vérifier s'il y a des collisions pour les Moving Unit
						int units_moving_size = unitsMoving.size();
						for(int unit_index = 0 ; unit_index < units_moving_size ; unit_index++){
							final MovingUnit moving_unit = (MovingUnit)unitsMoving.get(unit_index);
							if(!moving_unit.doesMove)
								continue;
							
							List<Unit> neighbourhood = world.getNearestUnit(moving_unit);
						
							for(int neighbour_index = 0 ; neighbour_index < neighbourhood.size() ; neighbour_index++){
								final Unit neighbour = neighbourhood.get(neighbour_index);
								
								if(moving_unit != neighbour)
									unitCollision(moving_unit, neighbour);
							}
						}
					
					}
					
					try {
						
						stop_tick = System.currentTimeMillis();
						
						current_delay = delay;
						
						if(start_tick > 0)
							current_delay = delay - (stop_tick - start_tick);
						
						if(current_delay < 0) 
							current_delay = 0;
						
						Thread.sleep(current_delay);
						
						tps = 1f / ((current_delay + stop_tick - start_tick) / 1000f);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
				}
				
				
			}

		});
	}

	private void tick()
	{
		
		//Vérifier si le focus est hors cadre et que le monde est répété, 
		//alors le placer au début/fin de l'autre monde
		world.tick();
		gameView.tick();

		for(int i = 0 ; i < tick_listeners.size() ; i++)
			tick_listeners.get(i).tick(this);
	}

	public void draw(Canvas c){ 
		
		this.drawWorld(c);

		//Vérifier s'il faut répéter le redessinage
		if(world.loop_x){
			if((world.focus_x * world.scale + world.focus_width / 2) > world.width * world.scale){

				c.save();
				c.translate(world.width * world.scale, 0);
				this.drawWorld(c);
				c.restore();
			}

			if((world.focus_x * world.scale - world.focus_width / 2) < 0){

				c.save();
				c.translate(-world.width * world.scale, 0);
				this.drawWorld(c);
				c.restore();
			}
		}
	}

	/**
	 * Dessiner toutes les unités sauf pour la couche du fond.
	 */
	private void drawWorld(Canvas c){

		for(int draw_layer = layers.length - 1 ; draw_layer >= 0 ; draw_layer--){
			if(draw_layer != Layer.LAYER_BACKGROUND)
				layers[draw_layer].draw(c);
		}
	}
	
	public void drawBackground(Canvas c){
		layers[Layer.LAYER_BACKGROUND].draw(c);
	}

	public PointF getGameScalePosition(float x, float y){
		
		RecycleBin.gameScalePointF.x = (x - world.focus_width / 2f) / world.scale + world.focus_x;
		RecycleBin.gameScalePointF.y = (y - world.focus_height / 2f) / world.scale + world.focus_y;
		return RecycleBin.gameScalePointF;
	}

	public PointF getGameNoScalePosition(float x, float y){
		RecycleBin.gameNoScalePointF.x = (x - world.focus_width / 2) + world.focus_x;
		RecycleBin.gameNoScalePointF.y = (y - world.focus_height / 2) + world.focus_y;
		return RecycleBin.gameNoScalePointF;
	}

	public ArrayList<Unit> getUnitsFromScreenPoint(float x, float y){

		RecycleBin.touchedUnits.clear();

		PointF scalePoint = getGameScalePosition(x, y);
		PointF noScalePoint = getGameNoScalePosition(x, y); 

		synchronized(lock)
		{
			for(Object obj : unitsMoving){
				Unit unit = (Unit)obj;
				if(unit.followFocus && ((unit.scaling && unit.contains(scalePoint)) || !unit.scaling && unit.contains(noScalePoint)))
					RecycleBin.touchedUnits.add(unit);
				else if(!unit.followFocus && unit.contains(x, y))
					RecycleBin.touchedUnits.add(unit);
			} 

			for(Object obj : unitsStatic){
				Unit unit = (Unit)obj;
				if(unit.followFocus && ((unit.scaling && unit.contains(scalePoint)) || !unit.scaling && unit.contains(noScalePoint)))
					RecycleBin.touchedUnits.add(unit);
				else if(!unit.followFocus && unit.contains(x, y))
					RecycleBin.touchedUnits.add(unit);
				
				/*
				if((unit.scaling && unit.contains(scalePoint)) ||
				  (!unit.scaling && unit.contains(noScalePoint)) ||
			      (!unit.followFocus && unit.contains(x, y)))
					RecycleBin.touchedUnits.add(unit);*/
			}
		}
		
		return RecycleBin.touchedUnits;
	}
	
	public PointF unitCollision(Unit unit1, Unit unit2){
		return unitCollision(unit1, unit2, true, true);
	}
	
	public PointF unitCollision(Unit unit1, Unit unit2, boolean event, boolean checkUnitCollide){
		
		Shape shape1 = null;
		Shape shape2 = null;
		
		PointF collisionPoint = null;
		if(!checkUnitCollide || 
		  (unit1.doesCollide && unit2.doesCollide && 
		  (unit1.canCollide(unit2) || unit2.canCollide(unit1)))){

			int unit1_shape_size = unit1.shapes.size();
			int unit2_shape_size = unit2.shapes.size();
			
			for(int unit1_index = 0 ; unit1_index < unit1_shape_size ; unit1_index++){
				shape1 = unit1.shapes.get(unit1_index);
				if(!shape1.doesCollide) continue;

				for(int unit2_index = 0 ; unit2_index < unit2_shape_size ; unit2_index++){
					shape2 = unit2.shapes.get(unit2_index);
					if(!shape2.doesCollide) continue;
					
					collisionPoint = Shape.collide(shape1, shape2);
					if(collisionPoint != null)
						break;
				}

				if(collisionPoint != null)
					break;
			}

			if(collisionPoint != null && event){
				
				RecycleBin.collisionUnit1.init(unit1);
				RecycleBin.collisionUnit2.init(unit2);
				unit1.collision(RecycleBin.collisionUnit2, collisionPoint, shape1, shape2);
				unit2.collision(RecycleBin.collisionUnit1, collisionPoint, shape2, shape1);
				unit1.inCollision = true;
				unit2.inCollision = true;
			}
		}
		
		return collisionPoint;
	}
	
	public ArrayList<PointF> unitCollision(Unit unit){ 
		synchronized (world.lock) {
			RecycleBin.collisionPointFs.clear();
			List<Unit> neighbourhood = world.getNearestUnit(unit);
			
			for(int index_neighbour = 0; index_neighbour < neighbourhood.size(); index_neighbour++){
				PointF collisionPoint = unitCollision(unit, neighbourhood.get(index_neighbour));
				if(collisionPoint != null)
					RecycleBin.collisionPointFs.add(collisionPoint);
			}
	 
			return RecycleBin.collisionPointFs;
		}
	}
	
	 private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		 stream.defaultReadObject();
		 this.lock = new Object();

		 this.world.init(this);
			for(int i = 0; i < layers.length; i++)
				layers[i].init(this);
			
		int count = unitsMoving.size();
		for(int i = 0 ; i < count ; i++)
			((Unit)unitsMoving.get(i)).setGame(this);
		
		count = unitsStatic.size();
		for(int i = 0 ; i < count ; i++)
			unitsStatic.get(i).setGame(this);
	 }
}
