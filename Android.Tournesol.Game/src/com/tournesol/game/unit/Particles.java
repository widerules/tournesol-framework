package com.tournesol.game.unit;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.tournesol.drawing.Drawing;
import com.tournesol.game.utility.Cache;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.game.unit.Unit;

public class Particles extends Unit{

	private static final long serialVersionUID = -38161348251096726L;
	
	public final static int MAX_PARTICLE_COUNT = 100;
	public final static int DEFAULT_MAX_TICK = 120;
	public final static int DEFAULT_MIN_TICK = 20;
	
	private final static Random r = new Random();
	
	public int current_count;
	public int max_tick = DEFAULT_MAX_TICK;
	public int min_tick = DEFAULT_MIN_TICK;
	
	private int tick = 0;
	private int[] m_ticks;
	private PointF[] m_positions;
	private PointF[] m_vectors;
	
	public Bitmap bmp;
	public Drawing drawing;
	public float drawing_rotate_increment = 0;
	public boolean reduce_alpha = false;
	public boolean random_tick = true;
	
	public boolean random_vector = true;
	public float vector_x = 1;
	public float vector_y = 1;
	
	public boolean random_position = true;
	
	public final static Cache<Particles> cache = new Cache<Particles>(Particles.class, MAX_PARTICLE_COUNT);
		
	public Particles(){
		
		this.manage_rotation = true;
		this.doesCollide = false;
		this.shouldCollide = false;
		
		m_positions = new PointF[MAX_PARTICLE_COUNT];
		m_vectors = new PointF[MAX_PARTICLE_COUNT];
		m_ticks = new int[MAX_PARTICLE_COUNT];

		for(int i = 0 ; i < MAX_PARTICLE_COUNT ; i++){
			m_positions[i] = new PointF();
			m_vectors[i] = new PointF();
		}
	}
	
	public void init(float x, float y, int count, Bitmap bmp){
		init(x, y, count, bmp, null);
	}
	
	public void init(float x, float y, int count, Drawing drawing){
		init(x, y, count, null, drawing);
	}
	
	private void init(float x, float y, int count, Bitmap bmp, Drawing drawing){
		this.x = x;
		this.y = y;
		this.tick = 0;
		this.bmp = bmp;
		this.drawing = drawing;
		current_count = count;
		if(current_count > MAX_PARTICLE_COUNT)
			current_count = MAX_PARTICLE_COUNT;
		
		for(int i = 0 ; i < current_count ; i++){
			
			if(random_position){
				m_positions[i].x = r.nextFloat() * (r.nextBoolean() ? 1 : -1) * width / 2;
				m_positions[i].y = r.nextFloat() * (r.nextBoolean() ? 1 : -1) * height / 2;
			}
			else{
				m_positions[i].x = 0;
				m_positions[i].y = 0;
			}
			
			if(random_vector){
	            m_vectors[i].x = r.nextFloat() * (r.nextBoolean() ? 1 : -1);
	            m_vectors[i].y = r.nextFloat() * (r.nextBoolean() ? 1 : -1);
			}
			else{
	            m_vectors[i].x = vector_x;
	            m_vectors[i].y = vector_y;
			}
				
			
            if(random_tick)
            	m_ticks[i] = r.nextInt(max_tick - min_tick) + min_tick;
            else
            	m_ticks[i] = max_tick - min_tick;
		}
	}
	
	
	public boolean canCollide(Unit unit){ 
		return false;
	}
	
	@Override
	public void draw(Canvas c) {
		
		PointF focus = this.getFocusPosition();
		for(int i = 0 ; i < current_count ; i++)
			if(m_ticks[i] > min_tick){
				if(bmp != null)
					c.drawBitmap(bmp, 
								(int)(m_positions[i].x + focus.x), 
								(int)(m_positions[i].y + focus.y), 
								null);
				
				if(drawing != null){
					if(reduce_alpha){
						
						if(m_ticks[i] < (max_tick - min_tick) / 2){
							//Décroissance d'alpha
							drawing.alpha = (int)(510 * (m_ticks[i]) / (float)((max_tick - min_tick)));
						}
						else{
							//Croissance d'alpha
							drawing.alpha = (int)(510 * ((max_tick - min_tick) - m_ticks[i]) / (float)((max_tick - min_tick)));
						}
					}
					
					float drawing_x = m_positions[i].x +focus.x;
					float drawing_y = m_positions[i].y +focus.y;
					drawing.draw(c, drawing_x, drawing_y, degrees, drawing.width / 2, drawing.height / 2);
				}
			}
	}

	@Override
	public void tick() {
		
		for(int i = 0 ; i < current_count ; i++){
			if(drawing != null)
				drawing.degrees += drawing_rotate_increment;
			m_positions[i].x += m_vectors[i].x;
			m_positions[i].y += m_vectors[i].y;
			m_ticks[i]--;
		}
		  
		tick++;
		alive = tick < max_tick;
	}
	
	@Override
	public RectF getCollideRange()
	{
		RecycleBin.collideRangeRectF.left = 0;
		RecycleBin.collideRangeRectF.top = 0;
		RecycleBin.collideRangeRectF.right = 0;
		RecycleBin.collideRangeRectF.bottom = 0;
		return RecycleBin.collideRangeRectF;
	}
}
