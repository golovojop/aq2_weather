package k.s.yarlykov.ui.fragmentbased

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import k.s.yarlykov.BuildConfig
import k.s.yarlykov.R
import kotlinx.android.synthetic.main.info_fragment_portrait.*

class InfoFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            inflater.inflate(R.layout.info_fragment_portrait, container, false)
        } else {
            inflater.inflate(R.layout.info_fragment_land, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvVersion.text = "${resources.getString(R.string.version)} ${BuildConfig.VERSION_NAME}"
    }
}