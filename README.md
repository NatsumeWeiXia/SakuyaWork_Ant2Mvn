# SakuyaWork_Ant2Mvn

A tool helps you to convert ant project to maven project

咲夜的工作

本小工具可以帮助你在转换 ANT 工程为 MAVEN 工程时自动根据 lib文件夹生成 POM 文件

- 在maven中央仓库采用checksum搜索方式查询JAR包并获取引用参数
- 无法查询到的生成安装至本地仓库的脚本并添加引用
- 使用内置的上传工具上传至私有仓库后删除pom中的安装至本地仓库部分即可使用

可以帮助简化工程转换，特别是内网环境下的繁杂手动操作

使用方法：

- 本地新建MAVEN工程，将本工程代码及pom拷入

- 修改Sakuya.kt中的路径指向要转换的jar文件夹

- 运行工具，会在jar文件夹下生成JAR2POM文件夹内有转换好的pom文件

- 新建空MAVEN工程，将生成的pom文件拷贝进工程，并将 YOUR_XXX 字样的字段修改为你的实际字段

      <groupId>#YOUR_GROUP_ID#</groupId>
      <artifactId>#YOUR_ARTIFACT_ID#</artifactId>
      <version>#YOUR_VERSION#</version>
      
      ...
      
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-resources-plugin</artifactId>
        <version>#YOUR_VERSION# (3.0.2)</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>#YOUR_VERSION# (3.7.0)</version>
        <configuration>
            <source>#YOUR_VERSION# (1.8)</source>
            <target>#YOUR_VERSION# (1.8)</target>
         </configuration>
      </plugin>

- reimport 下载pom内的依赖至本地仓库，执行 build 安装找不到的jar包至本地仓库（如果后续要上传至私有仓库，可重新制定本地仓库目录后执行）

- 删除生成的pom文件中的安装至本地仓库脚本后即为最终pom文件

- 将本地仓库上传至私有仓库，在maven的settings中配置好私有仓库即可使用
