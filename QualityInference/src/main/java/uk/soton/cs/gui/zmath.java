package uk.soton.cs.gui;

// zmath.java jplewis 97
// modified
// jan02	big in eig33
// feb01	huge bugfix in inv2x2, was only working for positive det!

// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Library General Public
// License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Library General Public License for more details.
// 
// You should have received a copy of the GNU Library General Public
// License along with this library; if not, write to the
// Free Software Foundation, Inc., 59 Temple Place - Suite 330,
// Boston, MA  02111-1307, USA.



import java.io.PrintWriter;

import VisualNumerics.math.*;	// for testing inverse 


/**
 * Misc. math routines,
 * adapted from Mmath.c.
 * routines formerly called Mxx
 *
 * @author jplewis
 */

/****************
#ifdef FPOW
float fpow(b,e)
float b,e;
{ return( exp(log(b)*e) ); }
#endif

#ifdef IMOD
int imod(a,b)
int a,b;
{ return(a - ((a/b)*b)); }
#endif 
****************/

public final class zmath 
{
  public static final  float PI = (float)Math.PI;
  public static final  float TWOPI = (float)(2.0*Math.PI);
  public static final  float DEGRAD = (float)Math.PI / 180.f;
  public static final  float RADDEG = 180.f / (float)Math.PI;

  public static final int imax(int a, int b)
  {
    if (a > b) return(a); else return(b);
  }

  public static final int imin(int a,int b)
  {
    if (a < b) return(a); else return(b);
  }

  public static final int ifloor(double f)
  {
    if ((float)(int)f == f) return((int)f);  /* otherwise floor(-1) => -2 */
    if (f >= 0.0) return((int)f);
    return( (int)f - 1 );
  }

  public static final int round(double a)
  {
    if (a >= 0.0)
	return((int)(a + 0.5));
    else
	return((int)(a - 0.5));
  }

  public static final double fmax(double a,double b)
  {
    if (a > b) return(a); else return(b);
  }

  public static final double fmin(double a, double b)
  {
    if (a < b) return(a); else return(b);
  }

  /**
   * IEEEremainder is screwy?  returns negative numbers
   */
  public static final float rem(float a, float b)
  {
    if (a >= b) {
      int n = (int)(a / b);
      a = a - n*b;
    }
    return a;
  }

  /* just guessing */
  private static float FAPX = 0.00001f;
  private static double DAPX = 0.00000001;

  public static final boolean fapprox(float a,float b)
  {
    double diff;
    diff = a - b;
    if (diff < 0.0) diff = - diff;
    return(diff < FAPX);
  }

  public static final boolean dapprox(double a,double b)
  {
    double diff;
    diff = a - b;
    if (diff < 0.0) diff = - diff;
    return(diff < DAPX);
  }


  public static final int ipower(int x, int n)
  {
    // if x==2 return 1<<n
    int p = 1;
    for( ; n>0; --n)  p *= x;
    return(p);
  }

  public static final double log10(double x)
  {
    return Math.log(x) / Math.log(10.0);
  }


  /**
   * return bounding power of 2
   */
  public static final int nextpowerof2(int len)	
  {
    int plen = 1;
    int ipow = 0;
    while(len > plen) {
	plen *= 2;
	ipow ++;
    }
    return(ipow);
  } /*nextpowerof2*/


  public static final int getpowerof2(int len)
  {
    int p = nextpowerof2(len);
    if (1<<p != len) zliberror.warning("getpowerof2");
    return(p);
  }

  /**
   */
  public static final boolean ispowerof2(int len)
  {
    return (len == 1<<nextpowerof2(len));
  }


  /**
   * round len to the next multiple of mult,
   * e.g. len=9, mult=4, result=12
   */
  public static final int nextmultipleof(int len, int mult)
  {
    return mult*((len + mult - 1) / mult);
  }


  /**
   */
  public static final int binom(int n,int k)
  {
    int b=1;
    for (int i=1; i<=k; i++) b = b*n--/i;
    return(b);
  }

  static void testBinom()
  {
    int n = 20;
    for(int k=1;k<n;k++)
      cstdio.printf("binom(%d,%d) -> %d\n",n,k,binom(n,k));
  }


  /**
   */
  public static final int fact(int n)
  {
     int f=1;
     for (; n>1; --n) f *= n;
     return(f);
  }


  /**
   * greatest common divisor
   */
  public static final int gcd(int a,int b)
  {
    return (a<b) ? gcd(b,a) : b>0 ? gcd(b,a%b) : a;
  }


  /**
   */
  public static final double hippo(double a,double b)	
  {
    return(Math.sqrt(a*a+b*b));
  }

