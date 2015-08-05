package wewe.fuding.fudingandroid;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import wewe.fuding.domain.Content;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AddPostingActivity extends ListActivity { 
	
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private ItemAdapter adapter;
	private ArrayList<Item> mItem;
	private String[] mItemName;
	private String[] mItemTime;
	ImageView btn_ok;
	ImageView btn_add;
	Dialog dialog;
	
	Bitmap image;
	Uri mImageCaptureUri;
	File copy_file; 
	ImageView btnImage;
	String upLoadServerUri;
	int serverResponseCode = 0;
	boolean flag = false;	// 편집 레이아웃으로 변경 
	
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

				ArrayList<Content> containArrayList = new ArrayList<Content>();

				for (int i = 0; i < mItem.size(); i++) {
					Content temp = new Content();
					temp.setContent(mItem.get(i).step);
					temp.setPhoto(mItem.get(i).image);

					containArrayList.add(temp);
				}
				
				upLoadServerUri = "http://54.213.91.157:52271/Photo/setphoto";
				if (mImageCaptureUri != null) {
					dialog = ProgressDialog.show(AddPostingActivity.this, "", "Uploading file...", true);

					new Thread(new Runnable() {
						public void run() {
							int result = uploadFile(mImageCaptureUri.getPath());
							Log.d("image upload", "result : "+result);
						}

					}).start(); 
					
				} else {
					Toast.makeText(AddPostingActivity.this, "업로드에 실패했습니다.", Toast.LENGTH_LONG).show();
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
				mItem.add(new Item("", "", ""));
				adapter.notifyDataSetChanged();
			}
		});
		
		ImageView btn_edit = (ImageView) findViewById(R.id.btn_edit);
		btn_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					flag  = false;
				} else {
					flag = true;
				}
				adapter.notifyDataSetChanged();
			}
		});
		
		// 처음 리스트뷰 불러오기 
//		DataManager dbManager = new DataManager(getApplicationContext());
//		getArrayList = dbManager.getItem(null);
//		ArrayList<Content> arrayList = new ArrayList<Content>();
		mItem = new ArrayList<Item>();
//
//		for (int i = 1; i < arrayList.size(); ++i) {
//			mItem.add(new Item(String.valueOf(arrayList.get(i).getFoodId()), arrayList.get(i).getContent()));
//		}

		
		
