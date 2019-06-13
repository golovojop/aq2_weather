package j.s.yarlykov.ui.fragmentbased;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import j.s.yarlykov.R;

public class FeedbackFragment extends Fragment {

    EditText etName, etMessage, etAddress;
    Button btnSend;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etName = view.findViewById(R.id.etName);
        etMessage = view.findViewById(R.id.etMessage);
        etAddress = view.findViewById(R.id.etEmail);

        btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms(etMessage.getText().toString());
            }
        });
    }

    private void sendSms (String sms){
        String to = etAddress.getText().toString();
        SmsManager manager = SmsManager.getDefault();
        if(manager != null && smsSendGranted() && isAddressOk(to)) {
            manager.sendTextMessage(to,
                    null,
                    sms,
                    null,
                    null);
        }

        // Закрыть фрагмент
        try {
            getFragmentManager().popBackStack();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean smsSendGranted() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private boolean isAddressOk(@NonNull String address) {
        // Проверка валиности телефонного номера
        // в формате +78001234567
        return address.matches("^\\+[0-9]{10,13}$");
    }
}
