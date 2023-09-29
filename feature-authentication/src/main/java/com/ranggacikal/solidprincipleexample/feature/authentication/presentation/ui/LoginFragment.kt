package com.ranggacikal.solidprincipleexample.feature.authentication.presentation.ui

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ranggacikal.solidprincipleexample.core.base.BaseFragment
import com.ranggacikal.solidprincipleexample.core.base.observeDataFlow
import com.ranggacikal.solidprincipleexample.core.entity.authentication.LoginRequest
import com.ranggacikal.solidprincipleexample.feature.authentication.R
import com.ranggacikal.solidprincipleexample.feature.authentication.databinding.FragmentLoginBinding
import com.ranggacikal.solidprincipleexample.feature.authentication.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    internal val viewModel: LoginViewModel by viewModels()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun initView() {
        setupListener()
        observeLogin()
    }

    private fun observeLogin() = with(viewModel) {
        observeDataFlow(
            postLogin,
            onLoad = {
                binding.pbLogin.visibility = View.VISIBLE
            },
            onError = {
                binding.pbLogin.visibility = View.GONE
                Toast.makeText(requireContext(), "LOGIN GAGAL", Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                binding.pbLogin.visibility = View.GONE
                Toast.makeText(requireContext(), "LOGIN BERHASIL", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupListener() = with(binding) {
        viewModel.email = edtEmailLogin.text.toString()
        viewModel.password = edtPasswordLogin.text.toString()
        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        safeCallJob.run { viewModel.login(LoginRequest(email = viewModel.email, password = viewModel.password)) }
    }

}