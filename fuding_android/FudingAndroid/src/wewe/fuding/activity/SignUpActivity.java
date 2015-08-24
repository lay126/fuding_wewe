package wewe.fuding.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SignUpActivity  extends Activity { 
	
	EditText id, pwd, email, content, pwd_check;
	ImageView ok_btn, cancel_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_signup);
		
		id = (EditText) findViewById(R.id.edit_id);
		pwd = (EditText) findViewById(R.id.edit_pwd);
		pwd_check = (EditText) findViewById(R.id.edit_pwd_check);
		email = (EditText) findViewById(R.id.edit_email); 
		
		id.setBackgroundColor(Color.TRANSPARENT);
		pwd.setBackgroundColor(Color.TRANSPARENT);
		pwd_check.setBackgroundColor(Color.TRANSPARENT);
		email.setBackgroundColor(Color.TRANSPARENT);
		
		ok_btn = (ImageView) findViewById(R.id.ok_btn);
		///cancel_btn = (ImageView) findViewById(R.id.cancel_btn);
		
		ok_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if ("".equals(id.getText().toString()) || "".equals(pwd.getText().toString()) || "".equals(email.getText().toString())) {
					Toast.makeText(SignUpActivity.this, "빈칸을 모두 입력해주세요.", Toast.LENGTH_LONG).show();
				} else {
					signupRequest(id.getText().toString(), pwd.getText().toString(), email.getText().toString());
				}
			}
		});
		
//		cancel_btn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
	}

	private void signupRequest(final String id, final String pwd, final String email) {
		String URL_address= "http://119.205.252.224:8000/join/user/"; 
		
		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(SignUpActivity.this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					String arrRes = "{'response':" + result + "}";

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
							String flag = jsonFrame.getString("result");
							if ("0".equals(flag)) {
								Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
								finish(); 
							} else {
								Toast.makeText(SignUpActivity.this, "이미 등록된 이메일입니다.", Toast.LENGTH_LONG).show();
							}	
						}
					} catch (Exception e){}	 
				} catch (Exception e){
					
				}
			}
		};

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(SignUpActivity.this, "네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
				}
		};
		
		StringRequest myReq = new StringRequest(Method.POST, URL_address, listener,
				errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("join_id", id);
				params.put("join_email", email);
				params.put("join_pwd", pwd);
			//	params.put("join_info", content);
				
				return params;
			};
		};
		mQueue2.add(myReq);
	

	
	}
	
}