  /**
   * fast inverse of 2x2 matrix.
   * Tested, looks good.
   */
  public static final float inv2x2(float M00, float M01, float M10, float M11,
				   float[][] MI)
  {
    float det = M00 * M11 - M10 * M01;
    if (det != 0.f) {
      MI[0][0] =  M11 / det;
      MI[0][1] = -M01 / det;
      MI[1][0] = -M10 / det;
      MI[1][1] =  M00 / det;
    }
    return det;
  } //inv2x2


  static void testInv2x2()
  {
    float[][] M = new float[2][2];
    float[][] MI = new float[2][2];
    double[][] dM = new double[2][2];

    for( int t=0; t < 100; t++ ) {

      // build random matrix
      for( int r=0; r < 2; r++ ) {
	for( int c=0; c < 2; c++ ) {
	  //M[r][c] = rnd.rndf();
	  M[r][c] = rnd.rndf11();
	  dM[r][c] = M[r][c];
	}
      }

      // symmetric case
      //dM[1][0] = M[1][0] = M[0][1];

      try {
	double[][] dMI = DoubleMatrix.inverse(dM);
	float det = inv2x2(M[0][0],M[0][1],M[1][0],M[1][1], MI);
	System.out.println("det = " + det);

	double maxdiff = 0.;
	for( int r=0; r < 2; r++ ) {
	  for( int c=0; c < 2; c++ ) {
	    double d = MI[r][c] - dMI[r][c];
	    if (d < 0.) d = - d;
	    if (d > maxdiff) maxdiff = d;
	  } //c
	} //r

	System.out.println("maxdiff = " + maxdiff);
	if (maxdiff > 0.01) {
	  matrix.print(new PrintWriter(System.out), "MI", MI);
	  matrix.print(new PrintWriter(System.out), "dMI", dMI);
	}
      }
      catch(Exception x) {
	zliberror.die(x);
      }

    } //t
  } //testInv2x2


  /**
   * Return eigenvalues of a 2x2 SYMMETRIC matrix in v[].
   * If e[] is not null, also return the leading eigenvector.
   * The second eigenvector is of course orthogonal to the first.
   * Return value is false if the matrix is near singular.
   */
  public static final boolean eigvalues22(final float[][] m,
					  float[] v, float[] e)
  {
    float a = m[0][0];
    float b = m[0][1];
    zliberror._assert(b == m[1][0]);
    float c = m[1][1];

    return eigvalues22(a,b,c, v, e);
  }

  public static final boolean eigvalues22(final float[][] m, float[] v)
  {
    return eigvalues22(m, v, null);
  }

  public static final boolean eigvalues22(float a, float b, float c, float[] v)
  {
    return eigvalues22(a, b, c, null);
  }


  /**
   * Return eigenvalues of a 2x2 SYMMETRIC matrix in v[].
   * If e[] is not null, also return the leading eigenvector.
   * The second eigenvector is of course orthogonal to the first.
   * Return value is false if the matrix is near singular.
   *
   * Tested against EigenJacobi routine for a number of random symmetric
   * positive/negative matrices, works!
   * <pre>
   *  [a b]
   *  [b c]
   */
  public static final boolean eigvalues22(float a, float b, float c,
					  float[] v, float[] e)
  {
    boolean singular = false;
    //System.out.println("\neigvalues abc = "+a+" "+b+" "+c);

    float dmin, dmax;
    if (a > c) {
      dmax = a;
      dmin = c;
    }
    else {
      dmax = c;
      dmin = a;
    }

    dmax *= 0.01f;


    if (dmin < dmax) singular = true;
    /****************
    if (dmin < dmax) {
      System.out.println("singular");
      v[1] = 0.f;
      return;
    }
    ****************/

    // diagonal matrix, eigenalues are the diagonal elements
    float babs = (b >= 0.f) ? b : (-b);
    if (babs < dmax) {
      if (a > c) {
	v[0] = a;
	v[1] = c;
	// think this is right, but be careful
	if (e != null) {  e[0] = 1.f;  e[1] = 0.f; }
      }
      else {
	v[0] = c;
	v[1] = a;
	if (e != null) {  e[0] = 0.f;  e[1] = 1.f; }
      }
      return !singular;
    }

    double det = a * c - b * b;
    double apc = a + c;
    double discr = apc * apc - 4. * det;
    if (!(discr >= 0.)) {
      System.out.println("\nproblem.  eigvalues abc = "+a+" "+b+" "+c);
      System.out.println("det = "+det);
      zliberror._assert(discr >= 0., "discr < 0: "+discr);
    }
    double sqrt = Math.sqrt(discr);
    v[0] = (float)((apc + sqrt) / 2.);
    v[1] = (float)((apc - sqrt) / 2.);
    zliberror._assert(v[0] >= v[1], ("eigs wrong order "+v[0]+" "+v[1]));

    if (e != null) {
      e[0] = 2.f * b;
      e[1] = (float)((c - a) + sqrt);
      double len = Math.sqrt(e[0]*e[0] + e[1]*e[1]);
      e[0] = (float)(e[0] / len);
      e[1] = (float)(e[1] / len);
    }

    return !singular;
  } //eigenvalues22



