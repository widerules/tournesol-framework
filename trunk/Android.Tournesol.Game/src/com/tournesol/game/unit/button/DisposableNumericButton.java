package com.tournesol.game.unit.button;

import com.tournesol.game.unit.button.DisposableButton;
import com.tournesol.game.unit.button.DisposableLeverButton;
import com.tournesol.motion.TouchEvent;

public class DisposableNumericButton extends DisposableButton{

	public int min = 0;
	public int max = 255;
	public int value = 1;
	
	public DisposableLeverButton lever;

	public DisposableNumericButton(){
		this.setValue(1);
		this.lever = new DisposableLeverButton();
	}
	
	public void touch(TouchEvent e){
		super.touch(e);
		
		//Initialiser l'unité de levier 
		if(!lever.alive){
			 lever.show();
			 
			 //Workaround
			 game.addUnit(lever, 1);
		}

	}
	
	@Override
	public void hideChildren(){
		super.hideChildren();
		lever.hide();
	}
	
	public void setValue(int value){
		
		this.value = value;
		
		if(this.value > max)
			this.value = max;
		else if(this.value < min)
			this.value = min;
		
		this.text.chars.set(String.valueOf(this.value));
	}
}
