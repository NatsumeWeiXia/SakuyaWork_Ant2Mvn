package th.weixia.sakuya.ant2mvn

import java.io.*
import java.util.concurrent.Executors
import java.util.regex.Pattern


class MavenUploader {

    val BASE_CMD = "cmd /c mvn " +
            "-s D:\\Maven\\settings.xml " +
            "deploy:deploy-file " +
            "-Durl=http://172.16.145.128:8081/repository/springboot-hosted/ " +
            "-DrepositoryId=springboot-hosted " +
            "-DgeneratePom=false"

    val DATE_PATTERN = Pattern.compile("-[\\d]{8}\\.[\\d]{6}-")

    val CMD = Runtime.getRuntime()

    val EXECUTOR_SERVICE = Executors.newFixedThreadPool(10)


    fun run(path: String) {

        deploy(File(path).listFiles()!!)

        EXECUTOR_SERVICE.shutdown()

    }


    fun deploy(files: Array<File>) {
        if (files.size == 0) {
            //ignore
        } else if (files[0].isDirectory) {
            for (file in files) {
                if (file.isDirectory) {
                    deploy(file.listFiles()!!)
                }
            }
        } else if (files[0].isFile) {
            var pom: File? = null
            var jar: File? = null
            var source: File? = null
            var javadoc: File? = null
            //忽略日期快照版本，如 xxx-mySql-2.2.6-20170714.095105-1.jar
            for (file in files) {
                val name = file.name
                if (DATE_PATTERN.matcher(name).find()) {
                    //skip
                } else if (name.endsWith(".pom")) {
                    pom = file
                } else if (name.endsWith("-javadoc.jar")) {
                    javadoc = file
                } else if (name.endsWith("-sources.jar")) {
                    source = file
                } else if (name.endsWith(".jar")) {
                    jar = file
                }
            }
            if (pom != null) {
                if (jar != null) {
                    deploy(pom, jar, source, javadoc)
                } else if (packingIsPom(pom)) {
                    deployPom(pom)
                }
            }
        }
    }

    fun packingIsPom(pom: File): Boolean {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(FileInputStream(pom)))
            var line: String = reader.readLine()
            while (line != null) {
                if (line.trim { it <= ' ' }.indexOf("<packaging>pom</packaging>") != -1) {
                    return true
                }
                line = reader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader!!.close()
            } catch (e: Exception) {
            }

        }
        return false
    }

    fun deployPom(pom: File) {
        EXECUTOR_SERVICE.execute {
            val cmd = StringBuffer(BASE_CMD)
            cmd.append(" -DpomFile=").append(pom.name)
            cmd.append(" -Dfile=").append(pom.name)
            try {
                val proc = CMD.exec(cmd.toString(), null, pom.parentFile)
                val inputStream = proc.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(inputStreamReader)
                var line: String = reader.readLine()
                val logBuffer = StringBuffer()
                logBuffer.append("\n\n\n==================================\n")
                while (line != null) {
                    if (line.startsWith("[INFO]") || line.startsWith("Upload")) {
                        logBuffer.append(Thread.currentThread().name + " : " + line + "\n")
                    }
                    line = reader.readLine()
                }
                println(logBuffer)
                val result = proc.waitFor()
                if (result != 0) {
                    error("上传失败：" + pom.absolutePath)
                }
            } catch (e: IOException) {
                error("上传失败：" + pom.absolutePath)
                e.printStackTrace()
            } catch (e: InterruptedException) {
                error("上传失败：" + pom.absolutePath)
                e.printStackTrace()
            }
        }
    }

    fun deploy(pom: File, jar: File?, source: File?, javadoc: File?) {
        EXECUTOR_SERVICE.execute {
            val cmd = StringBuffer(BASE_CMD)
            cmd.append(" -DpomFile=").append(pom.name)
            if (jar != null) {
                //当有bundle类型时，下面的配置可以保证上传的jar包后缀为.jar
                cmd.append(" -Dpackaging=jar -Dfile=").append(jar.name)
            } else {
                cmd.append(" -Dfile=").append(pom.name)
            }
            if (source != null) {
                cmd.append(" -Dsources=").append(source.name)
            }
            if (javadoc != null) {
                cmd.append(" -Djavadoc=").append(javadoc.name)
            }

            try {
                val proc = CMD.exec(cmd.toString(), null, pom.parentFile)
                val inputStream = proc.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                val reader = BufferedReader(inputStreamReader)
                var line: String = reader.readLine()
                val logBuffer = StringBuffer()
                logBuffer.append("\n\n\n==================================\n")
                while (line != null) {
                    if (line.startsWith("[INFO]") || line.startsWith("Upload")) {
                        logBuffer.append(Thread.currentThread().name + " : " + line + "\n")
                    }
                    line = reader.readLine()
                }
                println(logBuffer)
                val result = proc.waitFor()
                if (result != 0) {
                    error("上传失败：" + pom.absolutePath)
                }
            } catch (e: IOException) {
                error("上传失败：" + pom.absolutePath)
                e.printStackTrace()
            } catch (e: InterruptedException) {
                error("上传失败：" + pom.absolutePath)
                e.printStackTrace()
            }
        }
    }
}
