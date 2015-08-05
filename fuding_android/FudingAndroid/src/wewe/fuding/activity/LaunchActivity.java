package wewe.fuding.activity;

import wewe.fuding.fudingandroid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(LaunchActivity.this, FudingMainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);
    }
 }
