package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.domain.Frame;
import wewe.fuding.fudingandroid.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment_NewsFeed extends Fragment {
	public static final String TAG = Fragment_NewsFeed.class.getSimpleName();
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때
												// 설정되고 onDestroyView때 null이 된다.

	private static Fragment_NewsFeed instance = null;

	private Frame nfFrame; // newsfeed frame
	private ArrayList<Frame> nfList;
	private CustomAdapter_NewsFeed nfAdapter;
	private ListView nfListView;

	public static Fragment_NewsFeed getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_NewsFeed();
		}
		return instance;
	}

	public Fragment_NewsFeed() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_newsfeed, container, false);

		init(v);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void init(View v) {

		Frame tempF = new Frame("yeoeun", "불닭", "불,닭", "4인분", "30분", "#속쓰려", 4);
		nfList.add(tempF);
		
		nfAdapter = new CustomAdapter_NewsFeed(activity, nfList);
		nfListView.setAdapter(nfAdapter);
	}
}
