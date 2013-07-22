package com.clearevo.quran_thai;

import com.clearevo.quran_thai.R;
import java.io.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.util.Log;
import android.content.Intent;

//TODO: add search, toast about how to use GOTO option in settings to jump to chapter, verse

//image resize cmd example: convert -resize 96x96 qth.png ../drawable-xhdpi/ic_launcher.png

public class QuranTHAIActivity extends Activity implements ViewSwitcher.ViewFactory,
        View.OnClickListener, OnTouchListener {

	public static final String MENU_GOTO_STR = "ข้ามไปซูเราะห์อื่น...";
	
	final int DIALOG_GOTO_ID = 0;
	
		
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_GOTO_ID:
		{
			// get prompts.xml view
			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.goto_dialog, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText userInput0 = (EditText) promptsView.findViewById(R.id.goto_ch);

			final EditText userInput1 = (EditText) promptsView.findViewById(R.id.goto_v);
			
			// set dialog message
			alertDialogBuilder.setCancelable(true);
			DialogInterface.OnClickListener on_ok = new DialogInterface.OnClickListener() {				
				int c=0,v=1;
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{					
						// get user input and set it to result
						// edit text
						try
						{
					    c = Integer.parseInt(userInput0.getText().toString());
					    try{
					    v = Integer.parseInt(userInput1.getText().toString());
					    }catch(Exception e){}
					    display_verses(c,v,true);
						}catch(Exception e){							
							alert("กรุณาใส่ตัวเลขซูเราะห์");
							return;
						}
						//dialog.cancel();
				}
			};			
			alertDialogBuilder.setPositiveButton("ไป", on_ok);
		
			DialogInterface.OnClickListener on_cancel = new DialogInterface.OnClickListener() {				
				int c,v;
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{			
					//dialog.cancel();						
				}
			};		
			alertDialogBuilder.setNegativeButton("ยกเลิก",on_cancel);
			
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			
		}
		break;		
		}
		
		return dialog;
	}
		
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(MENU_GOTO_STR);
		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getTitle().equals(MENU_GOTO_STR))
        {
        	showDialog(DIALOG_GOTO_ID);
        	return true;
        }
           
        return super.onOptionsItemSelected(item);        
    }

	private TextSwitcher mSwitcher;

    private int mCounter = 0;
    
    VerseLoader g_vl_ar;//arabic
    VerseLoader g_vl_th;//thai

    TextView g_view;
    Typeface face_ar;
    Typeface face_th;

    ScrollView sv;
    LinearLayout ll;
    
    Button ppb, npb; //prev page button, next page button
    
    //prev chapter, verse index
    int g_prevc = 1;
    int g_prevv = 1;
    
    static int NVERSEPERPAGE = 12;
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub    	
    	outState.putInt("g_prevc",g_prevc);
    	outState.putInt("g_prevv",g_prevv);

	save_read_pos();
	super.onSaveInstanceState(outState);
	}

    public void	save_read_pos()
    {
	try
	    {
	String FILENAME = "read_pos.dat";	
	FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
	DataOutputStream dos = new DataOutputStream(fos);
	dos.writeInt(g_prevc);
	dos.writeInt(g_prevv);
	Log.d("qth_act","wrote 4 c:v "+g_prevc+":"+g_prevv);
	dos.close();
	fos.close();	
	    }
	catch(Exception e)
	    {
		Log.d("qth_act","write excep +"+e.toString());
	    }
    }

    public void	get_read_pos()
    {
	try
	    {
	String FILENAME = "read_pos.dat";
	FileInputStream fos = openFileInput(FILENAME);
	DataInputStream dos = new DataInputStream(fos);
	g_prevc = dos.readInt();
	g_prevv = dos.readInt();
	Log.d("qth_act","r 5 c:v"+g_prevc+":"+g_prevv);
	dos.close();
	fos.close();	
	    }
	catch(Exception e)
	    {
		Log.d("qth_act","read excep +"+e.toString());
	    }
    }

    
    public void init_sv()
    {
    	sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        
        //sv.setOnTouchListener(this);
        //sv.seto
        /*TextView tv = new TextView(this);
        try
        {
        tv.setText(g_vl.GotoVerse(1,1));
        ll.addView(tv);
        }
        catch(Exception e)
        {
        	alert("goto init verse failed: "+ e.toString());
        }*/
        
        ppb = (Button) new Button(this);    		
		ppb.setText("ก่อนหน้า...");
		ppb.setOnClickListener(this);
		
		npb = (Button) new Button(this);    		
		npb.setText("ต่อไป...");
		npb.setOnClickListener(this);
    }
    
    
    
    public void display_verses(int c, int v, boolean forward) //starting with c,v
    {
    	//check if valid first
    	if(!g_vl_th.does_verse_exist(c,v))
    	{
    		alert("ไม่พบ ซูเราะห์:อายะห์ "+c+":"+v+" ในฐานข้อมูล");
    		return;
    	}
    	////////////////////
    	
    	ll.removeAllViews();  
    	g_prevc = c;
    	g_prevv = v;
    	
    	for(int i=0;i<NVERSEPERPAGE;i++)
    	{
	    TextView tv_ar;
	    if (android.os.Build.VERSION.SDK_INT < 11) {
		tv_ar = null;
	    } else {		
		tv_ar = new TextView(this);	    
		tv_ar.setTypeface(face_ar); 
		tv_ar.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
	    }
		//tv_ar.setLineSpacing (0, (float) 1.5 );
		//float tsx = tv_ar.getTextScaleX();
		//tsx *= 2.0;
		//tv_ar.setTextScaleX(tsx);


	    	TextView tv_th = new TextView(this);
		tv_th.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
		tv_th.setTypeface(face_th); 

	        try
	        {
	        	String s = null;
	        	if(i==0)
	        		s = g_vl_th.GotoVerse(c, v);
	        	else	        	
	        	{
	        		if(forward)
	        			s= g_vl_th.GotoNextVerse();
	        		else
	        			s= g_vl_th.GotoPreviousVerse();
	        	}
	        	
	        	if(s == null)
	        	{	        		
	        		break;
	        	}

			s += "\n__________";

	        	tv_th.setText(s);

			//add arabic
			//thai arab universities alumni association's translation excludes bismillah except surah 1
			int tc, tv;
			tc = g_vl_th.chapter;			
			tv = g_vl_th.verse;
			
			
			/*if(tc == 1 || tc == 9)
			  ;
			else
			    tv += 1;
			*/

			if(tv_ar != null) {
			s = g_vl_ar.GotoVerse(tc, tv);
			    if (s != null)
				tv_ar.setText(s);
			}
	        	
	        	if(forward)
			    {
				if(tv_ar != null)
				    ll.addView(tv_ar);

				ll.addView(tv_th);
			    }
	        	else
			    {
	        		ll.addView(tv_th,0);
				
				if(tv_ar != null) 
				    ll.addView(tv_ar,0);
			    }
	        }
	        catch(Exception e)
	        {
	        	alert("get verse failed: "+ e.toString());
	        }
    	}   
    	
    	if(!forward) //set as if we loaded forward from c,v behind
    	{
    		int tc, tv;
    		tc = g_vl_th.chapter;
    		tv = g_vl_th.verse;
    		
    		g_vl_th.chapter = g_prevc;
    		g_vl_th.verse = g_prevv;
    		
    		g_prevc = tc;
    		g_prevv = tv;		
    	}
		
    	if(g_prevc == 1 && g_prevv == 1)    	
    		;//dont add prev_button
    	else
    		ll.addView(ppb,0);//add prev button
    	
    	if(g_vl_th.chapter == g_vl_th.NCHAPTERS && g_vl_th.verse == g_vl_th.GetNumberOfVerses(g_vl_th.NCHAPTERS))    	
    		;//dont add next
    	else
    		ll.addView(npb);//add next button
    	
    	if(forward)
    	{
    		sv.scrollTo(0, 0);    		
    	}
    	else
    	{
    		//sv.scrollTo(0, sv.getHeight()-1);
    		sv.post(new Runnable() {

    	        @Override
    	        public void run() {
    	            sv.fullScroll(ScrollView.FOCUS_DOWN);
    	        }
    	    });
    	}
    }
    
    public void change_page(boolean next)
    {
    	if(next)
    	{
    		display_verses(g_vl_th.chapter,g_vl_th.verse,true);
    	}
    	else
    	{
    		
    			display_verses(g_prevc,g_prevv,false);
    	}
    }
    
    
    public void alert(String s)
    {
    	Context context = getApplicationContext();
    	CharSequence text = s;
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();    	
    }

    public void alert_long(String s)
    {
    	Context context = getApplicationContext();
    	CharSequence text = s;
    	int duration = Toast.LENGTH_LONG;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();    	
    }

    
    @Override
    public void onDestroy ()
    {
	super.onDestroy();
	save_read_pos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

	if(savedInstanceState == null)
	    get_read_pos();
        
        //////////////////////NEW CODE - LONG VERTICAL LIST
        //http://www.dreamincode.net/forums/topic/130521-android-part-iii-dynamic-layouts/
        try
        {
	    g_vl_th = new VerseLoader(this,"data_th");
	    g_vl_ar = new VerseLoader(this,"data_ar");
	    g_vl_ar.skip_verse_num = true;
        }
        catch(Exception e)
        {
        	alert("create verse_loader failed: "+e.toString());	
        }
        
        init_sv();
        

        if(savedInstanceState != null)
        {
        	g_prevc = savedInstanceState.getInt("g_prevc", 1);
        	g_prevv = savedInstanceState.getInt("g_prevv", 1);
        }
        else
        {
	    //do nothing - already loaded from get_read_pos(); above
        }

	if(face_ar == null)
	    face_ar = Typeface.createFromAsset(getAssets(), "fonts/DroidSansArabic.ttf"); 

	if(face_th == null)
	    face_th = Typeface.createFromAsset(getAssets(), "fonts/Waree.ttf"); 


	try {
	//////if we got intetnt to open chapter to overwrite the chapter and verse
	Intent intent = getIntent();
	short read_chap;
	    read_chap = intent.getShortExtra("schap",(short) 0);	
	    Log.d("qth_act","start_chap key in intent val "+read_chap);
	    if(read_chap != 0)
		{
		    g_prevc = (int) read_chap;
		    g_prevv = 1;
		}
	} catch (Exception e) {}

        display_verses(g_prevc,g_prevv,true);
        
        this.setContentView(sv);

	if (android.os.Build.VERSION.SDK_INT < 11)
	    alert_long("โทรศัพท์นี้ไม่รองรับการแสดงอักษรอาหรับแบบติดกิน (ต้องการ Android 3.0 หรือใหม่กว่า)");
    }

    public void onClick(View v) {
        if(g_vl_th != null)
        {
        	if(v == npb)
        	{
        		change_page(true);
        	}
        	else if( v == ppb)
        	{
        		change_page(false);
        	}
        }
    }

    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(40);	
	//t.setTypeface(face); 
        t.setOnTouchListener(this);
        g_view = t;
        return t;
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//not used anymore...
		if(sv != null && sv == v)
		{
			
		}
		
		return false;
	}
}