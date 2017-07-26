package com.troyanskiievgen.dmd.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.troyanskiievgen.dmd.model.MapPoint;

/**
 * Created by Relax on 24.07.2017.
 */

public interface AppMapFragmentView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void addMarkerToMap(MapPoint point, int position);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String error);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onMarkerClick(MapPoint mapPoint);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void collapsePanel();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void expandPanel();
//
//    @StateStrategyType(OneExecutionStateStrategy.class)
//    void hidePanel();

    @StateStrategyType(SingleStateStrategy.class)
    void onBackPress();
}
