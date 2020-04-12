package com.fakhrimf.e_auctionlatihan.ui.getadmincode

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.e_auctionlatihan.HomeActivity
import com.fakhrimf.e_auctionlatihan.LoginActivity
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import kotlinx.android.synthetic.main.get_admin_code_fragment.*

class GetAdminCodeFragment(private val model: ProfileModel) : BaseFragment() {

    companion object {
        fun newInstance(model: ProfileModel) = GetAdminCodeFragment(model)
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(GetAdminCodeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.get_admin_code_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionBtn.setOnClickListener {
            vm.getAdminCode().observe(viewLifecycleOwner, Observer {
                if (codeInput.text.toString() == it) {
                    register()
                } else {
                    showError(getString(R.string.code_error))
                }
            })
        }
    }

    private fun register() {
        progressBarAdminCode.visibility = View.VISIBLE
        vm.register(requireContext(), model).observe(viewLifecycleOwner, Observer {
            if (it.success) {
                showSuccess(getString(R.string.logging_you_in))
                vm.login(requireContext(), viewLifecycleOwner, model).observe(viewLifecycleOwner, Observer { success ->
                    if (success) {
                        progressBarAdminCode.visibility = View.INVISIBLE
                        startActivity(Intent(context, HomeActivity::class.java))
                        requireActivity().finish()
                    } else {
                        progressBarAdminCode.visibility = View.INVISIBLE
                        showWarning(getString(R.string.login_manual))
                        startActivity(Intent(context, LoginActivity::class.java))
                        requireActivity().finish()
                    }
                })
            } else {
                progressBarAdminCode.visibility = View.INVISIBLE
                showError(it.message)
            }
        })
    }
}
