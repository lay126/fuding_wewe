package wewe.fuding.utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageDownloader { 
	public static final int IMGAE_CACHE_LIMIT_SIZE = 100;
	public static HashMap<String, Bitmap> mImageCache = new HashMap<String, Bitmap>();
	
	public static final int TYPE_IMAGE = 0;
	public static final int TYPE_IMAGE_ICON = 1;
	public static final int TYPE_IMAGE_ROUND = 2;
	public static final int TYPE_IMAGE_FULL = 3;
	
	public static void download(String url, ImageView imageView, int type) {
		Bitmap cachedImage = mImageCache.get(url);

		if (cachedImage != null) {
			//Log.d("number   cache", "cache is not null");
			imageView.setImageBitmap(cachedImage);
		} else if (cancelPotentialDownload(url, imageView)) {
			ImageDownloaderTask task = new ImageDownloaderTask(url, imageView, type);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);

			imageView.setImageDrawable(downloadedDrawable);
			task.execute(url);
		}
	}

	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		ImageDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static ImageDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {

				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<ImageDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(ImageDownloaderTask bitmapDownloaderTask) {
			super(Color.TRANSPARENT);
			bitmapDownloaderTaskReference = new WeakReference<ImageDownloaderTask>(bitmapDownloaderTask);
		}

		public ImageDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}
}
