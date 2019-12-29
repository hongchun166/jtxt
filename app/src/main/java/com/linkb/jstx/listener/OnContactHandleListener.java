
package com.linkb.jstx.listener;


import com.linkb.jstx.model.MessageSource;

public interface OnContactHandleListener {
    void onContactClicked(MessageSource source);

    boolean onContactSelected(MessageSource source);

    void onContactCanceled(MessageSource source);
}
