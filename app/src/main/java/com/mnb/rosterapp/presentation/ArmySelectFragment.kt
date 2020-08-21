package com.mnb.rosterapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.mnb.rosterapp.R
import com.mnb.rosterapp.databinding.ItemSelectionBinding

class ArmySelectFragment : Fragment() {

    private val stateModel by activityViewModels<StateViewModel>()

    companion object {
        const val ORIGIN = "army_select_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stateModel.state.observe(this, Observer {
            val inflater = activity?.layoutInflater
            val view = view

            // bail out if inflater/view aren't available (ie: before onCreateView is called)
            if (inflater == null) {
                System.out.println(ORIGIN + " state observer called, but inflater is null")
                return@Observer
            }
            if (view == null) {
                System.out.println(ORIGIN + " state observer called, but view is null")
                return@Observer
            }

            System.out.println(ORIGIN + " state observer called, inflater/view ok")

            // build army list
            val armyList = it.armyList
            val layout = view.findViewById(R.id.army_list) as LinearLayout
            if (armyList != null) {
                layout.removeAllViews()
                for (armyName in armyList) {
                    val binding = ItemSelectionBinding.inflate(inflater)
                    binding.setSelectionName(armyName)
                    val itemView = binding.root
                    itemView.setOnClickListener {
                        stateModel.handleEvent(Event.ArmySelectOpenArmy(armyName))
                        val argBundle = bundleOf(Keywords.ORIGIN to ORIGIN, Keywords.ARMY_NAME to armyName)
                        Navigation.findNavController(view).navigate(
                            R.id.action_armySelectFragment_to_armyViewFragment,
                            argBundle
                        )
                    }
                    layout.addView(itemView)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_army_select, container, false)
    }

    override fun onResume() {
        super.onResume()

        System.out.println(ORIGIN + " onResume called, handle event")
        stateModel.handleEvent(Event.RefreshUi())
    }
}
