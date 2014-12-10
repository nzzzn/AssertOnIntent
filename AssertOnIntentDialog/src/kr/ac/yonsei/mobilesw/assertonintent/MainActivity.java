package kr.ac.yonsei.mobilesw.assertonintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        String action = intent.getAction();
        if(action == "android.intent.action.SHOWDIALOG")
        {
        	String msg = intent.getExtras().getString("msg");
        	
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Unfortunately, the intent has malformed. " + msg)
	           .setPositiveButton("OK", 
	              new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface di, int which) {
	                     MainActivity.this.finish();
	                 }});
	        AlertDialog alert = builder.create();
	        alert.show();
        }
        else
        {
        	MainActivity.this.finish();
        }
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
