package wewe.fuding.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.activity.R;
import wewe.fuding.db.DbOpenHelper;
import wewe.fuding.domain.Frame;
import wewe.fuding.domain.Noti;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
	private Handler mHandler = new Handler();
	
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
		Log.d("start", "oncreateView");
		noti_array = new ArrayList<Noti>();
		updateDataList(v, noti_array);
		return v;
	}

	@Override
	public void onStart() {
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

	private void init(View v, ArrayList<Noti> noti_array) {  
		listView  = (ListView)v.findViewById(R.id.listview_noti);

		if (noti_array == null) {
			Log.d("notinoti", "noti_Array null");
			adapter = new CustomAdapter_Noti(activity);
		} else {
		
			adapter = new CustomAdapter_Noti(activity, noti_array);
		}
		
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter); 
	}
	
	// data_list를 최신으로 업데이트한다.  
		public void updateDataList(View v, final ArrayList<Noti> noti_array) {
			if (activity == null) return; 
			
//			if (activity != null) { // 최초 로딩 등의 이유로 activity가 null일 때는 넘어가고, activity가 존재한다면 list를 불러준다. 
//				if (noti_array.size() < 2) {
////					listView.setVisibility(View.GONE);
//				}
//			}
			String URL_address= "http://119.205.252.224:8000/get/noti/"; 
			
			RequestQueue mQueue2;
			mQueue2 = Volley.newRequestQueue(activity);
			Listener<String> listener = new Listener<String>() {
				@Override
				public void onResponse(String result) {
					try {
						
						// to make data available
						String arrRes = "{'response':" + result + "}";
						Log.d(TAG, arrRes);

						JSONObject jobject = null;
						try {
							jobject = new JSONObject(arrRes);
						} catch (JSONException e) {
							e.printStackTrace();
						}

						JSONArray jarray = null;
						try {
							jarray = jobject.getJSONArray("response");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						try {
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject jsonFrame = (JSONObject) jarray.get(i);
								noti = new Noti();
								noti.setType(jsonFrame.getString("noti_flag")); 
								noti.setFriendId(jsonFrame.getString("noti_id")); 
								noti.setWf_index(jsonFrame.getString("wf_index")); 
								noti.setNoti_read(jsonFrame.getString("noti_read")); 
								noti.setImage(jsonFrame.getString("noti_img")); 
								noti.setDate(jsonFrame.getString("noti_date")); 
								
								Log.d("notinoti", jsonFrame.getString("noti_flag")+jsonFrame.getString("noti_id"));
								
								noti_array.add(noti);
							}
						
						} catch (Exception e){}
					} catch (Exception e){}
				}
			};

			com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(activity, "noti : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
					}
			};
			
			StringRequest myReq = new StringRequest(Method.POST, URL_address, listener, errorListener) {
				@Override
				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					
					SharedPreferences pref = activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
			        String username = pref.getString("user_name", "1");
			        params.put("user_name", username);  
					return params;
				};
			};
			mQueue2.add(myReq);
			init(v, noti_array);
		}

}

