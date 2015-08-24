package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.FudingAPI;
import wewe.fuding.activity.DetailActivity;
import wewe.fuding.activity.OthersProfileActivity;
import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import wewe.fuding.domain.Noti;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.android.volley.toolbox.NetworkImageView;

public class CustomAdapter_Noti extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	int layout;
	ArrayList<Noti> arrList;

	public CustomAdapter_Noti(Context aContext) {
		context = aContext;
		inflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = new ArrayList<Noti>();
	}

	public CustomAdapter_Noti(Context aContext, ArrayList<Noti> noti_array) {
		context = aContext;
		inflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = noti_array;
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


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Context context = parent.getContext();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_noti_item, parent, false);
			
		}
		RelativeLayout mid_layout = (RelativeLayout) convertView.findViewById(R.id.mid_layout);
		NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.image);
		TextView text = (TextView) convertView.findViewById(R.id.txt_noti);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		
//		ImageDownloader.download("http://119.205.252.224:8000/get/image/"+arrList.get(position).getImage(), image, 2);	// 이미지 라운드 
		String URL_img_address = "http://119.205.252.224:8000/get/image/"+arrList.get(position).getImage();
		FudingAPI API = FudingAPI.getInstance(context);
		image.setImageUrl(URL_img_address, API.getmImageLoader());
		
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String orange = arrList.get(position).getFriendId();
		SpannableString redSpannable= new SpannableString(orange);
		redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,127,0)), 0, orange.length(), 0);
		builder.append(redSpannable);
		
		if ("1".equals(arrList.get(position).getType())) {
			builder.append(" 님이 게시글을 좋아합니다.");
		} else if ("2".equals(arrList.get(position).getType())) {
			builder.append(" 님이 게시글에 댓글을 남겼습니다.");
			//text.setText(arrList.get(position).getFriendId()+" 님이 회원님의 게시글에 댓글을 남겼습니다.");
		} else {
			builder.append(" 님이 회원님을 팔로우합니다.");
		}
		text.setText(builder, BufferType.SPANNABLE);

		date.setText(arrList.get(position).getDate());

		mid_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences pref = context.getSharedPreferences("pref",context.MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				
				if ("1".equals(arrList.get(position).getType()) || "2".equals(arrList.get(position).getType())) {
					// 게시물에 대한 알림은 해당 게시물로 이동 
					String wf_index = arrList.get(position).getWf_index();//FoodIndex();
					editor.putInt("wf_index", Integer.parseInt(wf_index));
					context.startActivity(new Intent(context, DetailActivity.class));
				} else if ("3".equals(arrList.get(position).getType())) {
					// 팔료우를 한 경우 상대방의 프로필로 이동 
					editor.putString("wf_writer", arrList.get(position).getFriendId());
					context.startActivity(new Intent(context, OthersProfileActivity.class));
					
				}
				editor.commit();
			}
		});
		
		return convertView;


	}
}
