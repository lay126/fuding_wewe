package wewe.fuding.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.FudingAPI;
import wewe.fuding.domain.Content;
import wewe.fuding.domain.Detail;
import wewe.fuding.domain.User;
import wewe.fuding.utils.ImageDownloader;
import wewe.fuding.widget.CustomAdapter_NewsFeed;
import wewe.fuding.widget.CustomAdapter_Profile;
import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class OthersProfileActivity extends Activity {
	public static final String TAG = OthersProfileActivity.class
			.getSimpleName();

	private NetworkImageView imgUserPhoto;
	private TextView tvUserId;
	private TextView tvWrites, tvFollowers, tvFollowings;
	private ImageButton btnFollow;
	private GridView gridView;

	private String profileName, userName;
	private User user;
	private String flagMe, flagFollow;

	private Content pfContent;
	private ArrayList<Content> contentArr;
	private CustomAdapter_Profile profileAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_profile);
		init();
		getProfile();
		getOtherFeed();
	}

	private void getProfile() {
		String URL_address = "http://119.205.252.224:8000/get/profile/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);
		// user_name // profile_name

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.i("othersprofile", arrRes);
				
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
					JSONObject json = (JSONObject) jarray.get(0);

					user = new User();
					user.setUserName(json.getString("user_name"));
					user.setUserPhoto(json.getString("user_img"));
					user.setFollowers(Integer.parseInt(json
							.getString("user_followers")));
					user.setFollowings(Integer.parseInt(json
							.getString("user_followings")));
					user.setWrites(Integer.parseInt(json
							.getString("user_writes")));
					Log.i("otherProfile",
							user.getUserName() + ", " + user.getFollowers());

					flagMe = json.getString("me_flag");
					flagFollow = json.getString("follow_flag");
					Log.i("othersProfile", "1 me : " + flagMe + ", follow : " + flagFollow);
					if (!flagMe.equals(null) && !flagFollow.equals(null)){
						setFollowBtn(flagMe, flagFollow);
					}

					String URL_img_address = "http://119.205.252.224:8000/get/image/"
							+ user.getUserPhoto();
					FudingAPI API = FudingAPI
							.getInstance(getApplicationContext());
					imgUserPhoto.setImageUrl(URL_img_address,
							API.getmImageLoader());
					imgUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

					tvUserId.setText(user.getUserName());
					tvWrites.setText("" + user.getWrites());
					tvFollowers.setText("" + user.getFollowers());
					tvFollowings.setText("" + user.getFollowings());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(OthersProfileActivity.this,
						"팔로우 페이지 : 네트워크상태가좋지 않습니다. 잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				params.put("profile_name", profileName);
				params.put("user_name", userName);

				return params;
			}
		};
		mQueue.add(req);
	}

	private void getOtherFeed() {
		String URL_address = "http://119.205.252.224:8000/get/myfeed/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.i("othersProfile arrRes", arrRes);

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
				Toast.makeText(OthersProfileActivity.this,
						"팔로우 페이지 : 네트워크상태가좋지 않습니다. 잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				params.put("user_name", profileName);
				return params;
			}
		};
		mQueue.add(req);
		adapterInit(contentArr);
	}

	private void setFollowBtn(String fMe, String fFollow) {
		Log.i("setFollowBtn", fMe + ", " + fFollow); /**/
		if (fMe.equals("yes")) { // 나 인 경우
			btnFollow.setImageResource(R.drawable.ic_launcher);
			btnFollow.setOnClickListener(setClickLisntener("fMe", fMe));
		} else {
			if (fFollow.equals("yes")) { // 팔로우 하고 있는 경우
				btnFollow.setImageResource(R.drawable.like_clicked);
			} else { // 팔로우 하고 있지 않는 경우
				btnFollow.setImageResource(R.drawable.like_unclicked);
			}
			btnFollow.setOnClickListener(setClickLisntener("fFollow", fFollow));
		}
	}

	private OnClickListener setClickLisntener(final String flag,
			final String value) {
		Log.i("setClickLisntener", flag + ", " + value); /**/
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag.equals("fMe")) {
					Log.i("followClickListener", "fMe");
					startActivity(new Intent(OthersProfileActivity.this,
							UpdateProfileActivity.class));
				} else if (flag.equals("fFollow")) {
					requestFollow();
					if (value.equals("yes")) {
						Log.i("followClickListener", "fFollow yes");
						btnFollow.setImageResource(R.drawable.like_unclicked);
					} else {
						Log.i("followClickListener", "fFollow no");
						btnFollow.setImageResource(R.drawable.like_clicked);
					}
				} else {

				}
			}
		};
		return clickListener;
	}

	private void requestFollow() {
		String URL_address = "http://119.205.252.224:8000/set/follow/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				String res = "{'response':" + response + "}";
				Log.d("requestFollow()", res);
			}
		};

		ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(OthersProfileActivity.this,
						"좋아요클릭 : 네트워크상태가좋지 않습니다. 잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				params.put("user_id", userName);
				params.put("following_id", profileName);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd", java.util.Locale.getDefault());
				params.put("noti_date", dateFormat.format(new Date()));

				return params;
			}

		};

		mQueue.add(req);
	}

	private void init() {
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		profileName = pref.getString("wf_writer", "");
		userName = pref.getString("user_name", "");

		imgUserPhoto = (NetworkImageView) findViewById(R.id.othersprofile_imgViewothersprofile);
		tvUserId = (TextView) findViewById(R.id.othersprofile_txtUserId);
		tvWrites = (TextView) findViewById(R.id.othersprofile_txtWrites);
		tvFollowers = (TextView) findViewById(R.id.othersprofile_txtFollowers);
		tvFollowings = (TextView) findViewById(R.id.othersprofile_txtFollowings);
		gridView = (GridView) findViewById(R.id.othersprofile_gridView);
		btnFollow = (ImageButton) findViewById(R.id.othersprofile_follow_btn);

		contentArr = new ArrayList<Content>();
	}

	private void adapterInit(ArrayList<Content> contentArr) {
		if (contentArr == null) {
			Log.i("othersprofile adapter", "contentArr null");
			profileAdapter = new CustomAdapter_Profile(
					OthersProfileActivity.this);
		} else {
			Log.i("othersprofile adapter", "contentArr not null");
			profileAdapter = new CustomAdapter_Profile(
					OthersProfileActivity.this, contentArr);
		}
		gridView.setAdapter(profileAdapter);
		profileAdapter.notifyDataSetChanged();
	}
}
