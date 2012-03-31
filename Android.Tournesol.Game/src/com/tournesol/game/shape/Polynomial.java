package com.tournesol.game.shape;

import android.util.FloatMath;

public class Polynomial {

	private static final float TOLERANCE = 1e-6f;
	
	private static final float[] RESULT_ZERO_ROOT = new float[0];
	private static final float[] RESULT_ONE_ROOT = new float[1];
	private static final float[] RESULT_TWO_ROOT = new float[2];
	private static final float[] RESULT_THREE_ROOT = new float[3];
	private static final float[] RESULT_FOUR_ROOT = new float[4];

	//Coefficients du plus grand au plus petit
	private float[] coefficients;
	private int degree;
	
	public Polynomial(float[] coefficients){
		
		this.coefficients = coefficients;
		this.degree = coefficients.length - 1;
		/*
		this.coefficients = new float[coefficients.length];
		for(int i = 0 ; i < coefficients.length ; i++)
			this.coefficients[i] = coefficients[coefficients.length - 1 - i];
		
		this.degree = coefficients.length - 1;
		*/
	}
	
	public void init(float[] coefficients){
		this.coefficients = coefficients;
		this.degree = coefficients.length - 1;
	}
	
	public float[] getRoots(){
		float[] result;

	    this.simplify();
	    switch (this.degree) {
	        case 0: result = RESULT_ZERO_ROOT;         break;
	        case 1: result = this.getLinearRoot();     break;
	        case 2: result = this.getQuadraticRoots(); break;
	        case 3: result = this.getCubicRoots();     break;
	        case 4: result = this.getQuarticRoots();   break;
	        default:
	            result = RESULT_ZERO_ROOT;
	            // should try Newton's method and/or bisection
	    }

	    return result;
	};
	
	private void simplify() {
		
		while(degree >= 0 && Math.abs(this.coefficients[degree]) <= TOLERANCE){
			degree--;
		}
		
	};
	
	public float[] getLinearRoot() {
		float a = this.coefficients[1];
	    
	    if ( a != 0 ){
	    	RESULT_ONE_ROOT[0] = -this.coefficients[0] / a;
	    	return RESULT_ONE_ROOT;
	    }

	    return RESULT_ZERO_ROOT;
	};
	
	
	private float[] getQuadraticRoots() {

	    if ( this.degree == 2 ) {
	        float a = this.coefficients[2];
	        float b = this.coefficients[1] / a;
	        float c = this.coefficients[0] / a;
	        float d = b*b - 4*c;

	        if ( d > 0 ) {
	        	float e = FloatMath.sqrt(d);
	        	RESULT_TWO_ROOT[0] = 0.5f * (-b + e);
	        	RESULT_TWO_ROOT[1] = 0.5f * (-b - e);
	        	return RESULT_TWO_ROOT;
	        } else if (d == 0) {
	        	RESULT_ONE_ROOT[0] = 0.5f * -b;
	        	return RESULT_ONE_ROOT;
	        }
	    }

	    return RESULT_ZERO_ROOT;
	};
	
