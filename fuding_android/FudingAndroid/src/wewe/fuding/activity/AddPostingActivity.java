package wewe.fuding.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wewe.fuding.FudingAPI;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

// content, image, time
// 마지막 이미지에는 1로 flag
// 마지막 사진 전송에는 frame url 시 사진까지 전송! 

public class AddPostingActivity extends ListActivity {

	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private ItemAdapter adapter;
	private ArrayList<Item> mItem;
	ImageView btn_ok;
	ImageView btn_add;
	Dialog dialog;

	int step_num = 0;
	
	
	Bitmap image;
	Uri mImageCaptureUri;
	Uri uri_copy;
	Uri imageList[] = { null, null, null, null, null, null, null, null, null };
	String contentList[] = { null, null, null, null, null, null, null, null, null };

	File copy_file;
	ImageView btnImage;
	String upLoadServerUri;
	int serverResponseCode = 0;
	boolean flag = false; // 편집 레이아웃으로 변경
	int content_index = 0;
	int content_position = 0; // 선택된 단계의 position 값을 통해 배을 저장한다.
	EditText ed_step, ed_time;
	ImageView dialog_image;
	int httpFlag = 0;

	// private DragSortListView.DropListener onDrop = new
	// DragSortListView.DropListener() {
	// @Override
	// public void drop(int from, int to) {
	// Item item = adapter.getItem(from);
	//
	// adapter.remove(item);
	// adapter.insert(item, to);
	// }
	// };
	//
	// private DragSortListView.RemoveListener onRemove = new
	// DragSortListView.RemoveListener() {
	// @Override
	// public void remove(int which) {
	// adapter.remove(adapter.getItem(which));
	// }
	// };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addposting);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// DragSortListView lv = (DragSortListView) getListView();
		//
		// lv.setDropListener(onDrop);
		// lv.setRemoveListener(onRemove);

		// 글 올리기 최종 확인 버튼
		btn_ok = (ImageView) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// ArrayList<Content> containArrayList = new
				// ArrayList<Content>();

				// adapter.notifyDataSetChanged();
				// for (int i = 0; i < mItem.size(); i++) {
				// Content temp = new Content();
				// temp.setContent(mItem.get(i).step);
				// temp.setPhoto(mItem.get(i).image);
				// Log.d("tag", "mItem.get(i).step"+mItem.get(i).step);
				// Log.d("tag", "mItem.get(i).image"+mItem.get(i).image);
				// containArrayList.add(temp);
				// Item item = new Item("",ed_step.getText().toString(),
				// ed_time.getText().toString() );
				// mItem_re.add(item);
				//
				// }
				// mItem = mItem_re;

