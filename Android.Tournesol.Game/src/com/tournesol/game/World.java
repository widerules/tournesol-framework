package com.tournesol.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tournesol.game.listener.IWorldFocusListener;
import com.tournesol.game.listener.IWorldOrientationListener;
import com.tournesol.game.listener.IWorldScaleListener;
import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.unit.IMovingUnit;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class World implements SensorEventListener, Serializable{

	private static final long serialVersionUID = 2141498264873468352L;
	
	public static final int ORIENTATION_PORTRAIT = 90;
	public static final int ORIENTATION_LANDSCAPE = 0;
	public static final int ORIENTATION_REVERSE_PORTRAIT = 270; 
	public static final int ORIENTATION_REVERSE_LANDSCAPE = 180;
	
	
	public static final int CELL_CONTAINER_FROM_DIMENSIONS = 0;
	public static final int CELL_CONTAINER_FROM_SHAPES = 1;
	
    private transient Sensor accelerometer;
    public transient Game game;
	public final List<IWorldFocusListener> focus_listeners = new ArrayList<IWorldFocusListener>();
	public final List<IWorldOrientationListener> orientation_listeners = new ArrayList<IWorldOrientationListener>();
	public final List<IWorldScaleListener> scale_listeners = new ArrayList<IWorldScaleListener>();

	//Objet pour synchroniser les boucles
	public transient Object lock;
	
    //Détermine la largeur et la hauteur du monde
    public float width;
	public float height;
	
	//Détermine le ratio à augmenter/réduire la taille des objets
	public float scale = 1;
	public float min_scale = 0.01f;
	public float max_scale = 1000f;
	
	//Détermine la rotation à effectuer sur touts les objets
	public float rotate = 0;
	
	//Détermine la zone de focus par rapport au monde
	public float focus_x;
	public float focus_y;
	public float focus_width;
	public float focus_height;
	
	//Détermine la rotation par défaut
	public int surface_rotation;
	public int orientation = ORIENTATION_PORTRAIT;

	//Détermine le vecteur et l'angle de l'appareil par rapport à la gravité
	public float gravity_degrees = 0;
	public float gravity_radians= 0;
	public float gravity_x;
	public float gravity_y;
	public float gravity_z;
	
	//Détermine la force du dernier secouage
	public float gravity_force = 0;
	
	//Détermine s'il faut répéter le monde lorsque l'on s'approche de ses frontières.
	public boolean loop_x = false;
	public boolean loop_y = false;
	 
	//World grid pour séparer les unités
	public float cell_width = 501;
	public float cell_height = 501;
	public int row_count;
	public int column_count;
	public transient ArrayList<Unit>[][] cellsStatic;
	public transient ArrayList<IMovingUnit>[][] cellsMoving;
	
	private transient static int cache_max = 1000;
	private transient static int[][] cache_cells = new int[cache_max][2];
	private transient static int cache_index = 0;
	
	
	private transient static int[] left_top;
	private transient static int[] right_bottom;
	
	public World(){
		
	}
	
	public void init(Game game){
		this.game = game;
		this.lock = new Object();
		
		this.createGrid();
		
		//Ajouter toutes les unités du jeu dans la monde
		int count = game.unitsMoving.size();
		for(int i = 0; i < count; i++)
			this.addUnit((Unit)game.unitsMoving.get(i));
		
		count = game.unitsStatic.size();
		for(int i = 0; i < count; i++)
			this.addUnit(game.unitsStatic.get(i));
	}
	
	public void setSensorManager(SensorManager sensorManager){
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		float new_gravity_x = 0;
		float new_gravity_y = 0;
		
		switch(surface_rotation){
			case Surface.ROTATION_0:
				new_gravity_x =  event.values[0];
				new_gravity_y =  event.values[1];
				break;
			case Surface.ROTATION_90:
				new_gravity_x =  event.values[1];
				new_gravity_y =  event.values[0];
				break;
			case Surface.ROTATION_180:
				new_gravity_x =  event.values[0];
				new_gravity_y =  event.values[1] * -1;
				break;
			case Surface.ROTATION_270:
				new_gravity_x =  event.values[1];
				new_gravity_y =  event.values[0] * -1;
				break;
		}
		
		float new_gravity_z =  event.values[2];
		gravity_force = Math.abs(new_gravity_x - gravity_x) + Math.abs(new_gravity_y - gravity_y) + Math.abs(new_gravity_z - gravity_z);
		gravity_x = new_gravity_x;
		gravity_y = new_gravity_y;
		gravity_z = new_gravity_z;
		gravity_degrees = GameMath.degrees(gravity_x, gravity_y);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	/*
	public int getOrientation(){
		
		if(Math.abs(gravity_x) + Math.abs(gravity_y) < Math.abs(gravity_z) * 0.85f || !sensorChangedInit)
			return last_orientation;
		
		if(gravity_degrees >= 45 && gravity_degrees < 135)
			last_orientation = ORIENTATION_PORTRAIT;
		else if(gravity_degrees >= 315 || gravity_degrees < 45)
			last_orientation = ORIENTATION_LANDSCAPE;
		else if(gravity_degrees >= 235 && gravity_degrees < 315)
			last_orientation = ORIENTATION_REVERSE_PORTRAIT;
		else if(gravity_degrees >= 135 && gravity_degrees < 235)
			last_orientation = ORIENTATION_REVERSE_LANDSCAPE;
		
		return last_orientation;
	}
	*/
	public PointF getGravityDirection(){
		PointF p = GameMath.direction(gravity_x, gravity_y);
		RecycleBin.gravityPointF.x = p.x;
		RecycleBin.gravityPointF.y = p.y;
		return RecycleBin.gravityPointF;
	}
	
	public void setFocusPoint(float x, float y){
		this.focus_x = x;
		this.focus_y = y;

		int count = focus_listeners.size();
		for(int i = 0 ; i < count ; i++)
			focus_listeners.get(i).focusChanged(this, x, y);
	}
	
	public void setDimension(float width, float height){
		this.width = width;
		this.height = height;
		this.createGrid();
	}
	
	public void setOrientation(int orientation){
		
		int old_orientation = this.orientation;
		this.orientation = orientation;
		int count = orientation_listeners.size();
		
		synchronized(game.lock){
			for(int i = 0 ; i < count ; i++)
				orientation_listeners.get(i).orientationChanged(this, orientation, old_orientation);
		}
	}
	
	public void setScale(float scale){
		
		float old_scale = this.scale;
		this.scale = scale;
		int count = scale_listeners.size();
		for(int i = 0 ; i < count ; i++)
			scale_listeners.get(i).scaleChanged(this, scale, old_scale);
	}
	
	public void initScreen(){
	
		Display display = ((WindowManager)game.gameView.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		
		float width = display.getWidth();
		float height = display.getHeight();

		//Forcer la distribution du monde en mode portrait
		if(width > height){
			game.world.focus_width = height;
			game.world.focus_height = width;
		}
		else{
			game.world.focus_width = width;
			game.world.focus_height = height;
		}
		
		int orientation = game.gameView.getContext().getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_PORTRAIT)
			game.world.orientation = World.ORIENTATION_PORTRAIT;
		else
			game.world.orientation = World.ORIENTATION_LANDSCAPE;
		
		game.world.setFocusPoint(game.world.focus_width / 2, game.world.focus_height / 2);
		game.world.setDimension(game.world.focus_width, game.world.focus_height);
		//game.world.surface_rotation = display.getRotation();
	}
	
	public void addUnit(Unit unit){
		synchronized (lock) {
			final List<int[]> cells = this.getCells(unit);
			int count = cells.size();
			if(unit instanceof IMovingUnit)
	
				for(int i = 0 ; i < count ; i++){
					final int[] cell = cells.get(i);
					cellsMoving[cell[0]][cell[1]].add((IMovingUnit)unit);
				}
	
			else
				for(int i = 0 ; i < count; i++){
					final int[] cell = cells.get(i);
					cellsStatic[cell[0]][cell[1]].add(unit);
				}
		}

	}
	
	public void removeUnit(Unit unit){
		
		if(unit == null) return;
		
		synchronized (lock) {
			
			final List<int[]> cells = this.getCells(unit);
			int count = cells.size();
			if(unit instanceof IMovingUnit)
				for(int i = 0 ; i < count ; i++){
					final int[] cell = cells.get(i);
					cellsMoving[cell[0]][cell[1]].remove(unit);
				}
			else
				for(int i = 0 ; i < count; i++){
					final int[] cell = cells.get(i);
					cellsStatic[cell[0]][cell[1]].remove(unit);
				}
		}
	}
	
	public void clearUnits(){

		synchronized (lock) {
			for(int row = 0 ; row < row_count ; row++)
				for(int column = 0 ; column < column_count ; column++){
					cellsStatic[column][row].clear();
					cellsMoving[column][row].clear();
				}
		}
	}
	
	public List<Unit> getNearestUnit(Unit unit){
		
		synchronized (lock) {
	
			RecycleBin.nearestUnit.clear();
			
			final List<int[]> cells = this.getCells(unit);
			
			int cells_size = cells.size();
			for(int i = 0 ; i < cells_size ; i++){
				
				final int[] cell = cells.get(i);

				int cells_static_size = cellsStatic[cell[0]][cell[1]].size();
				for(int j = 0 ; j < cells_static_size ; j++)
					if(!RecycleBin.nearestUnit.contains(cellsStatic[cell[0]][cell[1]].get(j)))
						RecycleBin.nearestUnit.add(cellsStatic[cell[0]][cell[1]].get(j));

				int cells_moving_size = cellsMoving[cell[0]][cell[1]].size();
				for(int j = 0 ; j < cells_moving_size ; j++)
					if(!RecycleBin.nearestUnit.contains(cellsMoving[cell[0]][cell[1]].get(j)))
						RecycleBin.nearestUnit.add((Unit)cellsMoving[cell[0]][cell[1]].get(j));
			}
		}
		
		return RecycleBin.nearestUnit;
	}
	
	public void tick(){
		refreshMovingUnit();
	}
	
	private void createGrid(){
		
		synchronized (lock) {
		
			column_count = (int)FloatMath.ceil(this.width / this.cell_width);
			row_count = (int)FloatMath.ceil(this.height / this.cell_height);
			cellsStatic = new ArrayList[column_count][row_count];
			cellsMoving = new ArrayList[column_count][row_count];
			
			for(int column = 0 ; column < column_count ; column++)
				for(int row = 0 ; row < row_count ; row++){
					cellsStatic[column][row] = new ArrayList<Unit>();
					cellsMoving[column][row] = new ArrayList<IMovingUnit>();
				}
			
		}
	}
	
	private void refreshMovingUnit(){
		
		synchronized (lock) {
				
			//Vider la grille des Moving Unit
			for(int row = 0 ; row < row_count ; row++)
				for(int column = 0 ; column < column_count ; column++)
					for(int index = 0; index < cellsMoving[column][row].size(); index++)
						cellsMoving[column][row].clear();
				 
			//Rafraîchir 
			for(int i = 0 ; i < game.unitsMoving.size() ; i++ ){
				final Unit unit = (Unit)game.unitsMoving.get(i);
				
				//Vérifier si l'unité peut bouger, autrement ne pas l'ajuster
				
				this.adjustUnit(unit);
				final List<int[]> cells = this.getCells(unit);
				for(int j = 0 ; j < cells.size() ; j++){
					final int[] cell = cells.get(j);
					cellsMoving[cell[0]][cell[1]].add((IMovingUnit)unit);
				}
			}
		}
		
	}
	
	/**
	 * Ajuster les Moving Unit qui ont dépassé le monde et que celui-ci est en loop
	 */
	private void adjustUnit(Unit unit){
		
		if(loop_x){
			if(unit.x > width)
				unit.x = width - unit.x;
			else if(unit.x < 0)
				unit.x = width + unit.x;
		}
		
		if(loop_y){
			if(unit.y > height)
				unit.y = height - unit.y;
			else if(unit.x < 0)
				unit.y = height + unit.y;
		}
	}
	
	private List<int[]> getCells(Unit unit) {
		
		if(unit.cell_container == CELL_CONTAINER_FROM_DIMENSIONS)
			return getCellsFromDimensions(unit);
		else
			return getCellsFromShapes(unit);
	} 
	
	private List<int[]> getCellsFromDimensions(Unit unit){
		
		RecycleBin.cells.clear();
		
		RectF unitRange = unit.getCollideRange();
		if(unitRange == null || column_count == 0 || row_count == 0)
			return RecycleBin.cells;
		 
		left_top = getCell(unitRange.left, unitRange.top);
		right_bottom = getCell(unitRange.right, unitRange.bottom);
		
		for(int column = left_top[0] ; column <= right_bottom[0]; column++)
			for(int row = left_top[1] ; row <= right_bottom[1]; row++){
				
				cache_index++;
				if(cache_index >= cache_max) cache_index = 0;
				cache_cells[cache_index][0] = column;
				cache_cells[cache_index][1] = row;
				RecycleBin.cells.add(cache_cells[cache_index]);
			}
		 
		return RecycleBin.cells;
	}
	
	private List<int[]> getCellsFromShapes(Unit unit){
		
		RecycleBin.cells.clear();
		
		int count = unit.shapes.size();
		for(int i = 0; i < count; i++){
			if(unit.shapes.get(i) instanceof ShapeLine)
				RecycleBin.cells.addAll(getCellsFromShapeLine((ShapeLine)unit.shapes.get(i)));
		}
			
		return RecycleBin.cells;
	}
	
	private List<int[]> getCellsFromShapeLine(ShapeLine line){
		
		RecycleBin.shapeCells.clear();

		if(column_count == 0 || row_count == 0)
			return RecycleBin.shapeCells;
		 
		int[] first_cell = getRawCell(line.unit.x + line.start.x, line.unit.y + line.start.y);
		int[] last_cell = getRawCell(line.unit.x + line.end.x, line.unit.y + line.end.y);
				
		int cell_column_count = Math.abs(last_cell[0] - first_cell[0]);
		int cell_row_count = Math.abs(last_cell[1] - first_cell[1]);
		
		if(cell_column_count <= 0) cell_column_count = 1;
		if(cell_row_count <= 0) cell_row_count = 1;
		
		if(cell_row_count < cell_column_count){
			
			final double ratio = ((double)cell_column_count) / cell_row_count;
			for(int row_section = 0; row_section < cell_row_count; row_section++){
				for(int column = -2; column <= ratio + 1; column++){
					double cell_x = first_cell[0] + (row_section * ratio + column) * (last_cell[0] - first_cell[0] > 0 ? 1 : -1);
					double cell_y = first_cell[1] + row_section * (last_cell[1] - first_cell[1] > 0 ? 1 : -1);
					addShapeCell(cell_x, cell_y);
				}
			}
		}
		else{
			
			final double ratio = ((double)cell_row_count) / cell_column_count;
			for(int column_section = 0; column_section < cell_column_count; column_section++){
				
				for(int row = -2; row <= ratio + 1; row++){
					double cell_x = first_cell[0] + column_section * (last_cell[0] - first_cell[0] > 0 ? 1 : -1);
					double cell_y = first_cell[1] + (column_section * ratio + row) * (last_cell[1] - first_cell[1] > 0 ? 1 : -1);
					addShapeCell(cell_x, cell_y);
				}
			}
		}
		
		
		 
		return RecycleBin.shapeCells;
	}
 
	private void addShapeCell(double cell_x, double cell_y){
		if(cell_x >= 0 && cell_x < column_count && cell_y >= 0 && cell_y < row_count){
			cache_index++;
			if(cache_index >= cache_max) cache_index = 0;
			cache_cells[cache_index][0] = (int)cell_x;
			cache_cells[cache_index][1] = (int)cell_y;
			RecycleBin.shapeCells.add(cache_cells[cache_index]);
		}
	}
	
	private int[] getCell(float x, float y){
		
		
		int[] raw_cell = getRawCell(x, y);

		if(raw_cell[0] >= column_count)
			raw_cell[0] = column_count - 1;
		
		if(raw_cell[0] < 0)
			raw_cell[0] = 0;
		
		if(raw_cell[1] >= row_count)
			raw_cell[1] = row_count - 1;
		
		if(raw_cell[1] < 0)
			raw_cell[1] = 0;
		
		return raw_cell;
	}
	
	private int[] getRawCell(float x, float y){
		
		int column = (int)FloatMath.floor(x / cell_width);
		int row = (int)FloatMath.floor(y / cell_height);
		
		cache_index++;
		if(cache_index >= cache_max) cache_index = 0;
		cache_cells[cache_index][0] = column;
		cache_cells[cache_index][1] = row;
		return cache_cells[cache_index];
	}
}
