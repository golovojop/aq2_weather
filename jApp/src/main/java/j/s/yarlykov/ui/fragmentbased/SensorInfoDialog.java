package j.s.yarlykov.ui.fragmentbased;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import j.s.yarlykov.R;

public class SensorInfoDialog extends DialogFragment implements View.OnClickListener {
    private String content = null;
    private final static String key = "key";

    public static SensorInfoDialog create(String data) {
        SensorInfoDialog dialog = new SensorInfoDialog();

        Bundle args = new Bundle();
        args.putString(key, data);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getArguments().getString(key);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.sensorInfo));
        return inflater.inflate(R.layout.sensor_info_dialog, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnCloseDialog).setOnClickListener(this);

        if(content != null) {
            ((TextView)view.findViewById(R.id.tvSensorInfo))
                    .setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