//		adapter = new ItemAdapter(this, R.layout.row_add_item, mItem);
//	    listView.setAdapter(adapter);
//	    listView.setDropListener(onDrop);
//	    listView.setRemoveListener(onRemove);
//
//	    DragSortController controller = new DragSortController(listView);
//	    controller.setDragHandleId(R.id.menu);
//	            //controller.setClickRemoveId(R.id.);
//	    controller.setRemoveEnabled(false);
//	    controller.setSortEnabled(true);
//	    controller.setDragInitMode(1);
//	            //controller.setRemoveMode(removeMode);
//
//	    listView.setFloatViewManager(controller);
//	    listView.setOnTouchListener(controller);
//	    listView.setDragEnabled(true);
				
		
		adapter = new ItemAdapter(mItem);
		setListAdapter(adapter);
	}

	class Item {

		private String image;
		private String step;
		private String time;
		
		public Item(String image, String step, String time) {
			this.image =  image;
			this.step = step;
			this.time = time;
		}

		@Override
		public String toString() {
			return step+image+time;
		}
	}

	private class ViewHolder {
		public TextView stepView;
		public ImageView imageView, holderBtn, deleteBtn;
		public TextView timeView;
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

				EditText stepView = (EditText) v.findViewById(R.id.stepEditText); 
				holder.stepView = stepView;
				
				EditText timeView = (EditText)v.findViewById(R.id.time_edit); 
				holder.timeView = timeView;
				
				ImageView btn_holder = (ImageView)v.findViewById(R.id.drag_handle);
				ImageView btn_delete = (ImageView)v.findViewById(R.id.btnRemove);
				holder.holderBtn = btn_holder;
				holder.deleteBtn = btn_delete;
				
				v.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) v.getTag();
			String stepText = getItem(position).step;
			holder.stepView.setText(stepText);
			String timeText = getItem(position).time;
			holder.timeView.setText(timeText);
			
			if (flag) {
				holder.holderBtn.setVisibility(View.VISIBLE);
				holder.deleteBtn.setVisibility(View.VISIBLE);
			} else {
				holder.holderBtn.setVisibility(View.GONE);
				holder.deleteBtn.setVisibility(View.GONE);
			}
			
			holder.imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btnImage = holder.imageView;
					makepicture();
				}
			});
			
			return v;
		}

		@Override
		public long getItemId(int position) {
		    return position;
		}

	}

	private int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			runOnUiThread(new Runnable() {
				public void run() {
				}
			});
			return 0;

		} else {
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				Log.d("image upload", "HTTP Request to : " + url);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"user_srl\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes("1004");
				// 源⑥???寃쎌슦媛€ ?덉쓬
				// dos.write(SPUtils.get(me, "user_name").getBytes("utf-8"));
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
						+ fileName + "\"" + lineEnd);
				Log.d("image upload", "sdfsdf : " + fileName);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				// final String serverResponseMessage =
				// conn.getResponseMessage();

				// Log.d(TAG, "HTTP Response is : " + serverResponseMessage +
				// ": " + serverResponseCode);

				if (serverResponseCode == 200) {
					Log.d("image upload", "success");
					Toast.makeText(AddPostingActivity.this, "업로드 완료 ", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Log.d("image upload", "fail");
					Toast.makeText(AddPostingActivity.this, "업로드 실패 ", Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(AddPostingActivity.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
					}
					 
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block

	}

	private void makepicture() {

		final LinearLayout change_picture = (LinearLayout) View.inflate(this, R.layout.dialog_move_picture, null);

		final ArrayList<String> arrList = new ArrayList<String>();
		arrList.add(getString(R.string.settings_profile_album));
		arrList.add(getString(R.string.settings_profile_camera));
		arrList.add(getString(R.string.settings_profile_delete));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrList);
		ListView choice_piture = (ListView) change_picture.findViewById(R.id.listView1);
		choice_piture.setAdapter(adapter);

		final Dialog pictureDialog = new Dialog(this);
		pictureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pictureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pictureDialog.setContentView(change_picture);
		pictureDialog.show();

		choice_piture.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

				if (arrList.get(position) == getString(R.string.settings_profile_album)) {
					doTakeAlbumAction();
					pictureDialog.dismiss();

				} else if (arrList.get(position) == getString(R.string.settings_profile_camera)) {
					doTakePhotoAction();
					pictureDialog.dismiss();

				} else if (arrList.get(position) == getString(R.string.settings_profile_delete)) {
					pictureDialog.dismiss();
					imageDelete();
				}
			}

			private void doTakeAlbumAction() {

				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				startActivityForResult(intent, PICK_FROM_ALBUM);
			}

			private void doTakePhotoAction() {

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// Crop된 이미지를 저장할 파일의 경로를 생성
				mImageCaptureUri = createSaveCropFile();

				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

				startActivityForResult(intent, PICK_FROM_CAMERA);
			}
		});

	}

	protected void imageDelete() {
		// 이미지 취소하고 싶을 때 
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_ALBUM: {

			mImageCaptureUri = data.getData();
			File original_file = getImageFile(mImageCaptureUri);
			mImageCaptureUri = createSaveCropFile();
			File cpoy_file = new File(mImageCaptureUri.getPath());

			//Log.d(TAG, "mImageCaptureUri.getPath() : "+mImageCaptureUri.getPath());
			// SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
			copyFile(original_file, cpoy_file);

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");
			// Crop한 이미지를 저장할 Path
			intent.putExtra("output", mImageCaptureUri);
			startActivityForResult(intent, CROP_FROM_CAMERA);
			break;

		}
		case PICK_FROM_CAMERA: {

			mImageCaptureUri = createSaveCropFile();

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mImageCaptureUri, "image/*");
			// Crop한 이미지를 저장할 Path 
			intent.putExtra("output", mImageCaptureUri);
			startActivityForResult(intent, CROP_FROM_CAMERA);
			break;
		}

		case CROP_FROM_CAMERA: {

			// Crop 된 이미지를 넘겨 받습니다.
			mImageCaptureUri = createSaveCropFile();

			final Bundle extras = data.getExtras();

			if (extras != null) {
				Bitmap bitm = extras.getParcelable("data");
				btnImage.setImageBitmap(bitm);
			}
			btnImage.setBackgroundColor(Color.WHITE);
			btnImage.setImageURI(mImageCaptureUri);
			btnImage.setScaleType(ImageView.ScaleType.CENTER_CROP);	//가운데 자름 (길쭉하게 자른 경우)
			break;
		}
		}

	}

	private Uri createSaveCropFile() {
		String url = "daeun.jpg";
		Uri uri = Uri.fromFile(new File(getExternalFilesDir(null), url));
		//Log.d(TAG, "uri : "+uri);
		
		return uri;
		// 유알아이도 있공. 스트링 유알엘도있음.
	}

	private File getImageFile(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		if (uri == null) {
			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}

		Cursor mCursor = getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
		if (mCursor == null || mCursor.getCount() < 1) {
			return null; // no cursor or no record
		}
		int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		mCursor.moveToFirst();

		String path = mCursor.getString(column_index);

		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
		}

		return new File(path);
	}

	public static boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			} finally {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	private static boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
				inputStream.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}

	}
}
	