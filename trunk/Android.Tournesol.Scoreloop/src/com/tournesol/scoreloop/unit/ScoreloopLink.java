package com.tournesol.scoreloop.unit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.scoreloop.client.android.ui.EntryScreenActivity;
import com.scoreloop.client.android.ui.LeaderboardsScreenActivity;
import com.tournesol.game.unit.button.Button;
import com.tournesol.motion.TouchEvent;
import com.tournesol.scoreloop.R;

public class ScoreloopLink extends Button {

	private static final long serialVersionUID = 2662686926608007314L;

	
	public void init(float x, float y, float width, float height){
		
		Bitmap bmp = createBitmap(R.drawable.sl_icon);
		super.init(x, y, bmp.getWidth(), bmp.getHeight());
	}
	
	@Override
	public void draw(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		
		Bitmap bmp = createBitmap(R.drawable.sl_icon);
		c.drawBitmap(bmp, focus.x - bmp.getWidth() / 2, focus.y - bmp.getHeight() / 2, paint);
	}
}
