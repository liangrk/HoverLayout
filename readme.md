设计需求:
可滑动范围受此控件的父view决定.

1.可以动态跟xml方式添加; 2.内容通过layout引用添加

--------
本库内部依赖以下库编译(ViewCompat):
使用请添加:

androidx.appcompat:appcompat:1.3.1


--------
throwable:
当container为空时 会触发 IllegalArgumentException