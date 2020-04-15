package com.fakhrimf.e_auctionlatihan.ui.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.databinding.EditProfileFragmentBinding
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.IMAGE_REQUEST_CODE
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import kotlinx.android.synthetic.main.edit_profile_fragment.*

class EditProfileFragment(private var model: ProfileModel) : BaseFragment() {

    companion object {
        fun newInstance(profileModel: ProfileModel) = EditProfileFragment(profileModel)
    }

    var path: Uri? = null

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(EditProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            setVariable(BR.model, this@EditProfileFragment.model)
            executePendingBindings()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && data != null && data.data != null) {
            path = data.data!!
            val bitmap = vm.getImageBitmap(requireActivity().contentResolver, path!!)
            dpImage.setImageBitmap(bitmap)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        btnAddPic.setOnClickListener {
            startActivityForResult(vm.getImageIntent(), IMAGE_REQUEST_CODE)
        }
        updateBtn.setOnClickListener {
            edit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                false
            }
            else -> false
        }
    }

    private fun edit() {
        if (!isEmpty()) {
            val sweet = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE).apply {
                titleText = getString(R.string.loading)
                showCancelButton(false)
                setCancelable(false)
            }
            sweet.show()
            vm.editProfile(getModel(), path).observe(viewLifecycleOwner, Observer {
                if (it.success) {
                    sweet.apply {
                        changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        titleText = getString(R.string.success)
                        confirmText = getString(R.string.ok)
                        setConfirmClickListener {
                            requireActivity().onBackPressed()
                        }
                    }
                }
            })
        }
    }

    private fun isEmpty() : Boolean {
        var check = false
        if (nameInput.text.toString() == "") {
            nameInput.error = getString(R.string.name_error)
            check = true
        }
        if (originInput.text.toString() == "") {
            originInput.error = getString(R.string.origin_error)
            check = true
        }
        return check
    }

    private fun getModel(): ProfileModel {
        model.name = nameInput.text.toString()
        if (path != null) model.image = path.toString()
        model.gender = spinnerGenderEdit.selectedItem.toString()
        model.origin = originInput.text.toString()
        model.info = infoInput.text.toString()
        showInfo(model.toString())
        return model
    }
}
