
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.support.annotation.RequiresApi;

public class MyIntentBroadcast {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void broadcast(Intent intent, Context context, UserHandle user,
                          BroadcastReceiver resultReceiver, Handler scheduler, int initialCode,
                          String initialData, Bundle initialExtras,
                          String broadcastPermission) {

        context.sendBroadcast(intent, broadcastPermission);
        context.sendBroadcastAsUser(intent, user, broadcastPermission);
        context.sendOrderedBroadcast(intent, broadcastPermission);
        context.sendOrderedBroadcastAsUser(intent, user,broadcastPermission, resultReceiver,
                scheduler, initialCode, initialData, initialExtras);
    }
}
