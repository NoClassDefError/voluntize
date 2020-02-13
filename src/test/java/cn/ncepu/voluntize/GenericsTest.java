package cn.ncepu.voluntize;

import cn.ncepu.voluntize.entity.Student;

import java.lang.reflect.ParameterizedType;

public class GenericsTest {
    public static void main(String[] a){
        new Ts<Student>(){}.printT();//只有加了{}Student才会被实例化。
    }
}

class Ts<T> {
    public void printT(){
  //      System.out.println(this.getClass().getTypeParameters()[0]);
        System.out.println(this.getClass().getGenericSuperclass().getTypeName());
        System.out.println((Class<T>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}

class Generic<T>{

}