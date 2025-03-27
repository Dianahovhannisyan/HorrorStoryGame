package com.example.quiz;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JsonHelper {
    public static List<Scene> loadStory(Context context) {
        try {

            InputStream inputStream = context.getAssets().open("story.json");
            InputStreamReader reader = new InputStreamReader(inputStream);


            return new Gson().fromJson(reader, new TypeToken<List<Scene>>(){}.getType());
        } catch (Exception e) {
            Log.e("JsonHelper", "Ошибка загрузки JSON", e);
            return null;
        }
    }
}
