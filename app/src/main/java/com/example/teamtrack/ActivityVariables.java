package com.example.teamtrack;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class ActivityVariables {
    View mView;
    Context mContext;

    Activity mActivity;

    ActivityVariables(View view, Context context, Activity activity){
        mView =view;
        mContext =context;
        mActivity = activity;
    }

}
