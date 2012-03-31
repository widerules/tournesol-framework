package com.tournesol.game.listener;

import com.tournesol.game.unit.Unit;

public abstract class DelayUnitTickListener implements IUnitTickListener{

	private static final long serialVersionUID = 3929972938698768487L;
	
	public int tick_delay;
	public int tick_current;
	
	public DelayUnitTickListener(int tick_delay){
		this.tick_delay = tick_delay;
		this.tick_current = 0;
	}

	@Override
	public void tick(Unit unit) {
		
		if(tick_current >= tick_delay){
			tick_current = 0;
			delayTick(unit);
		}
		else
			tick_current++;
	}
	
	protected abstract void delayTick(Unit unit);
}
