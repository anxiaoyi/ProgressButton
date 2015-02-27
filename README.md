# ProgressButton

## how to use:
```java
<com.zk.ProgressButton
    android:id="@+id/mProgressBtn1"
    style="@style/ProgressButton"
    android:layout_width="match_parent"
    android:layout_height="10dp"
    android:clickable="true" />
```

## style attributes description:
```java
<style name="ProgressButton">
	<!-- 0.0-1.0 当前进度 -->
    <item name="progress_ratio">0.2</item>

    <!-- 未覆盖的颜色 -->
    <item name="progress_unreached_color">@android:color/transparent</item>

    <!-- 已经覆盖的颜色 -->
    <item name="progress_reached_color">#33b5e5</item>

    <!-- 到1.0的时候按下的颜色 -->
    <item name="progress_finished_pressed_color">#0099cc</item>

    <!-- 没有到1.0的时候按下的颜色 -->
    <item name="progress_unfinished_pressed_color">#f2f2f2</item>

    <!-- 字体大小 -->
    <item name="text_size">20sp</item>

    <!-- 字体颜色 -->
    <item name="text_color">@android:color/white</item>

    <!-- 文字 -->
    <item name="text">begin download</item>

    <!-- 圆角半径，0为直角 -->
    <item name="corner_radius">0dp</item>

    <!-- 是否画边框 -->
    <item name="draw_border">true</item>
</style>
```