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
import wewe.fuding.domain.User;
import android.os.AsyncTask;

public class HttpRequestAddPost extends AsyncTask<String, Void, HttpResponse> {

    private static final String POST_URL = "http://www.g9.co.kr/Display/VIP/Index/701553517";
 
    public interface OnHttpRequestListener {
        public void onSuccess(Frame frame);
        public void onFailed();
    }
 
    private OnHttpRequestListener listener;
 
    public HttpRequestAddPost(OnHttpRequestListener onHttpRequestListener) {
        this.listener = onHttpRequestListener;
    }
 
    @Override
    protected HttpResponse doInBackground(String... params) {
        String userId = params[0];
        String foodName = params[1]; 
        String ingre = params[2]; 
        String totalTime = params[3]; 
        String amount = params[4]; 
        String tag = params[5]; 
 
        try {
            HttpPost post = new HttpPost(POST_URL);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("", userId));
            pairs.add(new BasicNameValuePair("", foodName));
            pairs.add(new BasicNameValuePair("", ingre));
            pairs.add(new BasicNameValuePair("", totalTime));
            pairs.add(new BasicNameValuePair("", amount));
            pairs.add(new BasicNameValuePair("", tag));
            HttpEntity entity = new UrlEncodedFormEntity(pairs);
            post.setEntity(entity);
 
            HttpClient client = new DefaultHttpClient();
 
            return client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    @Override
    protected void onPostExecute(HttpResponse httpResponse) {
        if (httpResponse == null) {
            listener.onFailed();
            return;
        }

        StatusLine line = httpResponse.getStatusLine();
        if (line.getStatusCode() == 200) {
            try {
                String result = EntityUtils.toString(httpResponse.getEntity());
                JSONObject object = new JSONObject(result);
                listener.onSuccess(Frame.fromJson(object));
                
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
 
        listener.onFailed();
    }
}