	private float[] getCubicRoots() {

	    if ( this.degree == 3 ) {
	    	double c3 = this.coefficients[3];
	    	double c2 = this.coefficients[2] / c3;
	    	double c1 = this.coefficients[1] / c3;
	    	double c0 = this.coefficients[0] / c3;

	    	double a       = (3*c1 - c2*c2) / 3;
	    	double b       = (2*c2*c2*c2 - 9*c1*c2 + 27*c0) / 27;
	    	double offset  = c2 / 3;
	        double discrim = b*b/4 + a*a*a/27;
	        double halfB   = b / 2;

	        if ( Math.abs(discrim) <= Polynomial.TOLERANCE ) 
	        	discrim = 0;
	        
	        if ( discrim > 0 ) {
	        	double e = Math.sqrt(discrim);
	        	double tmp;
	        	double root;

	            tmp = -halfB + e;
	            if ( tmp >= 0 )
	                root = Math.pow(tmp, 1/3f);
	            else
	                root = -Math.pow(-tmp, 1/3f);

	            tmp = -halfB - e;
	            if ( tmp >= 0 )
	                root += Math.pow(tmp, 1/3f);
	            else
	                root -= Math.pow(-tmp, 1/3f);

	            RESULT_ONE_ROOT[0] = (float)(root - offset);
	            return RESULT_ONE_ROOT;
	        } else if ( discrim < 0 ) {
	        	double distance = Math.sqrt(-a/3);
	        	double angle    = Math.atan2( Math.sqrt(-discrim), -halfB) / 3f;
	        	double cos      = Math.cos(angle);
	        	double sin      = Math.sin(angle);
	        	double sqrt3    = Math.sqrt(3);

	            RESULT_THREE_ROOT[0] = (float)(2*distance*cos - offset);
	            RESULT_THREE_ROOT[1] = (float)(-distance * (cos + sqrt3 * sin) - offset);
	            RESULT_THREE_ROOT[2] = (float)(-distance * (cos - sqrt3 * sin) - offset);
	            return RESULT_THREE_ROOT;
	        } else {
	        	double tmp;

	            if ( halfB >= 0 )
	                tmp = -Math.pow(halfB, 1/3f);
	            else
	                tmp = Math.pow(-halfB, 1/3f);

	            RESULT_TWO_ROOT[0] = (float)(2*tmp - offset);
	            RESULT_TWO_ROOT[1] = (float)(-tmp - offset);
	            return RESULT_TWO_ROOT;
	        }
	    }

	    return RESULT_ZERO_ROOT;
	};
	
	
	private static float[] POLYNOMIAL_QUARTIC_ROOTS = new float[4];
	private float[] getQuarticRoots() {

	    if ( degree == 4 ) {
	    	float c4 = this.coefficients[4];
	        float c3 = this.coefficients[3] / c4;
	        float c2 = this.coefficients[2] / c4;
	        float c1 = this.coefficients[1] / c4;
	        float c0 = this.coefficients[0] / c4;

	        /*
	        float[] resolveRoots = new Polynomial(
	            1, -c2, c3*c1 - 4*c0, -c3*c3*c0 + 4*c2*c0 -c1*c1).getCubicRoots();
	        */
	        POLYNOMIAL_QUARTIC_ROOTS[0] = -c3*c3*c0 + 4*c2*c0 -c1*c1;
	        POLYNOMIAL_QUARTIC_ROOTS[1] = c3*c1 - 4*c0;
	        POLYNOMIAL_QUARTIC_ROOTS[2] = -c2;
	        POLYNOMIAL_QUARTIC_ROOTS[3] = 1;
	        float[] resolveRoots = new Polynomial(POLYNOMIAL_QUARTIC_ROOTS).getCubicRoots();
	        
	        if(resolveRoots.length == 0)
	        	return RESULT_ZERO_ROOT;
	        
	        float y = resolveRoots[0];
	        float discrim = c3*c3/4 - c2 + y;

	        if ( Math.abs(discrim) <= Polynomial.TOLERANCE ) 
	        	discrim = 0;

	        if ( discrim > 0 ) {
	        	float e     = FloatMath.sqrt(discrim);
	        	float t1    = 3*c3*c3/4 - e*e - 2*c2;
	        	float t2    = ( 4*c3*c2 - 8*c1 - c3*c3*c3 ) / ( 4*e );
	        	float plus  = t1+t2;
	        	float minus = t1-t2;

	            if ( Math.abs(plus)  <= Polynomial.TOLERANCE ) plus  = 0;
	            if ( Math.abs(minus) <= Polynomial.TOLERANCE ) minus = 0;

	            if ( plus >= 0 && minus >= 0 ) {
	            	float f = FloatMath.sqrt(plus);
	            	RESULT_FOUR_ROOT[0] = -c3/4 + (e+f)/2;
	            	RESULT_FOUR_ROOT[1] = -c3/4 + (e-f)/2;
	            	
	            	f = FloatMath.sqrt(minus);
	            	RESULT_FOUR_ROOT[2] = -c3/4 + (f-e)/2;
	            	RESULT_FOUR_ROOT[3] = -c3/4 - (f+e)/2;
	            	return RESULT_FOUR_ROOT;
	            }
	            if ( plus >= 0 ) {
	            	float f = FloatMath.sqrt(plus);
	            	RESULT_TWO_ROOT[0] = -c3/4 + (e+f)/2;
	            	RESULT_TWO_ROOT[1] = -c3/4 + (e-f)/2;
	            	return RESULT_TWO_ROOT;
	            }
	            if ( minus >= 0 ) {
	                float f = FloatMath.sqrt(minus);
	                RESULT_TWO_ROOT[0] = -c3/4 + (f-e)/2;
	                RESULT_TWO_ROOT[1] = -c3/4 - (f+e)/2;
	            	return RESULT_TWO_ROOT;
	            }
	        } else if ( discrim < 0 ) {
	            return RESULT_ZERO_ROOT;
	        } else {
	            float t2 = y*y - 4*c0;

	            if ( t2 >= -Polynomial.TOLERANCE ) {
	                if ( t2 < 0 ) t2 = 0;

	                t2 = 2*FloatMath.sqrt(t2);
	                float t1 = 3*c3*c3/4 - 2*c2;
	                
	                if ( t1+t2 >= Polynomial.TOLERANCE && t1-t2 >= Polynomial.TOLERANCE ) {
	                	float d = FloatMath.sqrt(t1+t2);
	                	RESULT_FOUR_ROOT[0] = -c3/4 + d/2;
	                	RESULT_FOUR_ROOT[1] = -c3/4 - d/2;
	                	
	                	d = FloatMath.sqrt(t1-t2);
	                	RESULT_FOUR_ROOT[2] = -c3/4 + d/2;
	                	RESULT_FOUR_ROOT[3] = -c3/4 - d/2;
	                    return RESULT_FOUR_ROOT;
	                }
	                
	                if ( t1+t2 >= Polynomial.TOLERANCE ) {
	                	float d = FloatMath.sqrt(t1+t2);
	                	RESULT_TWO_ROOT[0] = -c3/4 + d/2;
	                	RESULT_TWO_ROOT[1] = -c3/4 - d/2;
	                    return RESULT_TWO_ROOT;
	                }
	                
	                if ( t1-t2 >= Polynomial.TOLERANCE ) {
	                	float d = FloatMath.sqrt(t1-t2);
	                	RESULT_TWO_ROOT[0] = -c3/4 + d/2;
	                	RESULT_TWO_ROOT[1] = -c3/4 - d/2;
	                    return RESULT_TWO_ROOT;
	                }
	            }
	        }
	    }

	    return RESULT_ZERO_ROOT;
	};
	
}
