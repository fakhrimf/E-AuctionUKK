package com.fakhrimf.e_auctionlatihan.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.e_auctionlatihan.HomeActivity
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.RegisterActivity
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val profile = vm.getCurrentUser(requireContext())
        if (profile != null) {
            usernameInput.setText(profile.username)
            passwordInput.setText(profile.password)
            showInfo(getString(R.string.auto_login))
            login()
        }
        actionBtn.setOnClickListener {
            if (!isEmpty()) {
                login()
            }
        }
        actionRegister.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun isEmpty(): Boolean {
        var check = false
        if (usernameInput.text.toString().isEmpty()) {
            check = true
            usernameInput.error = getString(R.string.username_error)
        }
        if (passwordInput.text.toString().isEmpty()) {
            check = true
            passwordInput.error = getString(R.string.password_error)
        }
        return check
    }

    private fun login() {
        progressBarLogin.visibility = View.VISIBLE
        vm.login(requireContext(), viewLifecycleOwner, getModel()).observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBarLogin.visibility = View.INVISIBLE
                startActivity(Intent(context, HomeActivity::class.java))
                requireActivity().finish()
            } else {
                progressBarLogin.visibility = View.INVISIBLE
                showWarning(getString(R.string.wrong_password))
            }
        })
    }

    private fun getModel(): ProfileModel {
        return ProfileModel(username = usernameInput.text.toString(), password = passwordInput.text.toString())
    }
}
