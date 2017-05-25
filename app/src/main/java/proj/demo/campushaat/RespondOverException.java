package proj.demo.campushaat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by stpl on 5/25/2017.
 */

public class RespondOverException implements Thread.UncaughtExceptionHandler {
    private Context context;

    public RespondOverException(Context context) {
        this.context = context;
    }


    protected void launch(Class<? extends Activity> activity, String exception) {

        Intent intent = new Intent(context, activity);


        intent.putExtra("error", exception);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    protected static Class<? extends Activity> getLauncherActivity(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
//                Log.e(TAG, "Error", e);
            }
        }

        return null;
    }





    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        ClipData clip = ClipData.newPlainText("Copied", stackTrace.toString());
        clipboard.setPrimaryClip(clip);
    }
}
