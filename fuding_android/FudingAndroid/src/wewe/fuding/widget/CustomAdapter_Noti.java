package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Noti;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

		ImageView image = (ImageView) convertView.findViewById(R.id.image);
		TextView text = (TextView) convertView.findViewById(R.id.txt_noti);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		
		if (arrList.get(position).getType() == "1") {
			text.setText(arrList.get(position).getFriendId()+" 님이 회원님의 게시글을 좋아합니다.");
		} else if (arrList.get(position).getType() == "2") {
			text.setText(arrList.get(position).getFriendId()+" 님이 회원님의 게시글에 댓글을 남겼습니다.");
		}
		date.setText(arrList.get(position).getDate());

		return convertView;


	}
}