  // matlab: m = [0.9, 0.35; 0.35, 0.4];  eig(m)
  static void testEigenValues22()
  {
    float[][] m = new float[2][2];
    m[0][0] = 0.9f;
    m[0][1] = m[1][0] = 0.35f;
    m[1][1] = 0.4f;

    float[] l = new float[2];
    float[] e = new float[2];
    boolean singular = eigvalues22(m, l, e);

    matrix.print(new PrintWriter(System.out), "m=", m);
    System.out.println("l1="+l[0]+"  l2="+l[1] + " singular="+!singular);
    System.out.println("e1="+e[0]+"  e2="+e[1]);

    // second test: diagonal matrix, eigenvalues are the diagonal elements
    m[0][0] = 0.3f;
    m[0][1] = m[1][0] = 0.0f;
    m[1][1] = 0.4f;
    singular=eigvalues22(m, l, e);
    matrix.print(new PrintWriter(System.out), "m=", m);
    System.out.println("l1="+l[0]+"  l2="+l[1] + " singular="+!singular);
    System.out.println("e1="+e[0]+"  e2="+e[1]);

    // third test
    m[0][0] = 0.99f;
    m[0][1] = m[1][0] = 0.2f; 
    m[1][1] = 0.001f;
    singular=eigvalues22(m, l, e);
    matrix.print(new PrintWriter(System.out), "m=", m);
    System.out.println("l1="+l[0]+"  l2="+l[1] + " singular="+!singular);
    System.out.println("e1="+e[0]+"  e2="+e[1]);

  } //testEigenValues22


  /**
   * Return eigenvalues of a 3x3 SYMMETRIC matrix in v[].
   * Return value is false if the matrix is near singular.
   * Tested, smallest eigenvalue is correct at least.
   */
  public static final boolean eigvalues33(final float[][] M, double[] lambda)
  {
    float trace = M[0][0] + M[1][1] + M[2][2];
    float trace3 = trace / 3.f;
    M[0][0] -= trace3;
    M[1][1] -= trace3;
    M[2][2] -= trace3;

    float a = M[0][0];
    float b = M[1][1];
    float c = M[2][2];
    float d = M[0][1];
    float e = M[0][2];
    float f = M[1][2];

    double p = a*b + a*c + b*c - d*d - e*e - f*f;
    double q = a*f*f + b*e*e + c*d*d - 2.*d*e*f - a*b*c;
    zliberror._assert(p <= 0.);
    //System.out.println("p="+p);
    //System.out.println("q="+q);

    double epsilon = 0.00001;	// Double.MIN_VALUE.  in case p,q are both 0
    double beta = Math.sqrt(-4.*p / 3.);
    double alphaarg = 3.*q / (p*beta - epsilon);
    if (alphaarg > 1.) alphaarg = 1.;
    double alpha = (1./3.)*Math.acos(alphaarg);
    double twopio3 = 2.*Math.PI/3.;
    //System.out.println("alphaarg="+alphaarg);
    //System.out.println("alpha="+alpha);

    lambda[0] = (float)(beta * Math.cos(alpha));
    lambda[1] = (float)(beta * Math.cos(alpha - twopio3));
    lambda[2] = (float)(beta * Math.cos(alpha + twopio3));

    return true;	// todo
  } //eigvalues33


  /**
   * this matrix fails with alphaarg slightly greater than 1,
   * causing alpha to be NaN.
   * Thus check for alphaarg > 1 above
   */
  private static void eigvalues33fails()
  {
    float[][] m = new float[3][3];
    m[0][0] = -59.2151f;
    m[0][1] = -4.6064f;
    m[0][2] = 14.0243f;

    m[1][0] = -4.6064f;
    m[1][1] = -42.9025f;
    m[1][2] = -53.3784f;

    m[2][0] = 14.0243f;
    m[2][1] = -53.3784f;
    m[2][2] = 102.1176f;

    double[] l = new double[3];
    zmath.eigvalues33(m, l);

    if (ArrUtils.hasNaN(l) >= 0) {
      matrix.print("got nan here: m=", m);
      matrix.print("got nan here: l=", l);
      System.exit(1);
    }
  } //eigvalues33fails


