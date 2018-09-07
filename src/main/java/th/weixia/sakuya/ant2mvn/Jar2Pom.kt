package th.weixia.sakuya.ant2mvn

import com.google.common.io.Files
import th.weixia.sakuya.constant.NEXUS_SEARCH_URL
import th.weixia.sakuya.constant.POM_BOTTOM
import th.weixia.sakuya.constant.POM_BOTTOM_PATCH
import th.weixia.sakuya.constant.POM_HEAD
import th.weixia.sakuya.http.get
import th.weixia.sakuya.md5.md5
import th.weixia.sakuya.utils.deleteDir
import java.io.File
import java.util.regex.Pattern


/***
 *
 *  lib 文件夹转 pom.xml 和 lib
 *
 *  @author SongYang
 *
 *  @date 2018-8-23
 *
 */

var basePath = ""

var dependency = StringBuilder()
var notFouds = StringBuilder("<!-- NOT FOUND -->\n")
var installPlugin = StringBuilder("<plugin>\n")
        .append("   <groupId>org.apache.maven.plugins</groupId>\n")
        .append("   <artifactId>maven-install-plugin</artifactId>\n")
        .append("   <version>2.5.2</version>\n")
        .append("   <inherited>false</inherited>\n")
        .append("   <executions>\n")


fun jar2Pom(path: String) {

    basePath = path

    var pomFile = creatPom(path)

    var folder = File(path)

    if (folder.isDirectory) {
        var fileTree = folder.walk()
        fileTree.filter { it.extension.toLowerCase() == "jar" }
                .filter { !it.absolutePath.contains("JAR2POM") }
                .forEach { generateDependencyString(it) }

        pomFile.appendText(buildPomString())
    }
}

fun creatPom(path: String): File {

    deleteDir("$path\\JAR2POM")

    var pomFile = File("$path\\JAR2POM\\pom.xml")

    Files.createParentDirs(pomFile)

    pomFile.createNewFile()

    return pomFile
}

fun generateDependencyString(jar: File) {

    System.out.println("WORKING ON: ${jar.name}")

    var info = get(NEXUS_SEARCH_URL + md5(jar))

    var infoString = info.replaceBefore("<groupId>", "", "404").replaceAfter("</version>", "")

    if (infoString == "404")
        doNotFound(jar)
    else
        dependency.append("<dependency>\n      ")
                .append(infoString)
                .append("\n</dependency>\n")
}

fun doNotFound(jar: File) {

    var name = jar.nameWithoutExtension

    var matcher = Pattern.compile("\\d+(\\.\\d+)*").matcher(name)
    matcher.find()

    var group = ""

    var version = ""

    try {
        var offset = 0

        var start = matcher.start()

        if (name[start - 1] == '-')
            offset = 1

        group = name.substring(0, start - offset)

        version = name.substring(start)

    } catch (e: IllegalStateException) {
        group = jar.nameWithoutExtension

        version = "1.0.0-Devops-Fh"
    }

    var jarPath = jar.absolutePath

    if (version.contains("SNAPSHOT")) {
        version = version.replace("SNAPSHOT", "Devops-Fh")

        jarPath = "$basePath\\$group-$version.jar"

        var newJar = File(jarPath)

        jar.renameTo(newJar)
    }

    notFouds.append("<dependency>\n")
            .append("      <groupId>$group</groupId>\n")
            .append("      <artifactId>$group</artifactId>\n")
            .append("      <version>$version</version>\n")
            .append("</dependency>\n")

    installPlugin
            .append("<execution>\n")
            .append("    <id>install-artifacts.$group</id>\n")
            .append("    <goals>\n")
            .append("        <goal>install-file</goal>\n")
            .append("    </goals>\n")
            .append("    <phase>validate</phase>\n")
            .append("    <configuration>\n")
            .append("        <file>$jarPath</file>\n")
            .append("        <groupId>$group</groupId>\n")
            .append("        <artifactId>$group</artifactId>\n")
            .append("        <packaging>jar</packaging>\n")
            .append("        <version>$version</version>\n")
            .append("    </configuration>\n")
            .append("</execution>\n")
}

fun buildPomString(): String {

    return StringBuilder(POM_HEAD)
            .append(dependency)
            .append(when(notFouds.toString()) {

                "<!-- NOT FOUND -->\n" -> StringBuilder(POM_BOTTOM_PATCH).append(POM_BOTTOM)

                else -> notFouds
                        .append(POM_BOTTOM_PATCH)
                        .append(installPlugin)
                        .append("</executions>\n")
                        .append("   </plugin>\n")
                        .append(POM_BOTTOM)
            })
            .toString()
}