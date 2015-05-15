package ru.panoptico.privately;

import android.content.Context;

/**
 * Created by Дмитрий on 15.05.2015.
 */
public class User {
    private Context _context;

    private String _userName;
    private int _userImage;
    private UserStatus _userStatus;

    public enum UserStatus {
        online,
        offline
    };

    public User(Context context, String name, int image) {
        this._context = context;
        this._userName = name;
        this._userImage = image;
        _userStatus = UserStatus.offline;
    }

    public String getName() {
        return _userName;
    }

    public int getImage() {
        return _userImage;
    }

    public UserStatus getStatus() {
        return _userStatus;
    }

    public String getStatusString() {
        if (_userStatus == UserStatus.online) {
            return _context.getString(R.string.user_status_online);
        }
        if (_userStatus == UserStatus.offline) {
            return _context.getString(R.string.user_status_offline);
        }
        return null;
    }

    public void setStatus(UserStatus status) {
        _userStatus = status;
    }
}
