package com.cocoyol.apps.choirbook.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toolbar;

import java.lang.reflect.Field;

public class MarqueeToolbar extends android.support.v7.widget.Toolbar {

    TextView title, subTitle;

    public MarqueeToolbar(Context context) {
        super(context);
    }

    public MarqueeToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Set marquee title
    boolean reflected = false;
    private boolean reflectTitle() {
        try {
            Field field = Toolbar.class.getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            title = (TextView) field.get(this);
            title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            title.setMarqueeRepeatLimit(-1);
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void selectTitle() {
        if (title != null)
            title.setSelected(true);
    }

    @Override
    public void setTitle(int resId) {
        super.setTitle(resId);
        if (!reflected) {
            reflected = reflectTitle();
        }
        selectTitle();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    // Set marquee subtitle
    boolean reflectedSub = false;
    private boolean reflectSubTitle() {
        try {
            Field field = Toolbar.class.getDeclaredField("mSubtitleTextView");
            field.setAccessible(true);
            subTitle = (TextView) field.get(this);
            subTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            subTitle.setMarqueeRepeatLimit(-1);
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void selectSubtitle() {
        if (subTitle != null)
            subTitle.setSelected(true);
    }

    @Override
    public void setSubtitle(int resId) {
        super.setSubtitle(resId);
        if (!reflected) {
            reflectedSub = reflectSubTitle();
        }
        selectSubtitle();
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        super.setSubtitle(subtitle);
        if (!reflected) {
            reflectedSub = reflectSubTitle();
        }
        selectSubtitle();
    }
}
