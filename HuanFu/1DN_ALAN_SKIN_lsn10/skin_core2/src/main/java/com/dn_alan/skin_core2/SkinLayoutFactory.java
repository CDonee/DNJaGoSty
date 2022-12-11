package com.dn_alan.skin_core2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class SkinLayoutFactory implements LayoutInflater.Factory2 {
    private static final String[] mClassPrefixlist = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private static final Class[] mConstructorSignature =
            new Class[]{Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> mConstructor =
            new HashMap<String, Constructor<? extends View>>();

    //属性处理类
    private SkinAttribute skinAttribute;

    public SkinLayoutFactory() {
        skinAttribute = new SkinAttribute();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 反射 classLoader
        View view = createViewFromTag(name, context, attrs);
        // 自定义View
        if(null ==  view){
            view = createView(name, context, attrs);
        }

        //筛选符合属性View
        skinAttribute.load(view, attrs);

        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attrs) {
        //包含自定义控件
        if (-1 != name.indexOf(".")) {
            return null;
        }
        //
        View view = null;
        for (int i = 0; i < mClassPrefixlist.length; i++) {
            view = createView(mClassPrefixlist[i] + name, context, attrs);
            if(null != view){
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = mConstructor.get(name);
        if (constructor == null) {
            try {
                //通过全类名获取class
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                //获取构造方法
                constructor = aClass.getConstructor(mConstructorSignature);
                mConstructor.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != constructor) {
            try {
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}
