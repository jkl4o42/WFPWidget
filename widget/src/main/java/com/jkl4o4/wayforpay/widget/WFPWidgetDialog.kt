package com.jkl4o4.wayforpay.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment

class WFPWidgetDialog(
    private val client: WFPClient,
    private val callback: WFPCallbacks
) : DialogFragment() {

    private lateinit var wfpWebView: WebView

   private val jsCode = """
                    window.addEventListener("message", function(event) {
                        if (event.data == 'WfpWidgetEventApproved') {
                            window.android.onPaymentSuccess();
                        }
                        if (event.data == 'WfpWidgetEventDeclined') {
                            window.android.onPaymentFailure();
                        }
                        if (event.data == 'WfpWidgetEventClose') {
                            window.android.onPaymentClose();
                        }
                    }, false);
                """.trimIndent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wfp_dialog, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        wfpWebView = view.findViewById(R.id.wfpWebView)
        wfpWebView.apply {
            settings.apply {
                javaScriptEnabled = true
            }
        }

        wfpWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                wfpWebView.evaluateJavascript(jsCode, null)
            }
        }

        wfpWebView.addJavascriptInterface(object : WFPInterface {
            @JavascriptInterface
            override fun onPaymentSuccess() {
                callback.result(WFPResult.Success)
            }

            @JavascriptInterface
            override fun onPaymentFailure() {
                callback.result(WFPResult.Error)
            }

            @JavascriptInterface
            override fun onPaymentClose() {
                callback.result(WFPResult.Close)
            }

        }, "android")

        val formattedHtml = getHtmlWithDynamicValues(client.request.toString())

        if (formattedHtml == null) {
            dismiss()
            return
        }

        wfpWebView.loadDataWithBaseURL(null, formattedHtml, "text/html", "utf-8", null)
    }

    private fun getHtmlWithDynamicValues(
        description: String,
    ): String? {
        val htmlTemplate = activity?.assets?.open("wfp_widget.html")?.bufferedReader().use { it?.readText() }
        return htmlTemplate?.replace("{{description}}", description)
    }
}