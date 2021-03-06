package com.troyanskiievgen.dmd.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.troyanskiievgen.dmd.view.MainActivityView;

/**
 * Created by Relax on 24.07.2017.
 */

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    public void showMapFragment() {
        getViewState().showMapFragment();
    }
}
