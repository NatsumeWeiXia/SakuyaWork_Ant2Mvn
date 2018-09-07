# SakuyaWork_Ant2Mvn

A tool helps you to convert ant project to maven project

咲夜的工作

本小工具可以帮助你在转换 ANT 工程为 MAVEN 工程时自动根据 lib文件夹生成 POM 文件

- 在maven中央仓库采用checksum搜索方式查询JAR包并获取引用参数
- 无法查询到的生成安装至本地仓库的脚本并添加引用
- 使用内置的上传工具上传至私有仓库后删除pom中的安装至本地仓库部分即可使用

可以帮助简化工程转换，特别是内网环境下的繁杂手动操作
