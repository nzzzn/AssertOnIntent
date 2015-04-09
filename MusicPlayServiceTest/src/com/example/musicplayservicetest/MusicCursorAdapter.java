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
	
	// MusicCursorAdapter ������
	public MusicCursorAdapter(Context context, ArrayList<Uri> musiclist) {
		ctx = context;
		this.musiclist = musiclist;
		
		// ����� ���� ����� �ҷ���
		ContentResolver cr = context.getContentResolver();
		c = cr.query(Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
	}
	
	public int getCount() { return c.getCount(); }
	public Object getItem(int position) { return position; }
	public long getItemId(int position) { return position; }
	
	// position = ListView ����� ��ġ
	// convertView = �����Ǿ� ����� View ��ü
	// paternt = Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv;
		
		// ������ �䰡 ���ٸ�
		if(convertView == null) {
			tv = new TextView(ctx);		// TextView ��ü�� ���� ����
			tv = (TextView) View.inflate(ctx, android.R.layout.simple_list_item_1, null);	// ���ҽ������� ���� �並 ����
		}
		else {
			tv = (TextView) convertView;		// ����� convertView ��ü�� �ѱ�
		}
		
		// position�� ��ġ�� ����� ������ ����Ű�� uri ��ü ����
		c.moveToPosition(position);
		Uri uri = Uri.withAppendedPath(Audio.Media.EXTERNAL_CONTENT_URI, c.getString(c.getColumnIndex(Audio.Media._ID)));
		
		musiclist.add(uri);	// musiclist�� uri ���� �߰�
		tv.setText(c.getString(c.getColumnIndex(Audio.Media.DISPLAY_NAME)));	// ����ڿ��� ������ ����� ������ �̸��� ����
		
		return tv;
	}
}
