package wewe.fuding.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.FudingAPI;
import wewe.fuding.activity.R;
import wewe.fuding.activity.UpdateProfileActivity;
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
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Fragment_Profile extends Fragment {
	public static final String TAG = Fragment_Profile.class.getSimpleName();
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때
												// 설정되고 onDestroyView때 null이 된다.

	private static Fragment_Profile instance = null;
	private GridView gridView;
	private CustomAdapter_Profile profileAdapter;
	public static NetworkImageView photo;

	private Content pfContent; // newsfeed frame
	private ArrayList<Content> contentArr;
	private TextView tvWrites, tvFollowers, tvFollowings;
	private String userName;

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

		// 미리 저장되어있던 값을 불러오는 과정
		TextView userName = (TextView) v.findViewById(R.id.profile_txtUserId);
		SharedPreferences pref = activity.getSharedPreferences("pref",
				activity.MODE_PRIVATE);
		String name = pref.getString("user_name", "ayoung");
		// String myProfile = pref.getString("myImage", "");
		// Log.d("url_fragment", myProfile);

		userName.setText(name);

		photo = (NetworkImageView) v.findViewById(R.id.profile_imgViewProfile);
		// String URL_img_address =
		// "http://119.205.252.224:8000/get/image/"+myProfile;
		//
		// FudingAPI API = FudingAPI.getInstance(activity);
		// photo.setImageUrl(URL_img_address, API.getmImageLoader());

		// photo.setBackgroundColor(Color.WHITE);
		// photo.setImageURI(myUri);
		// photo.setScaleType(ImageView.ScaleType.CENTER_CROP);

		ImageButton edit_profile = (ImageButton) v
				.findViewById(R.id.profile_edit_btn);
		edit_profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(activity, UpdateProfileActivity.class));
			}
		});
		init(v);
		getMyImage();
		getProfileInfo();
		showMyContent(v, contentArr);
		return v;
	}

	private void getMyImage() {
		String URL_address = "http://119.205.252.224:8000/get/user/profile/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(activity);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.d("detail_update_re", arrRes);

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
						String image_url = jsonFrame.getString("user_img");
						SharedPreferences pref = activity.getSharedPreferences(
								"pref", activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = pref.edit();
						editor.putString("myImage", image_url + "");
						editor.commit();
						Log.d("url_put", image_url);
						String URL_img_address = "http://119.205.252.224:8000/get/image/"
								+ image_url;
						FudingAPI API = FudingAPI.getInstance(activity);
						photo.setImageUrl(URL_img_address,
								API.getmImageLoader());
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
				Toast.makeText(activity, "profile : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				SharedPreferences pref = activity.getSharedPreferences("pref",
						activity.MODE_PRIVATE);
				String index = pref.getString("user_name", "");

				params.put("user_name", index + "");

				return params;
			}
		};
		mQueue.add(req);
	}
	
	private void getProfileInfo() {
		String URL_address = "http://119.205.252.224:8000/get/profile/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(activity);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.d("getProfileInfo", arrRes);

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
					
						tvWrites.setText("" + jsonFrame.getString("user_writes"));
						tvFollowers.setText("" + jsonFrame.getString("user_followers"));
						tvFollowings.setText("" + jsonFrame.getString("user_followings"));
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
				Toast.makeText(activity, "profile : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				SharedPreferences pref = activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
				userName = pref.getString("user_name", "");
				
				params.put("profile_name", userName);
				params.put("user_name", userName);

				return params;
			}
		};
		mQueue.add(req);
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
						pfContent.setContent(jsonContent.getString("wt_name"));
						pfContent.setPhoto(jsonContent.getString("wc_img"));
						pfContent.setFoodId(Integer.parseInt(jsonContent
								.getString("wf_index")));

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

				SharedPreferences pref = activity.getSharedPreferences("pref",
						activity.MODE_PRIVATE);
				String userName = pref.getString("user_name", "ayoung");
				params.put("user_name", userName);
				return params;
			}
		};

		mQueue.add(req);
		adapterInit(v, contentArr);
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

	private void adapterInit(View v, ArrayList<Content> contentArr) {
		gridView = (GridView) v.findViewById(R.id.profile_gridView);
		if (contentArr == null) {
			profileAdapter = new CustomAdapter_Profile(activity);
		} else {
			profileAdapter = new CustomAdapter_Profile(activity, contentArr);
		}
		gridView.setAdapter(profileAdapter);
		profileAdapter.notifyDataSetChanged();
	}
	
	private void init(View v) {
		tvWrites = (TextView) v.findViewById(R.id.profile_txtWrites);
		tvFollowers = (TextView) v.findViewById(R.id.profile_txtFollowers);
		tvFollowings = (TextView) v.findViewById(R.id.profile_txtFollowings);
	}
}
