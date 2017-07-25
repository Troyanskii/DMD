package com.troyanskiievgen.dmd.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Relax on 24.07.2017.
 */

public interface MainActivityView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMapFragment();
}
