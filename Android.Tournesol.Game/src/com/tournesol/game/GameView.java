package com.tournesol.game;

import java.util.ArrayList;
import java.util.List;

import com.tournesol.drawing.Drawing;
import com.tournesol.drawing.DrawingChars;
import com.tournesol.drawing.DrawingChars.IGetChars;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.listener.IGameViewTouchListener;
import com.tournesol.game.listener.IUnitTickListener;
import com.tournesol.game.unit.CharsUnit;
import com.tournesol.game.unit.CollisionLine;
import com.tournesol.game.unit.Particles;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.unit.button.Button;
import com.tournesol.game.utility.Chars;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.game.utility.SoundManager;
import com.tournesol.motion.TouchEvent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View{

	public Game game;
	public List<TouchEvent> touch_events = new ArrayList<TouchEvent>();
	public List<IGameViewTouchListener> touch_listeners = new ArrayList<IGameViewTouchListener>();
	
	//Button ayant le focus
	public Button focus_button = null;
	
	private Handler m_handler;
	private Runnable m_runnable = new Runnable() {
		
		@Override
		public void run() {
			invalidate();
		}
	};
	
	public GameView(Context context) {
		super(context);
		Game game = new Game();
		game.init();
		this.initGameView(game);
	}
	
	public GameView(Context context, Game game) {
		super(context);
		this.initGameView(game);
	}
	 
	protected void initGameView(Game game){

		m_handler = new Handler();
		this.game = game;
		this.game.gameView = this;
		
		this.game.world.initScreen();
		//this.game.world.setSensorManager((SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE));
		
		//Charger la cache des particules
		Particles.cache.get();
		
		//Initialiser les dessins
		Drawing.init(getContext());
		
		//Initialiser le gestionnaire de son
        SoundManager.init(getContext());
		
		//Permettre l'interception de touche
		this.setFocusableInTouchMode(true);
	}
	
	public void start(){
		game.start();
	}
	
	public void stop(){
		game.stop();
	}
	@Override
	protected void onDraw (Canvas c){
		
		synchronized (game) {
			super.onDraw(c);
			
			if(!game.running)
				return;
			
			//Dessiner la couleur du fond
			c.drawColor(PaintManager.backColor);
			
			if(game != null)
				game.drawBackground(c);
			
			if(game.world.orientation == World.ORIENTATION_LANDSCAPE){
				c.rotate(-90, 0, 0);
				c.translate(-game.world.focus_width, 0);
			}
			
			if(game != null)
				game.draw(c);
		}
	}
	
	public void tick(){
		m_handler.post(m_runnable);
	
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event){
		
		super.onKeyDown(keyCode, event);
		
		Button next_button = null;
		float focus_x = 0;
		float focus_y = 0;
		
		if(focus_button != null){
			
			//Si la commande est "ENTER" ou l'équivalent, simuler un touché avec le bouton
			if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || 
			   keyCode == KeyEvent.KEYCODE_ENTER){
				focus_button.touch(null);
				return false;
			}
			
			focus_x = focus_button.x;
			focus_y = focus_button.y;
			
			if(game.world.orientation == World.ORIENTATION_PORTRAIT){
				if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
					next_button = findButton(focus_x, focus_y, 0);
				else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
					next_button = findButton(focus_x, focus_y, 90);
				else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
					next_button = findButton(focus_x, focus_y, 180);
				else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
					next_button = findButton(focus_x, focus_y, 270);
			}
			else{
				if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
					next_button = findButton(focus_x, focus_y, 90);
				else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
					next_button = findButton(focus_x, focus_y, 180);
				else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
					next_button = findButton(focus_x, focus_y, 270);
				else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
					next_button = findButton(focus_x, focus_y, 0);
			}
		}
		else{
			synchronized (game.lock) {
				int count = game.unitsMoving.size();
				for(int i = 0; i < count; i++){
					if(game.unitsMoving.get(i) instanceof Button &&
					   !((Unit)game.unitsMoving.get(i)).isHidden()){
						next_button = (Button)game.unitsMoving.get(i);
						break;
					}
				}
			}
		}

		if(next_button != null){
			if(focus_button != null)
				focus_button.focus = false;
			
			focus_button = next_button;
			focus_button.focus = true;
		}
		
		return false;
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent event){

		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int pointerIndex = (event.getAction()) & MotionEvent.ACTION_POINTER_ID_MASK >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		int pointerId = event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		int pointerCount = event.getPointerCount();
		
		TouchEvent e = null;
		PointF p = null;
		long current_time = event.getEventTime();
		
		switch(action){
		
			case MotionEvent.ACTION_DOWN:
				//Log.d("GameView", "ACTION_DOWN");
			 	pointerId = event.getPointerId(pointerIndex);
			 	p = transformTouchPoint(event.getX(pointerIndex), event.getY(pointerIndex));
			 	e = new TouchEvent(p.x, p.y, TouchEvent.TOUCH_DOWN, pointerId, pointerCount, current_time);
			 	e.units = game.getUnitsFromScreenPoint(e.x, e.y);
			 	for(int k = 0 ; k < e.units.size() ; k++)
			 		if(e.units.get(k).doesTouch)
			 			e.units.get(k).touch(e);
			 	
			 	touch_events.add(e);
				this.onTouchDown(e);
			 	break;
				
			case MotionEvent.ACTION_POINTER_DOWN:
				//Log.d("GameView", "ACTION_POINTER_DOWN");
				for(int i = 0 ; i < pointerCount ; i++)
				{
					pointerIndex = i;
					pointerId = event.getPointerId(pointerIndex);
					boolean contains = false;
					for(TouchEvent touchEvent : touch_events)
						if(touchEvent.pointer == pointerId)
						{
							contains = true;
							break;
						}
					
					if(!contains){
						p = transformTouchPoint(event.getX(pointerIndex), event.getY(pointerIndex));
					 	e = new TouchEvent(p.x, p.y, TouchEvent.TOUCH_DOWN, pointerId, pointerCount, current_time);
					 	e.units = game.getUnitsFromScreenPoint(e.x, e.y);
					 	for(int k = 0 ; k < e.units.size() ; k++)
					 		if(e.units.get(k).doesTouch)
					 			e.units.get(k).touch(e);
					 	
					 	touch_events.add(e);
					 	this.onTouchDown(e);
					}
				}
				break;
				
				
			case MotionEvent.ACTION_UP:
				//Log.d("GameView", "ACTION_UP");
				
				if(event.getPointerCount() >= pointerIndex)
					pointerIndex = 0;
					
				pointerId = event.getPointerId(pointerIndex);
				for(TouchEvent touchEvent : new ArrayList<TouchEvent>(touch_events))
					if(touchEvent.pointer == pointerId)
					{
						touchEvent.type = TouchEvent.TOUCH_UP;	
						touch_events.remove(touchEvent);
					 	this.onTouchUp(touchEvent);
					 	e = touchEvent;
						break;
					}
				
				break;
				
			case MotionEvent.ACTION_POINTER_UP:
				//Log.d("GameView", "ACTION_POINTER_UP");	
				for(TouchEvent touchEvent : new ArrayList<TouchEvent>(touch_events)){
					if(touchEvent.pointer == pointerId)
					{ 
						touchEvent.type = TouchEvent.TOUCH_UP;
						touch_events.remove(touchEvent);
						this.onTouchUp(touchEvent);
						e = touchEvent;
						break;
					}
				}

				break;
				
			case MotionEvent.ACTION_MOVE:
				//Log.d("GameView", "ACTION_MOVE");
				
				TouchEvent firstEvent = null;
				for(int i = 0 ; i < pointerCount ; i++){
					pointerIndex = i;
					pointerId = event.getPointerId(pointerIndex);
				
					int count = touch_events.size();
					for(int index = 0; index < count; index++){
						
						TouchEvent touchEvent = touch_events.get(index);
						if(touchEvent.pointer == pointerId && touchEvent.type != TouchEvent.TOUCH_UP)
						{
							touchEvent.old_x = touchEvent.x;
							touchEvent.old_y = touchEvent.y;
							
							p = transformTouchPoint(event.getX(pointerIndex), event.getY(pointerIndex));
							touchEvent.x = p.x;
							touchEvent.y = p.y;
							touchEvent.type = TouchEvent.TOUCH_DRAGGED;
							touchEvent.live_time = current_time;
							
							this.onTouchDrag(touchEvent);
						}
						
						if(touchEvent.type != TouchEvent.TOUCH_UP){
							if(firstEvent == null)
								firstEvent = touchEvent;
							else
								this.onDoubleTouchDrag(firstEvent, touchEvent);
						}
						
						//e = touchEvent;
					}
					/*
					if(firstEvent != null)
						this.onTouchDrag(firstEvent);
					*/
					
				}
		}
		
		//Éliminer les vieux event (> 1 seconde)
		for(int i = 0; i < touch_events.size(); i++){
			TouchEvent touchEvent = touch_events.get(i);
			if(touchEvent.live_time <= current_time - 1000){
				touchEvent.type = TouchEvent.TOUCH_TIMEOUT;
				touch_events.remove(i);
				i--;
			}
		}
		
		int count_listeners = touch_listeners.size();
		for(int i = 0; i < count_listeners; i++)
			touch_listeners.get(i).onTouchEvent(e);
		
		return true;
	}


	
	protected void onTouchUp(TouchEvent e){
		
	}
	
	protected void onTouchDown(TouchEvent e){
		
	}
	
	protected void onTouchDrag(TouchEvent e){
		
	}

	protected void onDoubleTouchDrag(TouchEvent e1, TouchEvent e2){
		
	}
	
	public void cameraControl(TouchEvent e1, TouchEvent e2){
		
		//Déterminer s'il s'agit d'un zoom ou d'un déplacement
		PointF direction = GameMath.direction(e1.old_x - e1.x, e1.old_y - e1.y);
		float direction_0_x = direction.x;
		float direction_0_y = direction.y;
		
		direction = GameMath.direction(e2.old_x - e2.x, e2.old_y - e2.y);
		float direction_1_x = direction.x;
		float direction_1_y = direction.y;
	
		
		//Vérifier si les deux pointeurs ont eu tous les deux une mise à jour de leur état
		if((direction_0_x == 0 && direction_0_y == 0) || (direction_1_x == 0 && direction_1_y == 0))
			return;
		
		//Vérifier si les vecteurs de déplacements vont presque dans la même direction, alors
		//faire un déplacement de focus, autrement modifier le zoom
		if(Math.abs(direction_1_x - direction_0_x) < 0.75f &&
		   Math.abs(direction_1_y - direction_0_y) < 0.75f){
		
			game.world.focus_x += 0.5f * (e1.old_x - e1.x) / game.world.scale;
			game.world.focus_y += 0.5f * (e1.old_y - e1.y) / game.world.scale;
			
			//Ne pas dépasser les limites du monde
			if(game.world.focus_x < 0)
				game.world.focus_x = 0;
			
			if(game.world.focus_x > game.world.width)
				game.world.focus_x = game.world.width;
			
			if(game.world.focus_y < 0)
				game.world.focus_y = 0;
			
			if(game.world.focus_y > game.world.height)
				game.world.focus_y = game.world.height;
		}
		else{

			float new_distance = GameMath.distance(e2.x - e1.x, e2.y - e1.y);
			float old_distance = GameMath.distance(e2.old_x - e1.old_x, e2.old_y - e1.old_y);
			float difference = (new_distance - old_distance) * 0.005f;
			
			float new_scale = game.world.scale + difference * game.world.scale;
			
			//Ne pas dépasser les limites du monde
			if(new_scale < game.world.min_scale)
				new_scale =  game.world.min_scale;
			
			if(new_scale > game.world.max_scale)
				new_scale = game.world.max_scale;
			
			game.world.setScale(new_scale);
		}
	}

	public void showToast(final String message, final int duration) {
		
		if(!(this.getContext() instanceof Activity))
			return;
		
		Activity activity = (Activity)this.getContext();
		activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getContext(), message, duration).show();
            }
        });
    }
	
	private PointF transformTouchPoint(float x, float y){
		
		RecycleBin.transformPointF.x = x;
		RecycleBin.transformPointF.y = y;
		
		if(game.world.orientation == World.ORIENTATION_LANDSCAPE){
			RecycleBin.transformPointF.x = (game.world.focus_width - y);
			RecycleBin.transformPointF.y = x;
		}

		return RecycleBin.transformPointF;
	}
	
	private static final CollisionLine collisionLine = new CollisionLine();
	private Button findButton(float focus_x, float focus_y, float degrees){
		
		float max_distance = game.world.width + game.world.height;
		float min_distance = max_distance;
		float min_point_x = focus_x + max_distance * GameMath.cos(degrees);
		float min_point_y = focus_y + max_distance * GameMath.sin(degrees);
		
		collisionLine.collisionPoints.clear();
		collisionLine.collisionUnits.clear();
		collisionLine.followFocus = false;
		collisionLine.scaling = false;
		collisionLine.init(focus_x, focus_y, min_point_x, min_point_y);
		game.addUnit(collisionLine);
		
		Button collision_button = null;
		
		
		int count = game.unitsMoving.size();
		for(int i = 0; i < count; i++){
			if(game.unitsMoving.get(i) instanceof Button && !game.unitsMoving.get(i).equals(focus_button)){
				Button button = (Button)game.unitsMoving.get(i);
				
				if(button.isHidden())
					continue;
				
				PointF collisionPoint = game.unitCollision(collisionLine, button, false, false);
				if(collisionPoint == null)
					continue;
				
				float current_distance =  GameMath.distance(collisionPoint.x - focus_x, collisionPoint.y - focus_y);
				if(current_distance < min_distance){
					min_distance = current_distance;
					collision_button = button;
				}
			}
		}
		
		game.removeUnit(collisionLine);
		return collision_button;
	
	}
	
	
	public void createPositionText(){
		Unit position = new Unit();
		position.followFocus = false;
		position.scaling = false;
		position.x = game.world.focus_width * 0.1f;
		position.y = game.world.focus_height * 0.9f;
		DrawingChars drawing = new DrawingChars();
		drawing.color = PaintManager.foreColor;
		drawing.text_size = 12;
		drawing.text_align = Align.LEFT;
		position.drawings.add(drawing);
		position.tick_listeners.add(new IUnitTickListener() {
			
			@Override
			public void tick(Unit unit) {
				DrawingChars drawing = (DrawingChars)unit.drawings.get(0);
				drawing.chars.reset();
				drawing.chars.add(unit.game.world.focus_x, 2);
				drawing.chars.add(' ');
				drawing.chars.add(unit.game.world.focus_y, 2);
				drawing.chars.add(' ');
				drawing.chars.add(unit.game.world.scale, 2);
				drawing.chars.add(Chars.LINE_BREAK);
				drawing.chars.add(unit.game.tps,2);
			}
		});
		
		
		game.addUnit(position, Layer.LAYER_GAME_DESIGN);
	}
	
	public void createWorldGrid(){

		float cell_x = 0;
		for(; cell_x < game.world.width; cell_x += game.world.cell_width){
			
			EdgeUnit column = new EdgeUnit();
			column.doesMove = false;
			column.doesCollide = false;
			column.doesTick = false;
			column.addEdge(column.addVertex(cell_x, 0), column.addVertex(cell_x, game.world.height));
			game.addUnit(column, Layer.LAYER_GAME_DESIGN);
		}
		
		EdgeUnit column = new EdgeUnit();
		column.doesMove = false;
		column.doesCollide = false;
		column.doesTick = false;
		column.addEdge(column.addVertex(game.world.width, 0), column.addVertex(game.world.width, game.world.height));
		game.addUnit(column, Layer.LAYER_GAME_DESIGN);
		
		
		float cell_y = 0;
		for(; cell_y < game.world.height; cell_y += game.world.cell_height){
			
			EdgeUnit row = new EdgeUnit();
			row.doesMove = false;
			row.doesCollide = false;
			row.doesTick = false;
			row.addEdge(row.addVertex(0, cell_y), row.addVertex(game.world.width, cell_y));
			game.addUnit(row, Layer.LAYER_GAME_DESIGN);
		}
		
		EdgeUnit row = new EdgeUnit();
		row.doesMove = false;
		row.doesCollide = false;
		row.doesTick = false;
		row.addEdge(row.addVertex(0, game.world.height), row.addVertex(game.world.width, game.world.height));
		game.addUnit(row, Layer.LAYER_GAME_DESIGN);
		
		cell_x = 0;
		cell_y = 0;
		for(int irow = 0; irow < game.world.row_count; irow++){
			
			cell_x = 0;
			for(int icolumn = 0; icolumn < game.world.column_count; icolumn++){
				CharsUnit unit = new CharsUnit();
				unit.doesCollide = false;
				unit.doesTick = false;
				unit.init(cell_x, cell_y, 0, 0);
				final int index_row = irow;
				final int index_column = icolumn;
				unit.drawingChars.init(new IGetChars() {
					
					Chars c = new Chars();
					public Chars getChars() {
						c.reset();
						c.add(game.world.cellsMoving[index_column][index_row].size());
						c.add(Chars.LINE_BREAK);
						c.add(game.world.cellsStatic[index_column][index_row].size());
						return c;
					}
				});
				
				game.addUnit(unit, Layer.LAYER_GAME_DESIGN);
				
				cell_x +=  game.world.cell_width;
			}
			
			cell_y +=  game.world.cell_height;
		}
	}
}
