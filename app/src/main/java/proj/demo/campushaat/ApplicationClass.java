package proj.demo.campushaat;

import android.app.Application;

/**
 * Created by stpl on 5/25/2017.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//         Thread.setDefaultUncaughtExceptionHandler(new RespondOverException(this.getApplicationContext()));

    }
}
