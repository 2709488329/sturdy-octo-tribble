package com.puppyadopt.webapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

        // Ensure the game timer keeps running (approximately) when backgrounded
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Resume timer if we were paused
                if (isPaused) {
                    view.evaluateJavascript(
                        "if(typeof startTimer === 'function') startTimer();", null
                    );
                    isPaused = false;
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        // Load the game from assets
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
        // Pause the game timer to reduce CPU
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
        // The timer will be restarted in onPageFinished when we reload
        // Actually to keep the game state, just let the timer run
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

    /**
     * JavaScript interface for Android-to-JS communication.
     * Not strictly needed but available for future features like vibration, etc.
     */
    public class WebAppInterface {
        @JavascriptInterface
        public void showToast(String message) {
            handler.post(() ->
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show()
            );
        }

        @JavascriptInterface
        public void vibrate(long duration) {
            android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(duration);
            }
        }
    }
}
