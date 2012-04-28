package com.bia.privybrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class PrivyBrowserActivity extends Activity {
	/** Called when the activity is first created. */

	private WebView page;
	private EditText url;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		url = (EditText) this.findViewById(R.id.url);

		page = (WebView) this.findViewById(R.id.page);
		page.setWebViewClient(new SimpleWebViewClient());
		WebSettings setting = page.getSettings();
		setting.setJavaScriptEnabled(Boolean.TRUE);
		setting.setDomStorageEnabled(Boolean.TRUE);
		setting.setSavePassword(Boolean.FALSE);
		setting.setBuiltInZoomControls(Boolean.TRUE);
		
		page.loadUrl("http://www.google.com");

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

	}

	private void loadUrl() {
		String _url = url.getText().toString().trim();
		if (!_url.startsWith("http")) {
			_url = "http://" + _url;
		}
		page.loadUrl(_url);
	}
	private class SimpleWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (page.canGoBack()) {
				page.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}