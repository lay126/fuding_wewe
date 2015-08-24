package wewe.fuding.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.domain.Comment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CommentActivity extends Activity { 

	private ArrayList<Comment> arrayComment;
	private CommentAdapter Cadapter;
	ListView commentList;
	int comment_count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		arrayComment = new ArrayList<Comment>();

		getAllComment();
		
		final EditText ed = (EditText)findViewById(R.id.ed);
		ImageView add_btn = (ImageView) findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {

				SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
				String user_name = pref.getString("user_name", "");
				int wf_index =  pref.getInt("wf_index", 1);

				// 추가된 댓글 서버로 전송 
				String context = ed.getText().toString();
				sendAddComment(wf_index, user_name, context);
				ed.setText("");
//				Cadapter.notifyDataSetChanged();
			}
		});
		
		ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// 댓글 갯수 제어 
				comment_count = arrayComment.size(); 
				DetailActivity.text_comment_cnt.setText(comment_count+"");
				finish();
			}
		});
	}	
	
	// 댓글 갯수 제어 
	@Override
	public void onBackPressed() {
		//super.onBackPressed(); 
		comment_count = arrayComment.size(); 
		DetailActivity.text_comment_cnt.setText(comment_count+"");
		finish();
	}
	private void getAllComment() {
		String URL_address = "http://119.205.252.224:8000/get/comment/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.d("comment", arrRes);

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
						String result = jsonFrame.getString("result");
						if ("0".equals(result)) {
							Comment cm = new Comment();

							cm.setCommentIndex(jsonFrame.getString("wcm_index"));
							cm.setFoodIndex(jsonFrame.getString("wf_index"));
							cm.setCommentWriter(jsonFrame.getString("wcm_writer"));
							cm.setContentText(jsonFrame.getString("wcm_text"));
							Log.d("comment_get", jsonFrame.getString("wcm_text"));
							
							arrayComment.add(cm); 
						}
						
					}
					Cadapter = new CommentAdapter(arrayComment);
					commentList = (ListView)findViewById(R.id.comment_listview);
					commentList.setAdapter(Cadapter);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(CommentActivity.this, "상세페이지 : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
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
		public TextView comment_text;
		public ImageView delete_btn;
	}
	
private class CommentAdapter extends ArrayAdapter<Comment> {
		
		public CommentAdapter(ArrayList<Comment> arrayComment) {
		
			super(CommentActivity.this, R.layout.row_comment_item, R.id.comment, arrayComment);
		}	

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			final Comment comment = getItem(position);
			if (v != convertView && v != null) {
				ViewHolder holder = new ViewHolder();

				 
				TextView comment_text = (TextView) v.findViewById(R.id.comment);
				holder.comment_text = comment_text;
				
				ImageView delete_btn = (ImageView) v.findViewById(R.id.delete_comment_btn);
				holder.delete_btn = delete_btn;
				
				v.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) v.getTag();
			
			final String writer;
			final int wf_index;
			SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE); 
			wf_index = pref.getInt("wf_index", 1);
			writer = pref.getString("user_name", "");
			
			if (comment.getCommentWriter().equals(writer)) {
				holder.delete_btn.setVisibility(View.VISIBLE);
			} else {
			holder.delete_btn.setVisibility(View.GONE);
			}
			
			
			
			SpannableStringBuilder builder = new SpannableStringBuilder();
			String orange = comment.getCommentWriter();
			SpannableString redSpannable= new SpannableString(orange);
			redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,127,0)), 0, orange.length(), 0);
			builder.append(redSpannable);
			builder.append("_"+comment.getContentText());
			Log.d("comment builder", comment.getContentText());
			holder.comment_text.setText(builder, BufferType.SPANNABLE);

			holder.delete_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sendDeleteComment(wf_index, writer, comment.getCommentIndex());
					arrayComment.remove(position);
					Cadapter.notifyDataSetChanged();
				}
			});
			
			return v;
		}

		@Override
		public long getItemId(int position) {
		    return position;
		}

	}

	public void sendAddComment(final int wf_index, final String user_name, final String comment) {
		String URL_address = "http://119.205.252.224:8000/upload/write/comment/";

		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					Log.d("volley", "add comment result    : " + result);
					// to make data available
					String arrRes = "{'response':" + result + "}";
					Log.d("comment", arrRes);

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
							String wcm_index = jsonFrame.getString("comment_state"); 
							Log.d("comment_wcm_index", wcm_index+"");
							if ("no".equals(wcm_index)) {
								Log.d("delete comment", "fail");
							} else {
								Comment cm = new Comment();


								SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
								String user_name = pref.getString("user_name", "");
								int wf_index =  pref.getInt("wf_index", 1);

								cm.setCommentIndex(wcm_index);// ("wcm_index")
								cm.setFoodIndex(wf_index+"");// ("wf_index")
								cm.setCommentWriter(user_name);// ("wcm_writer")
								cm.setContentText(comment);// ("wcm_text")
								
								arrayComment.add(cm);
								Cadapter.notifyDataSetChanged();
							}
						}	
				} catch (Exception e) {}
				} catch (Exception e) {
				}
			}
		};
		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(CommentActivity.this,
						"네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
			}
		};

		StringRequest myReq = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault());
				Date date = new Date();
				String strDate = dateFormat.format(date);
				Log.d("tag", strDate);
				
				params.put("wf_index", wf_index+"");
				params.put("user_name", user_name);
				params.put("wcm_text", comment);
				
				Log.d("comment_add server", comment+", "+wf_index);
				
				params.put("noti_date", strDate);
				
//				쓸때 ->이름, 프레임 번호, 내용
//				삭제 ->이름  프레임 댓글번호..........
				
				return params;
			};
		};
		mQueue2.add(myReq);

	}
	
	public void sendDeleteComment(final int wf_index, final String user_name, final String commentIndex) {
		String URL_address = "http://119.205.252.224:8000/delete/comment/";

		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					Log.d("volley", "delete comment result    : " + result);
				} catch (Exception e) {
				}
			}
		};

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(CommentActivity.this,
						"네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
			}
		};

		StringRequest myReq = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault());
				Date date = new Date();
				String strDate = dateFormat.format(date);
				Log.d("tag", strDate);
				
				params.put("wf_index", wf_index+"");
				params.put("user_name", user_name);
				params.put("wcm_index", commentIndex);
//				삭제 ->이름  프레임 댓글번호..........
				
				return params;
			};
		};
		mQueue2.add(myReq);

	}
}
