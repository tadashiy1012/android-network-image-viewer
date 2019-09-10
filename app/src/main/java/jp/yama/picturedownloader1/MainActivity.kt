package jp.yama.picturedownloader1

import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.*
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.OutputStream

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launch {
            val url = "https://s.yimg.jp/images/top/sp2/cmn/logo-170307.png"
            val resp = HttpClient().get(url).await()
            Log.v("yama", resp.code.toString())
            withContext(Dispatchers.Main) {
                val buffer = resp.body?.byteStream()
                val bitmap = BitmapFactory.decodeStream(buffer)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}

class HttpClient {

    fun get(url: String): Deferred<Response> {
        return GlobalScope.async {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            return@async client.newCall(request).execute()
        }
    }

}