  /** W.Clocksin implemented this version.  TODO double check
   public static double gaussEval(int nd, double [] data, double [] means, double [][] covar)
   {
     int i;
     double s = 0, a = 1;
     double [] arg;
     double [] x = new double[nd];
     for (i = 0; i < nd; i++) x[i] = data[i] - means[i];
     try
     {
       a = Math.pow(2.0 * Math.PI, nd / 2.0) * Math.sqrt(DoubleMatrix.determinant(covar));
       arg = DoubleMatrix.multiply(x, DoubleMatrix.inverse(covar));
       s = 0.0;
       for (i = 0; i < nd; i++) s += arg[i] * x[i];
     } catch (MathException e) {};
     return  Math.exp(-0.5 * s) / a;
   }
  */


  /**
   * evaluate multi-dimensional gaussian.
   */
  public static final double gaussEval(double[] data,
				       double[] mean,
				       double[][] invcovariance)
    throws Exception
  {
    zliberror._assert(mean.length == data.length);

    int nd = mean.length;
    double det = DoubleMatrix.determinant(invcovariance);
    double f1 = Math.pow(2.*Math.PI, nd/2.);
    double f2 = 1. / Math.sqrt(det);  // det(inverse) = 1/det(original)
    double scale = 1. / (f1 * f2);

    return gaussEval(data, mean, invcovariance, scale);
  } //gaussEval


  /**
   * Return the scale part of the gaussian - requires sqrt,pow,
   * detminant of covariance.  If the probability of a bunch of points
   * wrt a fixed gaussian is being evaluated it is faster to evaluate
   * the scale outside the loop.
   */
  public static final double gaussEvalScale(double[] mean,
					    double[][] invcovariance)
    throws Exception
  {
    int nd = mean.length;
    double det = DoubleMatrix.determinant(invcovariance);
    double f1 = Math.pow(2.*Math.PI, nd/2.);
    double f2 = 1. / Math.sqrt(det);  // det(inverse) = 1/det(original)
    double scale = 1. / (f1 * f2);

    return scale;
  } //gaussEvalScale


  /**
   * evaluate multi-dimensional gaussian.
   */
  public static final double gaussEval(double[] data,
				       double[] mean,
				       double[][] invcovariance,
				       double scale)
  {
    int nd = data.length;
    double sum = 0.;

    for( int r=0; r < nd; r++ ) {
      double rsum = 0.;
      for( int c=0; c < nd; c++ ) {
	rsum += invcovariance[r][c] * (data[c] - mean[c]);
      }
      sum += (rsum * (data[r] - mean[r]));
    }

    return scale * Math.exp(-0.5 * sum);
  } //gaussEval


  /**
   * quadratic, float version
   * @return number of real roots (0,1,2)
   */
  public static int quadsolve(float a, float b, float c, float[] roots)
  {
    float discrim = b*b - 4.f*a*c;

    if (discrim < 0.f)
      return 0;
    else if (discrim == 0.f) {
      roots[0] = -b/(2.f*a);
      return 1;
    }
    else {
      discrim = (float)Math.sqrt(discrim);
      roots[0] = (-b + discrim)/(2.f*a);
      roots[1] = (-b - discrim)/(2.f*a);
      return 2;
    }
  } //quadsolve


  /**
   * quadratic
   * @return number of real roots (0,1,2)
   */
  public static int quadsolve(double a, double b, double c, double[] roots)
  {
    double discrim = b*b - 4.*a*c;

    if (discrim < 0.)
      return 0;
    else if (discrim == 0.) {
      roots[0] = -b/(2.*a);
      return 1;
    }
    else {
      discrim = Math.sqrt(discrim);
      roots[0] = (-b + discrim)/(2.*a);
      roots[1] = (-b - discrim)/(2.*a);
      return 2;
    }
  } //quadsolve

  //----------------------------------------------------------------

  // some code wants that name pythag also
   public final static double hypot(double a, double b) {
     return Math.sqrt(a*a + b*b);
   }

   /**
    * sqrt(a^2 + b^2) without under/overflow.
    * this approach recommended by e.g. jama
    */
  public final static double Hypot(double a, double b)
  {
     double r;
     if (Math.abs(a) > Math.abs(b)) {
       r = b/a;
       r = Math.abs(a)*Math.sqrt(1.+r*r);
     } else if (b != 0.) {
       r = a/b;
       r = Math.abs(b)*Math.sqrt(1.+r*r);
     } else {
       r = 0.0;
     }
     return r;
   }

  // was called len
  public final static double dist(double ix, double iy, double lx, double ly)
  {
      double dx2 = ix - lx;  dx2 = dx2*dx2;
      double dy2 = iy - ly;  dy2 = dy2*dy2;
      double len2 = dx2 + dy2;
      return Math.sqrt(len2);
  } //dist


  

  //================ TEST ================

  public static void main(String[] args)
  {
    //testBinom();
    //testEigenValues22();
    //testInv2x2();
    testBeta();
  } //main

} //zmath


