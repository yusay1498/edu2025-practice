package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP コードを<b>実行</b>するには、<shortcut actionId="Run"/> を押すか
// ガターの <icon src="AllIcons.Actions.Execute"/> アイコンをクリックします。
public class Main {
    public static void main(String[] args) {
        int quantity = 4;
        final int price = 100;

        int total = quantity * price;

        System.out.println(total);

        for (int i = 1; i <= 20; i++) {
            if (i % 15 == 0) System.out.println("FizzBuzz");
            else if (i % 5 == 0) System.out.println("Buzz");
            else if (i % 3 == 0) System.out.println("Fuzz");
            else System.out.println(i);
        }

        System.out.println();

        List<Integer> list = List.of(1, 2, 3, 4, 5);

        for (Integer i : list) {
            System.out.println(i);
        }

        Map<String, Object> obj = new HashMap<>();
        obj.put("name", "cid");
        obj.put("age", 40);

        Person person = new Person(obj.get("name").toString(), Integer.parseInt(obj.get("age").toString()));
    }

    void fizzbuzz() {
        for (int i = 1; i <= 20; i++) {
            if (i % 15 == 0) System.out.println("FizzBuzz");
            else if (i % 5 == 0) System.out.println("Buzz");
            else if (i % 3 == 0) System.out.println("Fuzz");
            else System.out.println(i);
        }
    }

    public static class Person{
        private final String name;
        private final int age;

        public Person(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName(){
            return name;
        }
        public int getAge(){
            return age;
        }
    }
}