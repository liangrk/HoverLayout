当前最新版本: [![](https://jitpack.io/v/liangrk/HoverLayout.svg)](https://jitpack.io/#liangrk/HoverLayout)
-----

有时项目需要单个或多个控件悬浮在Activity/Fragment上, 在经过项目迭代测试后, 便抽时间把这个控件抽取出来

> how to use
>

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        implementation "com.github.liangrk:HoverLayout:${version}"
}
```

### 用法

> 具体可参考本项目中的 MainActivity.kt/activity_main.xml

```
    <!-- 布局文件中使用. hover_container_layout用于添加悬浮拖动的布局 -->
    <component.kits.view.hover.HoverLayout
        android:id="@+id/hover"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:hover_container_layout="@layout/layout_inflate" />
```

```
    // 代码中监听点击
    binding.hover.setHoverClickListener {
        val params = it?.layoutParams
        val width = params?.width ?: 0
        val height = params?.height ?: 0
        params?.width = width - 10
        params?.height = height - 10
        it?.layoutParams = params
    }
```

--------

### 注意

1. 如果 "hover_container_layout" 属性没有声明 或者传入的布局有问题, 内部会throw IllegalArgumentException
2. HoverLayout只支持一个子view. 如果你使用方式如下:
   > 存在多个子view时, 库中亦会throw IllegalArgumentException

```
    <component.kits.view.hover.HoverLayout
        android:id="@+id/hover"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:hover_container_layout="@layout/layout_inflate">

        <TextView .../>

    </component.kits.view.hover.HoverLayout>
```
