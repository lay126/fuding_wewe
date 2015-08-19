package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter_NewsFeed extends BaseAdapter {

	// userId // foodName; // ingre
	// amount // totalTime // tag // likeCnt

	Context context;
	LayoutInflater inflater;
	int layout;
	private ArrayList<Frame> arrList;

	public CustomAdapter_NewsFeed(Context aContext) {
		context = aContext;
		inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = new ArrayList<Frame>();
	}

	public CustomAdapter_NewsFeed(Context aContext, ArrayList<Frame> aArrList) {
		context = aContext;
		inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = aArrList;
		// layout = aLayout;
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

	public void add(Frame frame) {
		arrList.add(frame);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();

		// 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 convertView가 null인 상태로 들어 옴
		if (convertView == null) {
			// view가 null일 경우 커스텀 레이아웃을 얻어 옴
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_newsfeed_item, parent,
					false);

			// // 좋아요 버튼을 터치 했을 때 이벤트 발생
			// Button btn = (Button)
			// convertView.findViewById(R.id.newsfeed_imgBtnLike);
			// btn.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// }
			// });

			// // 리스트 아이템을 터치 했을 때 이벤트 발생
			// convertView.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// }
			// });

		}

		TextView tvUserId = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewUserId);
		tvUserId.setText(arrList.get(pos).getUserId());

		TextView tvDate = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewWriteDate);
		tvDate.setText(arrList.get(pos).getWriteDate());

		TextView tvContentTitle = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewContentTitle);
		tvContentTitle.setText("" + arrList.get(pos).getFoodName());

		TextView tvLikeCnt = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewLikeCnt);
		tvLikeCnt.setText("" + arrList.get(pos).getLikeCnt());

		TextView tvTag = (TextView) convertView
				.findViewById(R.id.newsfeed_txtViewTag);
		tvTag.setText(arrList.get(pos).getTag());

		// userId // foodName; // ingre
		// amount // totalTime // tag // likeCnt
		return convertView;
	}
}
