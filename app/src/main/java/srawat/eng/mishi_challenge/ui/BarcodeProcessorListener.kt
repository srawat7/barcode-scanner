package srawat.eng.mishi_challenge.ui

import com.google.mlkit.vision.barcode.Barcode
import com.google.zxing.Result

interface BarcodeProcessorListener {
    fun onSuccess(barcodes: List<Barcode>)
    fun onFailure(e: Exception)
    fun onResultSuccess(result: Result)
}