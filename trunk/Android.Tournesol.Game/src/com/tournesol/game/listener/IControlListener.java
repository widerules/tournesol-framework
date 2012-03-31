package com.tournesol.game.listener;

import java.io.Serializable;

public interface IControlListener<T> extends Serializable {
	void controlChanged(T control, int new_state, int old_state);
}
