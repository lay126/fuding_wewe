package wewe.fuding.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
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
	
	EditText id, pwd, email, content;
	ImageView ok_btn, cancel_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_signup);
		
		id = (EditText) findViewById(R.id.edit_id);
		pwd = (EditText) findViewById(R.id.edit_pwd);
		email = (EditText) findViewById(R.id.edit_email);
		content = (EditText) findViewById(R.id.edit_content);

		ok_btn = (ImageView) findViewById(R.id.ok_btn);
		cancel_btn = (ImageView) findViewById(R.id.cancel_btn);
		
		ok_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (id.getText().toString() == null || pwd.getText().toString() == null || email.getText().toString() == null) {
					Toast.makeText(SignUpActivity.this, "아이디와 비밀번호, 이메일은 필수입력 사항입니다.", Toast.LENGTH_LONG).show();
				} else {
					signupRequest(id.getText().toString(), pwd.getText().toString(), email.getText().toString(), content.getText().toString());
				}
			}
		});
		
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void signupRequest(final String id, final String pwd, final String email, final String content) {
		String URL_address= "http://119.205.252.224:8000/upload/write/title/"; 
		
		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(SignUpActivity.this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					Log.d("signupActivity.class", "success");
					finish();
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
				params.put("user_id", id);
				params.put("user_pwd", pwd);
				params.put("user_email", email);
				params.put("user_content", content);
				
				return params;
			};
		};
		mQueue2.add(myReq);
	

	
	}
	
}
