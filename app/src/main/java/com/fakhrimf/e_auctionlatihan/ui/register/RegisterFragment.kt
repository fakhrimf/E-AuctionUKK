package com.fakhrimf.e_auctionlatihan.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.e_auctionlatihan.GetAdminCodeActivity
import com.fakhrimf.e_auctionlatihan.HomeActivity
import com.fakhrimf.e_auctionlatihan.LoginActivity
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY
import com.fakhrimf.e_auctionlatihan.utils.ROLE_ADMIN
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : BaseFragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionBtn.setOnClickListener {
            if (spinnerRole.selectedItem.toString() == ROLE_ADMIN && !isEmpty()) {
                val model = getModel()
                val intent = Intent(context, GetAdminCodeActivity::class.java)
                intent.putExtra(MODEL_INTENT_KEY, model)
                startActivity(intent)
            } else {
                if(!isEmpty()) {
                    val model = getModel()
                    progressBarRegister.visibility = View.VISIBLE
                    vm.register(requireContext(), model).observe(viewLifecycleOwner, Observer {
                        if (it.success) {
                            showSuccess(getString(R.string.logging_you_in))
                            vm.login(requireContext(), viewLifecycleOwner, model).observe(viewLifecycleOwner, Observer { success ->
                                if (success) {
                                    progressBarRegister.visibility = View.INVISIBLE
                                    startActivity(Intent(context, HomeActivity::class.java))
                                    requireActivity().finish()
                                } else {
                                    progressBarRegister.visibility = View.INVISIBLE
                                    showWarning(getString(R.string.login_manual))
                                    startActivity(Intent(context, LoginActivity::class.java))
                                    requireActivity().finish()
                                }
                            })
                        } else {
                            progressBarRegister.visibility = View.INVISIBLE
                            showError(it.message)
                        }
                    })
                }
            }
        }
        actionLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun isEmpty() : Boolean {
        var check = false
        if(usernameInput.text.toString().isEmpty()) {
            check = true
            usernameInput.error = getString(R.string.username_error)
        }
        if(passwordInput.text.toString().isEmpty() || passwordInput.text.toString().length < 8) {
            check = true
            passwordInput.error = getString(R.string.password_error_register)
        }
        if(nameInput.text.toString().isEmpty()){
            check = true
            nameInput.error = getString(R.string.name_error)
        }
        if(emailInput.text.toString().isEmpty() || !emailInput.text.toString().contains("@", true)) {
            check = true
            emailInput.error = getString(R.string.email_error)
        }
        return check
    }
    private fun getModel() : ProfileModel {
        return ProfileModel(
            name =  nameInput.text.toString(),
            gender = spinnerGender.selectedItem.toString(),
            role = spinnerRole.selectedItem.toString(),
            email = emailInput.text.toString(),
            username = usernameInput.text.toString(),
            password = passwordInput.text.toString()
        )
    }
}
