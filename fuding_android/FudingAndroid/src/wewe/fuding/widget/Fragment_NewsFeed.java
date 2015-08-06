package wewe.fuding.widget;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Fragment_NewsFeed extends Fragment {
	public static final String TAG = Fragment_NewsFeed.class.getSimpleName();
	public static FragmentActivity activity;
	private static Fragment_NewsFeed instance = null;

	private Frame nfFrame; // newsfeed frame
	private CustomAdapter_NewsFeed nfAdapter;
	private ListView nfListView;
	private ArrayList<Frame> nfFrameList;

	public static Fragment_NewsFeed getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_NewsFeed();
		}
		return instance;
	}

	public Fragment_NewsFeed() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_newsfeed, container, false);
		nfFrameList = new ArrayList<Frame>();

		showNewsfeed(nfFrameList);

		init(v);
		return v;
	}

	private void showNewsfeed(final ArrayList<Frame> frameArr) {
		String URL_address = "http://119.205.252.224:8000/get/newsfeed/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(activity);

		Listener<JSONArray> arrListener = new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d("onResponse", response.toString());

				try {
					for (int i = 0; i < response.length(); i++) {

						JSONObject jsonFrame = (JSONObject) response.get(i);

						nfFrame = new Frame();

						nfFrame.setUserId(jsonFrame.getString("wr_name"));
						nfFrame.setWriteDate(jsonFrame.getString("wr_date"));
						nfFrame.setLikeCnt(Integer.parseInt(jsonFrame
								.getString("wr_likes")));
						nfFrame.setTag(jsonFrame.getString("wr_tags"));

						frameArr.add(nfFrame);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {

				String arrRes = "{'response':" + response + "}";
				arrRes = arrRes.replace("\"", "");
				arrRes = arrRes.replace("'", "\"");
				arrRes = arrRes.replace(" ", "");
				// arrRes = arrRes.replace("", "");
				// arrRes = "" + arrRes;
				Log.d(TAG, arrRes);

				JSONObject jobject = null;
				try {
					jobject = new JSONObject(arrRes);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JSONArray jarray = null;
				try {
					jarray = jobject.getJSONArray("response");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					for (int i = 0; i < jarray.length(); i++) {

						JSONObject jsonFrame = (JSONObject) jarray.get(i);

						nfFrame = new Frame();

						nfFrame.setUserId(jsonFrame.getString("wr_name"));
						nfFrame.setWriteDate(jsonFrame.getString("wr_date"));
						nfFrame.setLikeCnt(Integer.parseInt(jsonFrame
								.getString("wr_likes")));
						nfFrame.setTag(jsonFrame.getString("wr_tags"));

						frameArr.add(nfFrame);
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
				Toast.makeText(activity, "네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		// JsonArrayRequest arrReq = new JsonArrayRequest(URL_address,
		// arrListener,
		// errorListener);

		StringRequest req = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			// do sth
		};

		mQueue.add(req);
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

	private void init(View v) {

		nfFrameList = new ArrayList<Frame>();
		Frame tempF = new Frame("yeoeun", "불닭", "불,닭", "4인분", "30분",
				"#짱매움#속쓰려", 4, "15/08/07 12:14");
		nfFrameList.add(tempF);//
		nfListView = (ListView) v.findViewById(R.id.listViewNewsfeed);
		nfAdapter = new CustomAdapter_NewsFeed(activity, nfFrameList);
		nfListView.setAdapter(nfAdapter);
	}
}
