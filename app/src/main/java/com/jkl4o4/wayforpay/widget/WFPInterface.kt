package com.jkl4o4.wayforpay.widget

import android.webkit.JavascriptInterface

interface WFPInterface {

    @JavascriptInterface
     fun onPaymentSuccess()

    @JavascriptInterface
    fun onPaymentFailure()

    @JavascriptInterface
    fun onPaymentClose()
}
