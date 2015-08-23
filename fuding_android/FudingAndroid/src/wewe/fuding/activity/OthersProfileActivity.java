package wewe.fuding.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.domain.Detail;
import wewe.fuding.domain.User;
import wewe.fuding.utils.ImageDownloader;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class OthersProfileActivity extends Activity {
	private ImageView imgUserPhoto;
	private TextView tvUserId1, tvUserId2;
	private TextView tvWrites, tvFollowers, tvFollowings;
	private ImageButton btnFollow;
	private GridView gridView;

	private String profileName, userName;
	private User user;
	private String flagMe, flagFollow;

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
				Log.d("othersProfile", arrRes);

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
				// int userNum, String userEmail, String userPwd, String userId,
				// String userName, String userPhoto
				try {
					JSONObject json = (JSONObject) jarray.get(0);

					flagMe = json.getString("me_flag");
					flagFollow = json.getString("follow_flag");

					user = new User();
					user.setUserName(json.getString("user_name"));
					user.setUserPhoto(json.getString("user_img"));
					user.setFollowers(Integer.parseInt(json
							.getString("user_followers")));
					user.setFollowings(Integer.parseInt(json
							.getString("user_followings")));
					user.setWrites(Integer.parseInt(json
							.getString("user_writes")));

					String URL_img_address = "http://119.205.252.224:8000/get/image/"
							+ user.getUserPhoto();
					ImageDownloader imgDownloader = new ImageDownloader();
//					imgDownloader.download(URL_img_address, imgUserPhoto, 0);
					imgUserPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

					tvUserId1.setText(user.getUserId());
					tvUserId2.setText(user.getUserId());
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

	}

	private void init() {
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		profileName = pref.getString("wf_writer", "");
		userName = pref.getString("user_name", "");

		imgUserPhoto = (ImageView) findViewById(R.id.othersprofile_imgViewothersprofile);
		tvUserId1 = (TextView) findViewById(R.id.othersprofile_txtUserId1);
		tvUserId2 = (TextView) findViewById(R.id.othersprofile_txtUserId2);
		tvWrites = (TextView) findViewById(R.id.othersprofile_txtWrites);
		tvFollowers = (TextView) findViewById(R.id.othersprofile_txtFollowers);
		tvFollowings = (TextView) findViewById(R.id.othersprofile_txtFollowings);
		gridView = (GridView) findViewById(R.id.othersprofile_gridView);
	}
}
