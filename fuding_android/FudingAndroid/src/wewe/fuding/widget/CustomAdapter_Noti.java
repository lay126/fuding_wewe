package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Noti;
import wewe.fuding.utils.ImageDownloader;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class CustomAdapter_Noti extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	int layout;
	ArrayList<Noti> arrList;

	public CustomAdapter_Noti() {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final Context context = parent.getContext();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_noti_item, parent, false);
			
		}
		RelativeLayout mid_layout = (RelativeLayout) convertView.findViewById(R.id.mid_layout);
		ImageView image = (ImageView) convertView.findViewById(R.id.image);
		TextView text = (TextView) convertView.findViewById(R.id.txt_noti);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		
		ImageDownloader.download("http://119.205.252.224:8000/get/image/"+arrList.get(position).getImage(), image, 2);	// 이미지 라운드 

		
		
		SpannableStringBuilder builder = new SpannableStringBuilder();


		
		
		
		String orange = arrList.get(position).getFriendId();
		SpannableString redSpannable= new SpannableString(orange);
		redSpannable.setSpan(new ForegroundColorSpan(Color.rgb(255,127,0)), 0, orange.length(), 0);
		builder.append(redSpannable);
		
		if ("1".equals(arrList.get(position).getType())) {
			builder.append(" 님이 회원님의 게시글을 좋아합니다.");
//			text.setText(arrList.get(position).getFriendId()+" 님이 회원님의 게시글을 좋아합니다.");
		} else if ("2".equals(arrList.get(position).getType())) {
			builder.append(" 님이 회원님의 게시글에 댓글을 남겼습니다.");
//			text.setText(arrList.get(position).getFriendId()+" 님이 회원님의 게시글에 댓글을 남겼습니다.");
		} else {
			builder.append(" 님이 회원님을 팔로우합니다.");
//			text.setText(arrList.get(position).getFriendId()+" 님이 회원님을 팔로우합니다.");
		}
		text.setText(builder, BufferType.SPANNABLE);
		date.setText(arrList.get(position).getDate());

		mid_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(Content)
//				startActivity()
			}
		});
		
		return convertView;


	}
}
