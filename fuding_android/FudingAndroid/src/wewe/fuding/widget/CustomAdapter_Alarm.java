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

public class CustomAdapter_Alarm extends BaseAdapter {

	// userId // foodName; // ingre
	// amount // totalTime // tag // likeCnt

	Context context;
	LayoutInflater inflater;
	int layout;
	ArrayList<Frame> arrList;

	public CustomAdapter_Alarm() {
		arrList = new ArrayList<Frame>();
	}

	public CustomAdapter_Alarm(Context aContext, ArrayList<Frame> item) {
		context = aContext;
		inflater = (LayoutInflater) aContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrList = item;
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
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_noti_item, parent, false);

		}
		return parent;


	}
}
