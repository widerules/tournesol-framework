package com.tournesol.game.unit;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.tournesol.drawing.Drawing;
import com.tournesol.drawing.DrawingPoint;
import com.tournesol.game.GameMath;
import com.tournesol.game.listener.ParticleCollisionListener;
import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.shape.ShapeArc;
import com.tournesol.game.shape.ShapeEllipse;
import com.tournesol.game.shape.Shape;
import com.tournesol.game.utility.PaintManager;

public class Projectile extends MovingUnit{

	private static final long serialVersionUID = -5126415668458574520L;

	public final static int MAX_PROJECTILE_COUNT = 10;
	public final static int MAX_TICK = 1000;
	
	public final static CacheUnit<Projectile> cache = new CacheUnit<Projectile>(Projectile.class, MAX_PROJECTILE_COUNT);
	
	public Drawing drawing;
	public int color;
	public ShapeArc arc;
	public int max_tick;
	public int current_tick;
	public ParticleCollisionListener particleCollisionListener;
	
	public Projectile(){

		this.doesCollide = true;
		this.shouldCollide = true;
		this.gravity_damper = 0;
		this.max_tick = MAX_TICK;
		
		arc = new ShapeEllipse(this);
		this.shapes.add(arc);
		
		particleCollisionListener = new ParticleCollisionListener();
		particleCollisionListener.drawing = DrawingPoint.singleton;
		collision_listeners.add(particleCollisionListener);
	}
	
	public void init(float x, float y, float size, float vector_x, float vector_y){
		super.init(x, y, size, size);
		this.vector_x = vector_x;
		this.vector_y = vector_y;
		this.degrees = GameMath.degrees(vector_x, vector_y);
		this.arc.height = size;
		this.arc.width = size;
		this.current_tick = 0;
		this.particleCollisionListener.layer = this.layer;
		this.particleCollisionListener.color = this.color;
	}

	@Override
	public boolean canCollide(Unit unit) {
		return true;
	}

	@Override
	public void draw(Canvas c) {
		PointF focus = this.getFocusPosition();
		PaintManager.save(PaintManager.moving_unit);
		PaintManager.moving_unit.setColor(color);
		
		drawing.draw(c, focus.x, focus.y, this.degrees);
		
		PaintManager.restore(PaintManager.moving_unit);
	}
	
	@Override
	public void tick(){
		super.tick();
		
		current_tick++;
		if(current_tick > max_tick)
			this.alive = false;	
	}
	
	@Override
	public void collision(CollisionUnit unit, PointF collisionPoint, Shape mine, Shape other){
		super.collision(unit, collisionPoint, mine, other);
		this.alive = false;
	}
}
