package com.example.musicplayservicetest;

import java.util.ArrayList;

import kr.ac.yonsei.mobilesw.assertonintent.AssertOnIntent;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicPlayService extends Service {
	private MediaPlayer mp;
	
	// MusicPlayService �� �̿�� ó�� ȣ��� �ʱ�ȭ �޼ҵ�
	public void onCreate() {
		super.onCreate();
		//mp = new MediaPlayer();	// MediaPlayer ��ü ����
	}
	
	// MusicPlayService �� ����� ȣ��Ǵ� �޼ҵ�
	public void onDestroy() {
		super.onDestroy();
		//mp.release();		// MediaPlayer ��ü�� ����� ���ҽ� ����
	}
	
	// MusicPlayService ���� ��û�� ȣ��Ǵ� �޼ҵ�
	public int onStartCommand(Intent intent, int flags, int startId) {
		final int _stratId = startId;
		
		new AssertOnIntent().assertOnIntent(intent, 
				"{ act = android.intent.action.Play cmp = com.example.musicplayservicetest/.MusicPlayService [test=String] }",
				null);
		
		int[] count = intent.getIntArrayExtra("count");
		int countSum = count[0] + 1;
		
		/*
		// intent�� ������ ����� ������ �Ǿ��� ��
		if (intent != null) {
			Uri uri = intent.getData();		// �÷��� �� ����� �����͸� �о��
			
			try {
				// ���� ����� �Ϸ�Ǹ�
				mp.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						MusicPlayService.this.stopSelf(_stratId);		// MusicPlayService ���� ��û ���� �Ϸ�
					}
				});
				
				mp.reset();												// MediaPlayer ��ü �ʱ�ȭ ���� ���� ���·� �缳��
				mp.setDataSource(getApplicationContext(), uri);	// MediaPlayer ��ü�� ����� ���� �����ͼҽ� ����
				mp.prepare();											// ���� ����� �����ϱ� ���� �÷��̾ �غ�
				mp.start();													// MediaPlayer ����
			} catch (Exception e) {
				e.printStackTrace();		// ������ ���� Ʈ���̽��� System.err �� ���
			}
		}
		
		*/
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	public IBinder onBind(Intent arg0) {
		return null;
	}

}

