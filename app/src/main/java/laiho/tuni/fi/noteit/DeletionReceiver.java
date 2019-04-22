package laiho.tuni.fi.noteit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DeletionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm!", Toast.LENGTH_SHORT);
        JsonController controller = new JsonController(context);
        controller.clearNoteData();
    }
}
