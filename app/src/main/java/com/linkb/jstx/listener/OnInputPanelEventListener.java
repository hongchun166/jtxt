
package com.linkb.jstx.listener;


public interface OnInputPanelEventListener {
    void onSendButtonClicked(String content);
    void onMessageInsertAt();
    void onMessageTextDelete(String txt);
    void onKeyboardStateChanged(boolean visable);
    void onPanelStateChanged(boolean switchToPanel);

}
