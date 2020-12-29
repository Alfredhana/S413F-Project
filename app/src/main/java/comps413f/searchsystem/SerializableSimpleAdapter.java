package comps413f.searchsystem;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


// SimpleAdapter that implements Serializable
// For supporting communication between activity and fragment
public class SerializableSimpleAdapter extends SimpleAdapter implements Serializable {
    // Constructor
    public SerializableSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }
}
