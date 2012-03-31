package com.tournesol.game.unit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Region.Op;

import com.tournesol.drawing.DrawingChars;
import com.tournesol.game.GameMath;
import com.tournesol.game.listener.ShowHideUnitTickListener;
import com.tournesol.game.unit.MovingUnit;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

public class Cannon extends Unit{

	private static final long serialVersionUID = -6349410926227034429L;

	//Puissance du canon
	public float power = 4.5f;
	
	//Nombre de balle restante
	public int ammo = 9;
	
	//Proportion du rectangle pa rapport au cercle
	public float canon_width_ratio = 0.65f;
	public float canon_height_ratio = 0.6f;
	
	//Animation lors du tir
	protected float tick_shoot = -1;
	protected float tick_shoot_max = 50;
	
	//Animation pour l'affichage du canon
	protected float tick_ready = -1;
	protected float tick_ready_max = 25;
	
	//Dessin qui présente le nombre de balle restante
	protected DrawingChars drawingAmmo;
	
	//Indique si le canon est prêt pour tirer
	public boolean ready = true;

	//Détermine si le cannon fait du son
	public boolean sound = true;
	
	public float goal_degrees = 0;
	public float goal_initial_speed = 0.01f;
	public float goal_speed = goal_initial_speed;
	public float goal_speed_step = 0.01f;
	
	public Cannon(){
		
		//Ajouter un dessin pour inscrire le nombre de canon restant
		drawingAmmo = new DrawingChars();
		this.drawings.add(drawingAmmo);
	}
	
	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		drawingAmmo.text_size = width * 0.5f;
		drawingAmmo.text_align = Align.CENTER;
		goal_degrees = degrees;
	}
	
	@Override
	public RectF getRectF()
	{
		//Déterminer la zone de collision selon le scale du monde
		float ratio = 1;
		if(game.world.scale < 1)
			ratio = 1 / game.world.scale;
			
		RecycleBin.collideRangeRectF.left = x - width * ratio;
		RecycleBin.collideRangeRectF.top = y - height * ratio;
		RecycleBin.collideRangeRectF.right = x + width * ratio;
		RecycleBin.collideRangeRectF.bottom = y + height * ratio;
		return RecycleBin.collideRangeRectF;
	}
	
	public void setGoal(float x, float y){
		int count = tick_listeners.size();
		for(int i = 0; i < count; i++){
			if(tick_listeners.get(i) instanceof ShowHideUnitTickListener){
				ShowHideUnitTickListener listener = (ShowHideUnitTickListener)tick_listeners.get(i);
				listener.original_x = x;
				listener.original_y = y;
			}
		}
	}

	@Override
	public void draw(Canvas c){
			
		PointF focus = getFocusPosition();
		
		Paint paint = getPaint();
		focus = getFocusPosition();
		
		c.save();
		if(tick_shoot >= 0)
			if(tick_shoot > tick_shoot_max / 2)
				c.scale(tick_shoot / tick_shoot_max, tick_shoot_max / tick_shoot, focus.x, focus.y);
			else
				c.scale(1 - (tick_shoot / (tick_shoot_max)), 2 - (tick_shoot_max - tick_shoot * 2) / tick_shoot_max, focus.x, focus.y);
		
		c.drawCircle(focus.x, focus.y, width / 2, paint);
		
		Path path = RecycleBin.drawPath;
		path.reset();
		path.addCircle(focus.x, focus.y, width / 2, Direction.CW);
		
		float ammo_degrees = -degrees;
		if(game != null)
			ammo_degrees += 90 - game.world.orientation;
		
		drawingAmmo.color = PaintManager.backColor;
		drawingAmmo.draw(c, focus.x, focus.y - drawingAmmo.height / 2, ammo_degrees, 0, drawingAmmo.height / 2);
		
		
		try{ c.clipPath(path, Op.DIFFERENCE); } catch(Exception e){}
		
		paint = getPaint();
		RecycleBin.drawRectF.left = focus.x;
		RecycleBin.drawRectF.top = focus.y - height * canon_height_ratio / 2;
		RecycleBin.drawRectF.right = focus.x + width * canon_width_ratio * ((tick_ready_max - tick_ready / 2) / tick_ready_max);
		RecycleBin.drawRectF.bottom = focus.y + height * canon_height_ratio / 2;
		c.drawRect(RecycleBin.drawRectF, paint);
		
		c.restore();
	}
	
	@Override
	public void tick(){
		super.tick();
		
		//Déterminer la rotation du cannon selon la direction voulue
		if(goal_degrees != degrees){
			float diff = (degrees - goal_degrees) % 360;
			if(diff < 0)
				diff += 360;
			
			if(diff < goal_speed)
				goal_degrees = degrees;
			else{
				this.rotate(diff > 180 ? goal_speed : -goal_speed, x, y);
				goal_speed += goal_speed_step;
			}
		}
		
		if(tick_shoot >= 0 )
			tick_shoot--;

		if(ready){
			if(tick_ready >= 0)
				tick_ready--;
		}
		else
			tick_ready = tick_ready_max;
	}
	
	
	public void shoot(MovingUnit unit){
		
		if(unit != null){
			unit.x = x;
			unit.y = y;
			
			PointF direction = GameMath.direction(degrees);
			unit.vector_x = direction.x * power;
			unit.vector_y = direction.y * power;
		}
		
		this.tick_shoot = tick_shoot_max;
		this.goal_degrees = this.degrees;
	}
	
	public void setAmmo(int ammo){
		
		this.ammo = ammo;
		drawingAmmo.chars.reset();
		
		if(this.ammo < 0)
			this.ammo = -1;
		else	
			drawingAmmo.chars.add(ammo);
	}
	
	public void adjustRotation(float goal_degrees){
		
		if(this.goal_degrees == degrees){
			this.goal_speed = this.goal_initial_speed;
			this.goal_degrees = goal_degrees;
		}
		else
			this.goal_degrees = degrees;
	}
}
