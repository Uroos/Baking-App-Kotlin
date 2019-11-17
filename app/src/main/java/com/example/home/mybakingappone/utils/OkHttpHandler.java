package com.example.home.mybakingappone.utils;
//
//import android.os.AsyncTask;
//
//import com.example.home.mybakingappone.SimpleIdlingResource;
//
//import org.jetbrains.annotations.Nullable;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public final class OkHttpHandler extends AsyncTask<String, Void, String> {
//
//    OnUpdateListener listener;
//    SimpleIdlingResource idlingResource;
//
//    public interface OnUpdateListener {
//        void onUpdate(String s);
//    }
//
//    public void setUpdateListener(OnUpdateListener listener,@Nullable final SimpleIdlingResource idlingResource) {
//        this.idlingResource=idlingResource;
//        this.listener = listener;
//    }
//
//    @Override
//    protected String doInBackground(String... s) {
//        if (idlingResource != null) {
//            idlingResource.setIdleState(false);
//        }
//        OkHttpClient client = new OkHttpClient();
//        Response response = null;
//        Request request = new Request.Builder()
//                .url(s[0])
//                .build();
//        try {
//            response = client.newCall(request).execute();
//            return response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//        if (listener != null) {
//            if (idlingResource != null) {
//                idlingResource.setIdleState(true);
//            }
//            listener.onUpdate(s);
//        }
//    }
//}
