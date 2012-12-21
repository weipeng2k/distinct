distinct
========

一个能够扫描你代码中无用方法的工具

通过输入本地磁盘的项目，将其载入DB，然后运行扫描，可以获取无用方法，目前使用HyperSQL在内存中存储

相比原有使用redis更加迅速、快捷和少约束。

注意：

扫描出的内容，仅是提示，需要结合实际进行判断，该工具还是主要提供了发现无用代码的过程。

使用方式：

(1)下载distinct.jar运行


(2)构建工程：
需要安装git，如果是ubuntu 可以sudo apt-get install git

git clone https://github.com/weipeng2k/distinct.git

cd distinct

mvn clean install

or

mvn eclipse:eclipse ==> import to eclipse. run com.murdock.tools.distinct.Main

![结果](http://photo2.bababian.com/upload6/20121214/DB99B2CAC65C6BD64D7DC2560A1AA33D_500.jpg)
