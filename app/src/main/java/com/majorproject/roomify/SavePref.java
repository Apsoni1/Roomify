/**
 * The SavePref class is a helper class in the com.wowcodes.prizex package that provides methods for
 * saving and retrieving data from SharedPreferences in an Android application.
 */
package com.majorproject.roomify;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePref {

    public final String PREFS_NAME = "extraclass_prefs";
    private final Context context;
    public static final String FAVORITES2 = "Image_Favorite";

    public final String FAVORITES = "Notification";

    public SavePref(Context context) {
        this.context = context;
    }


    public String getClasses() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("classes", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setClasses(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("classes", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getClassesName() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("classesname", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setClassesName(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("classesname", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getemail() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("email", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setemail(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserPhone() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("userphone", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setUserPhone(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userphone", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserId() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("userId", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setUserId(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userId", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("name", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setName(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCity(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("city", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCity() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("city", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setCityId(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("cityId", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCityId() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("cityId", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLang() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("lang", "en");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setLang(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("lang", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getMode() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            //return prefs.getBoolean("mode", false);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setMode(boolean mode) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("mode" , mode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getDefaultMode() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            //return prefs.getBoolean("mode", false);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void setThemeMode(boolean isDarkMode) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("theme_mode", isDarkMode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isDarkMode() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getBoolean("theme_mode", false); // Default to light mode
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void setDefaultMode(boolean mode) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("mode" , mode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}