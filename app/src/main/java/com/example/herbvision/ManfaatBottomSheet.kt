package com.example.herbvision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ManfaatBottomSheet(private val manfaat: String) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_manfaat, container, false)

        view.findViewById<TextView>(R.id.bottomsheetmanfaat).text = manfaat
        return view
    }
}
