package k.s.yarlykov.ui.fragmentbased

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import k.s.yarlykov.R

class DevInfoFragment : Fragment() {

    companion object {
        fun create(): DevInfoFragment = DevInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.developer_fr, container, false)
    }

}
