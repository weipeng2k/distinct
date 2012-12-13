distinct
========

一个能够扫描你代码中无用方法的工具
通过输入本地磁盘的项目，将其载入DB，然后运行扫描，可以获取无用方法

注意：
扫描出的内容，仅是提示，需要结合实际进行判断，该工具还是主要提供了发现无用代码的过程。

使用方式：
(1)下载distinct.jar运行

(2)构建工程：
git clone https://github.com/weipeng2k/distinct.git
cd distinct
mvn clean install
or
mvn eclipse:eclipse ==> import to eclipse. run com.murdock.tools.distinct.Main
