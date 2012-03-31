package com.tournesol.game.control;

import java.util.ArrayList;

import com.tournesol.game.listener.IControlListener;
import com.tournesol.game.unit.Unit;

public class Control<T> extends Unit{

	private static final long serialVersionUID = -4005457539389363173L;
	
	public final ArrayList<IControlListener<T>> control_listeners = new ArrayList<IControlListener<T>>();
	
	public Control(){
		this.doesCollide = false;
		this.shouldCollide = false;
		this.followFocus = false;
		this.scaling = false;
		//this.manage_rotation = true;
	}
	
	protected void controlChanged(int new_state, int old_state){
		
		int count = control_listeners.size();
		for(int i = 0; i < count; i++)
			control_listeners.get(i).controlChanged((T)this, new_state, old_state);
	}
}
