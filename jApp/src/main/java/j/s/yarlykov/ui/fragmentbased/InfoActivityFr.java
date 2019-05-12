package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import j.s.yarlykov.R;

public class InfoActivityFr extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, InfoActivityFr.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.hide();
        }

        setContentView(R.layout.activity_info_fr);
    }
}
