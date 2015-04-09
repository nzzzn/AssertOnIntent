package com.example.musicplayservicetest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ArrayList<Uri> musiclist;		// 음악 재생리스트를 저장할 ArrayList
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		ListView lv = (ListView)findViewById(R.id.listView);		// 레이아웃의 ListView 연결
		musiclist = new ArrayList<Uri>();							// ArrayList 객체 musiclist 생성
		
		lv.setAdapter(new MusicCursorAdapter(this, musiclist));	// ListView 객체에 MusicCursorAdapter 설정
		
		// ListView 의 항목을 선택시 발생되는 Listener 설정
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View itemview, int position, long id) {
				Uri uri = musiclist.get(position);	// 선택한 항목의 인덱스 position
				
				// 현재 선택된 항목에 대한 Intent 객체 생성
				intent = new Intent (MainActivity.this, MusicPlayService.class);
				intent.setData(uri);		// uri 데이터 설정
				startService(intent);		// service 시작(현재 intent에 대한 음악 재생)
			}
		});
	}
	
	// 백버튼을 누를시 다이얼로그 생성
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {			//	백키를 눌렀을 경우 다이얼로그 생성
			AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			dlg.setTitle("종료 다이얼로그");
			dlg.setMessage("프로그램을 종료하시겠습니까?");
			// 확인버튼을 누르면 프로그램 종료
			dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					moveTaskToBack(true);
					stopService(intent);		// service 종료
					finish();
				}
			});
			// 취소버튼을 누르면 프로그램으로 돌아감
			dlg.setNegativeButton("취소", null).show();
		}
		return super.onKeyDown(keyCode, event);
	}
}