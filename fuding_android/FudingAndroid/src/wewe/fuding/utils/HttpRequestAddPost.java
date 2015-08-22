package wewe.fuding.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.domain.Frame;
import android.os.AsyncTask;
import android.util.Log;

public class HttpRequestAddPost{
//	 로그인 세션 체크 (true -> 로그인 상태 / false -> 로그아웃 상태 / 초기 상태)
//	"login_check", "true"
//	
//   로그인 리턴 값 (회원의 고유 아이)		
//	"user_id", result
	
}