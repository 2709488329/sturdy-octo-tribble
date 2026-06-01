package com.puppyadopt.webapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;
    private final Handler handler = new Handler();
    private boolean isPaused = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);

        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isPaused) {
                    view.evaluateJavascript(
                        "if(typeof startTimer === 'function') startTimer();", null
                    );
                    isPaused = false;
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        webView.evaluateJavascript(
            "if(typeof window._pause !== 'undefined') window._pause();", null
        );
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
        webView.evaluateJavascript(
            "if(typeof window._resume !== 'undefined') window._resume();", null
        );
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void showToast(String message) {
            handler.post(() ->
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show()
            );
        }

        @JavascriptInterface
        public void vibrate(long duration) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(duration);
            }
        }
    }
}
