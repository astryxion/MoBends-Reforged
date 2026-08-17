package net.gobbob.mobends;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.gobbob.mobends.util.BendsLogger;

public class WebAPI {
   public static final WebAPI INSTANCE = new WebAPI();
   private static final String API_URL = "https://raw.githubusercontent.com/mobends/mobends-resources/master/static-api.json";
   private boolean initialized = false;
   private APIData data;

   private void initialize() {
      if (this.initialized) {
         return;
      }

      try {
         URL url = new URL(API_URL);
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setRequestMethod("GET");
         connection.setConnectTimeout(4000);
         connection.setReadTimeout(4000);
         connection.connect();
         BufferedReader json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         this.data = (APIData)(new Gson()).fromJson(json, APIData.class);
         json.close();
      } catch (Exception e) {
         BendsLogger.log("Couldn't fetch the WebAPI data. Some features may be disabled.", BendsLogger.INFO);
         e.printStackTrace();
      }

      this.initialized = true;
   }

   public String getOfficialAnimationEditorUrl() {
      this.initialize();
      return this.data == null ? null : this.data.officialAnimationEditorUrl;
   }

   private static class APIData {
      String officialAnimationEditorUrl = null;
   }
}
