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
	
	// MusicPlayService 를 이용시 처음 호출시 초기화 메소드
	public void onCreate() {
		super.onCreate();
		//mp = new MediaPlayer();	// MediaPlayer 객체 생성
	}
	
	// MusicPlayService 를 종료시 호출되는 메소드
	public void onDestroy() {
		super.onDestroy();
		//mp.release();		// MediaPlayer 객체에 연결된 리소스 해제
	}
	
	// MusicPlayService 서비스 요청시 호출되는 메소드
	public int onStartCommand(Intent intent, int flags, int startId) {
		final int _stratId = startId;
		
		new AssertOnIntent().assertOnIntent(intent, 
				"{ act = android.intent.action.Play cmp = com.example.musicplayservicetest/.MusicPlayService [test=String] }",
				null);
		
		int[] count = intent.getIntArrayExtra("count");
		int countSum = count[0] + 1;
		
		/*
		// intent의 정보가 제대로 전송이 되었을 시
		if (intent != null) {
			Uri uri = intent.getData();		// 플레이 할 오디오 데이터를 읽어옴
			
			try {
				// 음악 재생이 완료되면
				mp.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						MusicPlayService.this.stopSelf(_stratId);		// MusicPlayService 서비스 요청 수행 완료
					}
				});
				
				mp.reset();												// MediaPlayer 객체 초기화 되지 않은 상태로 재설정
				mp.setDataSource(getApplicationContext(), uri);	// MediaPlayer 객체에 오디오 파일 데이터소스 설정
				mp.prepare();											// 동시 재생을 시작하기 위한 플레이어를 준비
				mp.start();													// MediaPlayer 시작
			} catch (Exception e) {
				e.printStackTrace();		// 예외의 스택 트레이스를 System.err 에 출력
			}
		}
		
		*/
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	public IBinder onBind(Intent arg0) {
		return null;
	}

}

