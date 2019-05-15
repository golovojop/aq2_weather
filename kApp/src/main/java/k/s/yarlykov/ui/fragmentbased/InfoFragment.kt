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

        // Выбрать макет для текущей ориентации экрана
        val orientationCompatibleLayoutId = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> R.layout.info_fragment_portrait
            else -> R.layout.info_fragment_land
        }

        return inflater.inflate(orientationCompatibleLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvVersion.text = "${resources.getString(R.string.version)} ${BuildConfig.VERSION_NAME}"
    }
}