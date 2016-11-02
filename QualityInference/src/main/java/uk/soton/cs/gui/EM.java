package uk.soton.cs.gui;

// EM.java

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
// Primary author contact info:  www.idiom.com/~zilla  zilla@computer.org



import java.awt.*;		// for algorithm debug only

//import VisualNumerics.math.*;	// for testing inverse 

//import zlib.*;

/**
 * P(m|d) = P(d|m) P(m) / P(d)
 *
 * P(d|m) is the likelihood, it is the probability of the data assigned
 *   by this model.
 * P(m|d) is the posterior, it is the probability of
 *   this model given that the data has been observed.
 *
 * ndata - number of data items, each of nd dimensions
 * nmix - number of gaussians in the mixture model
 * @param data[ndata][nd]
 * @param covariances[nmix][nd][nd]
 * @param priors[nmix]
 * @param posteriors[ndata][nmix]
 */
final public class EM
{
  final static int	verbose = 1;

  public static void em(double[][] data,
			int nsteps,
			double[][] means,
			double[][][] covariances,
			double[] priors,
			double[][] posteriors)
    throws Exception
  {
    int ndata = data.length;
    int nmix = means.length;
    zliberror._assert(covariances.length == nmix, "em1");
    zliberror._assert(priors.length == nmix, "em2");
    zliberror._assert(posteriors.length == ndata, "em3");
    zliberror._assert(posteriors[0].length == nmix, "em4");

    init(data, means, covariances, priors);

    for( int step=0; step < nsteps; step++ ) {
      estep(data, means, covariances, priors, posteriors);
      mstep(data, means, covariances, priors, posteriors);
    }
  } //em


  /**
   * initialize means, covariances, priors to reasonable starting values
   */
  public static void init(double[][] data,
			  double[][] means,
			  double[][][] covariances,
			  double[] priors)
  {
    int ndata = data.length;
    int nmix = means.length;
    int nd = means[0].length;
    zliberror._assert(covariances[0].length == nd);
    zliberror._assert(covariances[0][0].length == nd);

    double[][] bounds = new double[nd][2];
    matrix.getNDBounds(data, bounds);
    double[] del = new double[nd];
    for( int id=0; id < nd; id++ ) {
      if (verbose > 0)
	System.out.println("data bounds: " + bounds[id][0]+".."+bounds[id][1]);
      del[id] = bounds[id][1] - bounds[id][0];
    }

    for( int im=0; im < nmix; im++ ) {
      for( int id=0; id < nd; id++ )  {
	means[im][id] = bounds[id][0] + rnd.rndf() * del[id];
      }
      matrix.setIdentity(covariances[im]);
      priors[im] = 1. / nd;
    }
  } //init


  public static void estep(double[][] data,
			   double[][] means,
			   double[][][] covariances,
			   double[] priors,
			   double[][] posteriors)
    throws Exception
  {
    int ndata = data.length;
    int nmix = means.length;
    zliberror._assert(posteriors.length == ndata, "estep0");
    zliberror._assert(posteriors[0].length == nmix, "estep1");

    for( int ip=0; ip < ndata; ip++ ) {

      double norm = 0.f;
      for( int im=0; im < nmix; im++ ) {
	// TODO: pull the im loop outside ip, then can evaluate gscale
	// once per model
	// double gscale = gaussEvalScale(means[im], covariances[im]);
	double g = zmath.gaussEval(data[ip], means[im], covariances[im]);
	norm += priors[im] * g;
      }

      for( int im=0; im < nmix; im++ ) {
	double g = zmath.gaussEval(data[ip], means[im], covariances[im]);
	posteriors[ip][im] = (priors[im] * g) / norm;
      }
    }
  } //estep


  public static void mstep(double[][] data,
			   double[][] means,
			   double[][][] covariances,
			   double[] priors,
			   double[][] posteriors)
    throws Exception
  {
    int ndata = data.length;
    int nmix = means.length;
    int nd = means[0].length;

    // prior probability for a particular gaussian is its
    // posterior probability summed over all data points.
    for( int im=0; im < nmix; im++ ) {
      double sum = 0.;
      for( int ip=0; ip < ndata; ip++ ) {
	sum += posteriors[ip][im];
      }

      priors[im] = sum / ndata;
    }

    // each mean is a weighted sum of the data points,
    // weighted by the convex normalized posterior probability for that G
    for( int im=0; im < nmix; im++ ) {
      // zero this mean
      for( int id=0; id < nd; id++ ) {
	means[im][id] = 0.;
      }

      // accumulate
      double sumposterior = 0.;
      for( int ip=0; ip < ndata; ip++ ) {
	double pw = posteriors[ip][im];
	sumposterior += pw;
	for( int id=0; id < nd; id++ ) {
	  means[im][id] += (pw * data[ip][id]);
	}
      } //ip

      double oosumposterior = 1. / sumposterior;
      for( int id=0; id < nd; id++ ) {
	means[im][id] *= oosumposterior;
      } //id

    } //im

    // lastly, covariance is weighted sum of outer products
    // of the de-meaned data, as weighted by the normalized  posteriors
    for( int im=0; im < nmix; im++ ) {
      double[][] cov = covariances[im];
      double[] mean = means[im];

      // zero this covariance
      for( int id=0; id < nd; id++ ) {
	for( int jd=0; jd < nd; jd++ ) {
	  cov[id][jd] = 0.;
	}
      }

      // accumulated weighted sum of outer product of de-meaned data
      double sumposterior = 0.;
      for( int ip=0; ip < ndata; ip++ ) {
	double[] d = data[ip];
	double pw = posteriors[ip][im];
	sumposterior += pw;
	for( int id=0; id < nd; id++ ) {
	  for( int jd=0; jd < nd; jd++ ) {
	    cov[id][jd] += (pw * (d[id] - mean[id]) * (d[jd] - mean[jd]));
	  }
	}
      } //ip

      // normalize the posterior weighting
      double oosumposterior = 1. / sumposterior;
      for( int id=0; id < nd; id++ ) {
	for( int jd=0; jd < nd; jd++ ) {
	  cov[id][jd] *= oosumposterior;
	}
      }

      // and invert, because in e step it is ony used in the gaussEval
      // TODO: yuck garbage
      covariances[im] = DoubleMatrix.inverse(cov);

    } //im

  } //mstep


