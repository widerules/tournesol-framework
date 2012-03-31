package com.tournesol.game.shape;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;

public class CollisionArcArc {
	
	private static float arc2_x;
	private static float arc2_y;
	private final static float[] a = new float[6];
	private final static float[] b = new float[6];
	private static float[] ARRAY_COLLIDE = new float[3];
	private static float[] ARRAY_BEZOUT = new float[5];
	private static Polynomial POLYNOMIAL_BEZONT = new Polynomial(ARRAY_BEZOUT);
	private static Polynomial POLYNOMIAL_COLLIDE = new Polynomial(ARRAY_COLLIDE);
	
	private static float rx1;
	private static float ry1;
	private static float rx2;
	private static float ry2;

	private static final float[] yRoots = new float[4];
	private static int yRoots_length = 0;
	
	public static PointF collide(ShapeArc arc1, ShapeArc arc2)
	{
		arc2_x = arc2.getAbsolutePoint().x - arc1.getAbsolutePoint().x;
		arc2_y = arc2.getAbsolutePoint().y - arc1.getAbsolutePoint().y; 
		rx1 = arc1.width / 2;
		ry1 = arc1.height / 2;
		rx2 = arc2.width / 2;
		ry2 = arc2.height / 2;
		
		a[0] = ry1*ry1;
		a[1] = 0;
		a[2] = rx1*rx1;
		a[3] = -2*ry1*ry1*arc1.x;
		a[4] = -2*rx1*rx1*arc1.y;
		a[5] = ry1*ry1*arc1.x*arc1.x + rx1*rx1*arc1.y*arc1.y - rx1*rx1*ry1*ry1;

		b[0] = ry2*ry2;
		b[1] = 0;
		b[2] = rx2*rx2;
		b[3] = -2*ry2*ry2*arc2_x;
		b[4] = -2*rx2*rx2*arc2_y;
		b[5] = ry2*ry2*arc2_x*arc2_x + rx2*rx2*arc2_y*arc2_y - rx2*rx2*ry2*ry2;
		
		Polynomial yPoly = bezout(a, b);
		
		float[] tmp =  yPoly.getRoots();
		yRoots_length = tmp.length;
		for(int i = 0; i < tmp.length ; i++)
			yRoots[i] = tmp[i];
		
		float epsilon = 0.5f;//0.1f;
		float norm0   = ( a[0]*a[0] + 2*a[1]*a[1] + a[2]*a[2] ) * epsilon;
		float norm1   = ( b[0]*b[0] + 2*b[1]*b[1] + b[2]*b[2] ) * epsilon;
		
	     for (int y = 0; y < yRoots_length; y++ ) {
	    	 
	    	 ARRAY_COLLIDE[2] = a[0];
	    	 ARRAY_COLLIDE[1] = a[3] + yRoots[y] * a[1];
	    	 ARRAY_COLLIDE[0] = a[5] + yRoots[y] * (a[4] + yRoots[y]*a[2]);
	    	 POLYNOMIAL_COLLIDE.init(ARRAY_COLLIDE);
	    	 Polynomial xPoly = POLYNOMIAL_COLLIDE;
	    	 //Polynomial xPoly = new Polynomial(ARRAY_COLLIDE);
	    	 
	    	 float[] xRoots = xPoly.getRoots();

	         for (int x = 0; x < xRoots.length; x++ ) {
	        	 float test =
	                 ( a[0]*xRoots[x] + a[1]*yRoots[y] + a[3] ) * xRoots[x] + 
	                 ( a[2]*yRoots[y] + a[4] ) * yRoots[y] + a[5];
	             if ( Math.abs(test) < norm0 ) {
	                 test =
	                     ( b[0]*xRoots[x] + b[1]*yRoots[y] + b[3] ) * xRoots[x] +
	                     ( b[2]*yRoots[y] + b[4] ) * yRoots[y] + b[5];
	                 if ( Math.abs(test) < norm1 ) {
	                	 
	                	 if(arc1.containsCollisionAngle(xRoots[x], yRoots[y]) &&
	                		arc2.containsCollisionAngle(xRoots[x] - arc2_x, yRoots[y] - arc2_y)){
		                	 RecycleBin.collisionPointF.x = xRoots[x] + arc1.getAbsolutePoint().x;
		                	 RecycleBin.collisionPointF.y = yRoots[y] + arc1.getAbsolutePoint().y;
		                	 return RecycleBin.collisionPointF;
	                	 }
	                 }
	             }
	         }
	     }
	     
	     return null;
	}
	

	private static Polynomial bezout(float[] e1, float[] e2) {
		float AB    = e1[0]*e2[1] - e2[0]*e1[1];
	    float AC    = e1[0]*e2[2] - e2[0]*e1[2];
	    float AD    = e1[0]*e2[3] - e2[0]*e1[3];
	    float AE    = e1[0]*e2[4] - e2[0]*e1[4];
	    float AF    = e1[0]*e2[5] - e2[0]*e1[5];
	    float BC    = e1[1]*e2[2] - e2[1]*e1[2];
	    float BE    = e1[1]*e2[4] - e2[1]*e1[4];
	    float BF    = e1[1]*e2[5] - e2[1]*e1[5];
	    float CD    = e1[2]*e2[3] - e2[2]*e1[3];
	    float DE    = e1[3]*e2[4] - e2[3]*e1[4];
	    float DF    = e1[3]*e2[5] - e2[3]*e1[5];
	    float BFpDE = BF + DE;
	    float BEmCD = BE - CD;

	    ARRAY_BEZOUT[0] = AD*DF - AF*AF;
	    ARRAY_BEZOUT[1] = AB*DF + AD*BFpDE - 2*AE*AF;
	    ARRAY_BEZOUT[2] = AB*BFpDE + AD*BEmCD - AE*AE - 2*AC*AF;
	    ARRAY_BEZOUT[3] = AB*BEmCD + AD*BC - 2*AC*AE;
	    ARRAY_BEZOUT[4] = AB*BC - AC*AC;
	    POLYNOMIAL_BEZONT.init(ARRAY_BEZOUT);
	    return POLYNOMIAL_BEZONT;//new Polynomial(ARRAY_BEZOUT);
	    /*
	    return new Polynomial( 
	        AB*BC - AC*AC,
	        AB*BEmCD + AD*BC - 2*AC*AE,
	        AB*BFpDE + AD*BEmCD - AE*AE - 2*AC*AF,
	        AB*DF + AD*BFpDE - 2*AE*AF,
	        AD*DF - AF*AF
	    );*/
	};
	
}
