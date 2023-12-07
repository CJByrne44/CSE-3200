package com.connerbyrne.criminalintent

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.connerbyrne.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import android.text.format.DateFormat
import android.provider.Settings.System.DATE_FORMAT
import kotlinx.coroutines.selects.select
import java.util.Date
import java.util.UUID

private const val DATE_FORMAT = "EEE, dd, mm"
//private const val TAG = "CrimeDetailFragment"
class CrimeDetailFragment : Fragment() {

    //private lateinit var crime : Crime
    private val args: CrimeDetailFragmentArgs by navArgs()
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels{  //added 1
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private val selectSuspect = registerForActivityResult(ActivityResultContracts.PickContact())
    { uri: Uri? ->
        uri?.let { parseContactSelection(it) }
    }


    private var _binding: FragmentCrimeDetailBinding? = null

    private val binding: FragmentCrimeDetailBinding
        get() = checkNotNull(_binding){
            "Error.  Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            crimeTitle.doOnTextChanged { text, _, _, _->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            crimeSuspect.setOnClickListener {
                selectSuspect.launch(null)
            }

            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            crimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)
            setFragmentResultListener(
                DatePickerFragment.REQUEST_KEY_DATE
            ){requestKey, bundle ->
                //TODO
                val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
                crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
            }

            crimeSolved.setOnCheckedChangeListener{_, isChecked ->
                //crime = crime.copy(isSolved = isChecked)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            deleteCrime.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {

                    crimeDetailViewModel.crime.collect {crime ->
                        if (crime != null) {
                            crimeDetailViewModel.deleteCrime(crime)
                            findNavController().navigateUp()
                        }
                    }

                }
            }
        }

        //added 2
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                crimeDetailViewModel.crime.collect{ crime ->
                    crime?.let{updateUi(it)}
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ){_, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()

        val suspectString = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(
            R.string.crime_report,
            crime.title,
            dateString,
            solvedString,
            suspectString
        )
    }

    //added 3
    private fun updateUi(crime: Crime){
        binding.apply {
            if(crimeTitle.text.toString() != crime.title){
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeDate.setOnClickListener {
                findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
            }
            crimeSolved.isChecked = crime.isSolved
            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                }
                val chooserIntent = Intent.createChooser(
                    reportIntent, getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }
            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }
        }
    }

    private fun parseContactSelection(contractUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireActivity().contentResolver
            .query(contractUri, queryFields, null, null, null)

        queryCursor?.use{ cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = suspect)
                }
            }

        }
    }

    private fun canResolveIntent(intent : Intent): Boolean {
        intent.addCategory(Intent.CATEGORY_HOME)
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent, PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity == null
    }
}

