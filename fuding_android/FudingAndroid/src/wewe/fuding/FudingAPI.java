package wewe.fuding;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;


public class FudingAPI {
	private static FudingAPI instance;
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	

	public static RequestQueue getmRequestQueue() {
		return mRequestQueue;
	}


	public static ImageLoader getmImageLoader() {
		return mImageLoader;
	}


	private FudingAPI(Context c) {
		
		mRequestQueue = Volley.newRequestQueue(c);
		mImageLoader = new ImageLoader(mRequestQueue, new ImageCache() {
            private final LruCache mCache = new LruCache(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return (Bitmap) mCache.get(url);
            }
        });
	}

	public static FudingAPI getInstance(Context c) {
		if (instance == null)
			instance = new FudingAPI(c);
		return instance;
	}
}