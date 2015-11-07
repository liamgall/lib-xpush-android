package io.xpush.chat;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import io.xpush.chat.common.Constants;
import io.xpush.chat.core.XPushCore;
import io.xpush.chat.R;

@ReportsCrashes( mailTo = "xpush.io@gmail.com",
        mode = ReportingInteractionMode.NOTIFICATION)
public class ApplicationController extends Application {

    public static final String TAG = ApplicationController.class.getSimpleName();
    private static ApplicationController mInstance;

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ACRA.init(this);
        XPushCore.initialize(this);
    }
}