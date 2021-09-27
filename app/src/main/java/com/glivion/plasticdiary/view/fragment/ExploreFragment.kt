package com.glivion.plasticdiary.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.glivion.plasticdiary.R
import com.glivion.plasticdiary.databinding.ExploreFragmentBinding
import com.glivion.plasticdiary.databinding.SubmitTipLayoutBinding
import com.glivion.plasticdiary.model.explore.Resource
import com.glivion.plasticdiary.model.explore.Tip
import com.glivion.plasticdiary.util.*
import com.glivion.plasticdiary.view.adapter.explore.ExploreResourcesAdapter
import com.glivion.plasticdiary.view.adapter.explore.ExploreTipsAdapter
import com.glivion.plasticdiary.view.dialog.LoadingDialog
import com.glivion.plasticdiary.view.ui.MapItemActivity
import com.glivion.plasticdiary.viewmodel.ExploreViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private lateinit var binding: ExploreFragmentBinding
    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var tipsAdapter: ExploreTipsAdapter
    private lateinit var resourcesAdapter: ExploreResourcesAdapter
    private lateinit var loadingDialog: LoadingDialog
    private val tipsList = ArrayList<Tip>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.explore_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        initViewModel()
        return binding.root
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(requireContext())
        tipsAdapter = object : ExploreTipsAdapter(){
            override fun viewTip(tip: Tip) {
                openViewMoreInfoDialog(requireActivity(), "Tip", tip.content!!)
            }
        }

        resourcesAdapter = object : ExploreResourcesAdapter(){
            override fun visitLink(resource: Resource) {
                showInWebView(requireContext(), resource.link, resource.id, "resource")
            }

        }
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                viewModel.getExplorePageItems()
            }
            submitTip.setOnClickListener {
                openSubmitTipDialog()
            }
            tipsRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = tipsAdapter
            }
            resourcesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = resourcesAdapter
            }
            openGoogleMaps.setOnClickListener {
                startActivity(Intent(requireContext(), MapItemActivity::class.java))
            }
            seeAll.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelableArrayList(WEB_VIEW_TIPS_LIST, tipsList)
                }
                viewMoreItems(requireContext(), "Tips", bundle)
            }

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun initViewModel() {
        viewModel.userLoader.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                binding.swipeRefresh.isRefreshing = it
            }
        })

        viewModel.loadingDialog.observe(viewLifecycleOwner, { isLoading ->
            isLoading.let {
                if (it) {
                    loadingDialog.startProgressDialog()
                } else {
                    loadingDialog.dismissDialog()
                }
            }
        })

        viewModel.userErrors.observe(viewLifecycleOwner, { isError ->
            isError.let { e ->
                Timber.e("initViewModel: ${e.localizedMessage}")
                binding.swipeRefresh.isRefreshing = false
                showErrorMessage(binding.parentLayout, e)
            }
        })

        viewModel.responses.observe(viewLifecycleOwner, { response ->
            if(viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED){
                binding.swipeRefresh.isRefreshing = false
                Timber.e("response: $response")
                showSnackBarMessage(binding.parentLayout, response)
            }

        })

        viewModel.tipData.observe(viewLifecycleOwner, {tips ->
            tipsList.clear()
            tipsList.addAll(tips)
            tipsAdapter.submitList(tips)
        })
        viewModel.resourcesData.observe(viewLifecycleOwner, {resources ->
            resourcesAdapter.submitList(resources)
        })
        viewModel.mapItemsData.observe(viewLifecycleOwner, {maps ->

        })
    }

    private fun openSubmitTipDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.myFullscreenAlertDialogStyle)
        val binding: SubmitTipLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.submit_tip_layout, null, false)

        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        binding.apply {
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            done.setOnClickListener {
                val tip = binding.tipInputField.text.toString()
                if (tip.isEmpty()) {
                    binding.tipInputField.error = "Please enter tip"
                    return@setOnClickListener
                }
                dialog.dismiss()
                viewModel.submitTip(tip)
            }
        }

        dialog.show()
    }


    fun getToken() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String = task.result.token!!
                    Timber.e("userToken: $idToken")
                    // ...
                } else {
                    Timber.e("token: ${task.exception}")
                    // Handle error -> task.getException();
                }
            }
    }

}