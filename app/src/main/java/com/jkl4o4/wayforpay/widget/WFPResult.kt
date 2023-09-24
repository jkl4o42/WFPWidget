package com.jkl4o4.wayforpay.widget

sealed class WFPResult {
    object Success : WFPResult()
    object Error : WFPResult()
    object Close : WFPResult()
}