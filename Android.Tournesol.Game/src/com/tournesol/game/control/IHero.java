package com.tournesol.game.control;

public interface IHero {

	public void changeDirection(float x, float y);
	public void changeOrientation(float x, float y);
	public void buttonHold(int button_id, long tick);
}
