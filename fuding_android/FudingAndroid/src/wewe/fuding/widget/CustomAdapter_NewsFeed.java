package wewe.fuding.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import wewe.fuding.utils.ImageDownloader;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter_NewsFeed extends BaseAdapter {

	// userId // foodName; // ingre
	// amount // totalTime // tag // likeCnt
	public static final String TAG = CustomAdapter_NewsFeed.class
			.getSimpleName();

	Context context;
	LayoutInflater inflater;
	int layout;
	private ArrayList<Frame> arrList;
	private ImageDownloader imgDownloader = new ImageDownloader();
	private String imgDownURL;

	public CustomAdapter_NewsFeed(Context aContext) {
		context = aContext;
		inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = new ArrayList<Frame>();
	}

	public CustomAdapter_NewsFeed(Context aContext, ArrayList<Frame> aArrList) {
		context = aContext;
		inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = aArrList;
		// layout = aLayout;
	}

	@Override
	public int getCount() {
		return arrList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(Frame frame) {
		arrList.add(frame);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();

		// 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 convertView가 null인 상태로 들어 옴
		if (convertView == null) {
			// view가 null일 경우 커스텀 레이아웃을 얻어 옴
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_newsfeed_item, parent,
					false);

			// 좋아요 버튼을 터치 했을 때 이벤트 발생
			Button btn = (Button) convertView
					.findViewById(R.id.newsfeed_imgBtnLike);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					clickLikeBtn();
				}

				private void clickLikeBtn() {
					String URL_address = "http://119.205.252.224:8000/get/newsfeed/";

					RequestQueue mQueue;
					mQueue = Volley.newRequestQueue(context);

					Listener<String> listener = new Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								
							}catch(Exception e) {
								e.printStackTrace();
							}
						}
					};

					ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							error.printStackTrace();
							Toast.makeText(context, "좋아요클릭 : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
									Toast.LENGTH_LONG).show();
						}
					};

					StringRequest req = new StringRequest(Method.POST, URL_address,
							listener, errorListener) {
						@Override
						protected Map<String, String> getParams() throws AuthFailureError {
							Map<String, String> params = new HashMap<String, String>();
							params.put("user_name", "ayoung");
							// 글넘버
							params.put("", "");
							return params;
						}

					};

					mQueue.add(req);					
				}
			});

			// // 리스트 아이템을 터치 했을 때 이벤트 발생
			// convertView.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// }
			// });

		}

		TextView tvUserId = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewUserId);
		tvUserId.setText(arrList.get(pos).getUserId());

		TextView tvDate = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewWriteDate);
		tvDate.setText(arrList.get(pos).getWriteDate());

		TextView tvContentTitle = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewContentTitle);
		tvContentTitle.setText("" + arrList.get(pos).getFoodName());

		TextView tvLikeCnt = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewLikeCnt);
		tvLikeCnt.setText("" + arrList.get(pos).getLikeCnt());

		TextView tvTag = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewTag);
		tvTag.setText(arrList.get(pos).getTag());

		ImageView imgFood = (ImageView) convertView
				.findViewById(R.id.newsfeed_imgView);
		String URL_img_address = "http://119.205.252.224:8000/get/image"
				+ arrList.get(pos).getFoodImgURL();
		Log.i(TAG, URL_img_address);
		imgDownloader.download(URL_img_address, imgFood, 0);

		// userId // foodName; // ingre
		// amount // totalTime // tag // likeCnt
		return convertView;
	}
}
