package com.mnb.rosterapp.framework

import android.os.Environment
import com.google.gson.Gson
import com.mnb.rosterapp.data.ArmyDataSource
import com.mnb.rosterapp.domain.Army
import com.mnb.rosterapp.domain.Unit
import java.io.*

class ArmyJsonDataSource : ArmyDataSource {

    private var armyCache: Army? = null

    override suspend fun listArmies(): List<String> {
        val armyList = ArrayList<String>()
        val dir = Environment.getExternalStorageDirectory()
        if (dir.exists()) {
            // get json files
            val fileList = dir.list { dir, file ->
                file.endsWith(".json")
            }
            // drop suffix to get names
            armyList.addAll(fileList.map { it ->
                it.removeSuffix(".json")
            })
        } else {
            System.out.println("OOPS - " + " directory " + dir.getPath() + " does not exist");
        }
        return armyList
    }

    override suspend fun loadArmy(fileName: String): Army? {
        val cachedName = armyCache?.name
        if (cachedName == null || !cachedName.equals(fileName)) {
            armyCache = readJson(fileName)
        }
        return armyCache
    }

    override suspend fun newArmy(name: String): Army {
        val army = Army(name, HashMap<String, Unit>())
        writeJson(army)
        armyCache = army
        return army
    }

    override suspend fun saveArmy(army: Army): Army {
        writeJson(army)
        armyCache = army
        return army
    }

    private fun readJson(fileName: String): Army? {
        // TODO: split out into json file access class
        val dir = Environment.getExternalStorageDirectory()
        if (dir.exists()) {
            val reader = FileReader(File(dir, fileName + ".json"))
            val army = Gson().fromJson(reader, Army::class.java)
            reader.close()
            return army
        } else {
            System.out.println("OOPS - " + " directory " + dir.getPath() + " does not exist");
            return null
        }
    }

    private fun writeJson(army: Army) {
        // TODO: split out into json file access class
        val dir = Environment.getExternalStorageDirectory()
        if (dir.exists()) {
            val writer = FileWriter(File(dir, army.name + ".json"))
            Gson().toJson(army, writer)
            writer.close()
        } else {
            System.out.println("OOPS - " + " directory " + dir.getPath() + " does not exist");
        }
    }
}