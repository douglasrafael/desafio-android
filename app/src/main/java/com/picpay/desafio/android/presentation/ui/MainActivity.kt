package com.picpay.desafio.android.presentation.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.repository.datasource.network.PicPayService
import com.picpay.desafio.android.presentation.ui.adapter.UserListAdapter
import com.picpay.desafio.android.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val url = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

    private val gson: Gson by lazy { GsonBuilder().create() }

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    //    private val getUsersUseCase: GetUsersUseCaseImpl by inject()
    private val mViewModel: UserViewModel by viewModel()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val service: PicPayService by lazy {
        retrofit.create(PicPayService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LOG_DEBUG", "onCreate()")

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.getUsers()
        }

        lifecycleScope.launch {
            // Safely collect from locationFlow when the lifecycle is STARTED
            // and stops collection when the lifecycle is STOPPED
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.uiViewState.collect {result ->
                    when (result) {
                        UserViewState.Loading -> {
                            swipeRefreshLayout.isRefreshing = false
                            progressBar.visibility = View.VISIBLE
                        }
                        is UserViewState.Error -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.GONE

                            Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
                        }
                        is UserViewState.ShowData -> {
                            progressBar.visibility = View.GONE
                            adapter.users = result.users
                        }
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    override fun onResume() {
        super.onResume()
//        recyclerView = findViewById(R.id.recyclerView)
//        progressBar = findViewById(R.id.user_list_progress_bar)
//
//        adapter = UserListAdapter()
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        mViewModel.data.observe(this, Observer { result ->
//            when (result) {
//                UserViewState.Loading -> progressBar.visibility = View.VISIBLE
//                is UserViewState.Error -> {
//                    progressBar.visibility = View.GONE
//                    recyclerView.visibility = View.GONE
//
//                    Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
//                }
//                is UserViewState.ShowData -> {
//                    progressBar.visibility = View.GONE
//                    adapter.users = result.users
//                }
//            }
//        })
    }
}
