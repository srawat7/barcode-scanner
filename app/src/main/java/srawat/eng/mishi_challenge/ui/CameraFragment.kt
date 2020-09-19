package srawat.eng.mishi_challenge.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.barcode.Barcode
import com.google.zxing.Result
import kotlinx.android.synthetic.main.fragment_preview.*
import srawat.eng.mishi_challenge.R

class CameraFragment : Fragment() {

    lateinit var mContext: Context
    lateinit var viewModel: HomeViewModel

    private var previewView: PreviewView? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    private var previewUseCase: Preview? = null
    private var barcodeUseCase: ImageAnalysis? = null

    private var needUpdateGraphicOverlay: Boolean = false

    private var processBarcode = true
    private var isCurrentFragment = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Shivam", "camera fragment")
        val view = inflater.inflate(R.layout.fragment_preview, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previewView = view.findViewById(R.id.preview_view)

        //        cameraProvider = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        //cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        initObservers()
        initUiListeners()
        //checkPermissions()

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun initUiListeners() {

    }

    private fun initObservers() {

        viewModel.getProcessCameraProvider().observe(activity!!, Observer {
            cameraProvider = it
            bindUseCases()
        })

        viewModel.items.observe(requireActivity(), Observer {
            Handler().postDelayed({
                processBarcode = true
                hideProgressBar()
                if (!it.error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                }
            }, 4000)

        })
    }



    private fun bindUseCases() {
        if (cameraProvider == null) return

        cameraProvider!!.unbindAll()
        bindPreviewUseCase()
//        bindBarcodeUseCase()
        bindBarcodeAnalyzer()
    }

    private fun bindBarcodeAnalyzer() {
        val builder = ImageAnalysis.Builder()
        //TODO: target analysis size

        barcodeUseCase = builder.build()

        needUpdateGraphicOverlay = true


        barcodeUseCase?.setAnalyzer(
            ContextCompat.getMainExecutor(mContext),
            ZxingProcessor(object : BarcodeProcessorListener {
                override fun onSuccess(barcodes: List<Barcode>) {
                    Log.d("Shivam", "On Success")
                }

                override fun onFailure(e: Exception) {
                    Log.d("Shivam", "On failure")
                }

                override fun onResultSuccess(result: Result) {
                    if(processBarcode && isCurrentFragment) {
                        processBarcode = false
                        Log.d("Shivam", "On result success")
                        Toast.makeText(
                            this@CameraFragment.mContext,
                            "Result is ${result.text}",
                            Toast.LENGTH_SHORT
                        ).show()
                        addItemToCart()
                    }
                }

            })
        )

        cameraProvider!!.bindToLifecycle(this, cameraSelector!!, barcodeUseCase)

    }

    private fun addItemToCart() {
//        viewModel.
        viewModel.addItemById()
        showProgressBar()
    }

    private fun showProgressBar() {
        progress_layout.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progress_layout.visibility = View.INVISIBLE
    }

    private fun bindPreviewUseCase() {
        if (previewUseCase != null)
            cameraProvider?.unbind(previewUseCase)

        previewUseCase = Preview.Builder().build()
        previewUseCase?.setSurfaceProvider(previewView!!.createSurfaceProvider())
        cameraProvider!!.bindToLifecycle(this, cameraSelector!!, previewUseCase)
    }

    fun isCurrentFragment(value: Boolean) {
        this.isCurrentFragment = value
    }
}