package wewe.fuding.fudingandroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import wewe.fuding.domain.Recipe;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class AddPostingActivity extends ListActivity { 
	
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	Bitmap image;
	Uri mImageCaptureUri;
	File copy_file;
//	ImageButton btnAdd;
	ImageView btnImage;
	private ItemAdapter adapter;
	private ArrayList<Item> mItem;
	private String[] mItemName;
	private String[] mItemTime;
	ImageView btn_ok;
	ImageView btn_add;
	Dialog dialog;
	
//	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
//		@Override
//		public void drop(int from, int to) {
//			Item item = adapter.getItem(from);
//
//			adapter.remove(item);
//			adapter.insert(item, to);
//		}
//	};
//
//	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
//		@Override
//		public void remove(int which) {
//			adapter.remove(adapter.getItem(which));
//		}
//	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addposting);

//		DragSortListView lv = (DragSortListView) getListView();
//
//		lv.setDropListener(onDrop);
//		lv.setRemoveListener(onRemove);
		
		// 글 올리기 최종 확인 버튼
		btn_ok = (ImageView) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				ArrayList<Recipe> containArrayList = new ArrayList<Recipe>();

				for (int i = 0; i < mItem.size(); i++) {
					Recipe temp = new Recipe();
					temp.setContent(mItem.get(i).step);
					temp.setImageURL(mItem.get(i).image);

					containArrayList.add(temp);
				}

//				DataManager dbManager = new DataManager(getApplicationContext());
//				dbManager.deleteItem(-1);
//				dbManager.createTable(containArrayList);
				finish();
			}
		});

		btn_add = (ImageView) findViewById(R.id.btn_add);
		// 단계 추가 버튼
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mItem.add(new Item("".toString(), "다음 단계를 입력해주세요.".toString()));
				adapter.notifyDataSetChanged();
			}
		});
		
		
//		// 처음 리스트뷰 불러오기 
//		DataManager dbManager = new DataManager(getApplicationContext());
		ArrayList<Recipe> getArrayList = new ArrayList<Recipe>();
//		getArrayList = dbManager.getItem(null);
		mItem = new ArrayList<Item>();

		for (int i = 1; i < getArrayList.size(); ++i) {
			mItem.add(new Item(String.valueOf(getArrayList.get(i).getImageURL()), getArrayList.get(i).getContent()));
		}
	 	adapter = new ItemAdapter(mItem);
		
		setListAdapter(adapter);
	}

	class Item {

		private String image;
		private String step; 
		
		public Item(String image, String step) {
			this.image =  image;
			this.step = step;
			
		}

		@Override
		public String toString() {
			return step+image;
		}
	}

	private class ViewHolder {
		public TextView stepView;
		public ImageView imageView;
	}

	private class ItemAdapter extends ArrayAdapter<Item> {
		
		public ItemAdapter(List<Item> objects) {
			super(AddPostingActivity.this, R.layout.row_add_item, R.id.stepEditText, objects);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			final Item item = getItem(position);
			if (v != convertView && v != null) {
				ViewHolder holder = new ViewHolder();

				ImageView image = (ImageView) v.findViewById(R.id.btnImage);
				holder.imageView = image;

				TextView stepView = (TextView) v.findViewById(R.id.stepEditText); 
				holder.stepView = stepView;
				
				v.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) v.getTag();
			String stepText = getItem(position).step;
			holder.stepView.setText(stepText);
			
			holder.imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//makepicture();
					btnImage = holder.imageView;
						
				}
			});
			
			return v;
		}

		@Override
		public long getItemId(int position) {
		    // TODO Auto-generated method stub
		    return position;
		}

	}

}
//public class AddPostingActivity extends Activity{
//
//	
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_addposting);    
//		// 추가할 레이아
//     	
//		final LinearLayout linearLayoutForm = (LinearLayout) findViewById(R.id.linearLayoutForm);
// 
//     	// 항목 추가 버튼
//     	btnAdd = (ImageButton) findViewById(R.id.btnAdd);
//     	btnAdd.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				final LinearLayout newView = (LinearLayout) getLayoutInflater().inflate(R.layout.row_add_item, null);
//				newView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//														 ViewGroup.LayoutParams.WRAP_CONTENT));
//
//				// 항목 삭제 버튼
//				ImageButton btnRemove = (ImageButton) newView.findViewById(R.id.btnRemove);
//				btnRemove.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						linearLayoutForm.removeView(newView);
//					}
//				});
//
//				// 항목 단계별 사진
//				btnImage = (ImageView) newView.findViewById(R.id.btnImage);
//				btnImage.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// 앨범으로 이동
//						Log.d("addPosting log : ", "");
//						makepicture();
//					}
//				});
//				linearLayoutForm.addView(newView);
//			}
//		});
//        
//    }

