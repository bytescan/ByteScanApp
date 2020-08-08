package com.bytescanapp.bytescan

import android.app.Application
import android.util.Log
import android.widget.Toast
import org.opencv.android.InstallCallbackInterface
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.OpenCVLoader.initAsync

class StarterApplication : Application() {

    companion object {
        const val TAG = "StarterApplication"
    }
}