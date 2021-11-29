package com.rex.flutter_subscreen_plugin

import android.app.Presentation
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * @Description:    副屏dialog
 * @Author:         liyufeng
 * @CreateDate:     2021/3/16 10:55 AM
 */

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class FlutterSubScreenPresentation(outerContext: Context?, display: Display?) :
        Presentation(outerContext, display) {

    lateinit var flutterEngine: FlutterEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val engine = FlutterEngine(context)
        flutterEngine = engine

        //指定初始化路由
        flutterEngine.navigationChannel.setInitialRoute(FlutterSubscreenPlugin.subMainRouter)
        flutterEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint(
                        FlutterInjector.instance().flutterLoader().findAppBundlePath(),
                        FlutterSubscreenPlugin.mainRouter
                )
        )


        val flContainer = FrameLayout(context)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        flContainer.layoutParams = params
        setContentView(flContainer)

        val flutterView = FlutterView(context)
        flContainer.addView(flutterView, params)
        flutterView.attachToFlutterEngine(flutterEngine)

        // 一定要调用 不然页面会卡死不更新
        flutterEngine.lifecycleChannel.appIsResumed()
    }

    override fun dismiss() {
        flutterEngine.lifecycleChannel.appIsDetached()
        super.dismiss()
    }

}