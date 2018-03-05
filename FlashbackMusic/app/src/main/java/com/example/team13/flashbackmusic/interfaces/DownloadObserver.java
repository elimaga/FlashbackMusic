package com.example.team13.flashbackmusic.interfaces;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * Created by Kazutaka on 2/28/18.
 */

public interface DownloadObserver {
    void onCompleteDownload(Context context, Uri uri, String mime);
}
