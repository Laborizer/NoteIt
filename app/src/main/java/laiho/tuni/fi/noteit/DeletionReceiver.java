package laiho.tuni.fi.noteit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeletionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        JsonController controller = new JsonController(context);
        controller.clearNoteData();
    }
}
