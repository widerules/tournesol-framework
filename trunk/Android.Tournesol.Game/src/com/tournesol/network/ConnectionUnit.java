package com.tournesol.network;

import com.tournesol.game.unit.Unit;

public abstract class ConnectionUnit extends Unit{

	private static final long serialVersionUID = -1167845835193984050L;


	public static final byte STATE_PENDING = 1;
	public static final byte STATE_CONNECTED = 2;
	public static final byte STATE_DISCONNECTED = 3;
	
	public byte state = STATE_DISCONNECTED;
	
	public float max_distance_ratio = 0.125f; 
	public float tick_increment = -0.5f;
	public float tick_wait_initial = 300;
	public float tick_wait = tick_wait_initial;
	
	protected float distance = 0;
	
	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		distance = max_distance_ratio * width;
	}
	
	@Override
	public void tick(){
		super.tick();
		
		if(state == STATE_CONNECTED && distance <= 0 /*&& tick_wait == tick_wait_initial / 2*/){
			distance = 0;
			tick_wait = tick_wait_initial / 2;
			return;
		}

		if(state == STATE_DISCONNECTED && distance >= width * max_distance_ratio){
			distance = width * max_distance_ratio;
			tick_wait = tick_wait_initial;
			return;
		}
		
		if(tick_wait > 0){
			tick_wait--;
			return;
		}
		
		distance += tick_increment;
		if(distance <= 0 || distance >= width * max_distance_ratio){
			tick_increment *= -1;
			tick_wait = tick_wait_initial;
		}
	}
}
