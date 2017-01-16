package com.forsrc.bytecode;

import com.forsrc.bytecode.pojo.Pojo;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import org.junit.Test;

public class BytecodeTest {

    @Test
    public void test() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(this.getClass()));

        CtClass pojo = classPool.get(Pojo.class.getName());

        CtClass ctClass = classPool.makeClass("com.forsrc.bytecode.pojo.User");
        ctClass.setSuperclass(pojo);

        CtField username = new CtField(classPool.getCtClass("java.lang.String"), "username", ctClass);
        username.setModifiers(Modifier.PRIVATE);
        ctClass.addField(username);
        ctClass.addMethod(CtNewMethod.getter("getUsername", username));
        ctClass.addMethod(CtNewMethod.setter("setUsername", username));

        /*
         CtField id = new CtField(classPool.getCtClass("java.lang.Long"), "id", ctClass);
         username.setModifiers(Modifier.PRIVATE);
         ctClass.addField(id);
         ctClass.addMethod(CtNewMethod.getter("getId", id));
         ctClass.addMethod(CtNewMethod.setter("setId", id));
         */

        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
        //ctConstructor.setBody("{this.id = new Long(0L); this.username = \"77\";}");
        ctConstructor.setBody(MessageFormat.format("'{'this.id = new Long({0}L); this.username = \"{1}\";'}'", 0, "77"));
        ctClass.addConstructor(ctConstructor);

        CtMethod printMethod = new CtMethod(CtClass.voidType, "print", new CtClass[]{}, ctClass);
        printMethod.setBody("{System.out.println(this.id  + \" -1-> \" + this.username); System.out.println(this.getId()  + \" -2-> \" + this.getUsername());}");
        ctClass.addMethod(printMethod);

        ctClass.writeFile();

        Class<?> clazz = ctClass.toClass();
        Object obj = clazz.newInstance();
        obj.getClass().getMethod("print", new Class[]{}).invoke(obj, new Object[]{});

        byte[] bytecode = ctClass.toBytecode();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(bytecode);
    }
}
