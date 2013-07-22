package com.clearevo.quran_thai;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.util.Log;

//credit to http://www.mkyong.com/android/android-listview-example/ 

public class QuranTHAIMenuActivity extends ListActivity {
 
    /*thai chapter list credit to http://www.alquran-thai.com */
    static final String[] MENU = new String[] { "อ่านต่อจากครั้งก่อน...", 
						    "1	Al-Fatiha - อัลฟาติหะฮฺ",
						    "2	Al-Baqarah - อัล-บะเกาะเราะฮฺ",
						    "3	Al-i-Imron - อาละอิมรอน",
						    "4	An-Nisaa - อัน-นิซาอฺ",
						    "5	Al-Maidah - อัล-มาอิดะฮฺ",
						    "6	Al-AnAm - อัล-อันอาม",
						    "7	Al-Araf - อัล-อะอฺรอฟ",
						    "8	Al-Anfal - อัล-อันฟาล",
						    "9	At-Touba - อัต-เตาบะฮฺ",
						    "10	Yunus - ยูนุส",
						    "11	Hud - ฮูด",
						    "12	Yusuf - ยูซุฟ",
						    "13	Ar-Rahdu - อัรเราะอฺดฺ",
						    "14	Ibrahim - อิบรอฮีม",
						    "15	Al-Hijr - อัลฮิจรฺ",
						    "16	An-Nahl - อันนะหฺลฺ",
						    "17	Al-Israa - อัลอิสรออฺ",
						    "18	Al-kahf - อัลกะฮฺฟฺ",
						    "19	Maryam - มัรยัม",
						    "20	Ta-Ha - ฏอฮา",
						    "21	Al-Anbiyaa - อัลอัมบิยาอฺ",
						    "22	Al-Hajj - อัลฮัจยฺ",
						    "23	Al-Muminun - อัลมุอฺมินูน",
						    "24	An-Nur - อันนูรฺ",
						    "25	Al-Furqan - อัลฟุรกอน",
						    "26	Ash-Shuaraa - อัชชุอะรออฺ",
						    "27	An-Naml - อันนัมลฺ",
						    "28	Al-Qasas - อัลเกาะศ็อศ",
						    "29	Al-Ankabut - อัลอังกะบูต",
						    "30	Ar-Rum - อัรฺรูม",
						    "31	Luqman - ลุกมาน",
						    "32	As-Sajda - อัซซัจญดะฮฺ",
						    "33	Al-Ahzab - อัลอะหฺซาบ",
						    "34	Saba - สะบะอฺ",
						    "35	Fatir - ฟาฏิร",
						    "36	Ya-Sin - ยาซีน",
						    "37	As-Saffat - อัศศ็อฟฟาต",
						    "38	Sad - ศอด",
						    "39	Az-Zumar - อัซซุมัร",
						    "40	Gafir - ฆอฟิ",
						    "41	Fussilat - ฟุศศิลัต",
						    "42	Ash-shura - อัซซูรอ",
						    "43	Az-Zukhruf - อัซซุครุฟ",
						    "44	Ad-Dukhan - อัดดุคอน",
						    "45	Al-Jathiya - อัลญาซียะอฺ",
						    "46	Al-Ahqaf - อัลอะฮฺก็อฟ",
						    "47	Muhammad - มุฮัมมัด",
						    "48	Al-fat-h - อัลฟัตฮฺ",
						    "49	Al-Hujurat - อัลหุญร๊อต",
						    "50	Qaf - ก็อฟ",
						    "51	Az-Zariyat - อัซซาริยาต",
						    "52	At-Tur - อัฎฏูร",
						    "53	An-Najm - อันนัจมฺ",
						    "54	Al-Qamar - อัลเกาะมัร",
						    "55	Ar-Rahman - อัรรอหฺมาน",
						    "56	Al-Waqi-ah - อัลวากิอะฮฺ",
						    "57	Al-Hadid - อัลหะดีด",
						    "58	Al-Mujadila - อัลมุญาดะละฮฺ",
						    "59	Al-Hashr - อัลหัซรฺ",
						    "60	Al-Mumtahana - อัลมุมตะฮินะฮฺ",
						    "61	As-Saff - อัศศ็อฟ",
						    "62	Al-Jumuah - อัลญุมุอะฮฺ",
						    "63	Al-Munafiqun - อัลมุนาฟิกูน",
						    "64	At-Tagabun - อัตตะฆอบุน",
						    "65	At-Talaq - อัฎเฎาะล๊าก",
						    "66	At-Tahrim - อัตตะหฺรีม",
						    "67	Al-Mulk - อัลมุลกฺ",
						    "68	Al-Qalam - อัลก้อลัม",
						    "69	Al-Haqqa - อัลหากเกาะฮฺ",
						    "70	Al-Maarig - อัลมาอาริจญ์",
						    "71	Nuh - นูหฺ",
						    "72	Al-Jinn - อัลญิน",
						    "73	Al-Muzzammil -  อัลมุซซัมมิล",
						    "74	Al-Muddaththir - อัลมุดดัซซิร",
						    "75	Al-Qiyamat - อัลกิยามะฮฺ",
						    "76	Al-Insan - อัลอินซาน",
						    "77	Al-Mursalat - อัลมุรซะล้าต",
						    "78	An-Nabaa - อันนะบะอฺ",
						    "79	An-Nazi - อันนาซิอ๊าต",
						    "80	Abasa - อะบะซะ",
						    "81	At-Takwir - อัตตักวีร",
						    "82	Al-Infitar - อัลอิมฟิฏอร",
						    "83	Al-Mutaffifeen - อัลมุฏ็อฟฟิฟีน",
						    "84	Al-Inshiqaq - อัลอินชิก๊อก",
						    "85	Al-Buruj - อัลบุรู๊จญ์",
						    "86	At-Tariq - อัฏฏอริก",
						    "87	Al-Ala - อัลอะอฺลา",
						    "88	Al-Gashiya - อัลฆอซิยะฮฺ",
						    "89	Al-Fajr - อัลฟัจญรฺ",
						    "90	Al-Balad - อัลบะลัด",
						    "91	Ash-Shams - อัชชัมซฺ",
						    "92	Al-Lail - อัลลัยลฺ",
						    "93	Ad-Dhuha - อัฎฎุฮา",
						    "94	Al-Sharh - อัลอินซิรอฮฺ",
						    "95	At-Tin - อัตตีน",
						    "96	Al-Alaq - อัลอะลัก",
						    "97	Al-Qadr - อัลก็อดร",
						    "98	Al-Baiyina - อัลบัยยินะฮฺ",
						    "99	Al-Zalzalah - อัลซัลซะละฮฺ",
						    "100 Al-Adiyat - อัลอาดิยาต",
						    "101 Al-Qariah - อัลกอริอะฮฺ",
						    "102 At-Takathur - อัตตะกาซุร",
						    "103 Al-Asr - อัลอัศรฺ",
						    "104 Al-Humaza - อัลฮุมะซะฮฺ",
						    "105 Al-Fil - อัลฟีล",
						    "106 Al-Quraish - อัลกุรอยซฺ",
						    "107 Al-Maun - อัลมาอูน",
						    "108 Al-Kauthar - อัลเกาซัร",
						    "109 Al-Kafirun - อัลกาฟิรูน",
						    "110 An-Nasr - อัลนัศรฺ",
						    "111 Al-Masad - อัลมะซัด",
						    "112 Al-Ikhlas - อัลอิคลาส",
						    "113 Al-Falaq - อัลฟะลัก",
						    "114 An-Nas - อันนาส",
						    "__________",
						    "ข้อมูลอ้างอิงจาก qurandatabase.org",
						    "แบบอักษรไทยจาก linux.thai.net",
						    "แบบอักษรอาหรับจาก ahmedre/quran_android"
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		// no more this
		// setContentView(R.layout.list_fruit);
 
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_menu,MENU));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    Intent myIntent = new Intent(QuranTHAIMenuActivity.this, QuranTHAIActivity.class);

			    Log.d("qth_actmenu","onitemclick id "+id);
	    
			    if(id > 114)
				return; //about pressed - do nothing

			    //id 0 just follow through - because it's "continue from last time"

			    if (id > 0)
				{
				    myIntent.putExtra("schap", (short) id); //Optional parameters
				    short ival = myIntent.getShortExtra("schap",(short)0);
				    Log.d("qth_actmenu","onitemclick id, added intent with extra val "+ival);
				}

			    QuranTHAIMenuActivity.this.startActivity(myIntent);
			}
		});
 
	}
 
}