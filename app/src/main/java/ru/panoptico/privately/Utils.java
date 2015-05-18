package ru.panoptico.privately;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import java.util.Random;

/**
 * Created by Дмитрий on 17.05.2015.
 */
public class Utils {
    private static Utils _instance = null;
    private Random _random = null;

    private Utils() {
        super();
        _random = new Random();
    }

    public static Utils getInstance() {
        if (_instance == null) {
            _instance = new Utils();
        }
        return _instance;
    }

    public int getRandomInt(int n) {
        return _random.nextInt(n);
    }

    public boolean getRandomBoolean() {
        return _random.nextBoolean();
    }

    public void setHeaderActionBar(Activity activity, String heading, boolean backBtn) {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(backBtn);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.slateblue)));
        actionBar.setTitle(heading);
        actionBar.show();
    }
}
