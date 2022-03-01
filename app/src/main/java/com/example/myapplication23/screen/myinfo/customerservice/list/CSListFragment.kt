package com.example.myapplication23.screen.myinfo.customerservice.list

import android.content.Intent
import android.media.Image

import com.example.myapplication23.util.provider.ResoucesProvider
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication23.R
import com.example.myapplication23.databinding.FragmentCsListBinding
import com.example.myapplication23.model.Model
import com.example.myapplication23.model.customerservicelist.CSModel
import com.example.myapplication23.model.customerservicelist.ImageData
import com.example.myapplication23.screen.base.BaseFragment
import com.example.myapplication23.screen.myinfo.customerservice.CSFragmentDirections
import com.example.myapplication23.screen.myinfo.customerservice.list.detail.CSDetailFragment
import com.example.myapplication23.widget.adapter.ModelRecyclerAdapter
import com.example.myapplication23.widget.adapter.listener.customerservice.CSModelListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.ArrayList

/**
 * @author HeeTae Heo(main),
 * Geonwoo Kim, Doyeop Kim, Namjin Jeong, Eunho Bae (sub)
 * @since
 * @throws
 * @description
 */

class CSListFragment : BaseFragment<FragmentCsListBinding>() {


    private val viewModel by viewModel<CSListViewModel> {
        parametersOf(csCategory)
    }
    private val csCategory by lazy {
        arguments?.getSerializable(CS_CATEGORY_KEY) as CSCategory
    }

    override fun getViewBinding(): FragmentCsListBinding =
        FragmentCsListBinding.inflate(layoutInflater)

    override fun observeData() = with(viewModel) {
        csListData.observe(viewLifecycleOwner) {
            adapter.submitList(it)

        }

    }

    private val resourcesProvider by inject<ResoucesProvider>()





    private val adapter by lazy {
        ModelRecyclerAdapter<CSModel, CSListViewModel>(
           listOf(), viewModel,resourcesProvider,
                    object : CSModelListener {
                    override fun onClickItem (listModel: CSModel){
                        view?.findViewById<TextView>(R.id.question_text)!!.setOnClickListener {
                          CSFragmentDirections.actionCSFragmentToCSDetailFragment()
                        }
                        val intent = Intent(context, CSDetailFragment::class.java).apply {
                           val data = ImageData(listModel.csTitle,listModel.csContent,listModel.csAuthor)
                            putExtra(CS_CATEGORY_KEY,data)
                            putExtra("CSTitle", listModel.csTitle)
                            putExtra("CSContent", listModel.csContent)
                            putExtra("CSAuthor", listModel.csAuthor)
                            putExtra("CSid", listModel.id)
                        }
                        startActivity(intent)




                    }
            }
        )

    }

    override fun initViews() = with(viewModel){
        super.initViews()
        fetchData()
        binding.csRecyclerView.adapter = adapter
        binding.csRecyclerView.layoutManager = LinearLayoutManager(this@CSListFragment.context)




    }
    companion object {
        const val CS_CATEGORY_KEY = "CSCategoryKey"
        fun newInstance(csCategory: CSCategory): CSListFragment {
            val bundle = Bundle().apply {
                putSerializable(CS_CATEGORY_KEY, csCategory)
            }

            return CSListFragment().apply {
                arguments = bundle
            }
        }
    }
}


