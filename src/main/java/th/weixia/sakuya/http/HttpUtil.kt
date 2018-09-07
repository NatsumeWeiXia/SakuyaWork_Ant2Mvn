package th.weixia.sakuya.http

import java.net.URL
import kotlin.text.Charsets.UTF_8


/***
 *
 *  网络接口
 *
 *  @author SongYang
 *
 *  @date 2018-8-23
 *
 */

fun get(url: String): String {

    return URL(url).readText(charset = UTF_8)
}