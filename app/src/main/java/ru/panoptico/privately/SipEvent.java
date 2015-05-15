package ru.panoptico.privately;

import java.util.EventObject;

/**
 * Created by shved on 11/19/14.
 */
public class SipEvent extends EventObject{
    public enum SipEventType {
        MESSAGE,BYE,CALL
    }

    public String content;
    public String from;
    public SipEventType type;
    public SipEvent(Object source,SipEventType type,String content,String from) {
        super(source);
        this.type = type;
        this.content = content;
        this.from = from;
    }
}
