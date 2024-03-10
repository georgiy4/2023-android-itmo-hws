package ru.ok.itmo.example

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment

class NavigationFragment : Fragment(R.layout.navigation_fragment) {


    var fragments : Array<NumberFragment> = Array((3..5).random()) {
        NumberFragment()
    }
    var fragmentsPos = IntArray(fragments.size) { -1 }
    var stack: ArrayList<Int> = arrayListOf()
    companion object {
        val getRB: Map<Int, Int> = mapOf(
            Pair(0, R.id.rb_A),
            Pair(1, R.id.rb_B),
            Pair(2, R.id.rb_C),
            Pair(3, R.id.rb_D),
            Pair(4, R.id.rb_E)
        )
    }

    private fun openFragment(ind: Int){
        if (fragmentsPos[ind] == -1) {
            fragmentsPos[ind] = stack.size
            childFragmentManager.beginTransaction()
                .hide(fragments[stack.last()])
                .show(fragments[ind])
                .commit()
            stack.add(ind)
        } else {
            childFragmentManager.beginTransaction()
                .hide(fragments[stack.last()])
                .show(fragments[ind])
                .commit()
            while (stack.last() != ind) {
                fragmentsPos[stack.last()] = -1
                stack.removeLast()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            val transaction = childFragmentManager.beginTransaction()
            fragments.forEach { fragment ->
                transaction.add(R.id.number_fragment_container, fragment)
                    .hide(fragment)
            }
            fragmentsPos[0] = stack.size
            transaction.show(fragments[0]).commit()
            stack.add(0)
            view.findViewById<RadioGroup>(R.id.radio_group).check(R.id.rb_A)
            view.findViewById<RadioButton>(R.id.rb_A).setOnClickListener{
                openFragment(0)
            }
            view.findViewById<RadioButton>(R.id.rb_B).setOnClickListener{
                openFragment(1)
            }
            view.findViewById<RadioButton>(R.id.rb_C).setOnClickListener{
                openFragment(2)
            }
            if (fragments.size >= 4) {
                view.findViewById<RadioButton>(R.id.rb_D).setOnClickListener {
                    openFragment(3)
                }
            } else {
                view.findViewById<RadioButton>(R.id.rb_D).isVisible = false
            }
            if (fragments.size >= 5) {
                view.findViewById<RadioButton>(R.id.rb_E).setOnClickListener {
                    openFragment(4)
                }
            } else {
                view.findViewById<RadioButton>(R.id.rb_E).isVisible = false
            }
        }
    }

    fun onBackPressed(): Boolean{
        if (stack.size == 1) {
            childFragmentManager.beginTransaction()
                .hide(fragments[stack.last()])
                .commit()
            fragmentsPos[stack.last()] = -1
            stack.removeLast()
            return true
        } else {
            val cur = stack.last()
            stack.removeLast()
            childFragmentManager.beginTransaction()
                .hide(fragments[cur])
                .show(fragments[stack.last()])
                .commit()
            view?.findViewById<RadioGroup>(R.id.radio_group)?.check(getRB[stack.last()]!!)
            fragmentsPos[cur] = -1
            return false
        }
    }
}