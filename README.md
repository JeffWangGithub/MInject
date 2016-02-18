###概述

MInject为一个方便View注入，点击事件注入的库。与ButterKnift类似。
与ButterKnift相比优点：
- 可在任意类中使用，不局限与Activity/Fragment/Dialog
- 可单独设置View注入或者点击事情方法注入，也可以同时设置View注入和点击事情注入
- 支持设置点击事件的点击抖动时间（在多长时间内不可连续点击）

###导入

####方式一： 使用本地MInject.aar文件
1. 下载MInject.aar，将此文件放置到工程libs文件夹下
2. 给点前Model添加v4依赖
3. 配置当前model的build.gradle文件
    添加此段代码：
    ```
    buildscript {
        repositories {
            jcenter()
            flatDir {
                dirs 'libs' // this way we can find the .aar file in libs folder 到libs文件夹下寻找.aar包
            }
        }
    }
    ```

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])

    //加载本地的lib下的aar文件
    compile(name: 'MInject', ext: 'aar')
    compile 'com.android.support:support-v4:22.1.1'
}
```

####方式二： 从中央仓库添加


###使用
1. 在合适的时机注入MInject.inject()方法。如在Activity中
    ```
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //注入View，并支持点击事件方法的注入
            MInject.inject(this);
        }
    ```
2. 使用@Inject注解注入View
    ```
        @Inject(R.id.btn_textview)
        TextView mBtnTextView;
        @Inject(R.id.btn_button)
        Button mBtnButton;
        @Inject(R.id.btn_imageview)
        ImageView mBtnImageView;
    ```
--------------------------华丽的分割线----------------------------------------

1. 给单个View注入点击事件方法
    ```
        @MOnclick(value = R.id.btn_textview)
        private void click1(View view){
            if(view.getId() == R.id.btn_textview){
                clickCount();
                ToastUtils.showShort(this,"点击了TextView按钮"+clickCount);
            }
        }
    ```
2. 同时给多个View注入点击方法
    ```
        @MOnclick(value = {R.id.btn_imageview, R.id.btn_button})
        private void click2(View view){
            switch (view.getId()) {
                case R.id.btn_button:
                    clickCount();
                    ToastUtils.showShort(this,"点击了Button按钮"+clickCount);
                    break;
                case R.id.btn_imageview:
                    clickCount();
                    ToastUtils.showShort(this,"点击了ImageView按钮"+clickCount);
                    break;
            }
        }
    ```
3. 设置点击抖动时间
    ```
        //设置了btn_textview2的点击抖动时间为5000毫秒，5秒内不能连续点击
        @MOnclick(value = R.id.btn_textview2, shakeTime = 5000)
        private void click3(View view){
            switch (view.getId()) {
                case R.id.btn_textview2:
                    clickCount();
                    ToastUtils.showShort(this,"点击抖动时间为5s，5s内不能连续点击"+clickCount);
                    break;
            }
        }
    ```


