package fr.inria.astor.util;

public class OsUtils
{
   private static String OS = null;
   public static String getOsName(){
      if(OS == null) { 
    	  OS = System.getProperty("os.name"); 
	  }
      return OS;
   }
   
   public static boolean isWindows(){
      return getOsName().startsWith("Windows");
   }

   public static boolean isLinux(){
	   return getOsName().startsWith("Linux");
   }

	public static ProcessBuilder getBashProcessBuilder() {
		if (OsUtils.isWindows())
			return new ProcessBuilder("cmd.exe");
		return new ProcessBuilder("/bin/bash");
	}
}