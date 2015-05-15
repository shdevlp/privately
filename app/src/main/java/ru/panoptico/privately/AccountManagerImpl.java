package ru.panoptico.privately;

import android.gov.nist.javax.sip.clientauthutils.AccountManager;
import android.gov.nist.javax.sip.clientauthutils.UserCredentials;
import android.javax.sip.ClientTransaction;

/**
 * Created by shved on 11/19/14.
 */
public class AccountManagerImpl implements AccountManager {

    public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
        return new UserCredentialsImpl(SipStackAndroid.getInstance().sipUserName,SipStackAndroid.getInstance().getRemoteIp(),SipStackAndroid.getInstance().sipPassword);
    }

}
