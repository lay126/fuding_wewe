package wewe.fuding.widget;

import wewe.fuding.activity.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_Profile extends Fragment {
	public static final String TAG = Fragment_Profile.class.getSimpleName(); 
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때 설정되고 onDestroyView때 null이 된다.
	
	private static Fragment_Profile instance = null;

	public static Fragment_Profile getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_Profile();
		}
		return instance;
	}

	public Fragment_Profile() {
		 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_profile, container, false);
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

	}
}