  //----------------------------------------------------------------
  // plotting code below.
  //----------------------------------------------------------------

  static final int 	RES = 400;
  static final int 	RESo2 = RES/2;
  static Frame		_f;
  static Graphics	_g;

  static void plot(double[][] data, double[][] means, double[][][] covariances)
  {
    int ndata = data.length;
    int nmix = means.length;
    int nd = means[0].length;

    _g.clearRect(0,0,RES,RES);

    _g.setColor(Color.blue);
    for( int ip=0; ip < ndata; ip++ ) {
      drawpt(data[ip][0],data[ip][1]);
    }

    _g.setColor(Color.red);
    for( int im=0; im < nmix; im++ ) {
      drawpt(means[im][0],means[im][1]);

      // vis of covariance:
      // cov is pos def, find set of x s.t. x C x = k
      int npts = 21;
      double k = 1.;
      for( int ip=0; ip < npts; ip++ ) {
	double angle = 2. * Math.PI * (ip / (double)(npts-1));
	double x = Math.cos(angle);
	double y = Math.sin(angle);
	double r = findroot(covariances[im], x, y, k);
	drawto(means[im][0] + r*x, means[im][1] + r*y, (ip==0));
      }
      
    } //nmix

  } //plot


  // x ( C11 x + C12 y ) + y ( C21 x + C22 y ) = k
  // y/x = s
  //
  // y = x*s
  // x ( C11 x + C12 x*s ) + x*s ( C21 x + C22 x*s ) = k
  // C11 x^2 + C12 s x^2  +  C21 x^2 s + C22 x^2 s^2 = k
  // x^2 ( C11 + C12 s + C21 s + C22 s^2 ) = k
  // x = sqrt( k / ( C11 + C12 s + C21 s + C22 s^2 ) )
  static double findroot(double[][] cov, double x, double y, double k)
  {
    double s = y / x;	// beware x 0
    double den = cov[0][0] + cov[0][1]*s + cov[1][0]*s + cov[1][1]*s*s;
    if (den < 0.) den = - den;
    double x1 = Math.sqrt(k / den);
    return x1 / Math.abs(x);	// ratio
  } //findroot

  static final int toscreen(double d)
  {
    return RESo2 + (int)(RESo2 * d);
  }

  static void drawpt(double dx, double dy)
  {
    int ix = toscreen(dx);
    int iy = toscreen(dy);
    _g.drawLine(ix-5, iy-5, ix+5, iy+5);
    _g.drawLine(ix+5, iy-5, ix-5, iy+5);
  }


  static int _lx = 0;
  static int _ly = 0;
  static void drawto(double dx, double dy, boolean moveto)
  {
    int ix = toscreen(dx);
    int iy = toscreen(dy);
    if (!moveto) {
      _g.drawLine(_lx, _ly, ix, iy);
    }
    _lx = ix;
    _ly = iy;
  }


  static void drawpt(int ix, int iy)
  {
    _g.drawLine(ix-5, iy-5, ix+5, iy+5);
    _g.drawLine(ix+5, iy-5, ix-5, iy+5);
  }
		   

  static void simpleTest()
    throws Exception
  {
    int ndata = 100;
    int nd = 2;
    int nmix = 2;

    double[][] data = new double[ndata][2];

    for( int ip=0; ip < ndata; ip++ ) {
      double angle = 2. * Math.PI * (ip / (double)ndata);
      data[ip][0] = 0.5 * Math.cos(angle) + 0.05 * rnd.rndf11();
      data[ip][1] = 0.5 * Math.sin(angle) + 0.05 * rnd.rndf11();
    }

    double[][] means = new double[nmix][nd];
    double[][][] covariances = new double[nmix][nd][nd];
    double[] priors = new double[nmix];
    double[][] posteriors = new double[ndata][nmix];

    int nsteps = 100;
    init(data, means, covariances, priors);
    for( int istep=0; istep < nsteps; istep++ ) {
      estep(data, means, covariances, priors, posteriors);
      mstep(data, means, covariances, priors, posteriors);
      if (istep%3 == 0) {
	plot(data, means, covariances);
	zlib.more();
      }
    }

  } //simpleTest


  public static void main(String[] cmdline)
  {
    try {
      _f = new Frame();
      _f.setSize(RES,RES);
      _f.setVisible(true);
      _g = _f.getGraphics();
      simpleTest();
    }
    catch(Exception x) { zliberror.die(x); }
  } //main

} //EM