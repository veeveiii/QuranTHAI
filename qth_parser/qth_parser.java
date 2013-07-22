import java.io.File;
import java.io.*;
import java.util.Vector;

public class qth_parser {
    
    public static String default_in_fn = "q.txt";
    public static final String endl_str = "\n";
    public static final String vseparator = "@";    
    public static final int datmax_size  = 1024*10;
    
    public static boolean rm_dir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = rm_dir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }

    public static void main(String[] args) {

        try{
	    File f;
       
	    f = new File("data");
        
	    if( rm_dir(f))
		System.out.println("rm old data folder");
        
	    if(f.mkdir())
		System.out.println("cre new data folder");
        
        
	    String s = new String();        
	    int read;
	    int pos = 0;
	    int chapter = 0;
        
	    byte[] fbytes=null;                

	    if(args.length != 0)
		default_in_fn = args[0];

	    f = new File(default_in_fn);
	    fbytes =  new byte[(int)f.length()];       
	    FileInputStream fs = new FileInputStream(default_in_fn);
	    fs.read(fbytes);        
       
        
	    String fstr =  new String(fbytes);    
        
	    fstr = fstr.replaceAll("\r","\n");
        
	    fstr = fstr.replaceAll("\n\n","\n");
        
	    //System.out.println(allverses.length + "verses found");
        
	    Vector[] verses = new Vector[114];
        
	    String fn;
        
	    if(true) //scope
		{
            
		    int parsepos = 0;
            
		    for(int chp=0;chp<114;chp++)
			{
			    verses[chp] = new Vector();
                
			    int verse = 0;
			    int startpos;
			    int endpos;
               
			    boolean breakatend = false;
                                            
			    while(true)
				{
				    startpos = fstr.indexOf( "\n"+(chp+1)+":"+(verse+1) , parsepos);
				    if(startpos>=0)
					parsepos = startpos;
				    else
					if(startpos<0)
					    {
						startpos = fstr.indexOf( " "+(chp+1)+":"+(verse+1) , parsepos);
						if(startpos>=0)
						    parsepos = startpos;
						else
						    {
							startpos = fstr.indexOf( "\n"+(chp+1)+":"+(verse+1)+"." , parsepos);
							if(startpos>=0)
							    parsepos = startpos;
							else                               
							    {
								System.out.println("chapter "+(chp+1)+" ending at verse" + verse);
								breakatend = true;
								break;
							    }
						    }
                       
					    }

                       
                       
				    endpos = fstr.indexOf( "\n"+(chp+1)+":"+(verse+2) , parsepos);
				    if(endpos<0)
					{
					    endpos = fstr.indexOf( " "+(chp+1)+":"+(verse+2) , parsepos);
					    if(endpos<0)
						{
                              
						    endpos = fstr.indexOf( "\n"+(chp+1)+":"+(verse+2)+"." , parsepos);
						    if(endpos<0)
							{
							    System.out.println("chapter "+(chp+1)+":"+(verse+2)+" not found");
                                   
							    endpos = fstr.indexOf( "\n"+(chp+2)+":"+(1)+" " , parsepos);
							    if(endpos<0)
								{
                                      
                                      
								    endpos = fstr.indexOf( " "+(chp+2)+":"+(1) , parsepos);
								    if(endpos<0)
									{
									    endpos = fstr.indexOf( "\n"+(chp+2)+":"+(1)+"." , parsepos);
									    if(endpos<0)
										{
										    System.out.println("Last chapter, last verse");
                                           
                                            
										    endpos = fstr.length()-1;

										    System.out.println(fstr.substring(startpos,endpos));
                                            
                                            
										}
									}                 
                                   
                                
								}
                                  
							    if(endpos<0)
								{
								    System.out.println("ERROR - ENDING VERSE NOT SUCCESS" + (chp+1)+":"+(verse+1));
								}
							    //System.out.println(fstr.substring(startpos,endpos));
							}
						}   
					}
                      
				    if(endpos<0)
					{
					    System.out.println("ERROR - NEXT VERSE NOT FOUND! from" + (chp+1)+":"+(verse+1));
					}
                      
				    //parsepos = endpos;
				    String thisverse = fstr.substring(startpos,endpos);
				    //System.out.println(thisverse);
				    verses[chp].add(thisverse.trim());      
                       
				    if(breakatend)
					break;
                       
				    verse++;
                        
                    

				}
               
                
              
                    
                    
			}
                  
		    //return;
		}
            
            
           
               
               
               
        
        
     
        
        
      
	    System.out.println("Writing data to \"data\" folder");
	    //make 2 key files and 1 data file: 
        
	    // - key chapter:numberofverses unsignedshort format: 114 unsignedshorts
	    // - key allverses:endcharpos
	    // - all data file
        
        
	    //number of verses of a chapter
	    String nvfn = "data";
	    nvfn += File.separatorChar;                
	    nvfn += "nv";
	    nvfn += ".key";               
	    FileOutputStream nvfo =  new FileOutputStream(nvfn);               
	    DataOutputStream nvdo =  new DataOutputStream(nvfo);
                
	    /* //number of verses before a chapter
	       String nvbffn = "data";
	       nvbffn += File.separatorChar;                
	       nvbffn += "vbfc";
	       nvbffn += ".key";               
	       FileOutputStream nvbffo =  new FileOutputStream(nvbffn);               
	       DataOutputStream nvbfdo =  new DataOutputStream(nvbffo);
	    */
                
	    //ending character position of a verse
	    String endcharfn = "data";
	    endcharfn += File.separatorChar;                
	    endcharfn += "endchar";
	    endcharfn += ".key";               
	    FileOutputStream endcharfo =  new FileOutputStream(endcharfn);               
	    DataOutputStream endchardo =  new DataOutputStream(endcharfo);
                
	    String alldatfn = "data";
	    alldatfn += File.separatorChar;                
	    alldatfn += "all";
	    alldatfn += ".dat";               
	    FileOutputStream alldatfo =  new FileOutputStream(alldatfn);               
                
	    pos = 0;        
	    //int nvbf=0;
	    int totalverses=0;
	    for (int i = 0; i < verses.length; i++) {            
                
                nvdo.writeShort(verses[i].size());   
                
                //nvbfdo.writeShort(nvbf);
                //nvbf+=verses[i].length;
                
                //nvfo.write(Integer.toString(verses[i].length).getBytes());
                //nvfo.close();
                
		for (int j = 0; j < verses[i].size(); j++)
		    {
			totalverses++;    
			byte[] bte = ((String)verses[i].get(j)).getBytes("UTF-8");
			//System.out.println((String)verses[i].get(j));
			pos+= bte.length;
			endchardo.writeLong(pos); //changed to writelong for android readint bug
			alldatfo.write(bte);               
		    }
            
	    }
        
	    alldatfo.flush();
	    alldatfo.close();
        
	    endchardo.flush();        
	    endcharfo.flush();
	    endchardo.close();        
	    endcharfo.close();
        
	    nvdo.flush();        
	    nvfo.flush();
	    nvdo.close();        
	    nvfo.close();
        
	    /*nvbfdo.flush();
	      nvbffo.flush();
	      nvbfdo.close();
	      nvbffo.close();*/
        
        
	    FileInputStream fis = new FileInputStream(alldatfn);
	    f = new File(alldatfn);
	    byte[] datba = new byte[datmax_size];
        
        
	    for(int i=0;i<((f.length())/(datmax_size))+(((f.length())%(datmax_size))>0?1:0);i++) 
		{
		    fn = "data";
		    fn += File.separatorChar;                
		    fn += i+1;
		    fn += ".dat";   
                
		    read = fis.read(datba);
		    FileOutputStream fo =  new FileOutputStream(fn);
		    fo.write(datba,0,read);
		    fo.close();
		}
        
	    fis.close();
        
	    File adf = new File(alldatfn);
	    adf.delete();
                
          
	    fn = "data";
	    fn += File.separatorChar;                
	    fn += "t";
	    fn += ".dat";               
	    FileOutputStream tfo =  new FileOutputStream(fn);               
	    DataOutputStream dtfo =  new DataOutputStream(tfo);
	    dtfo.writeShort(totalverses);
	    tfo.close();
        
        
	    /*
	      while(true)
	      {
         
	      if(pos == fbytes.length)
	      {
	      JOptionPane.showMessageDialog(null,"End of File Reached");
	      return;
	      }            
            
	      if()
	      {
	      System.out.println("found CHAPTERPREFIXSTR");
	      if(s.endsWith("\n"))
	      {
	      chapter = Integer.parseInt(s.substring(CHAPTERPREFIXSTR.length()-1,s.length()-1).trim());
	      s = "";
	      System.out.println(chapter);
	      }
                
	      }
            
	      /*if(s.endsWith("\n"))
	      {
	      String ofn = Integer.toString(chapter);
	      ofn+="_";
	      ofn+=verse;
	      FileOutputStream fo = new FileOutputStream(ofn);
	      byte[] bytes = s.getBytes();
	      fo.write(bytes);
	      fo.close();
	      s="";
	      verse++;
	      continue;
	      }
            
            
        
	      //}*/
        
	    System.out.println("\r\nDone\r\n");
        
        }
        catch(Exception e)
	    {e.printStackTrace();System.out.println("Encountered Exception: "+e.toString());}
        
        
        
        
    }
    
    
    public static String[] ParseChapterToVerses(String chapter,int chapternumber) throws Exception
    {
        String ori =  chapter.toString();
	chapter = chapter.trim();
	int cv=0;//current verse
	String verseStr;
           
	while(true)
	    {
		verseStr=Integer.toString(++cv);
		verseStr+=".";           
		if(chapter.contains(verseStr))
		    {    
               
               
			if(cv>1)
			    {
				/*String replacement;
				  replacement = vseparator.toString();
				  replacement += '[';
				  replacement += chapternumber;
				  replacement += ':';               
				  replacement += Integer.toString(cv);               
				  replacement += ']';
				  chapter = chapter.replaceFirst(verseStr,replacement);                */
				String rep = vseparator+verseStr;
				char[] repca = rep.toCharArray();
				for (int i = 0; i < repca.length; i++) {
				    if(repca[i]== '.')
					{
					    repca[i] = ' ';
					    break;
					}
				}   
                   
				rep = String.copyValueOf(repca);                    
				/*rep = rep.substring(0,rep.length()-1);
				  rep += " ";*/
				chapter = chapter.replaceFirst(verseStr,rep);
                   
			    }
			else
			    {
				/*    String replacement;
				      replacement = "[";
				      replacement += chapternumber;
				      replacement += ':';               
				      replacement += Integer.toString(cv);               
				      replacement += ']';
				      chapter = chapter.replaceFirst(verseStr,replacement);                */
                   
				//chapter =  chapter.replaceFirst("."," ");
				char[] repca = chapter.toCharArray();
				for (int i = 1; i < repca.length; i++) {
				    if(repca[i]== '.' && repca[i-1]>='0' && repca[i-1]<='9')
					{
					    repca[i] = ' ';
					    break;
					}
				}   
                   
				chapter = String.copyValueOf(repca);         
                   
                   
			    }
               
		    }
		else
		    {
			//System.out.println("out at"+verseStr);
			cv--;
			break;
		    }
           
	    }
           
           
	chapter = chapter.trim();
           
           
	String[] ret = chapter.split(vseparator);
           
	for(int j=0;j<ret.length;j++)
	    {
		verseStr=Integer.toString(j+1);  
               
		String rep = String.copyValueOf(verseStr.toCharArray());
		rep = Integer.toString(chapternumber) + ":";
		rep+=verseStr;
		ret[j] = ret[j].replaceFirst(verseStr,rep);
               
		//ret[j] =  chapternumber+":"+ret[j];
	    }
           
	if(ret.length!=cv)
	    {
		for(int j=0;j<ret.length;j++)
		    System.out.println(ret[j]);
               
		String errs = "ParseChapterToVerses-> verse parsing check failed:";
		errs += ret.length;
		errs+="!=";
		errs+=cv;
		//errs+=": ";
		//errs+=chapter;
		throw new Exception(errs);
	    }
        //parse the verses
	return ret;
           
    
    }
                    
    
                    
    
}
