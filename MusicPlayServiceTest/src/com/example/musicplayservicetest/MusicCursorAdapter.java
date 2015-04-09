package com.example.musicplayservicetest;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicCursorAdapter extends BaseAdapter {
	private Cursor c;
	private Context ctx;
	private ArrayList<Uri> musiclist;
	
	// MusicCursorAdapter 생성자
	public MusicCursorAdapter(Context context, ArrayList<Uri> musiclist) {
		ctx = context;
		this.musiclist = musiclist;
		
		// 오디오 파일 목록을 불러옴
		ContentResolver cr = context.getContentResolver();
		c = cr.query(Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
	}
	
	public int getCount() { return c.getCount(); }
	public Object getItem(int position) { return position; }
	public long getItemId(int position) { return position; }
	
	// position = ListView 목록의 위치
	// convertView = 삭제되어 재사용될 View 객체
	// paternt = Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv;
		
		// 삭제될 뷰가 없다면
		if(convertView == null) {
			tv = new TextView(ctx);		// TextView 객체를 새로 생성
			tv = (TextView) View.inflate(ctx, android.R.layout.simple_list_item_1, null);	// 리소스정보에 대한 뷰를 생성
		}
		else {
			tv = (TextView) convertView;		// 재사용될 convertView 객체를 넘김
		}
		
		// position에 위치한 오디오 파일을 가리키는 uri 객체 생성
		c.moveToPosition(position);
		Uri uri = Uri.withAppendedPath(Audio.Media.EXTERNAL_CONTENT_URI, c.getString(c.getColumnIndex(Audio.Media._ID)));
		
		musiclist.add(uri);	// musiclist에 uri 정보 추가
		tv.setText(c.getString(c.getColumnIndex(Audio.Media.DISPLAY_NAME)));	// 사용자에게 보여줄 오디오 파일의 이름을 저장
		
		return tv;
	}
}
