package com.jkl4o4.wayforpay.widget

import java.math.BigDecimal

data class WFPModel(
    var merchantAuthType: String = "SimpleSignature",
    var merchantTransactionSecureType: String = "AUTO",
    var merchantAccount: String,
    var merchantDomainName: String,
    var orderReference: String,
    var orderDate: Long,
    var amount: BigDecimal = BigDecimal.ZERO,
    var currency: String = "UAH",
    var productName: MutableList<String> = mutableListOf(),
    var productPrice: MutableList<BigDecimal> = mutableListOf(),
    var productCount: MutableList<Int> = mutableListOf(),
    var clientFirstName: String,
    var clientLastName: String,
    var clientEmail: String,
    var clientPhone: String,
    var merchantSignature: String = "",
    var language: String = "UA"
) {
    override fun toString(): String {
        return """
            merchantAuthType: "$merchantAuthType",
            merchantTransactionSecureType: "$merchantTransactionSecureType",
            merchantAccount: "$merchantAccount",
            merchantDomainName: "$merchantDomainName",
            orderReference: "$orderReference",
            orderDate: "$orderDate",
            amount: "$amount",
            currency: "$currency",
            productName: [${productName.joinToString(",") {"\"$it\""}}],
            productPrice: $productPrice,
            productCount: $productCount,
            clientFirstName: "$clientFirstName",
            clientLastName: "$clientLastName",
            clientEmail: "$clientEmail",
            clientPhone: "$clientPhone",
            language: "$language",
            merchantSignature: "$merchantSignature"
        """.trimIndent()
    }
}