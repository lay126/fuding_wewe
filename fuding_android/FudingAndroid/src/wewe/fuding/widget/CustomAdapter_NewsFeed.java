package wewe.fuding.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.FudingAPI;
import wewe.fuding.activity.CommentActivity;
import wewe.fuding.activity.DetailActivity;
import wewe.fuding.activity.OthersProfileActivity;
import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import wewe.fuding.utils.ImageDownloader;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

public class CustomAdapter_NewsFeed extends BaseAdapter {
	public static final String TAG = CustomAdapter_NewsFeed.class
			.getSimpleName();

	Context context;
	LayoutInflater inflater;
	int layout;
	private ArrayList<Frame> arrList;
	private ImageDownloader imgDownloader = new ImageDownloader();
	private String imgDownURL;
	private ImageButton btnLike;
	private TextView tvLikeCnt;

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final int pos = position;
		// final Context context = parent.getContext();

		// 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 convertView가 null인 상태로 들어 옴
		if (convertView == null) {
			// view가 null일 경우 커스텀 레이아웃을 얻어 옴
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_newsfeed_item, parent,
					false);
		}

		// 글쓴이 프로필 이미지
		NetworkImageView writerImg = (NetworkImageView) convertView
				.findViewById(R.id.newsfeed_profileImgView);
		String URL_user_img_address = "http://119.205.252.224:8000/get/image/"
				+ arrList.get(pos).getUserImgURL();
		FudingAPI API = FudingAPI.getInstance(context);
		writerImg.setImageUrl(URL_user_img_address, API.getmImageLoader());

		// 글쓴이 아이디 클릭시 프로필 액티비티로
		Button btnUserId = (Button) convertView
				.findViewById(R.id.newsfeed_btnViewUserId);
		final String wfWriter = arrList.get(pos).getUserId();
		btnUserId.setText(wfWriter);
		btnUserId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences pref = context.getSharedPreferences("pref",
						context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("wf_writer", wfWriter);
				editor.commit();
				context.startActivity(new Intent(context,
						OthersProfileActivity.class));
			}
		});

		// 날짜
		TextView tvDate = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewWriteDate);
		tvDate.setText(arrList.get(pos).getWriteDate());

		// 글제목
		TextView tvContentTitle = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewContentTitle);
		tvContentTitle.setText("" + arrList.get(pos).getFoodName());

		// 음식 이미지
		NetworkImageView imgFood = (NetworkImageView) convertView
				.findViewById(R.id.newsfeed_imgView);
		String URL_food_img_address = "http://119.205.252.224:8000/get/image/"
				+ arrList.get(pos).getFoodImgURL();
		imgFood.setImageUrl(URL_food_img_address, API.getmImageLoader());

		imgFood.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int wf_index = arrList.get(pos).getFoodIndex();
				SharedPreferences pref = context.getSharedPreferences("pref",
						context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putInt("wf_index", wf_index);
				editor.putInt("newsFeed_position", position);
				editor.commit();
				context.startActivity(new Intent(context, DetailActivity.class));
			}
		});

		// 좋아요 버튼 상태
		btnLike = (ImageButton) convertView
				.findViewById(R.id.newsfeed_imgBtnLike);
		if (arrList.get(pos).getLikeState() == 0) {
			// unclicked
			btnLike.setImageResource(R.drawable.like_n);
		} else {
			btnLike.setImageResource(R.drawable.like_s);
		}

		// 좋아요 수
		tvLikeCnt = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewLikeCnt);
		tvLikeCnt.setText("" + arrList.get(pos).getLikeCnt());

		// 좋아요 버튼을 터치 했을 때 이벤트 발생
		btnLike = (ImageButton) convertView
				.findViewById(R.id.newsfeed_imgBtnLike);
		btnLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickLikeBtn(v);
			}

			private void clickLikeBtn(View v) {
				String URL_address = "http://119.205.252.224:8000/set/like/";

				RequestQueue mQueue;
				mQueue = Volley.newRequestQueue(context);

				Listener<String> listener = new Listener<String>() {
					@Override
					public void onResponse(String response) {
						String res = "{'response':" + response + "}";
						Log.d(TAG, res);

						JSONObject jobject = null;
						try {
							jobject = new JSONObject(res);
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
							JSONObject jsonContent = (JSONObject) jarray.get(0);
							int state = Integer.parseInt(jsonContent
									.getString("like_state"));

							if (state == 1) {
								btnLike.setImageResource(R.drawable.like_s);
								arrList.get(pos).setLikeCnt(
										arrList.get(pos).getLikeCnt() + 1);
							} else {
								btnLike.setImageResource(R.drawable.like_n);
								arrList.get(pos).setLikeCnt(
										arrList.get(pos).getLikeCnt() - 1);
							}
							arrList.get(pos).setLikeState(state);
							tvLikeCnt.setText(""
									+ arrList.get(pos).getLikeCnt());
							notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				};

				ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
						Toast.makeText(context,
								"좋아요클릭 : 네트워크상태가좋지 않습니다. 잠시만 기다려주세요.",
								Toast.LENGTH_LONG).show();
					}
				};

				StringRequest req = new StringRequest(Method.POST, URL_address,
						listener, errorListener) {
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						Map<String, String> params = new HashMap<String, String>();
						// 사용자아이디, 글넘버
						SharedPreferences pref = context.getSharedPreferences(
								"pref", context.MODE_PRIVATE);
						String userName = pref.getString("user_name", "ayoung");
						params.put("user_name", userName);
						params.put("wf_index", ""
								+ arrList.get(pos).getFoodIndex());

						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd", java.util.Locale.getDefault());
						params.put("noti_date", dateFormat.format(new Date()));
						return params;
					}

				};

				mQueue.add(req);
			}
		});

		// 댓글 버튼 클릭 시 댓글 액티비티
		ImageButton imgComment = (ImageButton) convertView
				.findViewById(R.id.newsfeed_imgBtnComment);
		imgComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int wf_index = arrList.get(pos).getFoodIndex();
				SharedPreferences pref = context.getSharedPreferences("pref",
						context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putInt("wf_index", wf_index);
				editor.commit();
				context.startActivity(new Intent(context, CommentActivity.class));
			}
		});

		// 댓글 수
		TextView tvCommentCnt = (TextView) convertView
				.findViewById(R.id.newsfeed_txtCommentsCnt);
		tvCommentCnt.setText("" + arrList.get(pos).getCommentCnt());

		// 태그
		TextView tvTag = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewTag);
		tvTag.setText(arrList.get(pos).getTag());

		return convertView;
	}
}
