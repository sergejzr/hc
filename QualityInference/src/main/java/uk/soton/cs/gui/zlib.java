package uk.soton.cs.gui;

// zlib.java zilla 
// modified
// apr02	min/max integer
// mar01	bull
// feb01	scruff
// jan01	bull
// oct00	bull
// jun00	scruff
// may00	scruff
// apr00	scruff
// jan99	doom
// jan97	doom: created


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
import java.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * zlib - misc utilities
 *
 * @author jplewis
 */

final public class zlib
{

  /**
   * write string bytes on a stream
   */
  public static void writeString(BufferedOutputStream f, String s)
    throws IOException
  {
    //s.getBytes(0, s.length(), buf, 0);	//deprecated
    byte[] buf = s.getBytes();
    f.write(buf, 0, s.length());
  } //writeString


  /**
   * read the whole input stream into a string (beware)
   */
  public static String InputStreamToString(BufferedReader fr)
    throws IOException
  {
    StringBuffer s = new StringBuffer();

    String line = fr.readLine();
    while( line != null ) {
      s.append(line);
      line = fr.readLine();
    }
    fr.close();

    return s.toString();
  }

  /**
   * read the input stream through the next newline, then stop.
   */
  public static void InputStreamEatLine(InputStream f)
    throws IOException
  {
    int cc;
    do {
      cc = f.read();
    } while( cc != '\n' );
  } //InputStreamEatLine


  /**
   * Provide a readline method for an input stream.
   * (BufferedReader has a readLine method.)
   * Useful for reading binary ppm files - read the header in
   * a line-oriented way, then read binary bytes.
   * This method allocates a byte[] array each time.  Beware.
   */
  public static String InputStreamReadLine(InputStream f)
    throws IOException
  {
    byte[] buf = new byte[2048];	// TODO: garbage
    int cc;
    int i = 0;
    do {
      cc = f.read();
      if (cc != '\n') {
	buf[i] = (byte)cc;
	i++;
	if (i == 2048) break;
      }
    } while( cc != '\n' );

    return new String(buf, 0, i);
  } //InputStreamReadLine


  /**
   * read a token, stop at newline, tab, space
   */
  public static String InputStreamReadToken(InputStream f, String whitechars)
    throws IOException
  {
    byte[] buf = new byte[2048];	// TODO: garbage
    int cc;
    int i = 0;
    boolean iswhite = false;
    do {
      cc = f.read();
      if (cc == -1) break;

      iswhite = (whitechars.indexOf(cc) >= 0);

      if (!iswhite) {
	buf[i] = (byte)cc;
	i++;
	if (i == 2048) break;
      }
    } while(!iswhite);

    return new String(buf, 0, i);
  } //InputStreamReadToken



  /**
   * read the next token, skipping comment line
   * this is primarly for ppm parsing
   * NOT DEBUGGED
   */
  public static String InputStreamReadTokenComment(InputStream f,
						   String whitechars,
						   char commentchar)
    throws IOException
  {
    byte[] buf = new byte[2048];	// TODO: garbage
    int cc;
    int i = 0;
    boolean iswhite = false;

    do {
      cc = f.read();

      if (cc == commentchar) {
	while( (cc != '\n') && (cc != '\r') && (cc != -1) ) {
	  cc = f.read();
	}
	if (i > 0)	// token already started
	  break;
	else {
	  if (cc != -1) cc = f.read();
	}
      }

      if (cc == -1) break;

      iswhite = (whitechars.indexOf(cc) >= 0);      

      if (!iswhite)
      {
	buf[i] = (byte)cc;
	i++;
	if (i == 2048) break;
      }

    } while(!iswhite);

    return new String(buf, 0, i);
  } //InputStreamReadToken

  //----------------------------------------------------------------


  /**
  <pre>
  state table for streamtokenizer:

  Input                   Action                  New state 
  ----------------
  State=idle 
  ----------------
  word character          push back character     accumulate 

  ordinary character      return character        idle 

  whitespace character    consume character       idle 

  ----------------
  State=accumulate 
  ----------------
  word character          add to current word     accumulate 

  ordinary character      return current word     idle 
			  push back character 

  whitespace character    return current word     idle 
			  consume character 
 
  for jdk1.4.2:
  TT_EOF = -1
  TT_EOL = -10
  TT_WRD = -3
  TT_NUM = -2
  and ordinary (non word) characters are returned as their numberic value
  */

