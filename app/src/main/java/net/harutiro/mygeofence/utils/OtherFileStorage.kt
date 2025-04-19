package net.harutiro.mygeofence.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class OtherFileStorage(context: Context) {

    val fileAppend : Boolean = true //true=追記, false=上書き
    val context:Context = context
    var fileName : String = DateUtils.getNowDate()
    val extension : String = ".csv"
    val filePath: String = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString().plus("/").plus(fileName).plus(extension) //内部ストレージのDocumentのURL
    @SuppressLint("NewApi")
    val baceTime: OffsetDateTime = OffsetDateTime.now()

    init {
        writeText(firstLog(),filePath)
    }

    @SuppressLint("NewApi")
    fun doLog(text: String) {
        writeText(ChronoUnit.MILLIS.between(baceTime,OffsetDateTime.now()).toString().plus(",").plus(text), filePath)
    }

    //CSV一行目の出力をする。
    private fun firstLog():String {
        return "time,energy,latitude,longitude,result,isSensing"
    }

    //外部ストレージにファイル出力をする関数
    private fun writeText(text:String, path:String){
        val fil = FileWriter(path,fileAppend)
        val pw = PrintWriter(BufferedWriter(fil))
        pw.println(text)
        pw.close()
    }
}