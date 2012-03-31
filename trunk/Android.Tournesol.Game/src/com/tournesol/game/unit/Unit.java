package com.tournesol.game.unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.tournesol.drawing.Drawing;
import com.tournesol.game.Game;
import com.tournesol.game.GameMath;
import com.tournesol.game.World;
import com.tournesol.game.edge.Edge;
import com.tournesol.game.listener.ICollisionListener;
import com.tournesol.game.listener.IUnitTickListener;
import com.tournesol.game.listener.IUnitTouchListener;
import com.tournesol.game.listener.ShowHideUnitTickListener;
import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.motion.TouchEvent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Unit extends Drawing{

	private static final long serialVersionUID = -6344241452918420923L;

	//Détermine si l'unité est dans le jeu
	public boolean alive = false; 
	
	//Détermine si l'unité doit être zoomée
	public boolean scaling = true;
	
	//Position et dimension
	public float x;
	public float y;
	
	//Détermine si l'unité doit être dessiné ou frappé (variable)
	public boolean doesDraw = true;
	public boolean doesCollide = true;
	
	//Détermine si l'unité peut être dessiné ou frappé (invariable)
	public boolean shouldDraw = true;
	public boolean shouldCollide = true;
	
	//Détermine si l'unité doit et peut être chatouillé
	public boolean doesTick = true;
	public boolean shouldTick = true;
	
	//Détermine si l'unité doit et peut être touché
	public boolean doesTouch = true;
	public boolean shouldTouch = true;
	
	//Détermine si l'unité est en collision avec un autre unité
	public boolean inCollision = false;
	
	//Détermine si l'unité suit le focus
	public boolean followFocus = true;
	
	//Détermine si les dessins doivent tournés en suivant la gravité
	public boolean gravity_rotate = false;
	
	//Jeu
	public transient Game game;
	
	//ID de la couche que l'unité est contenu
	public int layer;
	
	//ID identifiant cette unité
	public long id;
	
	//Masse de l'unité utilisé pour la collision
	public float mass = 1;
	
	//Détermine comment l'unité est contenu dans les cellules du monde
	public int cell_container = World.CELL_CONTAINER_FROM_DIMENSIONS;
	
	public final List<Shape> shapes = new ArrayList<Shape>();
	public final List<Drawing> drawings = new ArrayList<Drawing>();
	public final List<IUnitTouchListener> touch_listeners = new ArrayList<IUnitTouchListener>();
	public final List<ICollisionListener> collision_listeners = new ArrayList<ICollisionListener>();
	public final List<IUnitTickListener> tick_listeners = new ArrayList<IUnitTickListener>();
	
	public Unit(){
		id = Long.MIN_VALUE;
	}
	
	public boolean canCollide(Unit unit){
		return false;
	}
	
	public void collision(CollisionUnit unit, PointF collisionPoint, Shape mine, Shape other)
	{
		for(int i = 0 ; i < collision_listeners.size() ; i++)
			collision_listeners.get(i).collision(this, other.unit, collisionPoint);
	}
	
	public PointF getFocusPosition(){
		
		if(followFocus && (game != null)){
			RecycleBin.focusPointF.x = x - (game.world.focus_x - game.world.focus_width / 2);
			RecycleBin.focusPointF.y = y - (game.world.focus_y - game.world.focus_height / 2);
		}
		else /*if (game != null)*/{
			RecycleBin.focusPointF.x = x;
			RecycleBin.focusPointF.y = y;
		}/*
		else{
			RecycleBin.focusPointF.x = 0;//width / 2;
			RecycleBin.focusPointF.y = 0;//height / 2;
		}*/
			
		
		return RecycleBin.focusPointF;
		
	}
	
	public void setGame(Game game){
		this.game = game;
		this.alive = true;
		
		if(id == Long.MIN_VALUE)
			id = game.sequence_unit_id.incrementAndGet();
	}
	
	@Override
	public void init(float width, float height){
		this.init(width / 2, height / 2, width, height);
	}
	
	public void init(float x, float y, float width, float height){
		super.init(width, height);
		this.x = x;
		this.y = y;
	}
	
	public boolean contains(PointF p){
		RectF r = this.getCollideRange();
		if(r != null)
			return r.contains(p.x, p.y);
		return false;
	}
	
	private static final PointF p = new PointF();
	public boolean contains(float x, float y){
		p.x = x;
		p.y = y;
		return contains(p);
	}
	
	public RectF getCollideRange()
	{
		return getRectF();
	}
	
	public RectF getRectF()
	{
		RecycleBin.collideRangeRectF.left = x - width / 2f;
		RecycleBin.collideRangeRectF.top = y - height / 2f;
		RecycleBin.collideRangeRectF.right = x + width / 2f;
		RecycleBin.collideRangeRectF.bottom = y + height / 2f;
		return RecycleBin.collideRangeRectF;
	}
	
	public void killed(){
		id = Long.MIN_VALUE;
	}
	
	public void touch(TouchEvent e){
		for(int i = 0 ; i < touch_listeners.size() ; i++){
			touch_listeners.get(i).touch(e, this);
		}
	}
	
	public void tick(){
		
		int count = tick_listeners.size();
		for(int i = 0 ; i < count ; i++)
			if(i < tick_listeners.size())
				tick_listeners.get(i).tick(this);
		
		count = drawings.size();
		for(int i = 0 ; i < count ; i++)
			if(drawings.get(i) instanceof Unit && ((Unit)drawings.get(i)).doesTick)
				((Unit)drawings.get(i)).tick();
	}
	
	@Override
	public void rotate(float degrees){
		this.rotate(degrees, x, y);
	}
	
	public void rotate(float degrees, float x, float y){
		
		//Déterminer le degrée actuel à partir du point
		if(x != this.x && y != this.y && this.degrees == 0){
			this.degrees = GameMath.degrees(this.x - x, this.y - y);
		}
		
		if(degrees == 0)
			return;
		
		int count = shapes.size();
		for(int i = 0; i < count; i++){
			if(shapes.get(i) instanceof Edge)
				continue;
			
			shapes.get(i).rotate(degrees, x, y);
		}
		
		super.rotate(degrees);
		
		float distance = GameMath.distance(x - this.x, y - this.y);

		this.x = x + distance * GameMath.cos(this.degrees);
		this.y = y + distance * GameMath.sin(this.degrees);
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Unit)
			return ((Unit)o).id == id;
		
		return false;
	}
	 
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		 stream.defaultReadObject();
		 readUnit();
	}
	
	protected void readUnit(){
		int count = shapes.size();
		 for(int i = 0; i < count; i++)
			 shapes.get(i).unit = this;
	}

	@Override
	protected void draw(Canvas c) {
		drawDrawings(c);
	}
	
	protected void drawDrawings(Canvas c){
		int count = drawings.size();
		if(count <= 0)
			return;
		 
		PointF focus = getFocusPosition(); 
		float focus_x = focus.x;
		float focus_y = focus.y;

		for(int i = 0; i < count; i++){
			Drawing drawing = drawings.get(i);
			
			//Appliquer le même niveau de transparance aux dessins enfants
			drawing.alpha = this.alpha;
			
			if(drawing instanceof Unit){
				if(!manage_rotation)
					drawing.draw(c, focus_x, focus_y, drawing.degrees, ((Unit)drawing).x, ((Unit)drawing).y);
				else
					drawing.draw(c, focus_x, focus_y, drawing.degrees + degrees, ((Unit)drawing).x, ((Unit)drawing).y);
			}
			else if(!manage_rotation)
				drawing.draw(c, focus_x, focus_y, drawing.degrees, 0, 0);
			else 
				drawing.draw(c, focus_x, focus_y, drawing.degrees + degrees, drawing.width / 2, drawing.height / 2);
		}
		
		focus.x = focus_x;
		focus.y = focus_y;
	}
	
	@Override
	public Paint getPaint(){
		Paint paint = super.getPaint();
		
		float scale = 1;
		if(game != null)
			scale = game.world.scale;
		
		if(scaling)
			paint.setStrokeWidth(stroke_width / scale);
		
		return paint;
	}
	
	/**
	 * Détermine si l'unité est caché selon le ShowHideUnitListener de celui-ci
	 * @return
	 */
	public boolean isHidden(){

		int count = this.tick_listeners.size();
		for(int i = 0; i < count; i++)
			if(this.tick_listeners.get(i) instanceof ShowHideUnitTickListener){
				ShowHideUnitTickListener listener = (ShowHideUnitTickListener)this.tick_listeners.get(i);
				return listener.hide;
			}
		
		return false;
	}
	
	@Override
	public void copy(Drawing drawing){
		super.copy(drawing);
		if(!(drawing instanceof Unit))
			return;
		
		Unit copy = (Unit)drawing;
		
		this.init(copy.x, copy.y, copy.width, copy.height);
		this.scaling = copy.scaling;
		this.doesDraw = copy.doesDraw;
		this.doesCollide = copy.doesCollide;
		this.shouldDraw = copy.shouldDraw;
		this.shouldCollide = copy.shouldCollide;
		this.shouldTick = copy.shouldTick;
		this.followFocus = copy.followFocus;
		this.gravity_rotate = copy.gravity_rotate;
		this.mass = copy.mass;
	}
}
