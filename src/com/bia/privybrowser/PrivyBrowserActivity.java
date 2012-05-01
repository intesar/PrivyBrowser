package com.bia.privybrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PrivyBrowserActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String TAG = "PrivyBrowserActivity";

	// private boolean initialized;
	private WebView webView;
	private EditText urlBox;
	private String url = "http://www.google.com";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate()");

		initialize();
		webView.loadUrl(url);
	}

	private void initialize() {
		setContentView(R.layout.main);

		webView = (WebView) this.findViewById(R.id.page);

		webView.setWebViewClient(new SimpleWebViewClient());

		WebSettings setting = webView.getSettings();
		setting.setJavaScriptEnabled(Boolean.TRUE);
		setting.setDomStorageEnabled(Boolean.TRUE);
		setting.setSavePassword(Boolean.FALSE);
		setting.setBuiltInZoomControls(Boolean.FALSE);

		webView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						loadUrl();
						return true;
					}
				}
				return false;
			}
		});

		urlBox = (EditText) this.findViewById(R.id.url);
		urlBox.setImeOptions(EditorInfo.IME_ACTION_GO);
		urlBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					loadUrl();
					return true;
				}
				return false;
			}
		});

		Button go = (Button) this.findViewById(R.id.go);

		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadUrl();
			}
		});

	}

	private void loadUrl() {

		String _url = urlBox.getText().toString().trim();
		if (_url.length() > 3) {
			if (!_url.startsWith("http")) {
				_url = "http://" + _url;
				urlBox.setText(_url);
			}
			webView.loadUrl(_url);
		}

	}

	// @Override
	public void onPause1() {
		super.onPause();
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			Log.d(TAG, "goBack");
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class SimpleWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String _url) {
			Context context = PrivyBrowserActivity.this.getApplicationContext();
			CharSequence text = "loading...";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			urlBox.setText(_url);
			view.loadUrl(_url);
			return false;
		}

	}

}