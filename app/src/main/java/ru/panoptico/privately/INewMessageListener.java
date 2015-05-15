package ru.panoptico.privately;

import java.util.EventListener;

/**
 * Created by shved on 11/25/14.
 */
public interface INewMessageListener extends EventListener {
    void onMessage(String from);
}
