package th.weixia.sakuya.md5

import com.google.common.hash.Hashing
import com.google.common.io.Files
import java.io.File

/***
 *
 *  MD5
 *
 *  @author SongYang
 *
 *  @date 2018-8-23
 *
 */

fun md5(file: File): String {

    return Files.asByteSource(file).hash(Hashing.sha1()).toString()

}