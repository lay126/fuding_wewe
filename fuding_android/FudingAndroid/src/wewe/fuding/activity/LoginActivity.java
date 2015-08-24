package wewe.fuding.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

// http://shary1012.tistory.com/78
public class LoginActivity extends Activity { 

	ImageView login_btn, sign_up_btn;
	EditText edit_id, edit_password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login_btn = (ImageView) findViewById(R.id.login_btn);
		sign_up_btn = (ImageView) findViewById(R.id.sign_up_btn);
		edit_id = (EditText) findViewById(R.id.id_text);
		edit_password = (EditText) findViewById(R.id.password_text);
		edit_id.setBackgroundColor(Color.TRANSPARENT);
		edit_password.setBackgroundColor(Color.TRANSPARENT);
		
		
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(edit_id.getText().toString()) || "".equals(edit_password.getText().toString())) {
					Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
				} else {
					loginRequest(edit_id.getText().toString(), edit_password.getText().toString());
				}
			}
		});
		
		sign_up_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
			}
		});
	}

	private void loginRequest(final String id, final String pwd) {
		String URL_address= "http://119.205.252.224:8000/login/user/"; 
		
		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(LoginActivity.this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					Log.d("LoginActivity", result);
					
						// 로그인 에서 받은 값 sp에 저장 (아이디 이메일)
						// 프로필 변경 엑티비티에서 띄워줘야함 , 변경 시 sp 변경 
						//           유저 아이디 넘겨줌
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
									String userName = jsonFrame.getString("username");
									String email = jsonFrame.getString("email"); 
								
									SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
									SharedPreferences.Editor editor = pref.edit();
									editor.putString("login_check", "true"); //로그인 세션 저장 
									editor.putString("user_name", userName); // 회원의 고유 아이디 
									editor.putString("user_email", email); // 회원의 이메일 주소  
									editor.commit();

									startActivity(new Intent(LoginActivity.this, FudingMainActivity.class));
									finish();
								} else {
									Toast.makeText(LoginActivity.this, "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
								}		
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					
					} catch (Exception e){
				}
			}
		};

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(LoginActivity.this, "네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
				}
		};
		
		StringRequest myReq = new StringRequest(Method.POST, URL_address, listener,
				errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("login_id", id);
				params.put("login_pwd", pwd);
				
				return params;
			};
		};
		mQueue2.add(myReq);
	

	
	}
	


}
