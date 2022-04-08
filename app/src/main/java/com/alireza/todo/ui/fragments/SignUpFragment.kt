package com.alireza.todo.ui.fragments

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
import com.alireza.todo.databinding.SignupFragmentBinding
import com.alireza.todo.ui.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment() {

    private lateinit var binding: SignupFragmentBinding

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.signup_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            checkUserInput( User(
                binding.usernameInput.text.toString(),
                binding.passwordInput.text.toString()
            ))

        }
    }
    private fun checkUserInput(user: User) {
        Log.d("user",user.toString())
                viewModel.signupUser(user)
                notifyRegisterComplete()

        }

    fun notifyRegisterComplete() {
        Snackbar.make(requireContext(),requireView(), "user created !", Snackbar.LENGTH_LONG)
            .show()
    }
    }

