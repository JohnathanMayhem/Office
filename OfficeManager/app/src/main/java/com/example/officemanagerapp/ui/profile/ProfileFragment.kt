package com.example.officemanagerapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.example.officemanagerapp.R
import com.example.officemanagerapp.databinding.FragmentProfileBinding
import com.example.officemanagerapp.responses.User
import com.example.officemanagerapp.util.logout
import com.example.officemanagerapp.util.visible


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    //lateinit var

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*binding = FragmentProfileBinding.bind(view)
        binding.progressbar.visible(false)
        viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visible(false)
                    updateUI(it.value.user)
                }
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        }*/

        /*binding.btnLogOut.setOnClickListener {
            logout()
        }*/
    }

    // TODO(What I need to show?)
    private fun updateUI(user: User) {
        /*with(binding) {
            textViewId.text = user.id.toString()
            textViewName.text = user.name
            textViewEmail.text = user.email
        }*/
    }

}