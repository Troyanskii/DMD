package com.troyanskiievgen.dmd.ui.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.MapFragment;
import com.troyanskiievgen.dmd.R;
import com.troyanskiievgen.dmd.presenter.MainActivityPresenter;
import com.troyanskiievgen.dmd.ui.fragment.AppMapFragment;
import com.troyanskiievgen.dmd.ui.listeners.ActionBackListener;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
            if (fragment instanceof AppMapFragment) {
                if(((ActionBackListener) fragment).onActionBackPress()) {
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
