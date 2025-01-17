package com.example.officemanagerapp.ui.services.planned.subcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.officemanagerapp.R
import com.example.officemanagerapp.databinding.FragmentPlannedSubcategoryBinding
import com.example.officemanagerapp.models.PlannedOrderPost
import com.example.officemanagerapp.ui.services.planned.category.PlannedCategoriesClick
import com.example.officemanagerapp.ui.services.planned.category.PlannedCategoriesAdapter


class PlannedSubcategoryFragment : Fragment() {

    private var plannedCategoriesAdapter: PlannedCategoriesAdapter? = null
    private val args by navArgs<PlannedSubcategoryFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plannedCategoriesAdapter?.categoriesLists = args.category.subcategory ?: listOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPlannedSubcategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_planned_subcategory, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        plannedCategoriesAdapter = PlannedCategoriesAdapter(PlannedCategoriesClick { subcategory ->
            val plannedOrderPost = PlannedOrderPost(args.category.categoryId, subcategory.categoryId)
            val action = PlannedSubcategoryFragmentDirections.actionPlannedSubcategoryFragmentToPlannedInfoFragment(subcategory.title, plannedOrderPost = plannedOrderPost)
            findNavController().navigate(action)
        })

        binding.root.findViewById<RecyclerView>(R.id.subcategoryRV).apply {
            layoutManager = GridLayoutManager(activity, 4)
            adapter = plannedCategoriesAdapter
        }

        return binding.root
    }
}

