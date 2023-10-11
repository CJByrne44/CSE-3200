package com.connerbyrne.criminalintent

import android.location.GpsSatellite
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.connerbyrne.criminalintent.databinding.FragmentCrimeDetailBinding
import java.util.Date
import java.util.UUID

class CrimeDetailFragment : Fragment() {
    private lateinit var binding : FragmentCrimeDetailBinding
    private lateinit var crime : Crime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crime = Crime(
            id = UUID.randomUUID(),
            title = "Bad Stuff Crime",
            date = Date(),
            isSolved = false
        )
    }

    override fun onCreateView(
        inflater : LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crime = crime.copy(title = text.toString())
            }
            
//            crimeDate.setText(crime.title)
//            crimeDate.setOnClickListener {view : View ->
//                crimeDate.setText(crime.title)
//            }
            crimeDate.apply {
                text = crime.date.toString()
                crimeDate.isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener { _,isChecked ->
                crime = crime.copy(isSolved = isChecked)
        }
    }
}