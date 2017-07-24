package com.troyanskiievgen.dmd.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.arellomobile.mvp.MvpActivity;
import com.troyanskiievgen.dmd.R;
import com.troyanskiievgen.dmd.ui.fragment.AppMapFragment;

public class MainActivity extends MvpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFragment(new AppMapFragment());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
