package com.example.dummy_api;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserPointService {

    private final Faker faker = new Faker();
    private final Random random = new Random();

    public List<User> generateRandomUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int id = i + 1; // ユーザーIDを1から始まる連番にする
            String name = faker.name().fullName();
            int points = random.nextInt(1000); // 0から1000までのランダムなポイント
            users.add(new User(id, name, points));
        }
        return users;
    }
}
