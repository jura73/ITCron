package com.example.itcron.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itcron.R
import com.example.itcron.databinding.FragmentMainBinding
import com.example.itcron.ui.user_details.UserDetailsFragment

internal class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private val usersAdapter = UsersAdapter {
        viewModel.onUserSelected(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.srlUsers.setOnRefreshListener {
            viewModel.onRefresh()
            binding.srlUsers.isRefreshing = false
        }

        binding.rcvUsers.adapter = usersAdapter
        val layoutManager = binding.rcvUsers.layoutManager as LinearLayoutManager

        binding.rcvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lastItemPosition: Int = layoutManager.findLastVisibleItemPosition()
                if (lastItemPosition > usersAdapter.itemCount - 3) {
                    viewModel.nextPage()
                }
            }
        })

        viewModel.getUsers().observe(viewLifecycleOwner) {
            usersAdapter.usersList = it
            usersAdapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.prbLoading.isVisible = it
        }

        viewModel.eventOpenUser.observe(viewLifecycleOwner) { user ->
            user.contentIfNotHandled?.let {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, UserDetailsFragment.newInstance(it.login))
                    .addToBackStack(UserDetailsFragment.name)
                    .commit()
            }

        }
    }

}