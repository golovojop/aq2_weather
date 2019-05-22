package j.s.yarlykov.ui.fragmentbased;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import j.s.yarlykov.R;

public class FeedbackFragment extends Fragment {

    public static FeedbackFragment create() {
        return new FeedbackFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int orientationCompatibleLayoutId =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                        R.layout.feedback_fr : R.layout.feedback_fr_land;
        return inflater.inflate(orientationCompatibleLayoutId, container, false);
    }
}
