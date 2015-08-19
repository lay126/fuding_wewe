package wewe.fuding.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import wewe.fuding.utils.ImageDownloader.DownloadedDrawable;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	public static final String TAG = ImageDownloaderTask.class.getSimpleName(); 
	public String url;
	public String targetUrl;
	private WeakReference<ImageView> imageViewReference;
	public int type;

	public ImageDownloaderTask(String url, ImageView imageView, int type) {
		this.targetUrl = url;
		this.imageViewReference = new WeakReference<ImageView>(imageView);
		this.type = type;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		return downloadBitmap(params[0]);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (imageViewReference != null) {
			ImageView imageView = imageViewReference.get();
			ImageDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

			if (this == bitmapDownloaderTask) {
				
				ImageDownloader.mImageCache.put(targetUrl, bitmap);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	private ImageDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	Bitmap downloadBitmap(String url) {
		final HttpClient client = new DefaultHttpClient();
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream inputStream = null;

				try {
					inputStream = entity.getContent();
					Bitmap bitm = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));

					Bitmap finalBitmap;
					if (type == ImageDownloader.TYPE_IMAGE_FULL) {
						finalBitmap = bitm;
					} else {
						if (bitm.getWidth() >= bitm.getHeight()) { 
							finalBitmap = Bitmap.createBitmap(bitm, bitm.getWidth() / 2 - bitm.getHeight() / 2, 0, bitm.getHeight(), bitm.getHeight());
						} else {
							finalBitmap = Bitmap.createBitmap(bitm, 0, bitm.getHeight() / 2 - bitm.getWidth() / 2, bitm.getWidth(), bitm.getWidth());
						}
					}
					
					int r = 15;	
					if (type == ImageDownloader.TYPE_IMAGE_ROUND) {
						r = finalBitmap.getWidth() / 2;
						//r = 300;
						Log.d(TAG, "getWidth : "+finalBitmap.getWidth()+", r : " + r);
					} else if (type == ImageDownloader.TYPE_IMAGE_FULL) {
						r = 0;
					}
					Bitmap bitmap = roundCornerImage(finalBitmap, r);	
					return bitmap;
				}

				finally {

					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
		}
		return null;
	}

	public static Bitmap roundCornerImage(Bitmap src, float round) {
		// Source image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create result bitmap output
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// set canvas for painting
		Canvas canvas = new Canvas(result);
		canvas.drawARGB(0, 0, 0, 0);

		// configure paint
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		// configure rectangle for embedding
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		// draw Round rectangle to canvas
		canvas.drawRoundRect(rectF, round, round, paint);

		// create Xfer mode
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// draw source image to canvas
		canvas.drawBitmap(src, rect, rect, paint);

		// return final image
		return result;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int bytes = read();
					if (bytes < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
}
