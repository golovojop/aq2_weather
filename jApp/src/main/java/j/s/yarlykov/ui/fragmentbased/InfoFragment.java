package j.s.yarlykov.ui.fragmentbased;

import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import j.s.yarlykov.BuildConfig;
import j.s.yarlykov.R;

public class InfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int orientationCompatibleLayoutId =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                        R.layout.info_fragment_portrait : R.layout.info_fragment_land;
        return inflater.inflate(orientationCompatibleLayoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvVersion = view.findViewById(R.id.tvVersion);
        tvVersion.setText(String
                .format("%s %s", getString(R.string.version), BuildConfig.VERSION_NAME));
    }
}
