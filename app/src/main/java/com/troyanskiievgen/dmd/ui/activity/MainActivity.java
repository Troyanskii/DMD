package com.troyanskiievgen.dmd.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.troyanskiievgen.dmd.R;
import com.troyanskiievgen.dmd.presenter.MainActivityPresenter;
import com.troyanskiievgen.dmd.ui.fragment.AppMapFragment;
import com.troyanskiievgen.dmd.view.MainActivityView;

public class MainActivity extends MvpActivity implements MainActivityView{

    @InjectPresenter
    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityPresenter.showMapFragment();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showMapFragment() {
        openFragment(AppMapFragment.getInstance());
    }
}
