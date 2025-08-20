package com.example.herbvision

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ManfaatBottomSheet : BottomSheetDialogFragment() {
    private var manfaat: String? = null
    private var plantName: String? = null

    companion object {
        fun newInstance(plantName: String, manfaat: String): ManfaatBottomSheet {
            val fragment = ManfaatBottomSheet()
            val args = Bundle()
            args.putString("plantName", plantName)
            args.putString("manfaat", manfaat)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_manfaat, container, false)

        plantName = arguments?.getString("plantName") ?: "Tanaman tidak dikenal"
        manfaat = arguments?.getString("manfaat") ?: "Manfaat tidak tersedia"

        view.findViewById<TextView>(R.id.tv_plant_name).text = "Tanaman: $plantName"
        view.findViewById<TextView>(R.id.bottomsheetmanfaat).text = manfaat

        return view
    }
}

