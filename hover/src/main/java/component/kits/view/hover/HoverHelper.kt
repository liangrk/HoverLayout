package component.kits.view.hover

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import component.kits.view.ViewOffsetHelper

/**
 * @author : wing-hong Create by 2021/12/17 14:01
 */
class HoverHelper(private val delegateView: View) : View.OnClickListener {

    private var containerView: View? = null

    private var offsetHelper: ViewOffsetHelper? = null
    private var touchSlop = 0
    private var touchDownX = 0f
    private var touchDownY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isDragging = false
    private var isTouchDownInContainer = false

    /**
     * 点击事件响应时把container返回出去
     */
    private var onClick: ((View?) -> Unit)? = null

    /**
     * 添加悬浮布局
     */
    fun onFinishInflateDelegate(targetView: View) {
        try {
            containerView = targetView
            containerView?.setOnClickListener(this)

            offsetHelper = ViewOffsetHelper(containerView)
            touchSlop = ViewConfiguration.get(targetView.context).scaledTouchSlop
        } catch (e: Throwable) {
            e.printStackTrace()
            containerView = null
        }
    }

    fun onLayoutDelegate() {
        if (!isContainerInflate()) return
        offsetHelper!!.onViewLayout()
    }

    fun onInterceptDelegate(event: MotionEvent): Boolean {
        println("action:${event.actionMasked}")
        val x = event.x
        val y = event.y
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            delegateView.parent.requestDisallowInterceptTouchEvent(true)
            isTouchDownInContainer = isDownInContainer(x, y)
            lastTouchX = x
            touchDownX = lastTouchX
            lastTouchY = y
            touchDownY = lastTouchY
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!isDragging && isTouchDownInContainer) {
                val dx = (x - touchDownX).toInt()
                val dy = (y - touchDownY).toInt()
                if (Math.sqrt((dx * dx + dy * dy).toDouble()) > touchSlop) {
                    isDragging = true
                }
            }
            if (isDragging) {
                var dx = (x - lastTouchX).toInt()
                var dy = (y - lastTouchY).toInt()
                val gx = containerView!!.left
                val gy = containerView!!.top
                val gw = containerView!!.width
                val w = delegateView.width
                val gh = containerView!!.height
                val h = delegateView.height
                if (gx + dx < 0) {
                    dx = -gx
                } else if (gx + dx + gw > w) {
                    dx = w - gw - gx
                }
                if (gy + dy < 0) {
                    dy = -gy
                } else if (gy + dy + gh > h) {
                    dy = h - gh - gy
                }
                offsetHelper!!.leftAndRightOffset =
                    offsetHelper!!.leftAndRightOffset + dx
                offsetHelper!!.topAndBottomOffset =
                    offsetHelper!!.topAndBottomOffset + dy
            }
            lastTouchX = x
            lastTouchY = y
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            isDragging = false
            isTouchDownInContainer = false
            delegateView.parent.requestDisallowInterceptTouchEvent(false)
        }
        return isDragging
    }

    fun onTouchDelegate(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            isTouchDownInContainer = isDownInContainer(x, y)
            lastTouchX = x
            touchDownX = lastTouchX
            lastTouchY = y
            touchDownY = lastTouchY
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!isDragging && isTouchDownInContainer) {
                val dx = (x - touchDownX).toInt()
                val dy = (y - touchDownY).toInt()
                if (Math.sqrt((dx * dx + dy * dy).toDouble()) > touchSlop) {
                    isDragging = true
                }
            }
            if (isDragging) {
                var dx = (x - lastTouchX).toInt()
                var dy = (y - lastTouchY).toInt()
                val gx = containerView!!.left
                val gy = containerView!!.top
                val gw = containerView!!.width
                val w = delegateView.width
                val gh = containerView!!.height
                val h = delegateView.height
                if (gx + dx < 0) {
                    dx = -gx
                } else if (gx + dx + gw > w) {
                    dx = w - gw - gx
                }
                if (gy + dy < 0) {
                    dy = -gy
                } else if (gy + dy + gh > h) {
                    dy = h - gh - gy
                }
                offsetHelper!!.leftAndRightOffset =
                    offsetHelper!!.leftAndRightOffset + dx
                offsetHelper!!.topAndBottomOffset =
                    offsetHelper!!.topAndBottomOffset + dy
            }
            lastTouchX = x
            lastTouchY = y
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            isDragging = false
            isTouchDownInContainer = false
        }
        return isDragging
    }

    fun onViewTouchDelegate(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            isTouchDownInContainer = isDownInContainer(x, y)
            lastTouchX = x
            touchDownX = lastTouchX
            lastTouchY = y
            touchDownY = lastTouchY
            return true
        } else if (action == MotionEvent.ACTION_MOVE) {
            var dx = (x - lastTouchX).toInt()
            var dy = (y - lastTouchY).toInt()
            val gx = containerView!!.left
            val gy = containerView!!.top
            val gw = containerView!!.width
            val w = delegateView.width
            val gh = containerView!!.height
            val h = delegateView.height
            if (gx + dx < 0) {
                dx = -gx
            } else if (gx + dx + gw > w) {
                dx = w - gw - gx
            }
            if (gy + dy < 0) {
                dy = -gy
            } else if (gy + dy + gh > h) {
                dy = h - gh - gy
            }
            offsetHelper!!.leftAndRightOffset =
                offsetHelper!!.leftAndRightOffset + dx
            offsetHelper!!.topAndBottomOffset =
                offsetHelper!!.topAndBottomOffset + dy
            lastTouchX = x
            lastTouchY = y
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            isDragging = false
            isTouchDownInContainer = false
        }
        return isDragging
    }

    private fun isDownInContainer(x: Float, y: Float): Boolean {
        if (containerView == null) return false
        return containerView!!.left < x && containerView!!.right > x && containerView!!.top < y && containerView!!.bottom > y
    }

    /**
     * 是否已经添加悬浮布局
     */
    private fun isContainerInflate() = containerView != null

    fun setOnClickDelegate(onClick: ((View?) -> Unit)?) {
        this.onClick = onClick
    }

    override fun onClick(v: View) {
        onClick?.invoke(containerView)
    }
}