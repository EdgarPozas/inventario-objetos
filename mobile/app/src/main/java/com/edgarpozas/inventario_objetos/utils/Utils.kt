package com.edgarpozas.inventario_objetos.utils

import android.content.Context
import android.net.ConnectivityManager
import java.util.regex.Matcher
import java.util.regex.Pattern

class Utils {
    companion object{
        fun isEmailValid(email: String?): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(email)
            return matcher.matches()
        }
        fun isNetworkAvailable(context: Context?):Boolean{
            val conManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val internetInfo =conManager.activeNetworkInfo
            return internetInfo!=null && internetInfo.isConnected
        }
    }
}