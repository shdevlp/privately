package ru.panoptico.privately;

import android.gov.nist.javax.sip.clientauthutils.UserCredentials;

/**
 * Created by shved on 11/19/14.
 */
public class UserCredentialsImpl implements UserCredentials{
    private String userName;
    private String sipDomain;
    private String password;

    public UserCredentialsImpl(String userName, String sipDomain, String password) {
        this.userName = userName;
        this.sipDomain = sipDomain;
        this.password = password;
    }


    public String getPassword() {
        return password;
    }


    public String getSipDomain() {
        return sipDomain;
    }


    public String getUserName() {

        return userName;
    }

}
