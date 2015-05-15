package ru.panoptico.privately;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Дмитрий on 15.05.2015.
 */
public class UsersAdapter extends ArrayAdapter<User> {
    private Context _context;

    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        _context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView userName = (TextView) convertView.findViewById(R.id.user_name);
        ImageView userImage = (ImageView) convertView.findViewById(R.id.user_image);
        TextView userStatus = (TextView) convertView.findViewById(R.id.user_status);

        userName.setText(user.getName());
        userImage.setImageResource(user.getImage());
        userStatus.setText(user.getStatusString());

        return convertView;
    }
}
