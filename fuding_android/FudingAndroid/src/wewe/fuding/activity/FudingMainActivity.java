package wewe.fuding.activity;

import wewe.fuding.widget.Fragment_AddPost;
import wewe.fuding.widget.Fragment_NewsFeed;
import wewe.fuding.widget.Fragment_Noti;
import wewe.fuding.widget.Fragment_Profile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FudingMainActivity extends FragmentActivity {
	public static final String TAG = FudingMainActivity.class.getSimpleName();
	public static FudingMainActivity me;
	int count = 0;
	boolean isFirstExecute;
	static Context main_context;

	Fragment_NewsFeed frag_newsfeed = null;
	Fragment_AddPost frag_addpost = null;
	Fragment_Noti frag_alarm = null;
	Fragment_Profile frag_profile = null;
	
	public static Activity FudingMainActivity;
	
	RelativeLayout newsfeed_layout, addpost_layout, alarm_layout, profile_layout;
	TextView newsfeed_textView, addpost_textView, alarm_textView, profile_textView;

	
	int selected_button = 0;
	ImageView top_menu, friend_btn, recent_btn, search_btn, setting_btn;
	View friend_bottom, recent_bottom, search_bottom, setting_bottom, view_mid;

	Handler mHandler = new Handler();
	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;

//	public static final boolean release = true;
//	public static final boolean market = false;

	/* Following the menu item constants which will be used for menu creation */
//	public static final int FIRST_MENU_ID = Menu.FIRST;
//	public static final int CONFIGURE_MENU_ITEM = FIRST_MENU_ID + 1;
//	public static final int ABOUT_MENU_ITEM = FIRST_MENU_ID + 2;
//	public static final int EXIT_MENU_ITEM = FIRST_MENU_ID + 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fuding_main);
		FudingMainActivity = FudingMainActivity.this;
		me = this;
		Log.d(TAG, "onCreate");

		// 탭
		newsfeed_layout = (RelativeLayout) findViewById(R.id.newsfeed_layout);
		addpost_layout = (RelativeLayout) findViewById(R.id.addpost_layout);
		alarm_layout = (RelativeLayout) findViewById(R.id.alarm_layout);
		profile_layout = (RelativeLayout) findViewById(R.id.profile_layout);
 
		// 프래그먼트 초기화. 스와이프를 사용하기위해 해놓은것.
		frag_newsfeed = Fragment_NewsFeed.getInstance();
		frag_addpost = Fragment_AddPost.getInstance();
		frag_alarm = Fragment_Noti.getInstance();
		frag_profile = Fragment_Profile.getInstance();

		// 스와이프를 사용하기위한 페이저어댑터
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() { // 스와이프
					@Override
					public void onPageSelected(int position) {
						// set tabbar item change
						setSelectedItem(position);
					}
				});
		mViewPager.setOffscreenPageLimit(4);

		newsfeed_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(0);
			}
		});
		addpost_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(1);

			}
		});
		alarm_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(2);

			}
		});
		profile_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(3);
				
			
			}
		});

		// 친구탭은 누른 상태로 시작
		newsfeed_layout.setBackgroundResource(R.drawable.tap01_s);

		Log.d(TAG, "getIntent() : " + getIntent());

		// 받아온 인텐트에 fragment항목이 있을 경우 해당 항목대로 이동
		String fragment = getIntent().getStringExtra("fragment");
		if (fragment != null) {
			if ("newsfeed".equals(fragment))
				mViewPager.setCurrentItem(0);
			else if ("addpost".equals(fragment))
				mViewPager.setCurrentItem(1);
			else if ("noti".equals(fragment))
				mViewPager.setCurrentItem(2);
			else if ("profile".equals(fragment))
				mViewPager.setCurrentItem(3);
		}
	}

	//kt.기어에 데이터를 전송하기 위한 인텐트를 브로드캐스트한다.
	public void sendDataToGear(String str) {
		Intent intent = new Intent("myData");
		intent.putExtra("data", "" + str);
		sendBroadcast(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent : " + intent);

		// 받아온 인텐트에 fragment항목이 있을 경우 해당 항목대로 이동
		String fragment = intent.getStringExtra("fragment");
		if (fragment != null) {
			if ("friend".equals(fragment))
				mViewPager.setCurrentItem(0);
			else if ("recent".equals(fragment))
				mViewPager.setCurrentItem(1);
			else if ("search".equals(fragment))
				mViewPager.setCurrentItem(2);
			else if ("setting".equals(fragment))
				mViewPager.setCurrentItem(3);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	private void setSelectedItem(int pos) {
		// 버튼 눌렸을때 눌린 느낌을 주기위한 버튼바꿔주는 함수.
		switch (pos) {
		case 0:
			newsfeed_layout.setBackgroundResource(R.drawable.tap01_s); 
			break;
		case 1:
			addpost_layout.setBackgroundResource(R.drawable.tap02_s); 
			break;
		case 2:
			alarm_layout.setBackgroundResource(R.drawable.tap03_s); 
			break;
		case 3:
			profile_layout.setBackgroundResource(R.drawable.tap04_s); 
			break;

		}

		switch (selected_button) {
		case 0:
			newsfeed_layout.setBackgroundResource(R.drawable.tap01_n); 
			break;
		case 1:
			addpost_layout.setBackgroundResource(R.drawable.tap02_n); 
			break;
		case 2:
			alarm_layout.setBackgroundResource(R.drawable.tap03_n); 
			break;
		case 3:
			profile_layout.setBackgroundResource(R.drawable.tap04_n); 
			break;

		}

		selected_button = pos;

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Context mContext;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			Log.d("number", "f   " + position);
			switch (position) {
			case 0:
				return frag_newsfeed;
			case 1:
				return frag_addpost;
			case 2:
				return frag_alarm;
			case 3:
				return frag_profile;
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}
	}
}
