package com.dn_alan.skin_core2;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.dn_alan.skin_core2.utils.SkinResources;
import com.dn_alan.skin_core2.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");

        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    private List<SkinView> skinViews = new ArrayList<>();

    public void load(View view, AttributeSet attrs) {
        List<SkinPain> skinPains = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获取属性名字
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                //获取属性对应的值
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    continue;
                }

                int resId;
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    //@1234564
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                if (resId != 0) {
                    SkinPain skinPain = new SkinPain(attributeName, resId);
                    skinPains.add(skinPain);
                }
            }
        }
        if (!skinPains.isEmpty()) {
            SkinView skinView = new SkinView(view, skinPains);
            skinView.applySkin();
            skinViews.add(skinView);
        }
    }

    static class SkinView {
        View view;
        List<SkinPain> skinPains;

        public SkinView(View view, List<SkinPain> skinPains) {
            this.view = view;
            this.skinPains = skinPains;
        }

        public void applySkin() {
            for (SkinPain skinPair : skinPains) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(
                                skinPair.resId);
                        //Color
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
//                        background = SkinResources.getInstance().getBackground(skinPair
//                                .resId);
//                        if (background instanceof Integer) {
//                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
//                                    background));
//                        } else {
//                            ((ImageView) view).setImageDrawable((Drawable) background);
//                        }
                        break;
                    case "textColor":
//                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList
//                                (skinPair.resId));
                        break;
                    case "drawableLeft":
//                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
//                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
//                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
//                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        break;
                }
//                if (null != left || null != right || null != top || null != bottom) {
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
//                            bottom);
//                }
            }
        }
    }

    static class SkinPain {
        String attributeName;
        int resId;

        public SkinPain(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
