package com.alireza.todo.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alireza.todo.R
import com.alireza.todo.data.model.model.User
import com.alireza.todo.databinding.LoginFragmentBinding
import com.alireza.todo.ui.MainActivity
import com.alireza.todo.ui.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: LoginFragmentBinding
    lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.login_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        binding.btnLogin.setOnClickListener {
            checkUserInput(
                User(
                    binding.usernameInputLogin.text.toString(),
                    binding.passwordInputLogin.text.toString()
                )
            )
        }
    }

    private fun checkUserInput(user: User) {

        viewModel.searchUser(user.username).observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("username", it.toString())
                if (it == user) {
                    val spEditor = sp.edit()
                    Log.d("username", it.toString())
                    spEditor.putString("username", it.username)
                    if (spEditor.commit()) {
                        val intent = Intent(activity, MainActivity::class.java)
                        requireActivity().finish()
                        startActivity(intent)
                    }
                }
                else if (it.username== user.username && it.password != user.password) notifyForgetPassword()
            }
        }
    }

    fun notifyForgetPassword() {
        Snackbar.make(requireContext(),requireView(), "Forget Password ?", Snackbar.LENGTH_LONG)
            .setAction("yes", View.OnClickListener {
                Log.d("data", "ok Action")
            }).show()
    }
}