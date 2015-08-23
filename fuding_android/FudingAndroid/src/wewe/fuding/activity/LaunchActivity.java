package wewe.fuding.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				 
				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		        String login_check = pref.getString("login_check", "false");
		         
//		        Log.d("LoginActivity.class", "login_check : "+ login_check);
//		        if ("true".equals(login_check)) {
//		        	Intent intent = new Intent(LaunchActivity.this, FudingMainActivity.class);
//		        	startActivity(intent);
//		        } else {
		        	Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
		        	startActivity(intent);
//		        }
				finish();
			}
		}, 2000);
    }
 }
