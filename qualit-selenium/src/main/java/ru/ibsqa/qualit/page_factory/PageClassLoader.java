package ru.ibsqa.qualit.page_factory;

import javassist.ClassPool;
import javassist.CtClass;

public class PageClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            CtClass ct = ClassPool.getDefault().get(name);
            if (ct.getSuperclass() != null &&
                    (ct.getSuperclass().getName().contains("DefaultPageObject") || ct.getSuperclass().getName().contains("DefaultCollectionObject"))
                    || (ct.getSuperclass().getSuperclass() != null && ct.getSuperclass().getSuperclass().getName().contains("DefaultPageObject"))
                    || (ct.getSuperclass().getSuperclass() != null && ct.getSuperclass().getSuperclass().getName().contains("DefaultCollectionObject"))) {

                byte[] buf = ct.toBytecode();
                int len = buf.length;
                return defineClass(name, buf, 0, len);
            }
            return getParent().loadClass(name);
        } catch (Exception e) {
            return getParent().loadClass(name);
        }
    }
}