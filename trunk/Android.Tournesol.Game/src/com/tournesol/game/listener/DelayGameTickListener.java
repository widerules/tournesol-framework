package com.tournesol.game.listener;

import com.tournesol.game.Game;

public abstract class DelayGameTickListener implements IGameTickListener{

	private static final long serialVersionUID = 4917983543184847174L;
	
	public int tick_delay;
	public int tick_current;
	
	public DelayGameTickListener(int tick_delay){
		this.tick_delay = tick_delay;
		this.tick_current = 0;
	}

	@Override
	public void tick(Game game) {
		
		if(tick_current >= tick_delay){
			tick_current = 0;
			delayTick(game);
		}
		else
			tick_current++;
	}
	
	protected abstract void delayTick(Game game);
}