				if (mImageCaptureUri != null) {
					// if (dialog==null) {
					// dialog = ProgressDialog.show(AddPostingActivity.this, "",
					// "Uploading file...", true);
					// }
					new Thread(new Runnable() {
						public void run() {
//							for (int i = 0; i < mItem.size(); i++) {
//								int finish_flag = 0;
//								int step_num = i + 1;
//								content_index = i + 1;
//								upLoadServerUri = "http://119.205.252.224:8000/upload/write/content/";
//								if (i + 1 == mItem.size()) {
//									finish_flag = 1;
//									uploadFile(mItem.get(i).image.getPath(),mItem.get(i).step,mItem.get(i).time, step_num, finish_flag);
//									finishContent(step_num);
//								} else {
//									uploadFile(mItem.get(i).image.getPath(),mItem.get(i).step, mItem.get(i).time, step_num, finish_flag);
//								}
//							}
							
							// 마지막 글쓰기시 전송
							int total_content_cnt = mItem.size();
							finishContent(total_content_cnt);
							
							
						}
					}).start();
//					Log.d("url", "url :" + mImageCaptureUri.getPath());
				} else {
					Toast.makeText(AddPostingActivity.this, "업로드에 실패했습니다.",
							Toast.LENGTH_LONG).show();
				}
				finish();
			}
		});

		btn_add = (ImageView) findViewById(R.id.btn_add);
		// 단계 추가 버튼
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// mItem.add(new Item("", "", ""));
				// adapter.notifyDataSetChanged();

				Rect displayRectangle = new Rect();
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				dialog = new Dialog(AddPostingActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				Window window = dialog.getWindow();
				window.getDecorView().getWindowVisibleDisplayFrame(
						displayRectangle);
				WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
				wlp.copyFrom(window.getAttributes());
				wlp.width = (int) (displayRectangle.width() * 0.69f);
				wlp.height = (int) (displayRectangle.height() * 0.65f);

				dialog.getWindow().setAttributes(wlp);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				View dialogView = (RelativeLayout) vi.inflate(
						R.layout.dialog_detail_setting, null);

				final EditText edit_step = (EditText) dialogView.findViewById(R.id.ed_step);
				final EditText edit_time = (EditText) dialogView.findViewById(R.id.ed_time);
				dialog_image = (ImageView) dialogView.findViewById(R.id.image);
				Button btn_add_ok = (Button) dialogView.findViewById(R.id.btn_ok);
				Button btn_add_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);

				btn_add_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (edit_step.length() > 0 && edit_time.length() > 0) {
							Toast.makeText(AddPostingActivity.this, "추가되었습니다.", Toast.LENGTH_LONG).show();

							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(edit_time.getWindowToken(), 0);
//							mItem.add(new Item(uri_copy, edit_step.getText().toString(), edit_time.getText().toString()));
//							adapter.notifyDataSetChanged();
							dialog.cancel();

							
						//==================================================================================================//	
//							new Thread(new Runnable() {
//							public void run() {
							step_num += 1;
							content_index += 1;
							upLoadServerUri = "http://119.205.252.224:8000/upload/write/content/";
							uploadFile(uri_copy.getPath(), edit_step.getText().toString() , edit_time.getText().toString() , step_num, 0);
//							}
//						}).start();
//						Log.d("url", "url :" + mImageCaptureUri.getPath());
							
						} else {
							Toast.makeText(AddPostingActivity.this,"빈칸을 모두 채워주세요.", Toast.LENGTH_LONG).show();
						}
					}
				});

				btn_add_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.cancel();
					}
				});

				dialog_image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Log.d("mItem", mItem.size() + 1 + "");
						content_position = mItem.size() + 1;
						makepicture();
					}
				});

				dialog.setCancelable(true);
				dialog.setContentView(dialogView);
				dialog.show();
			}
		});

