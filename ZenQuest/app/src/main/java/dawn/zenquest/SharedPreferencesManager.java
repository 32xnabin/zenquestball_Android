package dawn.zenquest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Navin
 */
public class SharedPreferencesManager {

    
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;
    public String NAME_PREFS = "ROUNDMAIZE";

    public SharedPreferencesManager(Context context) {
        mSharedPreference = context.getSharedPreferences(NAME_PREFS, Context.MODE_WORLD_WRITEABLE);
    }


   
    public void setLevel(int level) {
        mEditor = mSharedPreference.edit();
        mEditor.putInt("level", level);
        mEditor.commit();
    }
    
    public int getLevel(){    	
    	
    	return mSharedPreference.getInt("level", 1);
    }
    
    public void setClearedLevel(int level) {
        mEditor = mSharedPreference.edit();
        mEditor.putInt("level", level);
        mEditor.commit();
    }
    
    public int getClearedLevel(){    	
    	
    	return mSharedPreference.getInt("level", 0);
    }

  
}