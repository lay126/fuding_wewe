package wewe.fuding.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.activity.ProfileActivity;
import wewe.fuding.activity.R;
import wewe.fuding.domain.Content;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Fragment_Profile extends Fragment {
	public static final String TAG = Fragment_Profile.class.getSimpleName();
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때
												// 설정되고 onDestroyView때 null이 된다.

	private static Fragment_Profile instance = null;
	private GridView gridView;
	private CustomAdapter_Profile profileAdapter;
	public static ImageButton edit_profile;

	// ArrayList<Bitmap> picArr = new ArrayList<Bitmap>();
	private Content pfContent; // newsfeed frame
	private CustomAdapter_NewsFeed nfAdapter;
	private ListView nfListView;
	private ArrayList<Content> contentArr;

	public static Fragment_Profile getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_Profile();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_profile, container, false);
		contentArr = new ArrayList<Content>();
		
		edit_profile = (ImageButton) v.findViewById(R.id.profile_edit_btn);
		edit_profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(activity, ProfileActivity.class));
			}
		});

		// 미리 저장되어있던 값을 불러오는 과정
		TextView name = (TextView) v.findViewById(R.id.profile_txtUserId);
		SharedPreferences pref = activity.getSharedPreferences("pref",
				activity.MODE_PRIVATE);
		String shared_name = pref.getString("name", "name");
		name.setText(shared_name);

		showMyContent(v, contentArr);
		return v;

	}

	private void showMyContent(View v, final ArrayList<Content> contentArr) {
		String URL_address = "http://119.205.252.224:8000/get/myfeed/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(activity);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
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
						JSONObject jsonContent = (JSONObject) jarray.get(i);

						pfContent = new Content();
						pfContent.setContent(jsonContent
								.getString("wt_name"));
						pfContent.setPhoto(jsonContent.getString("wc_img"));
						//ay.. what's the difference between wt_index and wf_index?
						pfContent.setFoodId(Integer.parseInt(jsonContent.getString("wt_index")));

						contentArr.add(pfContent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(activity,
						"Profile : 네트워크상태가좋지 않습니다. 잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_name", "ayoung");
				return params;
			}
		};

		mQueue.add(req);
		init(v, contentArr);
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

	private void init(View v, ArrayList<Content> contentArr) {
		gridView = (GridView) v.findViewById(R.id.profile_gridView);
		if (contentArr == null) {
			profileAdapter = new CustomAdapter_Profile(activity);
		} else {
			profileAdapter = new CustomAdapter_Profile(activity, contentArr);
		}
		gridView.setAdapter(profileAdapter);
	}
}
