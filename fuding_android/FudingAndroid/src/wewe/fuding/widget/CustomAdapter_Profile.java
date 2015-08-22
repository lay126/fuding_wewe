package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.domain.Content;
import wewe.fuding.domain.Frame;
import wewe.fuding.utils.ImageDownloader;
import wewe.fuding.activity.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter_Profile extends BaseAdapter {

	public static final String TAG = CustomAdapter_Profile.class
			.getSimpleName();

	Context context;
	LayoutInflater inflater;
	int layout;
	private final Integer[] mThumbIds = {};
	private ArrayList<Content> arrList;
	private ImageDownloader imgDownloader = new ImageDownloader();

	public CustomAdapter_Profile(Context aContext) {
		context = aContext;
		inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = new ArrayList<Content>();
	}

	public CustomAdapter_Profile(Context aContext, ArrayList<Content> aArrList) {
		context = aContext;
		inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = aArrList;

		Log.i("customAdapter_profile", context + " " + " ");
	}

	@Override
	public int getCount() {
		return arrList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void add(Content content) {
		arrList.add(content);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();

		if (convertView == null) {
			// view가 null일 경우 커스텀 레이아웃을 얻어 옴
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grid_mycontent_item,
					parent, false);

			// // 리스트 아이템을 터치 했을 때 이벤트 발생
			// convertView.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// }
			// });
		}

		// image
		TextView tvMyContent = (TextView) convertView
				.findViewById(R.id.mycontent_txtView);
		tvMyContent.setText(arrList.get(pos).getContent());
		
		ImageView imgFoodPhoto = (ImageView) convertView.findViewById(R.id.mycontent_imgView);
		String URL_img_address = "http://119.205.252.224:8000/get/image/"
				+ arrList.get(pos).getPhoto();
		Log.i(TAG, URL_img_address);
		imgDownloader.download(URL_img_address, imgFoodPhoto, 0);

		return convertView;
	}
}
