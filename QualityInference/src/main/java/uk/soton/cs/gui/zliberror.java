package uk.soton.cs.gui;

//zliberror.java
//modified
//feb01 scruff



import java.io.*;

final public class zliberror 
{
public static boolean _strictassert = true;

/**
	  JOptionPane.showMessageDialog(_f,
					"error: something bad...",
					"alert", JOptionPane.ERROR_MESSAGE);
**/

// improve...
public static final void _assert(boolean b)
{
 if (!b) {
   System.out.flush();
   // TODO: may want to throw exception instead so app can trap it
   Error ex = new Error("assert failed");
   if (_strictassert) {
	System.err.println("assert failed");
	ex.printStackTrace();
	System.exit(1);
   }
   else
	throw ex;
 }
} //_assert


public static final void _assert(boolean b, String msg)
{
 if (!b) {
   System.out.flush();
   // TODO: may want to throw exception instead so app can trap it
   Error ex = new Error("assert failed: "+msg);
   if (_strictassert) {
	System.err.println("assert failed: "+msg);
	ex.printStackTrace();
	System.exit(1);
   }
   else
	throw ex;
 }
} //_assert



/**
* @deprecated

public static final void assert(boolean b)
{
 if (!b) {
   System.out.flush();
   // TODO: may want to throw exception instead so app can trap it
   Error ex = new Error("assert failed");
   if (_strictassert) {
	System.err.println("assert failed");
	ex.printStackTrace();
	System.exit(1);
   }
   else
	throw ex;
 }
} //assert
*/

/**
* @deprecated
public static void assert(boolean b, String msg)
{
 if (!b) {
   System.out.flush();
   // TODO: may want to throw exception instead so app can trap it
   Error ex = new Error("assert failed: "+msg);
   if (_strictassert) {
	System.err.println("assert failed: "+msg);
	ex.printStackTrace();
	System.exit(1);
   }
   else
	throw ex;
 }
} //assert
*/

/**
*/
public static void printStackTrace(String msg)
{
 Error ex = new Error(msg);
 System.err.println(msg);
 ex.printStackTrace();
}


/**
* assertion fails, but just give a warning
* (useful in debugging sometimes)
*/
public static void warn(boolean b, String msg)
{
 if (!b) {
   System.out.flush();
   // TODO: may want to throw exception instead so app can trap it
   Error ex = new Error("ASSERT WARNING: "+msg);
   ex.printStackTrace();
   System.err.println(ex);
 }
} //warn


public static void error(String s)
{
 System.out.flush();
 Error ex = new Error(s);
 System.err.println("ERROR: " + s);
 // throw ex;
 ex.printStackTrace();
 System.exit(1);
}

public static void warning(String s)
{
 // print on out rather than err because printing on
 // err puts it in the wrong place
 System.out.println("WARNING " + s);
}

/**
* this is for a user error so do not print stack
*/
public static void quit(int code, String msg)
{
 System.err.println(msg);
 System.exit(code);
}

/**
* print exception, stacktrace, quit
*/
public static void die(Exception x)
{
 System.out.flush();
 x.printStackTrace();
 System.out.flush();
 System.err.println(x);
 System.exit(1);
} //die

/**
* assemble and throw a nicely formatted message
* indicating where the StreamTokenizer is located.
*/
public static void parseError(StreamTokenizer st, String path)
 throws IOException
{
 String errmsg = "error parsing "+path+", line " + st.lineno();
 switch(st.ttype) {
 case StreamTokenizer.TT_WORD:
   errmsg += " near token " + st.sval;
   break;
 case StreamTokenizer.TT_NUMBER:
   errmsg += " near token " + st.nval;
   break;
 case StreamTokenizer.TT_EOL:
   errmsg += " near end of line";
   break;
 case StreamTokenizer.TT_EOF:
   errmsg += " near eof";
   break;
 }

 throw new IOException(errmsg);
} //parseError

} //class zlib