package wewe.fuding.widget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.Toast;

public class Fragment_Profile extends Fragment {
	public static final String TAG = Fragment_Profile.class.getSimpleName();
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때
												// 설정되고 onDestroyView때 null이 된다.

	private static Fragment_Profile instance = null;
	private GridView gridView;
	private CustomAdapter_Profile profileAdapter;
//	ArrayList<Bitmap> picArr = new ArrayList<Bitmap>();



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
		init(v);
		return v;

	}

	private void showMyContent() {
		String URL_address = "http://119.205.252.224:8000/get/mycontent/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(activity);

		Listener<JSONArray> arrListener = new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {

			}
		};

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {

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
		gridView = (GridView) v.findViewById(R.id.profile_gridView);
		profileAdapter = new CustomAdapter_Profile(activity);
		gridView.setAdapter(profileAdapter);
	}
}
