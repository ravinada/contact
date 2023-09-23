package com.ravinada.contact.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    /**
     * Hides the keyboard form the screen.
     */
    fun hideKeyboard() {
        val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(currentFocus?.rootView?.windowToken, 0)
    }

    /**
     * Displays a toast.
     * @param message String
     */
    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Display a SnackBar
     *
     * @param view
     * @param message
     * @param length
     */
    fun snackbar(
        view: View,
        message: CharSequence,
        length: Int,
    ) {
        Snackbar.make(view, message, length).show()
    }
}
