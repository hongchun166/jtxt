
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class ToMessageMapView extends BaseToMessageView {

    public ToMessageMapView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return mapView;
    }

    @Override
    public void displayMessage() {

        mapView.initViews(message);
        mapView.setOnClickListener(mapView);
    }

}
