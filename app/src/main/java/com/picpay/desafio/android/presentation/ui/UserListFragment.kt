package com.picpay.desafio.android.presentation.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.picpay.desafio.android.databinding.FragmentUserListBinding
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.presentation.ui.adapter.UserListAdapter
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import com.picpay.desafio.android.utils.collectLifecycleFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class UserListFragment : Fragment() {
    companion object {
        const val SAVED_LAYOUT_MANAGER = "layout-manager-state"
    }

    private var _binding: FragmentUserListBinding? = null
    private var mLayoutManagerSavedState: Parcelable? = null
    private val _viewModel: UserViewModel by viewModel()

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

        _binding?.swipeRefreshLayout?.setOnRefreshListener {
            _viewModel.getUsers()
        }

        collectLifecycleFlow(_viewModel.uiViewState) { result ->
            when (result) {
                UserViewState.Loading -> buildViewLoading(true)
                is UserViewState.ShowData -> buildViewData(result.users)
                is UserViewState.Error -> buildViewError(result.message)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            SAVED_LAYOUT_MANAGER,
            _binding?.recyclerView?.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        mLayoutManagerSavedState = savedInstanceState?.getParcelable(SAVED_LAYOUT_MANAGER)
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun buildViewData(users: List<User>) {
        Timber.d("buildViewData")
        buildViewLoading(false)
        _binding?.adapter?.update(users)

        mLayoutManagerSavedState?.let { state ->
            _binding?.recyclerView?.layoutManager?.onRestoreInstanceState(state)
            mLayoutManagerSavedState = null
        }
    }

    private fun buildViewError(message: Int) {
        buildViewLoading(false)
        _binding?.recyclerView?.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun buildViewLoading(isActive: Boolean) {
        Timber.d("buildViewLoading")
        _binding?.swipeRefreshLayout?.isRefreshing = false
        when (isActive) {
            true -> {
                _binding?.userListProgressBar?.visibility = View.VISIBLE
                _binding?.recyclerView?.visibility = View.GONE
            }
            else -> {
                _binding?.userListProgressBar?.visibility = View.GONE
                _binding?.recyclerView?.visibility = View.VISIBLE
            }
        }
    }
}
