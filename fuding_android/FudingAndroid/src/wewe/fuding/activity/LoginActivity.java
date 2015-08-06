package wewe.fuding.activity;

import java.security.acl.Permission;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.CallbackManager;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.Settings;

public class LoginActivity extends Activity implements Permission {

	CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_login);

		// callbackManager = CallbackManager.Factory.create();
		// LoginButton loginButton = (LoginButton)
		// findViewById(R.id.login_button);
		// loginButton.registerCallback(callbackManager, new
		// FacebookCallback<LoginResult>() {
		// @Override
		// public void onSuccess(LoginResult loginResult) {
		// // App code
		// Toast.makeText(LoginActivity.this, "onSuccess", 0).show();
		// }
		//
		// @Override
		// public void onCancel() {
		// // App code
		// Toast.makeText(LoginActivity.this, "onCancel", 0).show();
		// }
		//
		// @Override
		// public void onError(FacebookException exception) {
		// // App code
		// Toast.makeText(LoginActivity.this, "onError", 0).show();
		// }
		// });
	}
	
	// 다은이코드
//	public void SkipLoginOnClick(View view) {
//		if (view.getId() == R.id.skip_login_button) {
//			Intent intent = new Intent(LoginActivity.this,
//					FudingMainActivity.class);
//			startActivity(intent);
//		}
//	}
//
//	private void facebookInit(Bundle savedInstanceState) {
//		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//
//		Session session = Session.getActiveSession();
//		if (session == null) {
//			if (savedInstanceState != null) {
//				session = Session.restoreSession(this, null, statusCallback,
//						savedInstanceState);
//				session.openForRead(new Session.OpenRequest(this)
//						.setPermissions(
//								Arrays.asList("public_profile", "read_stream",
//										"user_likes", "user_birthday",
//										"user_tagged_places")).setCallback(
//								statusCallback));
//				Log.d("savedInstanceState", "savedInstanceState");
//			}
//			if (session == null) {
//				session = new Session(this);
//				session.openForRead(new Session.OpenRequest(this)
//						.setPermissions(
//								Arrays.asList("public_profile", "read_stream",
//										"user_likes", "user_birthday",
//										"user_tagged_places", "email"))
//						.setCallback(statusCallback));
//				Log.d("savedInstanceState", "not savedInstanceState");
//			}
//
//			Session.setActiveSession(session);
//			// session.openForRead(new Session.OpenRequest(this)
//			// .setPermissions(
//			// Arrays.asList("public_profile", "read_stream",
//			// "user_likes", "user_birthday",
//			// "user_tagged_places")).setCallback(
//			// statusCallback));
//
//			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//				session.openForRead(new Session.OpenRequest(this)
//						.setCallback(statusCallback));
//			}
//		}
//
//		updateView();
//	}

}
