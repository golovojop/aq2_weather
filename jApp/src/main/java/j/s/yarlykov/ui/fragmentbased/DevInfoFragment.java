package j.s.yarlykov.ui.fragmentbased;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import j.s.yarlykov.R;

public class DevInfoFragment extends Fragment {
    public static DevInfoFragment create() {
        return new DevInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.developer_fr, container, false);
    }
}
