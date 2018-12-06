package com.example.library.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.library.R;

public class MaterialTextView extends AppCompatTextView {

    private static final int AUTO_LINK_NONE = 0;
    private String font;

    public MaterialTextView(Context context) {
        super(context);
        applyCustomFonts(context, null);
    }

    public MaterialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFonts(context, attrs);
    }

    public MaterialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFonts(context, attrs);
    }

    private void applyCustomFonts(Context context, AttributeSet attrs) {
        applyTheme(context, attrs);

        /*TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CusttomFont, R.attr.typeface, 0);
        String typeface = a.getString(R.styleable.CustomFont_typeface);
        font = CustomFontFactory.Font.valueOf(typeface).getTypeface();*/
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        setTypeface(typeface);
    }

    private void applyTheme(Context context, AttributeSet attrs) {
        int autoLinkMask = getAutoLinkMask();
        if (autoLinkMask == AUTO_LINK_NONE) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialTextView);
        setLinkTextColor(a.getColor(R.styleable.MaterialTextView_linkColor, ContextCompat.getColor(context, R.color.linkColor)));
        a.recycle();
    }
}
