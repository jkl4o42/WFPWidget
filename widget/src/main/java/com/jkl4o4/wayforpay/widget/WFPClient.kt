package com.jkl4o4.wayforpay.widget

import java.math.BigDecimal
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class WFPClient(
    private var secretKey: String,
    var request: WFPModel
) {

    fun addProduct(name: String, price: BigDecimal, count: Int) {
        request.productName.add(name)
        request.productPrice.add(price)
        request.productCount.add(count)
        request.amount += price * BigDecimal(count)
    }

    fun deleteProducts() {
        request.productName.clear()
        request.productPrice.clear()
        request.productCount.clear()
        request.amount = BigDecimal.ZERO
    }

    fun generateSignature() {
        request.merchantSignature = generateSignatureString().hmac(key = secretKey)
    }

    private fun generateSignatureString(): String {
        val string = StringBuilder()
        addSignItem(string, request.merchantAccount)
        addSignItem(string, request.merchantDomainName)
        addSignItem(string, request.orderReference)
        addSignItem(string, request.orderDate.toString())
        addSignItem(string, request.amount.toString())
        addSignItem(string, request.currency)
        request.productName.forEach { name ->
            addSignItem(string, name)
        }
        request.productCount.forEach { count ->
            addSignItem(string, count.toString())
        }
        request.productPrice.forEach { price ->
            addSignItem(string, price.toString())
        }
        string.deleteCharAt(string.lastIndex)
        return string.toString()
    }

    private fun addSignItem(string: StringBuilder, value: String) {
        string.append(value).append(";")
    }
}

fun String.hmac(key: String): String {
    val algorithm = "HmacMD5"
    val keySpec = SecretKeySpec(key.toByteArray(), algorithm)
    val mac = Mac.getInstance(algorithm)
    mac.init(keySpec)
    val macBytes = mac.doFinal(this.toByteArray())
    return macBytes.joinToString("") { "%02x".format(it) }
}