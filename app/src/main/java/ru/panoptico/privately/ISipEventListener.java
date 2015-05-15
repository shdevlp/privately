package ru.panoptico.privately;

import java.util.EventListener;

/**
 * Created by shved on 11/19/14.
 */
public interface ISipEventListener extends EventListener {
    public void onSipMessage(SipEvent sipEvent);
}
