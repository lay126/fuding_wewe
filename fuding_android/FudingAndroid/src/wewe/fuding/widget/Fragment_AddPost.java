package wewe.fuding.widget;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.activity.AddPostingActivity;
import wewe.fuding.db.DbOpenHelper;
import wewe.fuding.domain.Frame;
import wewe.fuding.fudingandroid.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Fragment_AddPost extends Fragment {
	public static final String TAG = Fragment_AddPost.class.getSimpleName(); 
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때 설정되고 onDestroyView때 null이 된다.
	private static Fragment_AddPost instance = null;
	private DbOpenHelper mDbOpenHelper;
	private Cursor mCursor;
	Frame food;
	ProgressDialog dialog = null;
	int serverResponseCode = 0;
	
	public static Fragment_AddPost getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_AddPost();
		}
		return instance;
	}

	public Fragment_AddPost() {
		 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_addposting, container, false);
		
		food = new Frame();
		final EditText edit_title = (EditText)v.findViewById(R.id.edit_title);
		final EditText edit_ingredient = (EditText)v.findViewById(R.id.edit_ingredient);
		final EditText edit_time = (EditText)v.findViewById(R.id.edit_time);
		final EditText edit_amount = (EditText)v.findViewById(R.id.edit_amount);
		final EditText edit_tag = (EditText)v.findViewById(R.id.edit_tag); 
		
		Button btn = (Button) v.findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				
 				food.setUserId("noUserId");
 				food.setFoodName(edit_title.getText().toString());
 				food.setIngre(edit_ingredient.getText().toString());
 				food.setTotalTime(edit_time.getText().toString());
 				food.setAmount(edit_amount.getText().toString());
 				food.setTag(edit_tag.getText().toString());

 				// DB Create and Open
// 		        mDbOpenHelper = new DbOpenHelper(activity);
// 		        mDbOpenHelper.open();
// 		        mDbOpenHelper.insertFrameColumn(food);

 		        // 서버 http 전송 
// 		        sendFoodInfo(food);
 		        
 		        startActivity(new Intent(activity, AddPostingActivity.class));
 			}

		});
		
		init(v);
		return v;

	}

	private void sendFoodInfo(final Frame food) {
//			// 이미지와 메일 주소 가져오기 위해
//		HttpRequestAddPost request = new HttpRequestAddPost(
//			new OnHttpRequestListener() {
//				
//				@Override
//				public void onSuccess(Frame frame) {
//					Log.d("http", "success");
//				}
//
//				@Override
//				public void onFailed() {
//					Log.d("http", "fail");
//				}
//			});
//		request.execute(food.getUserId(), food.getFoodName(), food.getIngre(), food.getTotalTime(), food.getAmount(), food.getTag()); 
		
		String URL_address= "http://119.205.252.51:8000/upload/write/title/"; 
		
		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(activity);
		Listener<String> listener = new Listener<String>() {
		@Override
			public void onResponse(String result) {
				try {
					JSONObject jObject = new JSONObject(result);
					final String Value = jObject.getString("sending");
			
					Log.d("result", Value);
					
					SharedPreferences pref = activity.getSharedPreferences("pref", activity.MODE_PRIVATE);
			        SharedPreferences.Editor editor = pref.edit();
			        editor.putString("imageURL_index", "서버에서 보내준 유저id+고유번호+글번호");
			        editor.commit();
					
					if (Value.equals("success")) {
						Toast.makeText(activity, "업로드 1단계 성공 ", Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(activity, "업로드 실패 ", Toast.LENGTH_LONG).show();
					
				} catch (JSONException e) { 
					e.printStackTrace();
				}	
			}
		};

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity, "네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
				}
		};
		
		StringRequest myReq = new StringRequest(Method.POST, URL_address, listener,
				errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", food.getUserId());
				params.put("wt_name", food.getFoodName());
				params.put("wt_ingre", food.getIngre());
				params.put("wt_times", food.getTotalTime());
				params.put("wt_quant", food.getAmount());
				params.put("wt_tag", food.getTag());
				
				Log.d("volley", food.getUserId()+food.getFoodName()+food.getIngre()+food.getTotalTime()+food.getAmount()+food.getTag());
				return params;
			};
		};
		mQueue2.add(myReq);
	
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

	}
}
