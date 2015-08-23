package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.FudingAPI;
import wewe.fuding.activity.DetailActivity;
import wewe.fuding.activity.R;
import wewe.fuding.domain.Content;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class CustomAdapter_Profile extends BaseAdapter {

	public static final String TAG = CustomAdapter_Profile.class
			.getSimpleName();

	Context context;
	LayoutInflater inflater;
	int layout;
	private ArrayList<Content> arrList;

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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grid_mycontent_item,
					parent, false);
		}
		
		TextView tvMyContent = (TextView) convertView
				.findViewById(R.id.mycontent_txtView);
		tvMyContent.setText(arrList.get(pos).getContent());

		NetworkImageView imgFoodPhoto = (NetworkImageView) convertView
				.findViewById(R.id.mycontent_imgView);
		String URL_img_address = "http://119.205.252.224:8000/get/image/"
				+ arrList.get(pos).getPhoto();

		FudingAPI API = FudingAPI.getInstance(context);
		imgFoodPhoto.setImageUrl(URL_img_address, API.getmImageLoader());
		imgFoodPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int wf_index = arrList.get(pos).getContentId();
				SharedPreferences pref = context.getSharedPreferences("pref",
						context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putInt("wf_index", wf_index);
				editor.commit();
				context.startActivity(new Intent(context, DetailActivity.class));
			}
		});

		return convertView;
	}
}
