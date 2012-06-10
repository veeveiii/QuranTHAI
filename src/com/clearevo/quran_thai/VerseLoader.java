package com.clearevo.quran_thai;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class VerseLoader {
	
    int chapter;
    int verse;    
    public short[] nverses_per_chapter;
    public long[] verseFinalCharPos;    
    public byte[] verseBytes;
    
    public final int MAXVERSEBYTES = 6000;
    public final int MAXVERSECHARS = 6000;
    public final int NCHAPTERS = 114;
    char[] g_out_verse_chars;
    int g_out_text_len;
    
    Activity g_caller_act;
    
    public void alert(String s)
    {
    	Context context = g_caller_act.getApplicationContext();
    	CharSequence text = s;
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();    	
    }
    
    InputStream getResourceAsStream(String fn) throws IOException
    {    	
		return g_caller_act.getAssets().open(fn);	
    }
    
    VerseLoader(Activity caller_act) throws Exception
    {
    	g_caller_act = caller_act;    	
    	chapter = 1;
    	verse = 1;    	
    	verseBytes =  new byte[MAXVERSEBYTES];//based on largest verse size
    	g_out_verse_chars = new char[MAXVERSECHARS];
        
    	//TODO: load prev chap + verse from file
    	
    	//////////////////load n_verses per chapter
    	nverses_per_chapter = new short[NCHAPTERS];
        String fn =  "data/nv.key";            
        java.io.InputStream is = getResourceAsStream(fn);
        DataInputStream dis = new DataInputStream(is);
                
        try
        {
        	int i;
        	for(i=0;i<NCHAPTERS;i++)
	        	nverses_per_chapter[i] = dis.readShort();
        	
        }catch(Exception e)
        {
        throw new Exception("read nv.key failed: "+e.toString());	
        }   
        finally
        {
        is.close();
        }
        /////////////////////////////////////
        
                 
        ////////////////////////////////
        fn =  "data/t.dat";            
        is = getResourceAsStream(fn);
        dis = new DataInputStream(is);
        
        try{                
        	int total_verses = dis.readShort();
        	verseFinalCharPos = new long[total_verses];
        }
        catch(Exception e){
        	throw new Exception("read t.data failed: "+e.toString());
        }         
        finally{
        is.close();
        }
        ////////////////////////////////

        //////////////////////////////////
        fn =  "data/endchar.key";   
        //System.out    .println("db1");   
        is = getResourceAsStream(fn);
        //System.out    .println("db2");   
        dis = new DataInputStream(is);            
        
        try
        {
        	int i;
        	long v;
        	for(i=0;i<verseFinalCharPos.length;i++)
        	{        		
        		v = dis.readLong();
        		verseFinalCharPos[i] = v;
        	}
        }catch(Exception e)
        {
        	throw new Exception("read endchar.key failed: "+e.toString());
        }
        finally
        {
        	is.close();
        }        
        ////////////////////////////
    }
    
    public void close() 
    {
    	//TODO: try write cur chap and verse to file
    }
    
public synchronized String GotoNextVerse()
{
int c,v;
c = this.chapter;
v = this.verse;
v++;
try
{
   return GotoVerse(c,v);
}
catch(Exception e)
{           
    c++;
    v=1;
    try
    {
       return GotoVerse(c,v);
    }
    catch(Exception ee)
    {           
        alert("ถึงวรรคสุดท้ายแล้ว"); 
    }
}

return null;
}

public synchronized String GotoPreviousVerse()
{


int c,v;
c = this.chapter;
v = this.verse;
v--;
if(v>0)
{    
    try
    {
       return GotoVerse(c,v);
    }
    catch(Exception e)
    {           
    	//alert("load failed");              
    }   
}
else
{
    c--;
    v = GetNumberOfVerses(c);
    
    if(v<=0)
    {
    	alert("ถึงวรรคแรกแล้ว");
    }else
    {
    try
    {
       return GotoVerse(c,v);                
    }
    catch(Exception ee)
    {   
    	alert("gotoverse failed.");
    }
    
    }  
  
}      
return null;

}

public int GetNumberOfVerses(int chapter)
{
	if(chapter > NCHAPTERS)
		return 0;
	
	if(chapter == 0)
		return 0;
	else
		return (int) nverses_per_chapter[chapter-1];            
}

int arrayindex;
public long GetVersePos(int chapter, int verse)
{
arrayindex =0;

for (int i = 1; i < chapter; i++) {
    arrayindex+=GetNumberOfVerses(i);
}

arrayindex += verse-1;
--arrayindex;

if( arrayindex<0)
        return 0;

return verseFinalCharPos[arrayindex];

}

public static final int DATFILEMAXSIZE  = 1024*10;

public boolean does_verse_exist(int c, int v)
{
	if(c <1 || v <1 || v> GetNumberOfVerses(c))
		return false;
	
	return true;
}

public synchronized String GotoVerse(int chapter, int verse) throws Exception
{
    if(chapter <1 || verse <1 || verse> GetNumberOfVerses(chapter))
        throw new Exception("invalid verse index");
    
    long pos = GetVersePos(chapter,verse);
    long endpos = verseFinalCharPos[arrayindex+1];
    endpos--; // of this, not start of next 
    
    
    String startfn =  "data/";
    int startFileIndex =(int) (pos/DATFILEMAXSIZE);
    startfn+=((startFileIndex)+1);            
    startfn+=".dat";
    
    
    int endFileIndex = (int) (endpos/DATFILEMAXSIZE);
    
           
    int nread=0;
    int read = -1;
        
    if(startFileIndex == endFileIndex)
    {
        java.io.InputStream is = getResourceAsStream(startfn);           
        long fileStartPos = pos - (startFileIndex*DATFILEMAXSIZE);
        long fileEndPos = endpos - (startFileIndex*DATFILEMAXSIZE);

        try
        {

            long i=0;
            
            if(fileStartPos-1>0)
                i = (int)is.skip(fileStartPos-1);
            
            while(true)
            {
                
                read = is.read();
                
                if(read==-1)
                   break;                                      
                
                if(i>=fileStartPos && i<=fileEndPos)
                {                           
                    verseBytes[nread++] = (byte) read;        
                }
                else         
                if (i>fileEndPos)                        
                    break;
                
                i++;
                
                
            }
            
        }
        catch(Exception e){}  
    
        
        is.close();
    
    }
    else
    {
        java.io.InputStream is = getResourceAsStream(startfn);           
        
        long fileStartPos = pos - (startFileIndex*DATFILEMAXSIZE);         
        
        
        try
        {
            long i=0;
            if(fileStartPos-1>0)
                i = (int)is.skip(fileStartPos-1);
            
            while(true)
            {                        
                read = is.read();
                if(read==-1)
                   break;                                                              
                if(i>=fileStartPos && i<DATFILEMAXSIZE)
                {                           
                    verseBytes[nread++] = (byte)read;        
                }
                i++;
            }
            
        }
        catch(Exception e){}  
        
        is.close();
        
        String endfn = "data/";
        endfn+=((endFileIndex)+1);            
        endfn+=".dat";
        is = getResourceAsStream(endfn);           
        
        long fileEndPos = endpos - (endFileIndex*DATFILEMAXSIZE);                
         try
        {
            long i=0;
            while(true)
            {                        
                read = is.read();
                if(read==-1)
                   break;                                                              
                if(i<=fileEndPos)
                {                           
                    verseBytes[nread++] = (byte)read;        
                }
                i++;
            }
            
        }
        catch(Exception e){}  
        
         is.close();
        
                
    }

    /////convert from utf-8 bytes to chars
    g_out_text_len = 0;
    
    if(nread>0)
    {
        ByteArrayInputStream  bis = new ByteArrayInputStream(verseBytes,0,nread);

        try {

        InputStreamReader dis = new InputStreamReader(bis,"UTF-8");

        int r;

        while(true)
        {
            r = dis.read();
            if(r == -1)
                break;
            g_out_verse_chars[g_out_text_len++] = (char) r;
        }

        } catch (Exception ex) {}
    }
    ///////////////////////////////
    
    this.chapter = chapter;
    this.verse = verse;
    
    return new String(g_out_verse_chars,0,g_out_text_len);
}
	
	
}
