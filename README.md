# VmToFtl

convert velocity file to freemarker file 

### 示例:

-d后跟目录,-f后跟文件

-d H:\untitled html --转换H:\untitled目录以及子目录下的所有html文件到ftl文件,自动命名.ftl

-f index.html --转换index.html为index.ftl

-f index.html index.htm --转换index.html为index.htm,最好用ftl,idea能识别方便敲代码

### 关于pattern.txt

存放替换用的正则表达式,用逗号(,)分隔

""用thisisnull来表示

三个#即###表示注释

本代码只是在[cavary](http://doc.justonetech.com/freemarker-2.3.10/docs/usCavalry.html)上加了一点正则替换,感觉好笨,也没找到更好的.

如果有更好的,请告知zhvxiao@163.com,感激不尽.
