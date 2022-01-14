
import android.webkit.WebView;

WebView webView = (WebView) findViewById(R.id.webview);
webView.getSettings().setAllowFileAccess(true); // Sensitive
webView.getSettings().setAllowContentAccess(true); // Sensitive
