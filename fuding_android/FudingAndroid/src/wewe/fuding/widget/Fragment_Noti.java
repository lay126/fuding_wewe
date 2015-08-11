package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.activity.R;
import wewe.fuding.db.DbOpenHelper;
import wewe.fuding.domain.Noti;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment_Noti extends Fragment {
	public static final String TAG = Fragment_Noti.class.getSimpleName(); 
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때 설정되고 onDestroyView때 null이 된다.
	private static Fragment_Noti instance = null;

	public Noti noti;
	public ArrayList<Noti> noti_array;
	public ListView listView;
	public CustomAdapter_Noti adapter;
	
	private DbOpenHelper mDbOpenHelper;
	private Cursor mCursor;
	
	public static Fragment_Noti getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_Noti();
		}
		return instance;
	}

	public Fragment_Noti() {
		 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_noti, container, false);
		
		
		
		
		init(v);
		return v;

	}

	@Override
	public void onStart() {
		
		updateDataList(activity);
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void init(View v) { 
		
		noti_array = new ArrayList<Noti>();
		Noti noti = new Noti("", "1", "2015.11.10", "yundaeun");
		noti_array.add(noti);
		     noti = new Noti("", "2", "2015.11.14", "jungyeoeun");
		     noti_array.add(noti);
		
		listView  = (ListView)v.findViewById(R.id.listview_noti);
		adapter = new CustomAdapter_Noti(activity, noti_array);
		listView.setAdapter(adapter);
		
	}
	
	// data_list를 최신으로 업데이트한다. 
		@SuppressWarnings("null")
		public void updateDataList(Context context) {
			if (activity == null) return; 
			
			// DB에서 읽어온 최신 리스트, 데이터를 담은 후 updateData가 호출
//			Data_user.getInstance().refreshDataList(activity); // 탭 이동할 때 마다 실시간으로 증가하는 count를 적용하기 위해 db를 refresh 시켜줘야한다.
//			ArrayList<Noti> infoList = Data_user.getInstance().getDataList();

			mDbOpenHelper = new DbOpenHelper(activity);
		    mDbOpenHelper.open();
		    mDbOpenHelper.insertNotiColumn("yundaeun", "image", "1", "2013.03.11");
		    mDbOpenHelper.insertNotiColumn("jungyeoeun", "image", "2", "2014.01.12");
			
//		    mCursor = mDbOpenHelper.getNotiAllColumns(); 
//		    Noti noti;
//		    ArrayList<Noti> infoList = null;
//		    while (mCursor.moveToNext()) {
//	        	
//		    	noti = new Noti(
//						mCursor.getString(mCursor.getColumnIndex("friendId")),
//						mCursor.getString(mCursor.getColumnIndex("friendImage")),
//						mCursor.getString(mCursor.getColumnIndex("type")),
//						mCursor.getString(mCursor.getColumnIndex("date"))
//						);
//				Log.d("noti", mCursor.getString(mCursor.getColumnIndex("type")));
//		    	//infoList.add(noti);
//			}
//			
//			mCursor.close();
			
			
			
			
			
//			실시간 뷰 변경 
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					if (inner_cnt > 1) {
//						if (MainActivity.new_friend_num != null) {
//							MainActivity.new_friend_num.setVisibility(View.VISIBLE);
//							MainActivity.new_friend_image.setVisibility(View.VISIBLE);
//							MainActivity.new_friend_num.setText(inner_cnt - 1 + "");
//						}
//					} else {
//						if (MainActivity.new_friend_num != null) { //null 오류날 때  있음
//							MainActivity.new_friend_num.setVisibility(View.GONE);
//							MainActivity.new_friend_image.setVisibility(View.GONE);
//						}
//					}
//				}
//			});
			
			
//			if (activity != null) { // 최초 로딩 등의 이유로 activity가 null일 때는 넘어가고, activity가 존재한다면 list를 불러준다. 
//				if (infoList.size() < 2) {
//					friend_listview.setVisibility(View.GONE);
//					no_friend.setVisibility(View.VISIBLE);
//				} else {
//					if (friend_listview!=null) {
//						friend_listview.setVisibility(View.VISIBLE);
//						no_friend.setVisibility(View.GONE);
//						makeFriendList();
//					}
//				}
//			}
		}

}

