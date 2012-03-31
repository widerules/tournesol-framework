package com.tournesol.motion;

import java.io.Serializable;
import java.util.List;

import com.tournesol.game.unit.Unit;

public class TouchEvent implements Serializable{

	private static final long serialVersionUID = -7787846176294591743L;
	
	public static final int TOUCH_DOWN = 0;
	public static final int TOUCH_UP = 1;
	public static final int TOUCH_DRAGGED = 2;
	public static final int TOUCH_TIMEOUT = 3;
	
	public int type, create_type, pointer, pointer_count;
	public float x, y;
	public float old_x, old_y;
	public Object tag;
	public long live_time;
	public transient List<Unit> units;
	
	public TouchEvent(float x, float y, int type, int pointer, int pointer_count, long live_time){
		this.old_x = x;
		this.old_y = y;
		this.x = x;
		this.y = y;
		this.type = type;
		this.create_type = type;
		this.pointer = pointer;
		this.pointer_count = pointer_count;
		this.live_time = live_time;
	}
	
	
}
