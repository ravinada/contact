package com.ravinada.contact.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var _binding: ViewBinding? = null

    private var mJob = SupervisorJob()

    @Suppress("UNCHECKED_CAST")
    protected val binding: T
        get() = _binding as T
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        mJob = SupervisorJob()
    }

    override fun onStop() {
        super.onStop()
        mJob.cancel()
    }

    /**
     * Displays a toast
     *
     * @param message
     */
    fun toast(message: String?) {
        (activity as? BaseActivity)?.toast(message)
    }

    /**
     * Adds tap to dismiss behaviour to [View]
     * @receiver View
     */
    @SuppressLint("ClickableViewAccessibility")
    protected fun View.addTapToDismissBehaviour() {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        setOnTouchListener { view, event ->
            interceptTouchEvent(view, event)
        }
    }

    /**
     * Changes [TextInputLayout]'s end icon visibility
     * @receiver [TextInputLayout]
     * @param [hasFocus] True if [TextInputEditText] inside of [TextInputLayout] has focus
     * param [text] [TextInputEditText]'s text
     */
    fun TextInputLayout.changeEndIconVisibility(
        hasFocus: Boolean,
        text: Editable?
    ) {
        isEndIconVisible = hasFocus && text?.isNotEmpty() == true
    }

    /**
     * Intercepts [MotionEvent] triggered by [ViewGroup.setOnTouchListener]
     * @param event MotionEvent?
     * @param view View
     * @return Boolean True if the event has been consumed.
     */
    protected open fun interceptTouchEvent(view: View, event: MotionEvent?): Boolean {
        event?.let { ev ->
            if (ev.action == MotionEvent.ACTION_UP) {
                hideKeyboard()
                view.requestFocus()

                return true
            }
        }

        return false
    }

    /**
     * Hides keyboard from the screen.
     *
     */
    protected fun hideKeyboard() {
        (activity as? BaseActivity)?.hideKeyboard()
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
        length: Int = 3000,
    ) {
        val snack = Snackbar.make(view, message, length)
        snack.show()
    }

    /**
     * The best practice is to call this function when the lifecycle is initialized.
     * For example, onCreate in an Activity, or onViewCreated in a Fragment.
     * Otherwise, multiple repeating coroutines doing the same could be created and be executed at the same time.
     */
    protected fun <T> Flow<T>.collectLatestRepeatOnStarted(block: suspend (T) -> Unit) {
        launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectLatest { block(it) }
            }
        }
    }

    /**
     * Launches a new coroutine without blocking the current thread and returns a reference to the coroutine as a [Job].
     * The coroutine is cancelled when the resulting job is [cancelled][Job.cancel].
     */
    protected fun launch(block: suspend () -> Unit): Job {
        return viewLifecycleOwner.lifecycleScope.launch {
            block.invoke()
        }
    }
}
