package pony.xcode.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import java.lang.reflect.Field;

public class TitleBar extends Toolbar {
    private CharSequence mTitleText;
    private TextView mTitleTextView;
    private int mTitleTextAppearance;
    private ColorStateList mTitleTextColor;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("CustomViewStyleable") final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Toolbar);
        Object tc = getField("mTitleTextColor");
        if (tc instanceof ColorStateList) {
            setTitleTextColor((ColorStateList) tc);
        } else {
            if (a.hasValue(R.styleable.Toolbar_titleTextColor)) {
                setTitleTextColor(a.getColorStateList(R.styleable.Toolbar_titleTextColor));
            }
        }
        Object ta = getField("mTitleTextAppearance");
        if (ta != null) {
            setTitleTextAppearance((int) ta);
        } else {
            if (a.hasValue(R.styleable.Toolbar_titleTextAppearance)) {
                setTitleTextAppearance(a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0));
            }
        }
        a.recycle();
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    public void setTitleTextAppearance(int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            TextViewCompat.setTextAppearance(mTitleTextView, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        setTitleTextColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setTitleTextColor(@NonNull ColorStateList color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                mTitleTextView = new AppCompatTextView(getContext());
                mTitleTextView.setSingleLine();
                mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                if (mTitleTextAppearance != 0) {
                    TextViewCompat.setTextAppearance(mTitleTextView, mTitleTextAppearance);
                }
                if (mTitleTextColor != null) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                }
            }
            if (mTitleTextView.getParent() != this) {
                addTitleTextView();
            }
        } else if (mTitleTextView != null && mTitleTextView.getParent() == this) {// 当title为空时，remove
            removeView(mTitleTextView);
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }

    private void addTitleTextView() {
        ViewGroup.LayoutParams vlp = mTitleTextView.getLayoutParams();
        Toolbar.LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (Toolbar.LayoutParams) vlp;
        }
        lp.gravity = Gravity.CENTER;
        lp.setMargins(getTitleMarginStart(), getTitleMarginTop(), getTitleMarginEnd(), getTitleMarginBottom());
        addView(mTitleTextView, lp);
    }

    /*通过反射获取属性值*/
    @Nullable
    public Object getField(@NonNull String fieldName) {
        try {
            if (getClass().getSuperclass() == null) return null;
            Field field = getClass().getSuperclass().getDeclaredField(fieldName);//反射得到父类Field
            field.setAccessible(true);
            return field.get(this);
        } catch (Exception e) {
            return null;
        }
    }
}
