package com.paul0523;

import java.io.IOException;
import java.lang.reflect.Field;

public class HelloWorld{
    public native void hello();

    static {
        try {
            //设置查找路径为当前项目路径,注意直接设置java.library.path属性无效
            addDir("/Users/fangzhipeng/Documents/workspace-idea/java-jni-test/lib");
            //加载动态库的名称
            System.loadLibrary("JAVA_JNI3");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        long time = System.currentTimeMillis();
        new HelloWorld().hello();
        System.out.println((System.currentTimeMillis() - time));
    }

    public static void addDir(String s) throws IOException {
        try {
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[])field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length+1];
            System.arraycopy(paths,0,tmp,0,paths.length);
            tmp[paths.length] = s;
            field.set(null,tmp);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }
}
