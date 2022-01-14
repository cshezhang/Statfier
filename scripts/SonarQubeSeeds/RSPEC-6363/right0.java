
import android.webkit.WebView;

WebView webView = (WebView) findViewById(R.id.webview);
webView.getSettings().setAllowFileAccess(false);
webView.getSettings().setAllowContentAccess(false);
