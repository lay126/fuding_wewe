package wewe.fuding.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Fragment_NewsFeed extends Fragment {
	public static final String TAG = Fragment_NewsFeed.class.getSimpleName();
	public static FragmentActivity activity;
	private static Fragment_NewsFeed instance = null;

	private Frame nfFrame; // newsfeed frame
	private CustomAdapter_NewsFeed nfAdapter;
	private ListView nfListView;
	private ArrayList<Frame> frameArr;

	public static Fragment_NewsFeed getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_NewsFeed();
		}
		return instance;
	}

	public Fragment_NewsFeed() {
	}

	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();

		v = inflater.inflate(R.layout.fragment_newsfeed, container, false);
		frameArr = new ArrayList<Frame>();

		showNewsfeed(v, 0, null);

		final EditText searchEditText = (EditText) v
				.findViewById(R.id.newsfeed_search_edit);
		ImageButton searchBtn = (ImageButton) v
				.findViewById(R.id.newsfeed_search_btn);

		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String strSearchTag = searchEditText.getText().toString();
				if (!strSearchTag.equals("")) {
					frameArr = new ArrayList<Frame>();
					showNewsfeed(v, 1, strSearchTag);
					nfAdapter.notifyDataSetChanged();
					// keyboard hide
					InputMethodManager imm = (InputMethodManager) activity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							searchEditText.getWindowToken(), 0);
				} else {
					frameArr = new ArrayList<Frame>();
					showNewsfeed(v, 0, strSearchTag);
					nfAdapter.notifyDataSetChanged();
				}
			}
		});

		return v;
	}

	private void showNewsfeed(View v, 
			int type, final String tag) {
		// if type is 1, it means that user clicked search btn
		String URL_address;
		if (type == 0) {
			URL_address = "http://119.205.252.224:8000/get/newsfeed/";
		} else if (type == 1) {
			URL_address = "http://119.205.252.224:8000/hash/find/";
		} else {
			URL_address = null;
		}
		final int nfType = type;
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
						JSONObject jsonFrame = (JSONObject) jarray.get(i);

						nfFrame = new Frame();

						nfFrame.setUserId(jsonFrame.getString("wf_writer"));
						nfFrame.setUserImgURL(jsonFrame.getString("user_img"));
						nfFrame.setTag(jsonFrame.getString("wt_tag"));
						nfFrame.setFoodName(jsonFrame.getString("wt_name"));
						nfFrame.setWriteDate(jsonFrame.getString("wc_date"));
						nfFrame.setLikeCnt(Integer.parseInt(jsonFrame
								.getString("wf_likes")));
						nfFrame.setLikeState(Integer.parseInt(jsonFrame
								.getString("like_flag")));
						nfFrame.setFoodImgURL(jsonFrame.getString("wc_img"));
						nfFrame.setFoodIndex(Integer.parseInt(jsonFrame
								.getString("wf_index")));
						nfFrame.setCommentCnt(Integer.parseInt(jsonFrame
								.getString("wf_comments")));

						frameArr.add(nfFrame);
						nfAdapter.notifyDataSetChanged();
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
				Toast.makeText(activity, "뉴스피드 : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req;
		req = new StringRequest(Method.POST, URL_address, listener,
				errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				SharedPreferences pref = activity.getSharedPreferences("pref",
						activity.MODE_PRIVATE);
				String userName = pref.getString("user_name", "ayoung");
				params.put("user_name", userName);

				if (nfType == 1) { // 검색일경우
//					tag.replace("#", " #");
					Log.i(TAG, tag);
					params.put("search_text", tag);
				}
				return params;
			}
		};

		mQueue.add(req);
		init(v, frameArr);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();

		nfAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void init(View v, ArrayList<Frame> frameArr) {
		nfListView = (ListView) v.findViewById(R.id.listViewNewsfeed);
		if (frameArr == null) {
			nfAdapter = new CustomAdapter_NewsFeed(activity);
		} else {
			nfAdapter = new CustomAdapter_NewsFeed(activity, frameArr);
		}
		nfAdapter.notifyDataSetChanged();

		nfListView.setAdapter(nfAdapter);

	}
}
