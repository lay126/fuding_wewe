package wewe.fuding.widget;

import java.util.ArrayList;

import wewe.fuding.activity.R;
import wewe.fuding.domain.Frame;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment_Noti extends Fragment {
	public static final String TAG = Fragment_Noti.class.getSimpleName(); 
	public static FragmentActivity activity; // 자신을 포함하는 activity. onCreateView때 설정되고 onDestroyView때 null이 된다.
	
	private static Fragment_Noti instance = null;

	public static Fragment_Noti getInstance() {
		if (instance == null) { // 최초 1회 초기화
			instance = new Fragment_Noti();
		}
		return instance;
	}

	public Fragment_Noti() {
		 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = getActivity();

		View v;
		v = inflater.inflate(R.layout.fragment_noti, container, false);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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

