package assignment.home.taglocation.view.ui

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import assignment.home.taglocation.R
import assignment.home.taglocation.databinding.FragmentLocationMarkBinding
import assignment.home.taglocation.extras.Constants
import assignment.home.taglocation.models.TagLocationModel
import assignment.home.taglocation.viewmodel.MarkLocationFragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MarkLocationFragment : BottomSheetDialogFragment(), View.OnClickListener {

    lateinit var propertyCoordinates: String

    lateinit var fragmentLocationMarkBinding: FragmentLocationMarkBinding

    val markLocationFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(MarkLocationFragmentViewModel::class.java)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            fragmentLocationMarkBinding.fab.id -> {

                finishFragment()
            }
            fragmentLocationMarkBinding.submit.id -> {
//                Toast.makeText(
//                    context,
//                    "Property Name Saved ${fragmentLocationMarkBinding.propertyNameEdittext.text} with Coordinates",
//                    Toast.LENGTH_SHORT
//                ).show()
                runBlocking {
                    launch {
                        savePropertyName()
                    }
                }
            }
        }

    }

    /**
     * function to finsih the current fragment
     */
    private fun finishFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    /**
     * Function to save the Property Details to the Db
     */
    private fun savePropertyName() {
        fragmentLocationMarkBinding.propertyNameEdittext?.let {
            if (it.text.toString().isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    "Property Name field is empty ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Property Name Saved ${fragmentLocationMarkBinding.propertyNameEdittext.text}",
                    Toast.LENGTH_SHORT
                ).show()
                MarkLocationFragmentViewModel.tagLocationModel?.propertyName = it.toString()
                MarkLocationFragmentViewModel.tagLocationModel?.propertyCoordinates =
                    fragmentLocationMarkBinding.propertyCoordinatesEdittext.toString()
                markLocationFragmentViewModel.saveRealmObjects(TagLocationModel())
                finishFragment()
            }

        } ?: kotlin.run {
            Toast.makeText(context, "Enter Property Name", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLocationMarkBinding =
            FragmentLocationMarkBinding.inflate(inflater, container, false)
        val view = fragmentLocationMarkBinding.root

        if (savedInstanceState != null) {
            fragmentLocationMarkBinding.propertyCoordinatesEdittext.setText(
                savedInstanceState.getString(
                    Constants.PROPERTY_COORDINATES
                )
            )
            fragmentLocationMarkBinding.propertyCoordinatesEdittext.setText(
                savedInstanceState.getString(
                    Constants.PROPERTY_NAME
                )
            )

        } else {
            fragmentLocationMarkBinding.propertyCoordinatesEdittext.setText(propertyCoordinates)
        }

        MarkLocationFragmentViewModel.tagLocationModel?.propertyCoordinates = propertyCoordinates
        fragmentLocationMarkBinding.fab.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLocationMarkBinding.submit.setOnClickListener(this)

        fragmentLocationMarkBinding.propertyNameEdittext.addTextChangedListener {
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    updateSaveButton()
                }

                override fun afterTextChanged(p0: Editable?) {
                    TODO("Not yet implemented")
                }

            }

        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme_UserInfo
    }

    /**
     * Function to enable/Disable Submit button
     */
    private fun updateSaveButton() {
        try {
            fragmentLocationMarkBinding.submit.isEnabled =
                !TextUtils.isEmpty(fragmentLocationMarkBinding.propertyNameEdittext.text)
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        fragmentLocationMarkBinding.propertyNameEdittext.let {
            outState.putString(Constants.PROPERTY_NAME, it.toString())
            outState.putString(
                Constants.PROPERTY_COORDINATES,
                fragmentLocationMarkBinding.propertyCoordinatesEdittext.toString()
            )
        }
        super.onSaveInstanceState(outState)
    }
}