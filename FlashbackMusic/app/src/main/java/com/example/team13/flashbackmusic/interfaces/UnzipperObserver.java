package com.example.team13.flashbackmusic.interfaces;

import java.util.ArrayList;

/**
 * Created by Kazutaka on 3/3/18.
 */

public interface UnzipperObserver {
    void onUnzipSuccess(ArrayList<String> paths);
    void onUnzipFailure();
}
