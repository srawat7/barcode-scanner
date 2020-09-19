package srawat.eng.mishi_challenge.ui

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer

class ZxingProcessor(
    val barcodeProcessorListener: BarcodeProcessorListener
) : ImageAnalysis.Analyzer {

    companion object {
        val reader = MultiFormatReader()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        try {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            imageProxy.image?.let {
                if ((it.format == ImageFormat.YUV_420_888
                            || it.format == ImageFormat.YUV_422_888
                            || it.format == ImageFormat.YUV_444_888)
                    && it.planes.size == 3) {
                    val buffer = it.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)
                    // Create a LuminanceSource.
                    val rotatedImage = RotatedImage(bytes, imageProxy.width, imageProxy.height)

                    rotateImageArray(rotatedImage, rotationDegrees)

                    val source = PlanarYUVLuminanceSource(rotatedImage.byteArray,
                        rotatedImage.width,
                        rotatedImage.height,
                        0,
                        0,
                        rotatedImage.width,
                        rotatedImage.height
                        ,
                        false)


                    val binarizer = HybridBinarizer(source)
                    val binaryBitmap = BinaryBitmap(binarizer)
                    val result: Result
                    try {
                        result = reader.decode(binaryBitmap)
                        Log.d("Shivam", "Detectd a bar code")
                        Log.d("Shivam", result.barcodeFormat?.name)
                        barcodeProcessorListener.onResultSuccess(result)
                    } catch (e: NotFoundException) {
                        //e.printStackTrace()
                    }
//                    Log.d("Shivam", "Nothing was detected")
                } else {

                }
            }
        } catch (ise: IllegalStateException) {
            ise.printStackTrace()
        } finally {
            imageProxy.close()
        }
    }

    private fun rotateImageArray(imageToRotate: RotatedImage, rotationDegrees: Int) {
        if (rotationDegrees == 0) return
        if (rotationDegrees % 90 != 0) return

        val width = imageToRotate.width
        val height = imageToRotate.height

        val rotatedData = ByteArray(imageToRotate.byteArray.size)
        for (y in 0 until height) { // we scan the array by rows
            for (x in 0 until width) {
                when (rotationDegrees) {
                    90 -> rotatedData[x * height + height - y - 1] =
                        imageToRotate.byteArray[x + y * width] // Fill from top-right toward left (CW)
                    180 -> rotatedData[width * (height - y - 1) + width - x - 1] =
                        imageToRotate.byteArray[x + y * width] // Fill from bottom-right toward up (CW)
                    270 -> rotatedData[y + x * height] =
                        imageToRotate.byteArray[y * width + width - x - 1] // The opposite (CCW) of 90 degrees
                }
            }
        }

        imageToRotate.byteArray = rotatedData

        if (rotationDegrees != 180) {
            imageToRotate.height = width
            imageToRotate.width = height
        }
    }
}

private data class RotatedImage(var byteArray: ByteArray, var width: Int, var height: Int)