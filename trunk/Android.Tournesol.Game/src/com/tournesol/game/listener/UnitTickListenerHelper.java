package com.tournesol.game.listener;

import java.util.ArrayList;

import com.tournesol.game.unit.Unit;

public class UnitTickListenerHelper<T extends IUnitTickListener>{

	private Class<T> classT;
	private ArrayList<T> all = new ArrayList<T>();
	
	public UnitTickListenerHelper(Class<T> classT){
		this.classT = classT;
	}
	
	public T get(Unit unit){
		
		int count = unit.tick_listeners.size();
		for(int i = 0; i < count; i++)
			if(classT.isInstance(unit.tick_listeners.get(i)))
				return (T)unit.tick_listeners.get(i);
		
		return null;
	}
	
	public ArrayList<T> all(Unit unit){
		
		all.clear();
		
		int count = unit.tick_listeners.size();
		for(int i = 0; i < count; i++)
			if(classT.isInstance(unit.tick_listeners.get(i)))
				all.add((T) unit.tick_listeners.get(i));
		
		return all;
	}
}
