package com.picpay.desafio.android.presentation.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.FragmentUserListBinding
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.presentation.ui.adapter.UserListAdapter
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import com.picpay.desafio.android.utils.collectLifecycleFlow
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private var mLayoutManagerSavedState: Parcelable? = null
    val _viewModel: UserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(layoutInflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = UserListAdapter()
        }
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (_viewModel.viewState.value == UserViewState.Init) _viewModel.getUsers()

        _binding?.swipeRefreshLayout?.setOnRefreshListener {
            _viewModel.getUsers()
        }

        collectLifecycleFlow(_viewModel.viewState) { result ->
            buildViewState(state = result)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun buildViewState(state: UserViewState) {
        when (state) {
            UserViewState.Init -> buildViewInit()
            UserViewState.Loading -> buildViewLoading(true)
            is UserViewState.Success -> buildViewShowData(state.users)
            is UserViewState.Error -> buildViewShowError(state.message)
        }
    }

    private fun buildViewInit() {
        buildViewLoading(false)
        _binding?.boxContent?.visibility = View.GONE
    }

    private fun buildViewShowData(users: List<User>) {
        buildViewLoading(false)
        _binding?.boxContent?.visibility = View.VISIBLE
        _binding?.adapter?.update(users)

        mLayoutManagerSavedState?.let { state ->
            _binding?.recyclerView?.layoutManager?.onRestoreInstanceState(state)
            mLayoutManagerSavedState = null
        }
    }

    private fun buildViewShowError(message: String?) {
        buildViewLoading(false)
        _binding?.recyclerView?.visibility = View.GONE
        Toast.makeText(
            requireContext(), message ?: getString(R.string.error), Toast.LENGTH_SHORT
        ).show()
    }

    private fun buildViewLoading(isActive: Boolean) {
        _binding?.swipeRefreshLayout?.isRefreshing = false
        when (isActive) {
            true -> {
                _binding?.listProgressBar?.visibility = View.VISIBLE
                _binding?.recyclerView?.visibility = View.GONE
            }
            else -> {
                _binding?.listProgressBar?.visibility = View.GONE
                _binding?.recyclerView?.visibility = View.VISIBLE
            }
        }
    }
}
