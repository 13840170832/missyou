package com.lin.missyou.optional;


        import lombok.Getter;
        import lombok.Setter;
        import org.junit.Test;

        import java.util.Optional;

public class OptionalTest {
    @Test
    public void testOptional(){
        /**构建Optional*/
        //构建空的Optional
//        Optional<String> empty = Optional.empty();
//        empty.get();
//        //构建Optional传入空值会报错
//        Optional<String> t1 = Optional.of(null);
        //构建Optional允许传入空值
//        Optional<String> t2 = Optional.ofNullable("a");

//        String s = t2.get();
////        t2不为空时执行ifPresent()中的语句
//        t2.ifPresent(t-> System.out.println(t));  //lambada表达式写法
//        t2.ifPresent(System.out::println);          //方法引用写法
//
        //传统写法
//        String s = "默认值";
//        if(null != t2){
//            s = t2.get();
//        }else{
//
//        }
//        //应用Optional的写法
//        String s = t2.orElse("默认值");
//        System.out.println(s);
//
//        //Optional链式操作 .map()返回值会再次包装成Optional类
//        t2.map(t->t+"b").ifPresent(System.out::println);

        User user = new User("小红","12345");
        System.out.println("Using orElse");
        User result = Optional.ofNullable(user).orElse(createNewUser());
        System.out.println("Using orElseGet");
        User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
    }

    private User createNewUser(){
        System.out.println("createNewUser");
        return new User("小明","123456");
    }
}

@Getter
@Setter
class User{

    private String userName;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
