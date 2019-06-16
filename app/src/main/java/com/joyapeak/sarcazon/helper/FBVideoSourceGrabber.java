package com.joyapeak.sarcazon.helper;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by test on 10/11/2018.
 *
 * This class helps in getting a facebook source video url using its original url
 */

public class FBVideoSourceGrabber {

    private FBVideoSourceHandler mHandler;

    public interface FBVideoSourceHandler {
        void onFBVideoSourceUrlReady(String sourceUrl);
    }


    public FBVideoSourceGrabber() {}

    public static boolean isVideoSource(String videoUrl) {
        return videoUrl != null && videoUrl.toLowerCase().contains(".mp4");
    }


    public void setHandler(FBVideoSourceHandler handler) {
        mHandler = handler;
    }

    public void setupFBVideoGrabberWebView(Context context,  final WebView webView, String videoUrl) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.addJavascriptInterface(this, "FBDownloader");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onLoadResource(WebView view, String url)
            {
                webView.loadUrl("javascript:(function prepareVideo() { "
                        + "var el = document.querySelectorAll('div[data-sigil]');"
                        + "for(var i=0;i<el.length; i++)"
                        + "{"
                        + "var sigil = el[i].dataset.sigil;"
                        + "if(sigil.indexOf('inlineVideo') > -1){"
                        + "delete el[i].dataset.sigil;"
                        + "console.log(i);"
                        + "var jsonData = JSON.parse(el[i].dataset.store);"
                        + "el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');"
                        + "el[i].click();"
                        /*+ "FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");"*/
                        + "}" + "}" + "})()");

                webView.loadUrl("javascript:( window.onload=prepareVideo;"
                        + ")()");
            }
        });

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();

        webView.loadUrl(videoUrl);
    }

    @JavascriptInterface
    public void processVideo(final String vidData, final String vidID) {
        if (mHandler != null) {
            mHandler.onFBVideoSourceUrlReady(vidData);
        }
    }
}
