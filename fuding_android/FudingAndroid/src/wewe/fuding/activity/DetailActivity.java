package wewe.fuding.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.FudingAPI;
import wewe.fuding.domain.Detail;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class DetailActivity  extends Activity { 
	
	String quant, writer, likes, name, tag, times, ingre, total, user_img, comment_cnt;
	TextView text_quant, text_writer, text_likes, text_name, text_tag, text_times, text_ingre;
	public static TextView text_comment_cnt;
	NetworkImageView user_photo;
	TextView del_btn;
	private ArrayList<Detail> arrayDetail;
	private DetailAdapter Dadapter;
	ListView detailList;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		arrayDetail = new ArrayList<Detail>();
		
		detailList = (ListView)findViewById(R.id.detail_listview);

		// 서버로부터 값 받아오기 
		getAllContent();
		
		text_quant = (TextView)findViewById(R.id.text_quant);
		text_writer = (TextView)findViewById(R.id.text_writer);
		text_likes = (TextView)findViewById(R.id.text_likes);    
		text_name = (TextView)findViewById(R.id.text_name);
		text_tag = (TextView)findViewById(R.id.text_tag);
//		text_times = (TextView)findViewById(R.id.text_times);
		text_ingre = (TextView)findViewById(R.id.text_ingre); 
		user_photo = (NetworkImageView)findViewById(R.id.user_photo); 
		text_comment_cnt = (TextView)findViewById(R.id.comment_cnt);
		del_btn = (TextView)findViewById(R.id.del_btn);	
		
		
		del_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);     // 여기서 this는 Activity의 this

				// 여기서 부터는 알림창의 속성 설정
				builder.setMessage("해당 게시물을 삭제하시겠습니까?")        // 메세지 설정
				.setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("확인", new DialogInterface.OnClickListener(){       
				 // 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton){
						deleteContent();
						finish();
						dialog.cancel();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener(){      
				     // 취소 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton){
						dialog.cancel();
					}
				});

				AlertDialog dialog = builder.create();    // 알림창 객체 생성
				dialog.show();    // 알림창 띄우기
			}
		});
		
		ImageView comment_btn = (ImageView)findViewById(R.id.comment_btn);
		comment_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DetailActivity.this, CommentActivity.class));
			}
		});
		
		
		ImageView backBtn = (ImageView)findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ImageView gear = (ImageView)findViewById(R.id.gear);
		gear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				1번째 : 요리이름
//				2번째 : 요리재료
//				3번째 : 요리시간
//				4번째 : 단계갯수
//				5번째 : 단계,,로 구분 
				
				
				String head = name+":"+ingre+":"+times+":"+total+":";
				String tail = "";
				for (int i=0; i<Integer.parseInt(total); i++) {
					if (i==0) {
						tail = arrayDetail.get(i).getContent();
					} else {
						tail += ","+arrayDetail.get(i).getContent();
					}
				}
				Log.d("================", head+tail);
//				sendDataToGear(head+tail);
			}
		});
		
		
		
	}

	// kt.기어에 데이터를 전송하기 위한 인텐트를 브로드캐스트한다.
	public void sendDataToGear(String str) {
		Intent intent = new Intent("myData");
		intent.putExtra("data", "" + str);
		sendBroadcast(intent);
	}
			
	private void deleteContent() 
	{
		String URL_address = "http://119.205.252.224:8000/delete/write/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.d("detail", arrRes);
				//arrayDetail.remove();
			}
		};

		ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(DetailActivity.this, "상세페이지 : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address, listener, errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
				int index = pref.getInt("wf_index", 1);
				String name =  pref.getString("user_name", "");
				Log.d("===============", index+"s");
				params.put("wf_index", index+"");
				params.put("user_name", name);
				return params;
			}
		};
		mQueue.add(req);
	}
	
	private void getAllContent() {
		String URL_address = "http://119.205.252.224:8000/get/recipe/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.d("detail", arrRes);

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
						quant = jsonFrame.getString("wt_quant");
						writer = jsonFrame.getString("wf_writer");
						likes = jsonFrame.getString("wf_likes");
						name = jsonFrame.getString("wt_name");
						tag = jsonFrame.getString("wt_tag");
						times = jsonFrame.getString("wt_times");
						ingre = jsonFrame.getString("wt_ingre"); 
						total = jsonFrame.getString("wc_total");
						user_img = jsonFrame.getString("user_img");
						comment_cnt = jsonFrame.getString("wf_comments");
						
						text_quant.setText("#"+quant+"인분 " + "#"+times+"분");
						text_writer.setText(""+writer);
						text_likes.setText(likes);
						text_name.setText("#"+name);
						text_tag.setText(tag);
						text_ingre.setText(ingre);
						text_comment_cnt.setText(comment_cnt);
						
						String URL_img_address = "http://119.205.252.224:8000/get/image/"+ user_img;
						FudingAPI API = FudingAPI.getInstance(DetailActivity.this);
						user_photo.setImageUrl(URL_img_address, API.getmImageLoader());
						
						for (int j=1; j<=Integer.parseInt(total); j++) {
							Detail detail = new Detail();
							detail.setImage(jsonFrame.getString("wc_img_"+j));
							detail.setContent(jsonFrame.getString("wc_text_"+j));
							detail.setTime(jsonFrame.getString("wc_times_"+j));
							
							arrayDetail.add(detail);
						} 

						Dadapter = new DetailAdapter(arrayDetail);
						detailList.setAdapter(Dadapter);

						
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
				Toast.makeText(DetailActivity.this, "상세페이지 : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address, listener, errorListener) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
					int index = pref.getInt("wf_index", 1);
					Log.d("===============", index+"s");
					params.put("wf_index", index+"");

					return params;
				}
		};
		mQueue.add(req);
	}
	
	
	
	private class ViewHolder {
		public TextView quant, writer, likes, name, tag, times, ingre, tota, comment_text;
		public NetworkImageView foodImageView;
		public TextView content_text, time_text; 
	}

	private class DetailAdapter extends ArrayAdapter<Detail> {
		
		public DetailAdapter(ArrayList<Detail> arrayDetail) {
		
			super(DetailActivity.this, R.layout.row_detail_item, R.id.content_text, arrayDetail);
		}	

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			final Detail detail = getItem(position);
			if (v != convertView && v != null) {
				ViewHolder holder = new ViewHolder();

				NetworkImageView foodImage = (NetworkImageView) v.findViewById(R.id.foodImageView);
				holder.foodImageView = foodImage;

				TextView content = (TextView) v.findViewById(R.id.content_text);
				holder.content_text = content;
				 
				TextView time = (TextView) v.findViewById(R.id.time_text);
				holder.time_text = time;
 				
				v.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) v.getTag();
			
			holder.content_text.setText(detail.getContent()+" "+detail.getTime()+"분 소요");
			String URL_img_address = "http://119.205.252.224:8000/get/image/" + detail.getImage();
			FudingAPI API = FudingAPI.getInstance(DetailActivity.this);
			holder.foodImageView.setImageUrl(URL_img_address, API.getmImageLoader());
			
			return v;
		}

		@Override
		public long getItemId(int position) {
		    return position;
		}

	}
	

	
}	