package com.mnb.rosterapp.framework

import android.os.Environment
import com.google.gson.Gson
import com.mnb.rosterapp.data.CodexDataSource
import com.mnb.rosterapp.domain.Codex
import java.io.File
import java.io.FileReader

class CodexJsonDataSource : CodexDataSource {

    private var codexCache: Codex? = null

    override suspend fun listCodexes(): List<String> {
        val codexList = ArrayList<String>()
        // currently looking in download directory for shared codexes
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (dir.exists()) {
            // get json codex files
            val fileList = dir.list { dir, file ->
                file.endsWith(".codex.json")
            }
            // drop suffix to get names
            codexList.addAll(fileList.map { it ->
                it.removeSuffix(".codex.json")
            })
        } else {
            System.out.println("OOPS - " + " directory " + dir.getPath() + " does not exist");
        }
        return codexList
    }

    override suspend fun loadCodex(fileName: String): Codex? {
        val cachedName = codexCache?.name
        if (cachedName == null || !cachedName.equals(fileName)) {
            codexCache = readJson(fileName)
        }
        return codexCache
    }

    private fun readJson(fileName: String): Codex? {
        // currently looking in download directory for shared codexes
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (dir.exists()) {
            val reader = FileReader(File(dir, fileName + ".codex.json"))
            val codex = Gson().fromJson(reader, Codex::class.java)
            reader.close()
            return codex
        } else {
            System.out.println("OOPS - " + " directory " + dir.getPath() + " does not exist");
            return null
        }
    }
}

