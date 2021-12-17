package component.kits.view.hover

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * @author : wing-hong Create by 2021/12/16 17:46
 */
class HoverLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    private var containerViewRes: Int = -1

    init {
        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.HoverLayout)
        containerViewRes = typeArray.getResourceId(
            R.styleable.HoverLayout_hover_container_layout,
            containerViewRes
        )
        typeArray.recycle()
    }

    private var helper: HoverHelper? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (containerViewRes < 0) throw IllegalArgumentException("悬浮布局container不能为空,请在xml中配置 hover_container_layout")

        inflate(context, containerViewRes, this)
        if (childCount > 1) throw  IllegalArgumentException("$this 只能拥有一个子view, 请只保留 hover_container_layout")

        helper = HoverHelper(this)
        helper?.onFinishInflateDelegate(getChildAt(0))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        helper?.onLayoutDelegate()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return helper?.onInterceptDelegate(event) ?: super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return helper?.onTouchDelegate(event) ?: false || super.onTouchEvent(event)
    }

    /**
     * 设置点击
     */
    fun setHoverClickListener(onClick: ((View?) -> Unit)) {
        helper?.setOnClickDelegate(onClick)
    }

    private var startAnimSet: AnimatorSet? = null
    private var resetAnimSet: AnimatorSet? = null

    /**
     * 是否已经收起
     */
    private var isCollapse = false

    /**
     * 添加滑动动画
     */
    fun addStartAnim() {
        this.post {
            val isVisible = getGlobalVisibleRect(Rect())
            if (!isVisible || isCollapse ||
                resetAnimSet?.isRunning == true || startAnimSet?.isRunning == true
            ) return@post
            val tranX =
                ObjectAnimator.ofFloat(this, "translationX", 0f, (width - width / 3).toFloat())
            val alp = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.4f)
            startAnimSet = AnimatorSet()
            startAnimSet!!.duration = 500
            startAnimSet!!.play(tranX)
                .with(alp)
            startAnimSet!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isCollapse = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCollapse = false
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
            startAnimSet!!.start()
        }
    }

    /**
     * 重置动画
     */
    fun resetAnim() {
        val resetInvoke = {
            val tranX =
                ObjectAnimator.ofFloat(this, "translationX", (width - width / 3).toFloat(), 0f)
            val alp = ObjectAnimator.ofFloat(this, "alpha", 0.4f, 1f)
            resetAnimSet = AnimatorSet()
            resetAnimSet!!.duration = 500
            resetAnimSet!!.play(tranX)
                .with(alp)
            resetAnimSet!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isCollapse = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isCollapse = false
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
            resetAnimSet!!.start()
        }
        if (startAnimSet?.isRunning == true) {
            startAnimSet?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    resetInvoke()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
            return
        }
        if (resetAnimSet?.isRunning == true || !isCollapse) {
            return
        }
        resetInvoke()
    }
}