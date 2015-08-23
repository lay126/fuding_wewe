package wewe.fuding.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.activity.AddPostingActivity.Item;
import wewe.fuding.domain.Detail;
import wewe.fuding.utils.ImageDownloader;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class DetailActivity  extends ListActivity { 
	
	String quant, writer, likes, name, tag, times, ingre, total;
	TextView text_quant, text_writer, text_likes, text_name, text_tag, text_times, text_ingre;
	
	private ArrayList<Detail> arrayDetail;
	private ItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		arrayDetail = new ArrayList<Detail>();
		// 서버로부터 값 받아오기 
		getAllContent();
		
		text_quant = (TextView)findViewById(R.id.text_quant);
		text_writer = (TextView)findViewById(R.id.text_writer);
		text_likes = (TextView)findViewById(R.id.text_likes);
		text_name = (TextView)findViewById(R.id.text_name);
		text_tag = (TextView)findViewById(R.id.text_tag);
		text_times = (TextView)findViewById(R.id.text_times);
		text_ingre = (TextView)findViewById(R.id.text_ingre); 
		
		
		//ListView listView = (ListView)findViewById(R.id.listView);
		
		
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
						
						text_quant.setText("양 :"+quant);
						text_writer.setText("작성자 :"+writer);
						text_likes.setText("좋아요 :"+likes);
						text_name.setText("음식이름 :"+name);
						text_tag.setText("기타 태그 :"+tag);
						text_times.setText("총 소요시간 :"+times);
						text_ingre.setText("재료 태그 :"+ingre);

						Log.d("detail", quant+writer+likes+name+tag+times+ingre+total);
						String a = jsonFrame.getString("wc_img_"+1);
						
						for (int j=1; j<=Integer.parseInt(total); j++) {
							Detail detail = new Detail();
							detail.setImage(jsonFrame.getString("wc_img_"+j));
							detail.setContent(jsonFrame.getString("wc_text_"+j));
							detail.setTime(jsonFrame.getString("wc_times_"+j));
							
							arrayDetail.add(detail);
						}
						adapter = new ItemAdapter(arrayDetail);
						setListAdapter(adapter);
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
					
					params.put("wf_index", index+"");

					return params;
				}
		};
		mQueue.add(req);
	}
	
	
	private class ViewHolder {
		public TextView quant, writer, likes, name, tag, times, ingre, total;
		public ImageView foodImageView;
		public TextView content_text, time_text;
	}

	private class ItemAdapter extends ArrayAdapter<Detail> {
		
		public ItemAdapter(ArrayList<Detail> arrayDetail) {
		
			super(DetailActivity.this, R.layout.row_detail_item, R.id.content_text, arrayDetail);
		}	

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			final Detail detail = getItem(position);
			if (v != convertView && v != null) {
				ViewHolder holder = new ViewHolder();


				ImageView foodImage = (ImageView) v.findViewById(R.id.foodImageView);
				holder.foodImageView = foodImage;

				TextView content = (TextView) v.findViewById(R.id.content_text);
				holder.content_text = content;
				 
				TextView time = (TextView) v.findViewById(R.id.time_text);
				holder.time_text = time;
				
				v.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) v.getTag();
			
			holder.content_text.setText(detail.getContent());
			holder.time_text.setText(detail.getTime());
			ImageDownloader.download("http://119.205.252.224:8000/get/image/"+detail.getImage(), holder.foodImageView, 0);

			return v;
		}

		@Override
		public long getItemId(int position) {
		    return position;
		}

	}

	
	
	
	
	
	
}	