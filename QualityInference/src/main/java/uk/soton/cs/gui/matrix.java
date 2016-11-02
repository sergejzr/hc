package uk.soton.cs.gui;

// matrix.java jplewis 99
// modified
// jan03	minor
// dec01	scruff	
// nov01	scruff	convenience methods
// feb01	scruff  printFull
// dec00	scruff	floatClone/doubleClone methods
// nov00	scruff
// sep00	switch from cstdio.printf to numberformat

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
//
// contact info:  zilla@computer.org


import java.io.*;
import java.text.NumberFormat;

//import zlib.*;

final public class matrix
{
  // beware _stdout does not get flushed the same way/time as system.out.
  public static PrintWriter  _stdout = new PrintWriter(System.out);
  public static NumberFormat _nf;
  public static NumberFormat _if;	// for ints

  static {
    _nf = NumberFormat.getInstance();
    _nf.setMinimumFractionDigits(3);
    _nf.setMaximumFractionDigits(3);
    _nf.setGroupingUsed ( false );	// do not do 15,000  (no comma)

    // for integers
    _if = NumberFormat.getInstance();
    _if.setMinimumFractionDigits(0);
    _if.setMaximumFractionDigits(0);
  }

  /**
   */
  public static void setFractionDigits(int n)
  {
    _nf.setMinimumFractionDigits(n);
    _nf.setMaximumFractionDigits(n);
  }

  //----------------------------------------------------------------
  // matrix.  int,float,double
  //----------------------------------------------------------------

  /**
   */
  public static void print(String msg, int[][] M)
  {
    print(_stdout, msg, M);
    _stdout.flush();
  }

  /**
   * TODO: does printing int with _if work?
   */
  public static void print(PrintWriter f, String msg, int[][] M)
  {
    int nr = M.length;
    int nc = M[0].length;

    f.println(msg);

    for( int ir=0; ir < nr; ir++ ) {
      f.print("[ ");
      if (nc <= 10)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_if.format(M[ir][ic]) + "  ");
	}
	else { /* elide */
	  for( int ic=0; ic < 4; ic++ ) 
	    f.print(_if.format(M[ir][ic]) + "  ");
	  f.print(" ... ");
	  for( int ic=nc-2; ic < nc; ic++ ) 
	    f.print(_if.format(M[ir][ic]) + "  ");
	}

