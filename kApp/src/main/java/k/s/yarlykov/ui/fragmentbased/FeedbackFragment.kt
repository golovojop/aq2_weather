package k.s.yarlykov.ui.fragmentbased

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import k.s.yarlykov.R

class FeedbackFragment : Fragment() {

    companion object {
        fun create(): FeedbackFragment = FeedbackFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val orientationCompatibleLayoutId = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> R.layout.feedback_fr
            else -> R.layout.feedback_fr_land
        }
        return inflater.inflate(orientationCompatibleLayoutId, container, false)
    }
}
