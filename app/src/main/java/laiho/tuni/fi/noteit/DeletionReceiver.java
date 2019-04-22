package laiho.tuni.fi.noteit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * DeletionReceiver receives intents sent by AlarmManager and deletes the notes from storage.
 *
 * @author Lauri Laiho
 * @version 1.0
 * @since 2019-04-22
 */
public class DeletionReceiver extends BroadcastReceiver {

    /**
     * Method is invoked upon receiving an intent from the AlarmManager. Removes all note data
     * using the JsonController.
     *
     * @param context Application context from which the intent is coming from.
     * @param intent Intent from the context.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        JsonController controller = new JsonController(context);
        controller.clearNoteData();
    }
}
