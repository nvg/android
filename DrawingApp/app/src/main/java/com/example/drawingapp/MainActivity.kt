package com.example.drawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.iterator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPaint: ImageButton? = null

    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val result = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return result
    }

    private fun requestStoragePermission() {
        var permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions.toString())) {
            Toast.makeText(
                this,
                "Need permissions to add a background image",
                Toast.LENGTH_LONG
            ).show()
            return@requestStoragePermission
        }

        ActivityCompat.requestPermissions(this, permissions, STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var message = ""
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                message = "Permission is granted - you can read the storage now"
            } else {
                message = "Oops, you just denied the permission"
            }
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isReadStorageAllowed(): Boolean {
        var result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        drawing_view.setBrushSize(5f)
        mImageButtonCurrentPaint = ll_paint_colors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pallet_pressed
            )
        )

        ib_brush.setOnClickListener {
            showBrushSizeSelectorDialog()
        }

        for (btn in ll_paint_colors) {
            btn.setOnClickListener { v: View -> paintClicked(v) }
        }

        ib_gallery.setOnClickListener {
            if (!isReadStorageAllowed()) {
                requestStoragePermission()
                return@setOnClickListener
            }

            val pickPhotoIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhotoIntent, GALLERY)
        }

        ib_undo.setOnClickListener {
            drawing_view.undo()
        }

        ib_save.setOnClickListener {
            if (isReadStorageAllowed()) {
                BitmapAsyncTaks(getBitmapFromView(fl_drawing_view_container)).execute()
            } else {
                requestStoragePermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                try {
                    if (data!!.data != null) {
                        iv_background.visibility = View.VISIBLE
                        iv_background.setImageURI(data.data)
                    } else {
                        Toast.makeText(this@MainActivity, "Error parsing image", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("", "", e)
                }
            }
        }
    }

    fun paintClicked(view: View) {
        if (view === mImageButtonCurrentPaint) {
            return
        }

        val imageButton = view as ImageButton
        val colorTag = imageButton.tag.toString()
        drawing_view.setColor(colorTag)
        imageButton.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_normal)
        )
        mImageButtonCurrentPaint = view
    }

    private fun showBrushSizeSelectorDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")

        brushDialog.ib_small_brush.setOnClickListener {
            drawing_view.setBrushSize(5f)
            brushDialog.dismiss()
        }

        brushDialog.ib_medium_brush.setOnClickListener {
            drawing_view.setBrushSize(12f)
            brushDialog.dismiss()
        }

        brushDialog.ib_large_brush.setOnClickListener {
            drawing_view.setBrushSize(20f)
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    private inner class BitmapAsyncTaks(val mBitmap: Bitmap) : AsyncTask<Any, Void, String>() {

        private lateinit var mProgressDialog: Dialog

        private fun showProgressDialog() {
            mProgressDialog = Dialog(this@MainActivity)
            mProgressDialog.setContentView(R.layout.dialog_custom_progress)
            mProgressDialog.show()
        }

        private fun hideProgressDialog() {
            mProgressDialog.dismiss()
        }

        override fun doInBackground(vararg params: Any?): String {
            var result = ""
            if (mBitmap == null) {
                return result
            }

            val baos = ByteArrayOutputStream()
            baos.use {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 89, baos)
                val f = File(
                    externalCacheDir!!.absoluteFile.toString()
                            + File.separator + "DrawingApp_"
                            + System.currentTimeMillis() / 1000 + ".png"
                )

                val fos = FileOutputStream(f)
                fos.use {
                    fos.write(baos.toByteArray())
                }

                result = f.absolutePath
            }
            return result
        }

        override fun onPreExecute() {
            showProgressDialog()
        }

        override fun onPostExecute(result: String?) {
            hideProgressDialog()

            var message = "File saved successfully " + result
            if (result?.isEmpty()!!) {
                message = "There was an error saving file"
            }
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()

            if (!result?.isEmpty()!!) {
                MediaScannerConnection.scanFile(
                    this@MainActivity,
                    arrayOf(result),
                    null
                ) { path, uri ->
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    shareIntent.type = "image/png"
                    startActivity(Intent.createChooser(shareIntent, "Share"))
                }
            }
        }
    }
}
