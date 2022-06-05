package cz.mendelu.xpaseka.bodybeat.architecture

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<B : ViewBinding,
        VM : ViewModel>(private val viewModelClass: KClass<VM>) : Fragment(),
    LifecycleObserver {
    protected abstract val bindingInflater: (LayoutInflater) -> B
    private var baseBinding: ViewBinding? = null
    protected val binding: B
        get() = baseBinding as B
    val viewModel: VM by lazy { getViewModel(null, viewModelClass) }
    abstract fun initViews()

    abstract fun onActivityCreated()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseBinding = bindingInflater(inflater)
        val toolbar = (requireActivity() as AppCompatActivity?)?.supportActionBar
        initViews()
        return baseBinding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        baseBinding = null
    }

    fun finishCurrentFragment(){
        requireActivity().runOnUiThread {
            hideKeyboard()
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun hideKeyboard() {
        activity?.let {
            val inputManager: InputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusedView: View? = requireActivity().currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(
                    currentFocusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    fun setToolbarTitle(title: String){
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = title
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onCreated(){
        onActivityCreated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(this)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.removeObserver(this)
    }

    fun showSnackbar(text: String){
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }
}