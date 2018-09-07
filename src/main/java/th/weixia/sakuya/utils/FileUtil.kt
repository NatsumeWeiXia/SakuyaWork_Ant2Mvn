package th.weixia.sakuya.utils

import java.io.File


/***
 *
 *  文件处理
 *
 *  @author SongYang
 *
 *  @date 2018-8-23
 *
 */

fun deleteDir(dirpath: String) {
    val dir = File(dirpath)
    deleteDirWihtFile(dir)
}

fun deleteDirWihtFile(dir: File?) {
    if (dir!!.checkFile())
        return
    for (file in dir.listFiles()) {
        if (file.isFile)
            file.delete()
        else if (file.isDirectory)
            deleteDirWihtFile(file)
    }
    dir.delete()
}

private fun File.checkFile(): Boolean {
    return this == null || !this.exists() || !this.isDirectory
}