package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.domain.Frame;
import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomAdapter_Profile extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	int layout;
	ArrayList<ImageView> arrList;
	private final Integer[] mThumbIds = {};

	public CustomAdapter_Profile() {
	}

	public CustomAdapter_Profile(Context aContext) {
			context = aContext;
			inflater = (LayoutInflater) aContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arrList = new ArrayList<ImageView>();
	}
	
	
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		return null;
	}

}
