package com.example.myapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple Fragment subclass.
 * Use the (RewardFragment.newInstance) factory method to
 * create an instance of this fragment.
 */
class reward : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reward, container, false)

        // 뒤로 가기 버튼 클릭 리스너 설정
        val backBtn: ImageView = view.findViewById(R.id.back)
        backBtn.setOnClickListener {
            // HomeFragment로 전환
            val homeFragment = home()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, homeFragment)
            transaction.commit()

            // BottomNavigationView의 선택된 아이템을 'homeFragment'로 설정
            val bottomNav: BottomNavigationView = activity?.findViewById(R.id.bottom_navigation)!!
            bottomNav.selectedItemId = R.id.homeFragment
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param (param1) Parameter 1.
         * @param (param2) Parameter 2.
         * @return A new instance of fragment RewardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            reward().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}