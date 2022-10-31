package com.example.itcron.ui.user_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.itcron.R
import com.example.itcron.databinding.FragmentUserDetailsBinding

internal class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    companion object {
        private const val LOGIN_KEY = "LOGIN_KEY"
        fun newInstance(login: String) = UserDetailsFragment().apply {
            arguments = Bundle().apply { putString(LOGIN_KEY, login) }
        }

        const val name = "UserDetails"
    }

    private val viewModel: UserDetailsViewModel by viewModels()
    private lateinit var binding: FragmentUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(LOGIN_KEY)?.let { login ->
            viewModel.setLogin(login)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getUser().observe(viewLifecycleOwner) {

            Glide.with(requireContext()).load(it.avatar_url).into(binding.imgAvatar)

            binding.txtName.text = it.name

            binding.txtEmail.setTextOrGone(it.email)

            binding.txtOrganization.setTextOrGone(
                it.organisation,
                getString(R.string.caption_organisation, it.organisation)
            )
            binding.txtFollowings.text =
                getString(R.string.caption_following, it.following)
            binding.txtFollowers.text =
                getString(R.string.caption_followers, it.followers)

            binding.txtDateCreated.setTextOrGone(
                it.created_at.toString(),
                getString(R.string.caption_created_at, it.created_at.toString())
            )
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.prbLoading.isVisible = it
        }
    }

    private fun TextView.setTextOrGone(checkText: String?, text: String? = checkText) {
        if (checkText.isNullOrEmpty().not()) {
            isVisible = true
            this.text = text
        } else {
            isVisible = false
        }
    }

}