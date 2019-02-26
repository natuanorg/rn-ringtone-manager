package com.natuan;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Promise;

import java.io.File;

public class RNRingtoneManagerModule extends ReactContextBaseJavaModule {

  private static final String TAG = "RingtoneManagerModule";
  private final ReactApplicationContext reactContext;

  final static class SettingsKeys {
    public static final String URI = "uri";
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
  }

  public RNRingtoneManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNRingtoneManager";
  }

  @ReactMethod
  public void settingsCanWrite(final Promise promise) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      boolean settingsCanWrite = Settings.System.canWrite(this.reactContext);
      promise.resolve(settingsCanWrite);
    }
    promise.resolve(true);
  }

  @ReactMethod
  public void requestWriteSettings() {
    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
    intent.setData(Uri.parse("package:" + this.reactContext.getPackageName()));
    this.reactContext.startActivity(intent);
  }

  @ReactMethod
  public void setRingtone(ReadableMap settings) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      boolean settingsCanWrite = Settings.System.canWrite(this.reactContext);
      if (!settingsCanWrite) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + this.reactContext.getPackageName()));
        this.reactContext.startActivity(intent);
        return;
      }
    }

    String uriStr = settings.getString(SettingsKeys.URI);

    File ringtone = new File(uriStr);
    try {
      int fileSize = Integer.parseInt(String.valueOf(ringtone.length() / 1024));
      MediaMetadataRetriever mmr = new MediaMetadataRetriever();
      mmr.setDataSource(uriStr);
      int duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
      Log.i(TAG, "setRingtone.duration: " + duration);
      String mimeType = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
      Log.i(TAG, "setRingtone.mimeType: " + mimeType);
      String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
      Log.i(TAG, "setRingtone.artist: " + artist);
      String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
      Log.i(TAG, "setRingtone.title: " + title);

      ContentValues values = new ContentValues();
      values.put(MediaStore.MediaColumns.DATA, ringtone.getAbsolutePath());
      values.put(MediaStore.MediaColumns.TITLE, !TextUtils.isEmpty(title) ? title : settings.getString(SettingsKeys.TITLE));
      values.put(MediaStore.MediaColumns.SIZE, fileSize);
      values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
      values.put(MediaStore.Audio.Media.ARTIST, !TextUtils.isEmpty(artist) ? artist : settings.getString(SettingsKeys.ARTIST));
      values.put(MediaStore.Audio.Media.DURATION, duration);
      values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
      values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
      values.put(MediaStore.Audio.Media.IS_ALARM, false);
      values.put(MediaStore.Audio.Media.IS_MUSIC, false);

      Log.i(TAG, "values: " + values.toString());

      if (ringtone.exists() && getCurrentActivity() != null) {

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtone.getAbsolutePath());
        Log.d(TAG, "uri: " + uri);
        ContentResolver contentResolver = getCurrentActivity().getContentResolver();
        contentResolver.delete(uri, null, null);
        Uri newUri = contentResolver.insert(uri, values);
        Log.d(TAG, "newUri: " + newUri);
        RingtoneManager.setActualDefaultRingtoneUri(
                getCurrentActivity(),
                RingtoneManager.TYPE_RINGTONE,
                newUri
        );
      }

    } catch (Exception e) {
      Log.d(TAG, "setRingtone.Exception" + e.getMessage());
    }
  }
}