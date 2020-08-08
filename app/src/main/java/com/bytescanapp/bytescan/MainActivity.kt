package com.bytescanapp.bytescan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface.SUCCESS
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


class MainActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private val baseLoaderCallback by lazy {
        object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    SUCCESS -> camera_view.enableView()
                    else -> super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera_view.setCameraPermissionGranted()
        camera_view.setCvCameraViewListener(this)
    }

    private var mRgba: Mat? = null
    private var mRgbaT: Mat? = null
    private var dst: Mat? = null

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mRgbaT = Mat()
        dst = Mat()
    }

    override fun onCameraViewStopped() {
        mRgba?.release()
        mRgbaT?.release()
        dst?.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        inputFrame?.let {
            mRgba = inputFrame.rgba()
            val tempMat = mRgba?.t()
            Core.flip(tempMat, mRgbaT, 1)
            Imgproc.resize(mRgbaT, dst, mRgba?.size())
            Imgproc.cvtColor(dst,dst,Imgproc.COLOR_RGB2GRAY)
            Imgproc.Canny(dst,dst,255.0,80.0)
            tempMat?.release()
            return dst!!
        }
        return Mat()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera_view?.disableView()
    }


    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            baseLoaderCallback.onManagerConnected(SUCCESS)
            Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            OpenCVLoader.initAsync(
                OpenCVLoader.OPENCV_VERSION,
                this,
                baseLoaderCallback
            )
        }
    }
}
