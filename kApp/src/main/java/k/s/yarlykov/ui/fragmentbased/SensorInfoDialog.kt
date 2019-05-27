package k.s.yarlykov.ui.fragmentbased

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import k.s.yarlykov.R
import kotlinx.android.synthetic.main.sensor_info_dialog.*

class SensorInfoDialog: DialogFragment() {

    var content: String? = null

    companion object {
        const val key: String = "key"

        fun create(data: String): SensorInfoDialog {

            return SensorInfoDialog().apply {
                arguments = Bundle().apply {
                    putString(key, data)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(key)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setTitle(getString(R.string.sensorInfo))
        return inflater.inflate(R.layout.sensor_info_dialog, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCloseDialog.setOnClickListener { dismiss() }

        content?.let {
            tvSensorInfo.text = it
        }
    }

}