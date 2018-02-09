package com.tricks4speed.skbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MySimpleBrowserActivity extends Activity implements
		OnClickListener {

	private WebView webView;
	private ImageButton bAddress, bKeyword;
	private Button bBack, bForward, bOption, bRefresh;
	private EditText addr, keyword;
	private String[] agents = {"Mozilla/5.0", "AppleWebKit/537.36", "Chrome/60.0.3112.116", "Mobile Safari/537.36"};
	private WebSettings webSettings;

	// for setting WebView Client to webView rather than opera browser
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// progresss Bar initialisation
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.main);
		initialise();


		// to set go back and go forward
		webView.canGoBack();
		webView.canGoForward();

		// enable javascript for webview and flash
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		//webSettings.setUserAgentString("Mozilla/5.0");
		//webSettings.setUserAgentString("AppleWebKit/537.36");
		//webSettings.setUserAgentString("Chrome/60.0.3112.116");
		//webSettings.setUserAgentString("Mobile Safari/537.36");

		// setting new WebViewClient so at to prevent opera from opening the
		// site
		webView.setWebViewClient(new MyWebViewClient());

		bAddress.setOnClickListener(this);
		bKeyword.setOnClickListener(this);

		bBack.setOnClickListener(this);
		bForward.setOnClickListener(this);
		bOption.setOnClickListener(this);
		bRefresh.setOnClickListener(this);

		// setTitle(webView.getTitle());

		Intent intent = getIntent();

		String site = intent.getStringExtra("Site");
		if(site != null) {
			addr.setText(site);
			bAddress.performClick();
		}

	}

	public void initialise() {
		webView = (WebView) findViewById(R.id.wvContent);

		bAddress = (ImageButton) findViewById(R.id.bAddr);
		bKeyword = (ImageButton) findViewById(R.id.bKeyword);
		bBack = (Button) findViewById(R.id.Back);
		bForward = (Button) findViewById(R.id.Forward);
		bOption = (Button) findViewById(R.id.Options);
		bRefresh = (Button) findViewById(R.id.Refresh);

		addr = (EditText) findViewById(R.id.etAddr);
		keyword = (EditText) findViewById(R.id.etKeyword);

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		try {

			// <<<<<<<<<<<<<<<<<PROGRESS BAR>>>>>>>>>>>>
			// set a webChromeClient to track progress
			webView.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					// update the progressBar
					MySimpleBrowserActivity.this.setTitle("Loading...");
					MySimpleBrowserActivity.this.setProgress(progress * 100);
					
					//set the title of the layout
					if(progress == 100)
						MySimpleBrowserActivity.this.setTitle(R.string.app_name);
				}
			});
			
			

			switch (v.getId()) {
			case R.id.bAddr:
				String url = addr.getText().toString();
				if (url != "") {
					url = URLUtil.isValidUrl(url) ? addr.getText().toString()   : "http://" + addr.getText().toString();
					webView.loadUrl(url);
					addr.setText(url);
					webView.requestFocus();
				}
				break;
			case R.id.bKeyword:
				String key = keyword.getText().toString();
				if (key != "") {
					key = "http://www.google.com/search?q="+ keyword.getText().toString();
					webView.loadUrl(key);
					addr.setText(key);
					webView.requestFocus();
				}
				break;

			case R.id.Back:
				webView.goBack();
				break;

			case R.id.Forward:
				webView.goForward();
				break;

			case R.id.Options:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(true);

				builder.setItems(agents, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						webSettings.setUserAgentString(agents[i]);
						Toast.makeText(getApplicationContext(), "Set user agent: " + agents[i], Toast.LENGTH_SHORT).show();
						webView.reload();
					}
				});
				builder.show();
				break;

			case R.id.Refresh:
				webView.reload();
				break;

			}

		} catch (Exception e) {
			// TODO: handle exception]
			e.printStackTrace();
		}

	}

}