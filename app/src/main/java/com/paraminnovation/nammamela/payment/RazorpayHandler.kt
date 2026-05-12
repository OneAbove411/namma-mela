package com.paraminnovation.nammamela.payment

import android.app.Activity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

/**
 * Thin wrapper around Razorpay Checkout SDK for TEST MODE only.
 *
 * To use:
 * 1. Sign up at razorpay.com (free, no KYC needed for test mode).
 * 2. Dashboard → Settings → API Keys → Generate Test Key.
 * 3. Paste the key (starts with `rzp_test_`) into the Manager panel's
 *    "Razorpay Test Key" field. It is persisted in DataStore.
 *
 * No real money moves. Razorpay's test mode accepts dummy card
 * 4111 1111 1111 1111 with any future expiry and any CVV.
 *
 * The Activity that starts Checkout MUST implement PaymentResultListener.
 */
object RazorpayHandler {

    fun preload(activity: Activity) {
        Checkout.preload(activity.applicationContext)
    }

    fun startPayment(
        activity: Activity,
        testKey: String,
        amountInPaise: Int,
        description: String,
        customerName: String?,
        receiptId: String
    ) {
        val checkout = Checkout()
        checkout.setKeyID(testKey)
        val options = JSONObject().apply {
            put("name", "Namma Mela")
            put("description", description)
            put("currency", "INR")
            put("amount", amountInPaise) // smallest unit
            put("receipt", receiptId)
            put("prefill", JSONObject().apply {
                if (!customerName.isNullOrBlank()) put("name", customerName)
            })
            put("theme", JSONObject().put("color", "#E23744"))
        }
        try {
            checkout.open(activity, options)
        } catch (e: Exception) {
            // Bubble up to the activity to show a toast.
            throw e
        }
    }
}
