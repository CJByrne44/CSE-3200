package com.connerbyrne.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.connerbyrne.criminalintent.databinding.FragmentCrimeDetailBinding

private const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {
    private val crimeListViewModel : CrimeListViewModel by viewModels()

    private var _binding : FragmentCrimeListBinding? = null
    private val binding : FragmentCrimeListBinding
        get() = checkNotNull(_binding) {
            "error: Can we see the view"
        }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Num Crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater : LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)//TODO
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO do stuff?
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

}