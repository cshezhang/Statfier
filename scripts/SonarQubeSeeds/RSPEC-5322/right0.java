
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

public class MyIntentReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void register(Context context, BroadcastReceiver receiver,
                         IntentFilter filter,
                         String broadcastPermission,
                         Handler scheduler,
                         int flags) {

        context.registerReceiver(receiver, filter, broadcastPermission, scheduler);
        context.registerReceiver(receiver, filter, broadcastPermission, scheduler, flags);
    }
}
