package th.weixia.sakuya.constant


/***
 *
 *  常量
 *
 *  @author SongYang
 *
 *  @date 2018-8-23
 *
 */

val NEXUS_SEARCH_URL = "https://repository.sonatype.org/service/local/lucene/search?_dc=1534989648415&sha1="

val FHNEXUS_SEARCH_URL = "http://192.168.100.231:8080/nexus/service/local/lucene/search?_dc=1535004524944&sha1="

val POM_HEAD =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
        "    <modelVersion>4.0.0</modelVersion>\n" +
        "\n" +
        "    <groupId>YOUR_GROUP_ID</groupId>\n" +
        "    <artifactId>YOUR_ARTIFACT_ID</artifactId>\n" +
        "    <version>YOUR_VERSION</version>\n" +
        "    <packaging>war</packaging>\n" +
        "\n" +
        "    <dependencies>"

val POM_BOTTOM_PATCH =
        "    </dependencies>\n" +
        "\n" +
        "    <build>\n" +
        "        <plugins>\n"

val POM_BOTTOM =
        "            <plugin>\n" +
        "                <groupId>org.apache.maven.plugins</groupId>\n" +
        "                <artifactId>maven-resources-plugin</artifactId>\n" +
        "                <version>YOUR_VERSION (3.0.2)</version>\n" +
        "            </plugin>\n" +
        "            <plugin>\n" +
        "                <groupId>org.apache.maven.plugins</groupId>\n" +
        "                <artifactId>maven-compiler-plugin</artifactId>\n" +
        "                <version>YOUR_VERSION (3.7.0)</version>\n" +
        "                <configuration>\n" +
        "                    <source>YOUR_VERSION (1.8)</source>\n" +
        "                    <target>YOUR_VERSION (1.8)</target>\n" +
        "                </configuration>\n" +
        "            </plugin>\n" +
        "        </plugins>\n" +
        "    </build>\n" +
        "</project>"