      f.println("]");
    }

    f.flush();
  } //print(int)

  //----------------------------------------------------------------

  /**
   */
  public static void print(String msg, float[][] M)
  {
    print(_stdout, msg, M);
    _stdout.flush();
  }

  /**
   */
  public static void print(PrintWriter f, String msg, float[][] M)
  {
    int nr = M.length;
    int nc = M[0].length;

    f.println(msg);

    for( int ir=0; ir < nr; ir++ ) {
      f.print("[ ");
      if (nc <= 10)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(M[ir][ic]) + "  ");
	}
	else { /* elide */
	  for( int ic=0; ic < 4; ic++ ) 
	    f.print(_nf.format(M[ir][ic]) + "  ");
	  f.print(" ... ");
	  for( int ic=nc-2; ic < nc; ic++ ) 
	    f.print(_nf.format(M[ir][ic]) + "  ");
	}

      f.println("]");
    }

    f.flush();
  } //print

  //----------------------------------------------------------------

  /**
   */
  public static void print(String msg, double[][] M)
  {
    print(_stdout, msg, M);
    _stdout.flush();
  }

  /**
   */
  public static void print(PrintWriter f, String msg, double[][] M)
  {
    int nr = M.length;
    int nc = M[0].length;

    f.println(msg);

    for( int ir=0; ir < nr; ir++ ) {
      f.print("[ ");
      if (nc <= 10)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(M[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(M[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(M[ir][ic]) + "  ");
      }

      f.println("]");
    }

    f.flush();
  } //print

  //----------------------------------------------------------------
  // vector.  short,int,float,double
  //----------------------------------------------------------------

  /**
   */
  public static void print(String msg, short[] v)
  {
    print(_stdout, msg, v);
    _stdout.flush();
  }

  /**
   */
  public static void print(PrintWriter f, String msg, short[] v)
  {
    int n = v.length;

    f.println(msg);
    f.print("[ ");

    if (n <= 10)   
      for(int ic=0; ic<n; ic++) {
	f.print(_if.format(v[ic]) + "  ");
      }
    else { /* elide */
      for( int ic=0; ic < 4; ic++ ) 
	f.print(_if.format(v[ic]) + "  ");
      f.print(" ... ");
      for( int ic=n-2; ic < n; ic++ ) 
	f.print(_if.format(v[ic]) + "  ");
    }

    f.println("]");

    f.flush();
  } //print(short vector)

  //----------------------------------------------------------------
  /**
   */
  public static void print(String msg, int[] v)
  {
    print(_stdout, msg, v);
    _stdout.flush();
  }

  /**
   */
  public static void print(PrintWriter f, String msg, int[] v)
  {
    int n = v.length;

    f.println(msg);
    f.print("[ ");

    if (n <= 10)   
      for(int ic=0; ic<n; ic++) {
	f.print(_if.format(v[ic]) + "  ");
      }
    else { /* elide */
      for( int ic=0; ic < 4; ic++ ) 
	f.print(_if.format(v[ic]) + "  ");
      f.print(" ... ");
      for( int ic=n-2; ic < n; ic++ ) 
	f.print(_if.format(v[ic]) + "  ");
    }

    f.println("]");

    f.flush();
  } //print(int vector)

  //----------------------------------------------------------------

  /**
   */
  public static void print(String msg, float[] v)
  {
    print(_stdout, msg, v);
    _stdout.flush();
  }

  /**
   */
  public static void print(PrintWriter f, String msg, float[] v)
  {
    int n = v.length;

    f.println(msg);
    f.print("[ ");

    if (n <= 10)   
      for(int ic=0; ic<n; ic++) {
	f.print(_nf.format(v[ic]) + "  ");
      }
    else { /* elide */
      for( int ic=0; ic < 4; ic++ ) 
	f.print(_nf.format(v[ic]) + "  ");
      f.print(" ... ");
      for( int ic=n-2; ic < n; ic++ ) 
	f.print(_nf.format(v[ic]) + "  ");
    }

    f.println("]");

    f.flush();
  } //print(float vector)

  //----------------------------------------------------------------

  /**
   */
  public static void print(String msg, double[] v)
  {
    print(_stdout, msg, v);
    _stdout.flush();
  }

  /**
   */
  public static void print(PrintWriter f, String msg, double[] v)
  {
    int n = v.length;

    f.println(msg);
    f.print("[ ");

    if (n <= 10)   
      for(int ic=0; ic<n; ic++) {
	f.print(_nf.format(v[ic]) + "  ");
      }
    else { /* elide */
      for( int ic=0; ic < 4; ic++ ) 
	f.print(_nf.format(v[ic]) + "  ");
      f.print(" ... ");
      for( int ic=n-2; ic < n; ic++ ) 
	f.print(_nf.format(v[ic]) + "  ");
    }

    f.println("]");

    f.flush();
  } //print(double vector)

  //----------------------------------------------------------------

  /**
   * print rows of A, B side by side for comparison
   */
  public static void printCompare(PrintWriter f, String msg,
				  double[][] A, double[][] B)
  {
    int nr = A.length;
    int nc = A[0].length;
    zliberror._assert(B.length == A.length);
    zliberror._assert(B[0].length == A[0].length);

    f.println(msg);

    for( int ir=0; ir < nr; ir++ ) {
      f.print("[ ");
      if (nc <= 5)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(A[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(A[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(A[ir][ic]) + "  ");
      }

      f.print("] vs. [");

      if (nc <= 5)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(B[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(B[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(B[ir][ic]) + "  ");
      }

      f.println("]");
    }

    f.flush();
  } //printCompare

  //----------------------------------------------------------------

  /**
   * print rows of A, B side by side for comparison
   */
  public static void printCompare(PrintWriter f, String msg,
				  float[][] A, float[][] B)
  {
    int nr = A.length;
    int nc = A[0].length;
    zliberror._assert(B.length == A.length);
    zliberror._assert(B[0].length == A[0].length);

    f.println(msg);

    for( int ir=0; ir < nr; ir++ ) {
      f.print("[ ");
      if (nc <= 5)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(A[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(A[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(A[ir][ic]) + "  ");
      }

      f.print("] vs. [");

      if (nc <= 5)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(B[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(B[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(B[ir][ic]) + "  ");
      }

      f.println("]");
    }

    f.flush();
  } //printCompare

  //----------------------------------------------------------------

  /**
   * print rows of A, B side by side for comparison.
   * float vs. double version.
   */
  public static void printCompare(PrintWriter f, String msg,
				  float[][] A, double[][] B)
  {
    int nr = A.length;
    int nc = A[0].length;
    zliberror._assert(B.length == A.length);
    zliberror._assert(B[0].length == A[0].length);

    f.println(msg);

    for( int ir=0; ir < nr; ir++ ) {
      f.print("[ ");
      if (nc <= 5)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(A[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(A[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(A[ir][ic]) + "  ");
      }

      f.print("] vs. [");

      if (nc <= 5)   
	for(int ic=0; ic<nc; ic++) {
	  f.print(_nf.format(B[ir][ic]) + "  ");
	}
      else { /* elide */
	for( int ic=0; ic < 4; ic++ ) 
	  f.print(_nf.format(B[ir][ic]) + "  ");
	f.print(" ... ");
	for( int ic=nc-2; ic < nc; ic++ ) 
	  f.print(_nf.format(B[ir][ic]) + "  ");
      }

      f.println("]");
    }

    f.flush();
  } //printCompare float vs double

  //----------------------------------------------------------------

  /**
   * print the whole thing to a file, for debugging.
   */
  public static void printFull(String msg, float[][] A, String path)
    throws IOException
  {
    PrintWriter f
      = (path.equals("stdout")
	 ? _stdout
	 : new PrintWriter(new BufferedWriter(new FileWriter(path))));
    f.print(msg);

    int yres = A.length;
    int xres = A[0].length;

    for( int y=0; y < yres; y++ ) {
      
      for( int x=0; x < xres; x++ ) {
	if (x%7 == 0) {
	  f.println("");
	  int end = ((x+6) >= xres) ? (xres-1) : (x+6);
	  f.print("row "+y+" "+x+".."+end+": ");
	}
	f.print(_nf.format(A[y][x]) + "  ");	
      }
      f.println("");

    } //y
    
    if (f == _stdout)
      f.flush();
    else 
      f.close();
  } //printFull


  /**
   * print the whole thing to a file, for debugging.
   */
  public static void printFull(String msg, double[][] A, String path)
    throws IOException
  {
    PrintWriter f
      = (path.equals("stdout")
	 ? _stdout
	 : new PrintWriter(new BufferedWriter(new FileWriter(path))));
    f.print(msg);

    int yres = A.length;
    int xres = A[0].length;

    for( int y=0; y < yres; y++ ) {
      
      for( int x=0; x < xres; x++ ) {
	if (x%7 == 0) {
	  f.println("");
	  int end = ((x+6) >= xres) ? (xres-1) : (x+6);
	  f.print("row "+y+" "+x+".."+end+": ");
	}
	f.print(_nf.format(A[y][x]) + "  ");	
      }
      f.println("");

    } //y
    
    if (f == _stdout)
      f.flush();
    else 
      f.close();
  } //printFull

  //----------------------------------------------------------------

  /**
   * print every element of a 1d float array, for detailed debugging.
   */
  public static void printFull(String msg, float[] v, String path)
    throws IOException
  {
    PrintWriter f
      = (path.equals("stdout")
	 ? _stdout
	 : new PrintWriter(new BufferedWriter(new FileWriter(path))));
    f.print(msg);

    int len = v.length;

    for( int x=0; x < len; x++ ) {
      if (x%7 == 0) {
	f.println("");
	int end = ((x+6) >= len) ? (len-1) : (x+6);
	f.print(x+".."+end+": ");
      }
      f.print(_nf.format(v[x]) + "  ");	
    } //x
    f.println("");

    if (f == _stdout)
      f.flush();
    else 
      f.close();
  } //printFull


  /**
   * print every element of a 1d double array, for detailed debugging.
   */
  public static void printFull(String msg, double[] v, String path)
    throws IOException
  {
    PrintWriter f
      = (path.equals("stdout")
	 ? _stdout
	 : new PrintWriter(new BufferedWriter(new FileWriter(path))));
    f.print(msg);

    int len = v.length;
f.println("--> v[0] = " + v[0]);

    for( int x=0; x < len; x++ ) {
      if (x%7 == 0) {
	f.println("");
	int end = ((x+6) >= len) ? (len-1) : (x+6);
	f.print(x+".."+end+": ");
      }
      f.print(_nf.format(v[x]) + "  ");	
    } //x
    f.println("");

    if (f == _stdout)
      f.flush();
    else 
      f.close();
  } //printFull


  //----------------------------------------------------------------
  // dimension compare
  //----------------------------------------------------------------

  /** @deprecated moved to zlib.array */
  public static void assertCongruent(int[] v1, int[] v2)
  {
    zliberror._assert(v1.length == v2.length,
		     "vector length mismatch");
  } //assertCongruent

  /** @deprecated moved to zlib.array */
  public static void assertCongruent(float[] v1, float[] v2)
  {
    zliberror._assert(v1.length == v2.length,
		     "vector length mismatch");
  } //assertCongruent

  /** @deprecated moved to zlib.array */
  public static void assertCongruent(double[] v1, double[] v2)
  {
    zliberror._assert(v1.length == v2.length,
		     "vector length mismatch");
  } //assertCongruent

  /** @deprecated moved to zlib.array */
  public static void assertCongruent(int[][] m1, int[][] m2)
  {
    zliberror._assert((m1.length == m2.length) &&
		     (m1[0].length == m2[0].length),
		     "matrix dimension mismatch");
  } //assertCongruent

  /** @deprecated moved to zlib.array */
  public static void assertCongruent(float[][] m1, float[][] m2)
  {
    zliberror._assert((m1.length == m2.length) &&
		     (m1[0].length == m2[0].length),
		     "matrix dimension mismatch");
  } //assertCongruent

  /** @deprecated moved to zlib.array */
  public static void assertCongruent(double[][] m1, double[][] m2)
  {
    zliberror._assert((m1.length == m2.length) &&
		     (m1[0].length == m2[0].length),
		     "matrix dimension mismatch");
  } //assertCongruent

  /** @deprecated moved to zlib.array */
  public static void assertDim(int[][] m, int nr, int nc)
  {
    zliberror._assert((m.length == nr) && (m[0].length == nc));
  } //assertDim

  /** @deprecated moved to zlib.array */
  public static void assertDim(float[][] m, int nr, int nc)
  {
    zliberror._assert((m.length == nr) && (m[0].length == nc));
  } //assertDim

  /** @deprecated moved to zlib.array */
  public static void assertDim(double[][] m, int nr, int nc)
  {
    zliberror._assert((m.length == nr) && (m[0].length == nc));
  } //assertDim

  /** @deprecated moved to zlib.array */
  public static void assertDim(int[] m, int nr)
  {
    zliberror._assert(m.length == nr);
  } //assertDim

  /** @deprecated moved to zlib.array */
  public static void assertDim(float[] m, int nr)
  {
    zliberror._assert(m.length == nr);
  } //assertDim

  /** @deprecated moved to zlib.array */
  public static void assertDim(double[] m, int nr)
  {
    zliberror._assert(m.length == nr);
  } //assertDim

  //----------------------------------------------------------------

  /**
   * data is an array of n-dimensional data, not a matrix
   * depending on how you look at it.
   * Get the min/max bounds for each dimension.
   * Could be called column bounds.
   */
  public static void getNDBounds(double[][] data, double[][] bounds)
  {
    int nd = data[0].length;
    zliberror._assert(nd == bounds.length, "matrix.getNDbounds:1");
    zliberror._assert(bounds[0].length == 2, "matrix.getNDbounds:2");

    for( int id=0; id < nd; id++ ) {
      bounds[id][0] = Double.POSITIVE_INFINITY;
      bounds[id][1] = Double.NEGATIVE_INFINITY;
    }

    int ndata = data.length;
    for( int i=0; i < ndata; i++ ) {
      for( int id=0; id < nd; id++ ) {
	double v = data[i][id];
	if (v < bounds[id][0])  bounds[id][0] = v;
	if (v > bounds[id][1])  bounds[id][1] = v;
      }
    }
  } //getBounds

  //----------------------------------------------------------------
  // clone 
  //----------------------------------------------------------------

  /** @deprecated moved to zlib.array */
  public static double[][] clone(double[][] m)
  {
    double[][] mc = (double[][])m.clone();
    for( int r=0; r < mc.length; r++ ) {
      mc[r] = (double[])m[r].clone();
    }
    return mc;
  } //clone

  /**
   * return a double[][] copy of this float matrix
   * @deprecated moved to zlib.array
   */
  public static double[][] doubleClone(float[][] matrix)
  {
    int nr = matrix.length;
    int nc = matrix[0].length;

    double[][] cmatrix = new double[nr][nc];

    for( int r = 0; r < nr; r++ ) {
      for( int c = 0; c < nc; c++ ) {
	cmatrix[r][c] = matrix[r][c];
      }
    }

    return cmatrix;
  } //doubleClone

  //----------------------------------------------------------------

  /**
   * return a double[] copy of this float vector
   * @deprecated moved to zlib.array
   */
  public static double[] doubleClone(float[] vec)
  {
    int nr = vec.length;

    double[] cvec = new double[nr];

    for( int r = 0; r < nr; r++ ) {
      cvec[r] = vec[r];
    }

    return cvec;
  } //doubleClone

  //----------------------------------------------------------------

  /**
   * return a float[][] copy of this double matrix
   * @deprecated moved to zlib.array
   */
  public static float[][] floatClone(double[][] matrix)
  {
    int nr = matrix.length;
    int nc = matrix[0].length;

    float[][] cmatrix = new float[nr][nc];

    for( int r = 0; r < nr; r++ ) {
      for( int c = 0; c < nc; c++ ) {
	cmatrix[r][c] = (float)matrix[r][c];
      }
    }

    return cmatrix;
  } //floatClone

  //----------------------------------------------------------------

  /**
   * return a float[] copy of this double vector
   * @deprecated moved to zlib.array
   */
  public static float[] floatClone(double[] vec)
  {
    int nr = vec.length;

    float[] cvec = new float[nr];

    for( int r = 0; r < nr; r++ ) {
      cvec[r] = (float)vec[r];
    }

    return cvec;
  } //floatClone

  //----------------------------------------------------------------
  //----------------------------------------------------------------
  // actual matrix operations
  //----------------------------------------------------------------
  //----------------------------------------------------------------

  //----------------------------------------------------------------
  // zero
  //----------------------------------------------------------------

  /**
   */
  public static void zero(float[][] mat)
  {
    int nr = mat.length;
    int nc = mat[0].length;

    for( int r = 0; r < nr; r++ ) {
      float[] mr = mat[r];
      for( int c = 0; c < nc; c++ ) {
	mr[c] = 0.f;
      }
    }
  } //zero


  /**
   */
  public static void zero(double[][] mat)
  {
    int nr = mat.length;
    int nc = mat[0].length;

    for( int r = 0; r < nr; r++ ) {
      double[] mr = mat[r];
      for( int c = 0; c < nc; c++ ) {
	mr[c] = 0.;
      }
    }
  } //zero


  //----------------------------------------------------------------
  // colonEx, colonSet - matlab : operator
  //----------------------------------------------------------------

  /**
   * colonExtract, like the matlab : operator, but only works with 2d arrays
   * colonex(0,0) = matlab mat(:,1)   result is [nr,1]
   * colonex(0,1) = matlab mat(:,2)
   * colonex(1,0) = matlab mat(1,:)
   * colonex(1,1) = matlab mat(2,:)   result is [1,nc]
   */
  public static int[][] colonEx(int[][] mat, int dim, int el)
  {
    int nr = mat.length;
    int nc = mat[0].length;
    int[] size = new int[]{ nr,nc };

    int[][] v = null;

    // dim=0
    if (dim == 0) {
      int vdim = size[dim];
      v = new int[vdim][1];
      for( int i = 0; i < vdim; i++ ) {
	v[i][0] = mat[i][el];
      }
    }

    else if (dim == 1) {
      int vdim = size[dim];
      v = new int[1][vdim];
      for( int i = 0; i < vdim; i++ ) {
	v[0][i] = mat[el][i];
      }
    }

    else zliberror._assert(false, "colonEx only handles 2d arrays");

    return v;
  } //colonEx


  /**
   * colonSet(m,0,k,v) = matlab mat(:,k+1) = v
   * colonSet(m,1,k,v) = matlab mat(k+1,:) = v
   */
  public static void colonSet(int[][] mat, int dim, int el, int[][] v)
  {
    int nr = mat.length;
    int nc = mat[0].length;
    int[] size = new int[]{ nr,nc };

    // dim=0
    if (dim == 0) {
      int vdim = size[dim];
      array.assertDim(v, vdim,1);
      for( int i = 0; i < vdim; i++ ) {
	mat[i][el] = v[i][0];
      }
    }

    else if (dim == 1) {
      int vdim = size[dim];
      array.assertDim(v, 1,vdim);
      for( int i = 0; i < vdim; i++ ) {
	mat[el][i] = v[0][i];
      }
    }

    else zliberror._assert(false, "extract only handles 2d arrays");
  } //colonSet

  //----------------------------------------------------------------
  // identity
  //----------------------------------------------------------------

  /**
   * float version
   */
  public static void setIdentity(float[][] mat)
  {
    int nr = mat.length;
    int nc = mat[0].length;

    for( int r = 0; r < nr; r++ ) {
      for( int c = 0; c < nc; c++ ) {
	if (r == c)
	  mat[r][c] = 1.f;
	else
	  mat[r][c] = 0.f;
      }
    }
  } //setIdentity

  /**
   */
  public static void setIdentity(double[][] mat)
  {
    int nr = mat.length;
    int nc = mat[0].length;

    for( int r = 0; r < nr; r++ ) {
      for( int c = 0; c < nc; c++ ) {
	if (r == c)
	  mat[r][c] = 1.;
	else
	  mat[r][c] = 0.;
      }
    }
  } //setIdentity


  //----------------------------------------------------------------
  // min,max
  //----------------------------------------------------------------

  /**
   */
  public static int[][] min(int[][] m1, int[][] m2)
  {
    int nr = m1.length;
    int nc = m1[0].length;
    int[][] mr = new int[nr][nc];

    min( m1, m2, mr );

    return mr;
  } //min(int)


  /**
   */
  public static void min(int[][] m1, int[][] m2, int[][] mr)
  {
    array.assertCongruent(m1, m2);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      int[] m1row = m1[ir];
      int[] m2row = m2[ir];
      int[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	int m1v = m1row[ic];
	int m2v = m2row[ic];
	mrrow[ic] = (m1v < m2v) ? m1v : m2v;
      }
    }
  } //min(int)


  /**
   */
  public static int[][] max(int[][] m1, int[][] m2)
  {
    int nr = m1.length;
    int nc = m1[0].length;
    int[][] mr = new int[nr][nc];

    max( m1, m2, mr );

    return mr;
  } //add(int)


  /**
   */
  public static void max(int[][] m1, int[][] m2, int[][] mr)
  {
    array.assertCongruent(m1, m2);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      int[] m1row = m1[ir];
      int[] m2row = m2[ir];
      int[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	int m1v = m1row[ic];
	int m2v = m2row[ic];
	mrrow[ic] = (m1v > m2v) ? m1v : m2v;
      }
    }
  } //max(int)

  //----------------------------------------------------------------
  // scale
  //----------------------------------------------------------------

  /**
   * functional
   */
  public static float[] scalefun(float scale, float[] v)
  {
    int len = v.length;
    float[] sv = new float[len];

    for( int i = 0; i < len; i++ ) {
      sv[i] = v[i] * scale;
    }

    return sv;
  } //scale


  public static void scale(float scale, float[] v)
  {
    int len = v.length;

    for( int i = 0; i < len; i++ ) {
      v[i] *= scale;
    }
  } //scale


  /**
   * functional
   */
  public static double[] scalefun(double scale, double[] v)
  {
    int len = v.length;
    double[] sv = new double[len];

    for( int i = 0; i < len; i++ ) {
      sv[i] = v[i] * scale;
    }

    return sv;
  } //scale


  public static void scale(double scale, double[] v)
  {
    int len = v.length;

    for( int i = 0; i < len; i++ ) {
      v[i] *= scale;
    }
  } //scale


  //----------------------------------------------------------------
  // add
  //----------------------------------------------------------------

  /**
   * functional 
   */
  public static int[][] addfun(int[][] m1, int[][] m2)
  {
    int nr = m1.length;
    int nc = m1[0].length;
    int[][] mr = new int[nr][nc];

    add( m1, m2, mr );

    return mr;
  } //add(int)


  /**
   */
  public static void add(int[][] m1, int[][] m2, int[][] mr)
  {
    array.assertCongruent(m1, m2);
    array.assertCongruent(m1, mr);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      int[] m1row = m1[ir];
      int[] m2row = m2[ir];
      int[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	mrrow[ic] = m1row[ic] + m2row[ic];
      }
    }
  } //add(int)


  /**
   * mr = s1*v1 + s2*v2
   */
  public static void addscaled(float s1, float[] v1,
			       float s2, float[] v2,
			       float[] vr)
  {
    array.assertCongruent(v1, v2);
    array.assertCongruent(v1, vr);
    int len = v1.length;

    for( int i = 0; i < len; i++ ) {
      vr[i] = s1*v1[i] + s2*v2[i];
    }
  } //addscaled(float)


  /**
   * mr = s1*m1 + s2*m2
   */
  public static void addscaled(float s1, float[][] m1,
			       float s2, float[][] m2,
			       float[][] mr)
  {
    array.assertCongruent(m1, m2);
    array.assertCongruent(m1, mr);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      float[] m1row = m1[ir];
      float[] m2row = m2[ir];
      float[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	mrrow[ic] = s1 * m1row[ic] + s2 * m2row[ic];
      }
    }
  } //addscaled(float)


  /**
   * mr = s1*m1 + s2*m2
   */
  public static void addscaled(double s1, double[][] m1,
			       double s2, double[][] m2,
			       double[][] mr)
  {
    array.assertCongruent(m1, m2);
    array.assertCongruent(m1, mr);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      double[] m1row = m1[ir];
      double[] m2row = m2[ir];
      double[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	mrrow[ic] = s1 * m1row[ic] + s2 * m2row[ic];
      }
    }
  } //addscaled(double)

  //----------------------------------------------------------------

  /**
   */
  public static float[][] addfun(float[][] m1, float[][] m2)
  {
    int nr = m1.length;
    int nc = m1[0].length;
    float[][] mr = new float[nr][nc];

    add( m1, m2, mr );

    return mr;
  } //add(float)


  public static void add(float[][] m1, float[][] m2, float[][] mr)
  {
    array.assertCongruent(m1, m2);
    array.assertCongruent(m1, mr);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      float[] m1row = m1[ir];
      float[] m2row = m2[ir];
      float[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	mrrow[ic] = m1row[ic] + m2row[ic];
      }
    }
  } //add(float)


  //----------------------------------------------------------------

  /**
   */
  public static double[][] addfun(double[][] m1, double[][] m2)
  {
    int nr = m1.length;
    int nc = m1[0].length;
    double[][] mr = new double[nr][nc];

    add( m1, m2, mr );

    return mr;
  } //add(double)


  public static void add(final double[][] m1, final double[][] m2,
			 double[][] mr)
  {
    array.assertCongruent(m1, m2);
    array.assertCongruent(m1, mr);
    int nr = m1.length;
    int nc = m1[0].length;

    for( int ir = 0; ir < nr; ir++ ) {
      double[] m1row = m1[ir];
      double[] m2row = m2[ir];
      double[] mrrow = mr[ir];
      for( int ic = 0; ic < nc; ic++ ) {
	mrrow[ic] = m1row[ic] + m2row[ic];
      }
    }
  } //add(double)


  public static void add(final double[] v1, final double[] v2,
			 double[] v3)
  {
    int len = v1.length;
    zliberror._assert(v2.length == len);
    zliberror._assert(v3.length == len);

    for( int i = 0; i < len; i++ ) {
      v3[i] = v1[i] + v2[i];
    }
  } //add


  // dont like the 'addfun' naming
  public static double[] add(final double[] v1, final double[] v2)
  {
    int len = v1.length;
    zliberror._assert(v2.length == len);
    double[] v3 = new double[len];

    add(v1, v2, v3);

    return v3;
  } //add

  //----------------------------------------------------------------
  // BLAS-like  naming conventions:
  // saxpy = scalar a x + y   = ax+y
  // gaxpy = general a x + y =  Ax+y
  // sscal = single scale = a x
  // dscal = double scale = a x
  // (zilla)
  // funsaxpy = functional version, returns 
  //----------------------------------------------------------------

  /**
   */
  public static double[][] alloc(int nr, int nc)
  {
    return new double[nr][nc];
  }

  /**
   */
  public static double[] alloc(int nr)
  {
    return new double[nr];
  }

  //----------------------------------------------------------------

  /**
   * todo is there a better algorithm?
   * maybe not, jama uses this algorithm
   */
  public static double[][] transpose(final double[][] A)
  {
    int N = A.length;
    zliberror._assert(A[0].length == N, "transpose needs sqauare matrix");
    double[][] B = alloc(N,N);
    for( int r=0; r < N; r++ ) {
	for( int c=0; c < N; c++ ) {
	    B[r][c] = A[c][r];
	}
    }
    return B;
  } //transpose

  //----------------------------------------------------------------
  // matrix-matrix multiply
  //----------------------------------------------------------------

  /**
   * Conformal matrix multiply M3[n1,n3] = M1[n1,n2] * M2[n2,n3]; 
   * M3 must be distinct from M1,M2.
   */
  public static void multiply(final double[][] A, final double[][] B,
			      double[][] C)
  {
    int nr = A.length;
    int nc = B[0].length;
    int ni = A[0].length;
    zliberror._assert(ni == B.length, "matrices do not conform");

    for( int r=0; r < nr; r++ ) {
      for( int c=0; c < nc; c++ ) {
	double sum = 0.0;
	for( int i=0; i < ni; i++ ) {
	  sum += (A[r][i] * B[i][c]);
	}
	C[r][c] = sum;
      }
    }
  } //multiply

  public static double[][] multiply(final double[][] A, final double[][] B)
  {
    int nr = A.length;
    int nc = B[0].length;
    double[][] C = new double[nr][nc];

    multiply(A,B, C);

    return C;
  } //multiply

  //----------------------------------------------------------------
  // matrix-vector multiply
  //----------------------------------------------------------------

  /**
   * multiply M,v
   */
  public static double[] multiply(final double[][] M, final double[] v1)
  {
    int nr = M.length;
    int nc = M[0].length;
    zliberror._assert(v1.length == nc);

    double[] v2 = new double[nr];

    for( int r=0; r < nr; r++ ) {
	double sum = 0.0;
	for( int c=0; c < nc; c++ ) sum += (M[r][c] * v1[c]);
	v2[r] = sum;
    }

    return v2;
  } /*multiply*/


  /**
   * multiply M,v1 -> v2
   */
  public static void multiply(final double[][] M, final double[] v1,
			      double[] v2)
  {
    int nr = M.length;
    int nc = M[0].length;
    zliberror._assert(v1.length == nc);
    zliberror._assert(v2.length == nr);

    for( int r=0; r < nr; r++ ) {
	double sum = 0.0;
	for( int c=0; c < nc; c++ ) sum += (M[r][c] * v1[c]);
	v2[r] = sum;
    }

  } /*multiply*/

} //matrix


/* matrix.c -3 - a few general matrix routines
 * modified
 * 9apr/2       ansi update
 *
 * package: Mat
 * todo:
 * include solve(), renamed to MatSolve() for conflicts.
 */

/****************************************************************

  /+* Copies M1 to M2.
   +/

  final public static void MatCopy(M1,M2,m,n)
  Flotype *M1,*M2;
  int4 m,n;
{
    Zbcopy((char *)M1,(char *)M2,m*n*sizeof(Flotype));
} /+Copy+/


/+OLDENTRY
MatMul(M1,M2,M3,N)
Square matrix multiply M3 = M1*M2; M3 must be distinct from M1,M2.
+/

final public static void MatMul(A_,B_,C_,N)
  Flotype *A_,*B_,*C_;
  int4 N;
{
    register int4 r,c,i;
    register Flotype sum;

#   define A(i,j) A_[(i)*N+(j)]
#   define B(i,j) B_[(i)*N+(j)]
#   define C(i,j) C_[(i)*N+(j)]

    for( r=0; r < N; r++ ) {
	for( c=0; c < N; c++ ) {
	    sum = 0.0;
	    for( i=0; i < N; i++ ) {
		sum += A(r,i) * B(i,c);
	    }
	    C(r,c) = sum;
	}
    }

#   undef A
#   undef B
#   undef C

} /+matmul+/





/+OLDENTRY
double MatInvert(M,N)
Inverts M in place using Gauss-Jordan elimination, returning the determinant.
Code from 'invert' in IBM scientific subroutine library (public domain).
+/

double
MatInvert( mat, n )
  register Flotype mat[];
  register int4 n;
{
#   define MAXMAT 100
    double det;
    register Flotype biga, hold;
    int4 l[MAXMAT], m[MAXMAT];
    register int4 i,j,k;
    int4 ij,jk,ik,ji, iz, ki, kj;
    int4 jp,jq,jr;
    int4 nk,kk;
    extern double fabs();
 
    if( n >= MAXMAT ) {
      fprintf( stderr, "Matrix Invert : %d too large \n", n);
      return 0.0;
    }

    det = 1.0;
    nk = -n;
    for(k=0; k<n; k++ ) {
	nk=nk+n; kk=nk+k;
	/+ 
	 *  Search for maximum value
	 +/
	l[k] = k;
	m[k] = k;
	biga = mat[kk];
	for(j=k; j<n; j++ ) {
	    iz=n*j;
	    for(i=k; i<n; i++) {
		ij=iz+i;
		if( fabs(biga)-fabs(mat[ij]) < 0.0 ) {
		    biga=mat[ij];
		    l[k]=i;
		    m[k]=j;
		 }
             }
	 }

	/+
	 *  Interchange rows  
	 +/
	j = l[k];
	if(j>k) {
	    ki=k-n;
	    for(i=0; i<n; i++ ) {
		ki=ki+n; ji=ki-k+j;
		hold= -mat[ki]; mat[ki]=mat[ji]; mat[ji]=hold;
	      }
	 }

	/+
	 *  Interchange columns 
	 +/
	i=m[k];
	if(i>k) {
	    jp=n*i;
	    for(j=0; j<n; j++ ) {
		jk=nk+j; ji=jp+j;
		hold = -mat[jk]; mat[jk]=mat[ji]; mat[ji]=hold;
	     }
	 }

	/+
	 *	Divide column by minus pivot. The value of 
	 *	pivot is contained in biga.
	 +/
	if(biga==0.0) {
	    fprintf(stderr,"Matrix Invert : singular matrix \n");
	    return 0.0;
	 }
	for(i=0; i<n; i++ ) {
	    if(i!=k) {
		ik = nk + i;
		mat[ik]=mat[ik]/(-biga);
	     }
	 }

	/+
	 *	Reduce matrix
	 +/
	for(i=0; i<n; i++) {
	    ik=nk+i; hold = mat[ik]; ij=i-n;
	    for(j=0; j<n; j++ ) {
		ij=ij+n;
		if(i!=k) if(j!=k) {
		    kj=ij-i+k;
		    mat[ij]=hold*mat[kj]+mat[ij];
		 }
	     }
	 }

	/+
	 *  Divide row by pivot  
	 +/
	kj=k-n;
	for(j=0; j<n; j++) {
	    kj=kj+n;
	    if(k!=j) mat[kj] = mat[kj]/biga;
	 }

	/+
	 *  Product of pivots  
	 +/
	det *= biga;

	/+
	 *  Replace pivot by reciprocal 
	 +/
	mat[kk]=1.0/biga;
    }

  /+  Final row and column interchange +/
  for(k=n-2; k>=0; k--) {
      i=l[k];
      if(i>k) {
          jq=n*k; jr=n*i;
          for(j=0; j<n; j++ ) {
            jk=jq+j; ji=jr+j; 
            hold=mat[jk]; mat[jk] = -mat[ji]; mat[ji]=hold;
           }
       }

       j=m[k];
       if(j>k) {
	   ki=k-n;
           for(i=0; i<n; i++ ) {
	       ki=ki+n; ji=ki-k+j;
	       hold=mat[ki]; mat[ki]= -mat[ji]; mat[ji]=hold;
            }
        }
    }

  return( (Flotype)det );
} /+matinvert+/


/+OLDENTRY
double MatInvert2(M1,M2,N)
Inverts M1 into M2, returning the determinant.
+/

double
MatInvert2( a, b, n )
  register Flotype *a, *b;
  int4 n;
{
  register int4 i, j;
  register Flotype *bb;
  bb = b;
  for( i=0; i<n; i++ ) for( j=0; j<n; j++ ) *bb++ = *a++;
  return( MatInvert( b, n ) );
} /+matinvert2+/


/+OLDENTRY
double MatSolve(n,M,b)
Solve an nxn system of linear equations Mx=b
using Gaussian elimination with partial pivoting.
leave solution x in b array (destroying original A and b in the process)
Returns determinant.
(code from Paul Heckbert).
+/

double
MatSolve(n,A,b)
  register Flotype *A,*b;
  int4 n;
{
   register int4 i,j,k;
   double max,t,det,sum,pivot;	/+ keep these double +/
   extern double fabs();

#  define swap(a,b,t) {t=a; a=b; b=t;}
#  define a(i,j) A[(i)*n+(j)]

   /+---------- forward elimination ----------+/

   det = 1.;
   for (i=0; i<n; i++) {		/+ eliminate in column i +/
      max = -1.;
      for (k=i; k<n; k++)		/+ find pivot for column i +/
         if (fabs(a(k,i))>max) {
            max = fabs(a(k,i));
            j = k;
         }
      if (max<=0.) return(0.);		/+ if no nonzero pivot, PUNT +/
      if (j!=i) {			/+ swap rows i and j +/
         for (k=i; k<n; k++)
            swap(a(i,k),a(j,k),t);
         det = -det;
         swap(b[i],b[j],t);		/+ swap elements of column vector +/
      }
      pivot = a(i,i);
      det *= pivot;
      for (k=i+1; k<n; k++)		/+ only do elems to right of pivot +/
         a(i,k) /= pivot;

      /+ we know that a(i,i) will be set to 1, so why bother to do it? +/
      b[i] /= pivot;
      for (j=i+1; j<n; j++) {		/+ eliminate in rows below i +/
         t = a(j,i);			/+ we're gonna zero this guy +/
         for (k=i+1; k<n; k++)		/+ subtract scaled row i from row j +/
            a(j,k) -= a(i,k)*t;		/+ (ignore k<=i, we know they're 0) +/
         b[j] -= b[i]*t;
      }
   }

   /+---------- back substitution ----------+/

   for (i=n-1; i>=0; i--) {		/+ solve for x[i] (put it in b[i]) +/
      sum = b[i];
      for (k=i+1; k<n; k++)		/+ really a(i,k)*x[k] +/
         sum -= a(i,k)*b[k];
      b[i] = sum;
   }

   return(det);

#  undef swap
#  undef a

} /+solve+/


/+OLDENTRY
MatRandom(M,m,n)
Fills M with random numbers between -1..1 
(for testing matrix inversion routines).
+/

final public static void MatRandom(M,m,n)
  Flotype *M;
  int4 m,n;
{
    int4 i,l;

    l = n*m;
    for( i=0; i < l; i++ ) *M++ = rndf11();
}


/+***************************************************************
 ************************  Vector  ******************************
 ***************************************************************+/

#define VecKey 'VEC!'

/+OLDENTRY
Flotype *V = VecAlloc(int4 n)
Allocates storage and a type-checking field for
a vector of length n.
+/

Flotype *
VecAlloc(n)
  int4 n;
{
    Flotype *V;

    V = (Flotype *)malloc((n+3) * sizeof(Flotype));

    /+ set type-checking keys before,after the data
     * [key,length,...data...,key]
     +/
    *((int4 *)(V+0)) = (int4)VecKey;
    *((int4 *)(V+1)) = n;	/+ length +/
    *((int4 *)(V+2+n)) = (int4)VecKey;

    return(V+2);
} /+Alloc+/


/+OLDENTRY
VecFree(V)
Frees V; complains if V was not allocated with VecAlloc.
+/

final public static void VecFree(V)
  Flotype *V;
{
    int4 n;

    V -= 2;
    if (*((int4 *)(V+0)) != VecKey)  Zcodeerror("VecFree");

    n = *((int4 *)(V+1));
    Ztrace(("VecFree recovered length %d\n",n));

    if (*((int4 *)(V+2+n)) != (int4)VecKey)
	Zcodeerror("VecFree:corrupted matrix");

    free((char *)V);
} /+Free+/






/+OLDENTRY
VecCopy(V1,V2,n)
Copies V1 to V2.
+/

final public static void VecCopy(V1,V2,n)
  Flotype *V1,*V2;
  int4 n;
{
    Zbcopy((char *)V1,(char *)V2,n*sizeof(Flotype));
} /+Copy+/



/+OLDENTRY
MatVecMul(M,V1, V2,N)
Postmultiplies matrix M by vector V1, result in V2.
+/

final public static void MatVecMul(M,V1, V2,N)
  Flotype *M,*V1,*V2;
  register int4 N;
{
    register int4 i,j;
    register Flotype sum;

    for( i=0; i < N; i++ ) {
	sum = 0.0;
	for( j=0; j < N; j++ ) sum += *M++ * V1[j];
	V2[i] = sum;
    }

} /+MatVecMul+/



/+OLDENTRY
VecRandom(V,n)
Fills V with random numbers between -1..1 
(for testing purposes).
+/

final public static void VecRandom(V,n)
  Flotype *V;
  int4 n;
{
    int4 i;

    for( i=0; i < n; i++ ) *V++ = rndf11();
}



#ifdef TESTIT /+%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%+/

/+% cc -DTESTIT -DFLTPREC -Dztrace % -lZ -lm -o matrixTST %+/

main()
{
    Flotype *m,*v,*v2;
    m = MatAlloc(5,5);
    MatFree(m);

    v = VecAlloc(2);
    v2 = VecAlloc(2);

    VecRandom(v,2);
    VecCopy(v,v2,2);
    VecPrint("v: ",v,2); VecPrint("copied: ",v2,2);

    VecFree(v);
    VecFree(v2);
}

#endif /+TESTIT %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%+/

****************************************************************/