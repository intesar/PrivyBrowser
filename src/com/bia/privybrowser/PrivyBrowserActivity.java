package com.bia.privybrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PrivyBrowserActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String TAG = "PrivyBrowserActivity";

	private boolean initialized;
	private WebView page;
	private EditText url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate()");

		initialize();

	}

	private void initialize() {
		Log.d(TAG, "initialized " + initialized);
		if (initialized)
			return;

		setContentView(R.layout.main);

		url = (EditText) this.findViewById(R.id.url);

		page = (WebView) this.findViewById(R.id.page);
		page.setWebViewClient(new SimpleWebViewClient());
		WebSettings setting = page.getSettings();
		setting.setJavaScriptEnabled(Boolean.TRUE);
		setting.setDomStorageEnabled(Boolean.TRUE);
		setting.setSavePassword(Boolean.FALSE);
		setting.setBuiltInZoomControls(Boolean.FALSE);

		page.loadUrl("http://www.google.com");

		loadUrl();

		page.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						loadUrl();
					}
				}
				return true;
			}
		});

		Button go = (Button) this.findViewById(R.id.go);

		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadUrl();
			}
		});

		initialized = true;
	}

	private void loadUrl() {

		String _url = url.getText().toString().trim();
		if (_url.length() > 3) {
			if (!_url.startsWith("http")) {
				_url = "http://" + _url;
				url.setText(_url);
			}

			Context context = getApplicationContext();
			CharSequence text = "loading...";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();

			page.loadUrl(_url);

		}

	}

	@Override
	public void onPause() {
		super.onPause();
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK && page.canGoBack()) {
			Log.d(TAG, "goBack");
			page.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class SimpleWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String _url) {
			url.setText(_url);
			view.loadUrl(_url);
			return true;
		}
	}

}