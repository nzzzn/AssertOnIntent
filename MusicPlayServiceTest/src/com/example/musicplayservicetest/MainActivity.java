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
	private ArrayList<Uri> musiclist;		// ���� �������Ʈ�� ������ ArrayList
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		ListView lv = (ListView)findViewById(R.id.listView);		// ���̾ƿ��� ListView ����
		musiclist = new ArrayList<Uri>();							// ArrayList ��ü musiclist ����
		
		lv.setAdapter(new MusicCursorAdapter(this, musiclist));	// ListView ��ü�� MusicCursorAdapter ����
		
		// ListView �� �׸��� ���ý� �߻��Ǵ� Listener ����
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View itemview, int position, long id) {
				Uri uri = musiclist.get(position);	// ������ �׸��� �ε��� position
				
				// ���� ���õ� �׸� ���� Intent ��ü ����
				intent = new Intent (MainActivity.this, MusicPlayService.class);
				intent.setData(uri);		// uri ������ ����
				startService(intent);		// service ����(���� intent�� ���� ���� ���)
			}
		});
	}
	
	// ���ư�� ������ ���̾�α� ����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {			//	��Ű�� ������ ��� ���̾�α� ����
			AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			dlg.setTitle("���� ���̾�α�");
			dlg.setMessage("���α׷��� �����Ͻðڽ��ϱ�?");
			// Ȯ�ι�ư�� ������ ���α׷� ����
			dlg.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					moveTaskToBack(true);
					stopService(intent);		// service ����
					finish();
				}
			});
			// ��ҹ�ư�� ������ ���α׷����� ���ư�
			dlg.setNegativeButton("���", null).show();
		}
		return super.onKeyDown(keyCode, event);
	}
}