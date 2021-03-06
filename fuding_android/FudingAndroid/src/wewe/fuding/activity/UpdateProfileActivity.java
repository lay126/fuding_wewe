package wewe.fuding.activity;

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
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wewe.fuding.FudingAPI;
import wewe.fuding.widget.Fragment_Profile;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class UpdateProfileActivity extends Activity {
	private static final int PICK_FROM_CAMERA = 0;
	private static final int PICK_FROM_ALBUM = 1;
	private static final int CROP_FROM_CAMERA = 2;
	Uri mImageCaptureUri;
	Dialog dialog;
	NetworkImageView profile_image; 
	String upLoadServerUri;
	int serverResponseCode = 0;
	EditText info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		info = (EditText)findViewById(R.id.edit_info);
		profile_image = (NetworkImageView)findViewById(R.id.profile_image);
		
		ImageView logout_btn = (ImageView)findViewById(R.id.logout_btn);
		logout_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("login_check", "false"); //로그아웃 세션 저장 
				editor.commit();
				startActivity(new Intent(UpdateProfileActivity.this, LoginActivity.class));
				FudingMainActivity aActivity = (FudingMainActivity)FudingMainActivity.FudingMainActivity;
				aActivity.finish();
				finish();
			}
			
		});
		
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String myProfile = pref.getString("myImage", "");
        String URL_img_address = "http://119.205.252.224:8000/get/image/"+myProfile;
		FudingAPI API = FudingAPI.getInstance(UpdateProfileActivity.this);
		profile_image.setImageUrl(URL_img_address, API.getmImageLoader());

		info.setText(pref.getString("user_info", ""));
		
		profile_image.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				makepicture();
 			}
 		});
		
		ImageView ok_btn = (ImageView)findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
				Toast.makeText(UpdateProfileActivity.this, "프로필 수정을 완료했습니다.", Toast.LENGTH_LONG).show();
				
				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();

				// 이미지 미리 변경 
				if (mImageCaptureUri!=null) {
					Fragment_Profile.photo.setBackgroundColor(Color.WHITE);
					Fragment_Profile.photo.setImageURI(mImageCaptureUri);
					Fragment_Profile.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);	//가운데 자름 (길쭉하게 자른 경우)
				}

				editor.putString("user_info", info.getText().toString());
		        editor.commit();
				
				// 변경된 프로필 내용 서버에 전송
				// 이미지 주소, 이름, 아이디, 등등..
 				upLoadServerUri = "http://119.205.252.224:8000/update/user/"; 
		        uploadProfile(mImageCaptureUri.getPath(), info.getText().toString());
				finish();
 			}
 		});
		
		
		ImageView cancel_btn = (ImageView)findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(new View.OnClickListener() {
	 			@Override
	 			public void onClick(View v) {
	 				Toast.makeText(UpdateProfileActivity.this, "프로필 수정을 취소했습니다.", Toast.LENGTH_LONG).show();
	 				finish();
	 			}
	 		});
	}

	protected int uploadProfile(String sourceFileUri, String info) {

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
			return 0;

		} else {
			try {
				
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
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
					
				SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		        String user_name = pref.getString("user_name", "ayoung");
				
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"user_name\""+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.write((user_name).getBytes("utf-8")); 
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"user_info\""+ lineEnd);
				dos.writeBytes(lineEnd);
				dos.write((info+"").getBytes("utf-8")); 
				dos.writeBytes(lineEnd);

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"image_name\"" + lineEnd);
				dos.writeBytes(lineEnd);
		        String name = user_name + ".jpg";
		        Log.d("image upload wc_photo_name", "" + name);
				
				dos.write(name.getBytes("utf-8"));
				dos.writeBytes(lineEnd);
				
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";fileName=\"" + fileName + "\"" + lineEnd);
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

				 Log.d("multipart", "HTTP Response is : " + serverResponseMessage +": " + serverResponseCode);
				 if (serverResponseCode == 200) {
					 getMyImage();
				 }

			} catch (Exception e) {

				//dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(UpdateProfileActivity.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
					}
					 
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			//dialog.dismiss();
			return serverResponseCode;

		} 
	}

	private void getMyImage() {
		String URL_address = "http://119.205.252.224:8000/get/user/profile/";

		RequestQueue mQueue;
		mQueue = Volley.newRequestQueue(this);

		Listener<String> listener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// to make data available
				String arrRes = "{'response':" + response + "}";
				Log.d("detail_update_re", arrRes);

				JSONObject jobject = null;
				try {
					jobject = new JSONObject(arrRes);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				JSONArray jarray = null;
				try {
					jarray = jobject.getJSONArray("response");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				try {
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jsonFrame = (JSONObject) jarray.get(i);
						String image_url = jsonFrame.getString("user_img");
						SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
						SharedPreferences.Editor editor = pref.edit();
						editor.putString("myImage", image_url+"");
				        editor.commit();
						Log.d("url_put", image_url);
						String URL_img_address = "http://119.205.252.224:8000/get/image/"+ image_url;
						FudingAPI API = FudingAPI.getInstance(UpdateProfileActivity.this);
						profile_image.setImageUrl(URL_img_address, API.getmImageLoader());
						
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};

		ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				Toast.makeText(UpdateProfileActivity.this, "상세페이지 : 네트워크상태가좋지 않습니다.잠시만 기다려주세요.",
						Toast.LENGTH_LONG).show();
			}
		};

		StringRequest req = new StringRequest(Method.POST, URL_address, listener, errorListener) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
					String index = pref.getString("user_name", "");
					
					params.put("user_name", index+"");

					return params;
				}
		};
		mQueue.add(req);
	}

	
	
	protected void makepicture() {


		final LinearLayout change_picture = (LinearLayout) View.inflate(UpdateProfileActivity.this, R.layout.dialog_move_picture, null);

		final ArrayList<String> arrList = new ArrayList<String>();
		arrList.add(getString(R.string.settings_profile_album));
		arrList.add(getString(R.string.settings_profile_camera));
		arrList.add(getString(R.string.settings_profile_delete));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_list_item_1, arrList);
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
					//imageDelete();
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case PICK_FROM_ALBUM: {
			
			mImageCaptureUri = data.getData();
			File original_file = getImageFile(mImageCaptureUri);
			mImageCaptureUri = createSaveCropFile();
			File cpoy_file = new File(mImageCaptureUri.getPath());

			Log.d("url", "mImageCaptureUri.getPath() : "+mImageCaptureUri.getPath());
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
				profile_image.setImageBitmap(bitm);
			}
			profile_image.setBackgroundColor(Color.WHITE);
			profile_image.setImageURI(mImageCaptureUri);
			profile_image.setScaleType(ImageView.ScaleType.CENTER_CROP);	//가운데 자름 (길쭉하게 자른 경우)
			
			break;
		}
		}

	}

	private Uri createSaveCropFile() {
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String user_name = pref.getString("user_name", "ayoung");
		String url = user_name+".jpg";
		Uri uri = Uri.fromFile(new File(getExternalFilesDir(null), url));
		//Log.d(TAG, "uri : "+uri);
		
		Log.d("url createSaveCropFile", url+"  ");
		return uri;
		// 유알아이도 있공. 스트링 유알엘도있음.
	}

	private File getImageFile(Uri uri) {
		
		Log.d("url getImageFile", uri+"");
		
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