  /**
   * handy setup a streamtokenizer
   */
  public static StreamTokenizer getParsingStream(String file,
				   String whitechars, String wordchars,
				   String commentchars, String breakchars,
				   boolean eolIsSignificant,
				   boolean lowercase)
    throws IOException
  {
    return getParsingStream(file,
			    whitechars,wordchars,
			    commentchars,breakchars,
			    eolIsSignificant,lowercase,
			    false);
  }


  /**
   * I think breakchars are things like ; that should be returned
   * as separate tokens.
   * Typical call:
   <pre>
      StreamTokenizer st = zlib.getParsingStream(cmdline[0],
						 " \t\r\n", "'",
						 "", "",
						 false,true,
						 false);
   */
  public static StreamTokenizer getParsingStream(String file,
				   String whitechars, String wordchars,
				   String commentchars, String breakchars,
			  	   boolean eolIsSignificant, boolean lowercase,
 		 	 	   boolean doNumbers)

    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(file));
    StreamTokenizer st = new StreamTokenizer(br);
    st.resetSyntax();

    if (doNumbers) st.parseNumbers();

    st.wordChars((int)'A',(int)'Z');
    st.wordChars((int)'a',(int)'z');

    //st.whitespaceChars((int)'\u0000',(int)'\u0020');
    char[] arr = whitechars.toCharArray();
    for( int i = 0; i < arr.length; i++ )
      st.whitespaceChars(arr[i],arr[i]);

    arr = wordchars.toCharArray();
    for( int i = 0; i < arr.length; i++ )
      st.wordChars(arr[i],arr[i]);

    arr = commentchars.toCharArray();
    for( int i = 0; i < arr.length; i++ )
      st.commentChar(arr[i]);

    arr = breakchars.toCharArray();
    for( int i = 0; i < arr.length; i++ )
      // note 'ordinary' as opposed to 'word'
      st.ordinaryChars(arr[i],arr[i]);

    st.eolIsSignificant(eolIsSignificant);
    //System.out.println("eolIsSignificant = "+eolIsSignificant);
    st.lowerCaseMode(lowercase);

    return st;
  } //getParsingStream


  /**
   * print the current StreamTokenizer token
   */
  public static void printToken(StreamTokenizer st)
  {
    switch(st.ttype) {
    case StreamTokenizer.TT_WORD:
      System.out.print(st.sval);
      break;
    case StreamTokenizer.TT_NUMBER:
      System.out.print(st.nval);
      break;
    case StreamTokenizer.TT_EOL:
      System.out.print("<eol>");
      break;
    case StreamTokenizer.TT_EOF:
      System.out.print("<eof>");
      break;
    default:
      System.out.print("character("+st.ttype+")");
    }
  } //printToken


  /**
   * read the next token from st,
   * call parseError if it is not 'desired'
   */
  public static void parseToken(StreamTokenizer st,
				String desired,
				String file)
    throws IOException
  {
    int val = st.nextToken();
    if (val != StreamTokenizer.TT_WORD) zliberror.parseError(st, file);
    if (!st.sval.equals(desired)) zliberror.parseError(st, file);
  } //parseToken


  /**
   * read the next token from st,
   * call parseError if it is not a string, else return it.
   * (jul05 added) exception: if EOF return null
   */
  public static String parseString(StreamTokenizer st,
				   String file)
    throws IOException
  {
    int val = st.nextToken();
    //System.out.println("val = "+val);
    if (val == StreamTokenizer.TT_EOF) return null;
    if (val != StreamTokenizer.TT_WORD) zliberror.parseError(st, file);
    return st.sval;
  } //parseString



  /**
   * Debugging version of parseString -
   * prints out all the internal variables.
   */
  public static String parseStringDebug(StreamTokenizer st,
					String file)
    throws IOException
  {
    int val = st.nextToken();
    System.out.println("parseStringDebug val = " + val);
    System.out.println("   TT_WORD = " + StreamTokenizer.TT_WORD);
    System.out.println("   TT_NUMBER = " + StreamTokenizer.TT_NUMBER);
    System.out.println("   TT_EOL = " + StreamTokenizer.TT_EOL);
    System.out.println("   TT_EOF = " + StreamTokenizer.TT_EOF);
    System.out.println("   nval = " + st.nval);
    System.out.println("   sval = " + st.sval);
    if (val != StreamTokenizer.TT_WORD) zliberror.parseError(st, file);
    return st.sval;
  } //parseStringDebug


  /**
   * read the next token from st,
   * call parseError if it is not a double, else return it
   * Beware: if the input has E notation numbers, this wont
   * work, it is necessary to do:
   * <pre>
   * st.resetSyntax();
   * st.eolIsSignificant(false);
   * st.whitespaceChars((int)'\u0000',(int)'\u0020');
   * st.wordChars((int)'0',(int)'9');
   * st.wordChars((int)'.',(int)'.');
   * st.wordChars((int)'E',(int)'E');
   * st.wordChars((int)'-',(int)'-');
   * st.wordChars((int)'+',(int)'+');
   * //st.commentChar('#');
   * </pre>
   */
  public static double parseDouble(StreamTokenizer st,
				   String file)
    throws IOException
  {
    int val = st.nextToken();
    if (val != StreamTokenizer.TT_NUMBER) zliberror.parseError(st, file);
    return st.nval;
  } //parseDouble

  //----------------------------------------------------------------

  /**
   */
  public static String getFilename(String path)
  {
    File f = new File(path);
    return f.getName();
  }


  /**
   */
  public static String getDirectory(String path)
  {
    File f = new File(path);
    return f.getParent();
  }

  /**
   * return the root part of a pathname (excluding the period).
   * null if no such.
   */
  public static String getRoot(String path)
  {
    int i = path.lastIndexOf('.');
    if (i == -1) return path;
    return path.substring(0,i);
  }


  /**
   * return the .ext part of a pathname (including the period).
   * null if no such.
   */
  public static String getExtension(String path)
  {
    int i = path.lastIndexOf('.');
    if (i == -1) return null;
    return path.substring(i);
  }


  /**
   * if path has no extension, add the given one;
   * ext should include the period.
   * TODO this does not work properly if the path is  abc.0120
   */
  public static String addExtensionIfMissing(String path, String ext)
  {
    String hasext = getExtension(path);
    if (hasext == null)
      path = path + ext;
    return path;
  } 


  /**
   * Java link detection:
   * <p>
   * For a link that actually points to something (either a file or
   * a directory), the absolute path is the path through the link,
   * whereas the canonical path is the path the link references.
   * <p>
   * Dangling links appear as files of size zero - 
   * no way to distinguish dangling links from non-existent files
   * other than by consulting the parent directory.
   */
  public static boolean isLink(File file)
  {
    try {
      if (!file.exists())
	return true;
      else

      {
	String cnnpath = file.getCanonicalPath();
	String abspath = file.getAbsolutePath();
	//System.out.println(abspath+" <-> "+cnnpath);
	return !abspath.equals(cnnpath);
      }
    }
    catch (java.io.FileNotFoundException ex) {
      return true;	      // a dangling link.
    }
    catch (IOException ex) { /*ignore other errors*/ }

    return false;
  } //isLink


  /**
   * launch JFileChooser to select a file
   */
  public static String chooseFile(Component parent)
  {
    String wdir = System.getProperty("user.dir");
    JFileChooser chooser = new JFileChooser(wdir);
    int retval = chooser.showOpenDialog(parent);
    if (retval != JFileChooser.APPROVE_OPTION) return null;
    File f = chooser.getSelectedFile();
    if (f == null || f.isDirectory()) return null;
    return f.getPath();
  }

  /**
   * launch JFileChooser to select a directory
   */
  public static String chooseDir(Component parent)
  {
    String wdir = System.getProperty("user.dir");
    JFileChooser chooser = new JFileChooser(wdir);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int retval = chooser.showOpenDialog(parent);
    if (retval != JFileChooser.APPROVE_OPTION) return null;
    File f = chooser.getSelectedFile();
    if (f == null) return null;
    return f.getPath();
  } //chooseDir


  /** pop up a GUI to ask the user to pick one of a couple choices
   * choces are passed like: Object[] values = { "draw mode", "paint mode" };
   */
  public static Object getUIInput(Object[] values)
  {
    Object selected
      = JOptionPane.showInputDialog(null, "Choose one", "Input",
				    JOptionPane.INFORMATION_MESSAGE, null,
				    values, values[0]);
    return selected;
  } //getUIInput

  //----------------------------------------------------------------

  /**
   * load properties from file
   */
  public static java.util.Properties readProps(String path)
    throws IOException
  {
    java.util.Properties props = new java.util.Properties();
    InputStream f = new FileInputStream(path);
    props.load(f);
    f.close();
    return props;
  } //readProps


  /**
   * go over cmdline, for any arg that looks like an -option,
   * add the next arg as its value unless it too begins with a hypen,
   * in that case add True as its value.
   */
  public static void addCmdlineProps(java.util.Properties props,
				     String[] cmdline)
  {
    for( int i=0; i < cmdline.length; i++ ) {
      if (cmdline[i].startsWith("-")) {
	String key = cmdline[i];
	Object val = Boolean.TRUE;
	if (i < (cmdline.length-1) &&
	    !cmdline[i+1].startsWith("-"))
	  val = cmdline[i+1];
	props.put(key, val);  // relying on the fact that props is a hashtable
      }
    }
  } //addCmdlineProps

  //----------------------------------------------------------------

  /**
   * return a string with any characters c removed
   */
  public static String stripchar(String tok, char c)
  {
    if (tok.indexOf(c) < 0)
      return tok;

    StringBuffer sb = new StringBuffer();
    int len = tok.length();
    for( int i = 0; i < len; i++ ) {
      char c1 = tok.charAt(i);
      if (c1 != c) sb.append(c1);
    }

    return sb.toString();
  } //stripchars


  /**
   */
  public static void sleepfor(int msecs)
  {
    //System.out.println("sleeping for " + msecs);
    System.out.print(".");
    try { Thread.sleep(msecs); } catch(Exception x) {}
  }

  /**
   * Print --more--, wait for user to hit enter to continue.
   */
  public static void more()
  {
    //if (verbose == 0) return;
    System.out.println("--more--");
    try { System.in.read(); }
    catch(Exception x) {}
  }

  /**
   * substitute becomes for was in str, return new string
   */
  public static String stringsubst(String str, String was, String becomes)
  {
    int idx = str.indexOf(was);
    if (idx < 0) return str;
    String start = str.substring(0, idx);
    String end = str.substring(idx+was.length(), str.length());
    return start + becomes + end;
  }

  /**
   * Split string at delimiters, like python string.split.
   * NOTE There is a jdk1.4 String.split routine
   * TODO: could probably speed this up by coding without the stringtokenizer
   */
  public static ArrayList stringSplit(String s, String delim)
  {
    ArrayList l = null;
    StringTokenizer st = new StringTokenizer(s, delim, false);
    while( st.hasMoreTokens() ) {
      String tok = st.nextToken();
      if (l == null) l = new ArrayList(5);
      l.add(tok);
    }
    return l;
  } //stringSplit

  //----------------------------------------------------------------

  /**
   * full clone of a 2d int array
   * @deprecated moved to zlib.array
   */
  public static int[][] cloneArray(int[][] arr)
  {
    int[][] rarr = (int[][])arr.clone();
    int nr = arr.length;
    for( int r=0; r < nr; r++ ) {
      rarr[r] = (int[])(arr[r]).clone();
    }

    return rarr;
  } //cloneArray

  /**
   * full clone of a 2d float array
   * @deprecated moved to zlib.array
   */
  public static float[][] cloneArray(float[][] arr)
  {
    float[][] rarr = (float[][])arr.clone();
    int nr = arr.length;
    for( int r=0; r < nr; r++ ) {
      rarr[r] = (float[])(arr[r]).clone();
    }

    return rarr;
  } //cloneArray

  /**
   * full clone of a 2d double array
   * @deprecated moved to zlib.array
   */
  public static double[][] cloneArray(double[][] arr)
  {
    double[][] rarr = (double[][])arr.clone();
    int nr = arr.length;
    for( int r=0; r < nr; r++ ) {
      rarr[r] = (double[])(arr[r]).clone();
    }

    return rarr;
  } //cloneArray

  //----------------------------------------------------------------

  /**
   * copy float[][] to double[][]
   */
  public static double[] toDouble(float[] arr)
  {
    int len = arr.length;
    double[] d = new double[len];
    for( int i=0; i < len; i++ ) {
      d[i] = arr[i];
    }
    return d;
  } //toDouble


  /**
   * copy float[][] to double[][]
   * @deprecated moved to zlib.array
   */
  public static double[][] toDouble(float[][] arr)
  {
    int nr = arr.length;
    int nc = arr[0].length;
    double[][] darr = new double[nr][nc];

    for( int r=0; r < nr; r++ ) {
      for( int c=0; c < nc; c++ ) {
	darr[r][c] = arr[r][c];
      }
    }

    return darr;
  } //toDouble


  /**
   * copy double[][] to float[][]
   * @deprecated moved to zlib.array
   */
  public static float[] toFloat(double[] arr)
  {
    int len = arr.length;
    float[] d = new float[len];
    for( int i=0; i < len; i++ ) {
      d[i] = (float)arr[i];
    }
    return d;
  } //toFloat


  /**
   * copy double[][] to float[][]
   * @deprecated moved to zlib.array
   */
  public static float[][] toFloat(double[][] arr)
  {
    int nr = arr.length;
    int nc = arr[0].length;
    float[][] farr = new float[nr][nc];

    for( int r=0; r < nr; r++ ) {
      // TODO: row access to speed up
      for( int c=0; c < nc; c++ ) {
	farr[r][c] = (float)arr[r][c];
      }
    }

    return farr;
  } //toFloat


  //----------------------------------------------------------------

  /**
   * this is in java.util.Arrays
   * @deprecated
   */
  public static boolean equals(double[] arr1, double[] arr2)
  {
    int len = arr1.length;
    if (len != arr2.length) return false;

    for( int i=0; i < len; i++ ) {
      if (arr1[i] != arr2[i]) return false;
    }

    return true;
  } //equals(double[],double[])


  /**
   * this is in java.util.Arrays
   * @deprecated
   */
  public static boolean equals(float[] arr1, float[] arr2)
  {
    int len = arr1.length;
    if (len != arr2.length) return false;

    for( int i=0; i < len; i++ ) {
      if (arr1[i] != arr2[i]) return false;
    }

    return true;
  } //equals(float[],float[])

  //----------------------------------------------------------------

  /**
   * @deprecated moved to zlib.array
   */
  public static int max(int[] arr)
  {
    int len = arr.length;
    int max = Integer.MIN_VALUE;

    for( int i=0; i < len; i++ ) {
      if (arr[i] > max) max = arr[i];
    }

    return max;
  } //max


  /**
   * @deprecated moved to zlib.array
   */
  public static int min(int[] arr)
  {
    int len = arr.length;
    int min = Integer.MAX_VALUE;

    for( int i=0; i < len; i++ ) {
      if (arr[i] < min) min = arr[i];
    }

    return min;
  } //min


  /**
   * @deprecated moved to zlib.array
   */
  public static float max(float[] arr)
  {
    int len = arr.length;
    float max = Float.NEGATIVE_INFINITY;

    for( int i=0; i < len; i++ ) {
      if (arr[i] > max) max = arr[i];
    }

    return max;
  } //max


  /**
   * @deprecated moved to zlib.array
   */
  public static float min(float[] arr)
  {
    int len = arr.length;
    float min = Float.POSITIVE_INFINITY;

    for( int i=0; i < len; i++ ) {
      if (arr[i] < min) min = arr[i];
    }

    return min;
  } //min

  //----------------------------------------------------------------

  public static int atoi(String s)
  {
    return Integer.parseInt(s);
  }

  public static double atof(String s)
  {
    return Double.valueOf(s).doubleValue();
  }

  //----------------------------------------------------------------

  /** test the InputStreamReadLine
   */
  public static void main(String[] args)
  {
    try {
      InputStream f = new FileInputStream("zlib/zlib.java");
      String s = InputStreamReadLine(f);
      System.out.println("line :"+s+":");
      f.close();
    }
    catch(Exception ex) { System.err.println(ex); }
  }

} //zlib