//		ImageView btn_edit = (ImageView) findViewById(R.id.btn_edit);
//		btn_edit.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (flag) {
//					flag = false;
//				} else {
//					flag = true;
//				}
//				adapter.notifyDataSetChanged();
//			}
//		});

		// 처음 리스트뷰 불러오기
		// DataManager dbManager = new DataManager(getApplicationContext());
		// getArrayList = dbManager.getItem(null);
		// ArrayList<Content> arrayList = new ArrayList<Content>();
		mItem = new ArrayList<Item>();
		//
		// for (int i = 1; i < arrayList.size(); ++i) {
		// mItem.add(new Item(String.valueOf(arrayList.get(i).getFoodId()),
		// arrayList.get(i).getContent()));
		// }

		// adapter = new ItemAdapter(this, R.layout.row_add_item, mItem);
		// listView.setAdapter(adapter);
		// listView.setDropListener(onDrop);
		// listView.setRemoveListener(onRemove);
		//
		// DragSortController controller = new DragSortController(listView);
		// controller.setDragHandleId(R.id.menu);
		// //controller.setClickRemoveId(R.id.);
		// controller.setRemoveEnabled(false);
		// controller.setSortEnabled(true);
		// controller.setDragInitMode(1);
		// //controller.setRemoveMode(removeMode);
		//
		// listView.setFloatViewManager(controller);
		// listView.setOnTouchListener(controller);
		// listView.setDragEnabled(true);

		adapter = new ItemAdapter(mItem);
		setListAdapter(adapter);
	}

	class Item {

		private String image;
		private String step;
		private String time;

		public Item(String image, String step, String time) {
			this.image = image;
			this.step = step;
			this.time = time;
		}

		@Override
		public String toString() {
			return step + image + time;
		}
	}

	private class ViewHolder {
		public EditText stepView;
		public ImageView deleteBtn; //holderBtn
		public EditText timeView;
		public NetworkImageView imageView;
	}

	private class ItemAdapter extends ArrayAdapter<Item> {

		public ItemAdapter(List<Item> objects) {

			super(AddPostingActivity.this, R.layout.row_add_item, R.id.stepEditText, objects);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			final Item item = getItem(position);
			if (v != convertView && v != null) {
				ViewHolder holder = new ViewHolder();

				NetworkImageView image = (NetworkImageView) v.findViewById(R.id.btnImage);
				holder.imageView = image;

				EditText stepView = (EditText) v
						.findViewById(R.id.stepEditText);
				holder.stepView = stepView;

				EditText timeView = (EditText) v.findViewById(R.id.time_edit);
				holder.timeView = timeView;

//				ImageView btn_holder = (ImageView) v.findViewById(R.id.drag_handle);
				ImageView btn_delete = (ImageView) v.findViewById(R.id.btnRemove);
//				holder.holderBtn = btn_holder;
				holder.deleteBtn = btn_delete;

				v.setTag(holder);
			}

			final ViewHolder holder = (ViewHolder) v.getTag();
			String stepText = getItem(position).step;
			holder.stepView.setText(stepText);
			String timeText = getItem(position).time;
			holder.timeView.setText(timeText);
			ed_step = holder.stepView;
			ed_time = holder.timeView;

			//holder.imageView.setBackgroundColor(Color.WHITE);
			//.setImageURI(getItem(position).image);
			
			int end = getItem(position).image.length();
			String result = getItem(position).image.substring(1,end-1);
			Log.d("resultresultresultresultresultresultresult", result);

			String URL_img_address = "http://119.205.252.224:8000/get/image/" + result;
			
			FudingAPI API = FudingAPI.getInstance(AddPostingActivity.this);
			holder.imageView.setImageUrl(URL_img_address, API.getmImageLoader());
			
			if (flag) {
//				holder.holderBtn.setVisibility(View.VISIBLE);
				holder.deleteBtn.setVisibility(View.VISIBLE);
			} else {
//				holder.holderBtn.setVisibility(View.GONE);
				holder.deleteBtn.setVisibility(View.GONE);
			}
			return v;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	private int uploadFile(String sourceFileUri, String step, String time,
			int step_num, int finish_flag) {

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

				// if (finish_flag < 2) {
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"wc_index_num\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.write((step_num + "").getBytes("utf-8"));
				Log.d("image upload wc_index_num", step_num + "");
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"wc_text\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.write(step.getBytes("utf-8"));
				Log.d("image upload wc_text", step + "-");
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"wc_times\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.write((time + "").getBytes("utf-8"));
				Log.d("image upload wc_times", time + "-");

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"wc_finish_index\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.write((finish_flag + "").getBytes("utf-8"));
				Log.d("image upload wc_finish_index", "" + finish_flag);
				dos.writeBytes(lineEnd); 

				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"user_name\"" + lineEnd);
				dos.writeBytes(lineEnd);
				SharedPreferences pref = getSharedPreferences("pref",
						MODE_PRIVATE);
				String user_name = pref.getString("user_name", "ayoung");
				dos.write(user_name.getBytes("utf-8"));
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"wt_index\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				String imageId = pref.getString("imageURL_index", "1");
				dos.write((imageId + "").getBytes("utf-8"));
				Log.d("image upload wt_index", imageId + "");
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"wc_photo_name\""
						+ lineEnd);
				dos.writeBytes(lineEnd);
				String name = imageId + "_" + content_index + ".jpg";
				Log.d("image upload wc_photo_name", "" + name);

				dos.write(name.getBytes("utf-8"));
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";fileName=\""
						+ fileName + "\"" + lineEnd);
				Log.d("image upload fileName", "" + fileName);
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
				final String serverResponseMessage = conn.getResponseMessage();

				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(
							conn.getInputStream()));

					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						System.out.println(inputLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Log.d("multipart", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
				if (serverResponseCode == 200) {
					getServerImgeURL(step_num, imageId, step, time);
				}
				
			} catch (Exception e) {

				// dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(AddPostingActivity.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
			}
			// dialog.dismiss();
			return serverResponseCode;

		} // End else block

	}
		
	private void getServerImgeURL(final int step_num2, final String imageId, final String content, final String time) {
		String URL_address = "http://119.205.252.224:8000/get/content/url/";

		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					Log.d("volley", "content!!! result    : " + result);
					 
					mItem.add(new Item(result, content+"", time));
					Log.d("volley", result+", "+content+", "+time);
					adapter.notifyDataSetChanged();
					
					
				} catch (Exception e) {
				}
			}
		};

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(AddPostingActivity.this,
						"네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
			}
		};

		StringRequest myReq = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
				String imageId = pref.getString("imageURL_index", "1"); 

				params.put("wc_index_num", step_num2+"");
				params.put("wt_index", imageId); 
				return params;
			};
		};
		mQueue2.add(myReq);

	}







	private void finishContent(final int step_num) {

		String URL_address = "http://119.205.252.224:8000/upload/write/frame/";

		RequestQueue mQueue2;
		mQueue2 = Volley.newRequestQueue(this);
		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					Log.d("volley", "step1 result    : " + result);
				} catch (Exception e) {
				}
			}
		};

		com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(AddPostingActivity.this,
						"네트워크상태가좋지 않습니다.잠시만 기다려주세요.", Toast.LENGTH_LONG).show();
			}
		};

		StringRequest myReq = new StringRequest(Method.POST, URL_address,
				listener, errorListener) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();

				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
				String imageId = pref.getString("imageURL_index", "1");
				String username = pref.getString("user_name", "1");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault());
				Date date = new Date();
				String strDate = dateFormat.format(date);
				Log.d("tag", strDate);

				params.put("user_name", username);
				params.put("wt_index", imageId);
				params.put("wc_total", step_num + "");
				params.put("wc_date", strDate);
				return params;
			};
		};
		mQueue2.add(myReq);

	}

	private void makepicture() {

		final LinearLayout change_picture = (LinearLayout) View.inflate(this,
				R.layout.dialog_move_picture, null);

		final ArrayList<String> arrList = new ArrayList<String>();
		arrList.add(getString(R.string.settings_profile_album));
		arrList.add(getString(R.string.settings_profile_camera));
		arrList.add(getString(R.string.settings_profile_delete));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, arrList);
		ListView choice_piture = (ListView) change_picture
				.findViewById(R.id.listView1);
		choice_piture.setAdapter(adapter);

		final Dialog pictureDialog = new Dialog(this);
		pictureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pictureDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		pictureDialog.setContentView(change_picture);
		pictureDialog.show();

		choice_piture.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int position, long id) {

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

				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mImageCaptureUri);

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

			Log.d("url",
					"mImageCaptureUri.getPath() : "
							+ mImageCaptureUri.getPath());
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
				dialog_image.setImageBitmap(bitm);
			}
			dialog_image.setBackgroundColor(Color.WHITE);
			dialog_image.setImageURI(mImageCaptureUri);
			uri_copy = mImageCaptureUri;
			dialog_image.setScaleType(ImageView.ScaleType.CENTER_CROP); 

			// 각 url값을 배열에 저장
			Log.d("url", "content_position : " + content_position);
			imageList[content_position] = mImageCaptureUri;
			Log.d("url crop url final", "" + imageList[content_position]);
			break;
		}
		}

	}

	private Uri createSaveCropFile() {
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		String imageId = pref.getString("imageURL_index", "1");

		String url = imageId + "_" + content_position + ".jpg";
		Uri uri = Uri.fromFile(new File(getExternalFilesDir(null), url));
		// Log.d(TAG, "uri : "+uri);

		Log.d("url createSaveCropFile", url + "  ");
		return uri;
		// 유알아이도 있공. 스트링 유알엘도있음.
	}

	private File getImageFile(Uri uri) {

		Log.d("url getImageFile", uri + "");

		String[] projection = { MediaStore.Images.Media.DATA };
		if (uri == null) {
			uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		}

		Cursor mCursor = getContentResolver().query(uri, projection, null,
				null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
		if (mCursor == null || mCursor.getCount() < 1) {
			return null; // no cursor or no record
		}
		int column_index = mCursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
