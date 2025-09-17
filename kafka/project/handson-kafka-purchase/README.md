Kafka hands on
================================================================================


Quickstart
--------------------------------------------------------------------------------

Step1. Run Kafka container

```bash
docker compose up -d
```

Step2. Run Spring Boot application (Kafka Producer and Consumer)

```bash
./mvnw clean spring-boot:run
```

Step3. Post new message (in New terminal)

```bash
curl -X POST localhost:8080 -H 'Content-Type:application/json' -d '{
"type": "food_eatin",  
"name": "BurgerKing Whopper",
"price": 590,
"qty": 1
}'
```

```bash
curl -X POST localhost:8080 -H 'Content-Type:application/json' -d '{
"type": "food_takeout",  
"name": "BurgerKing Whopper",
"price": 590,
"qty": 1
}'
```

Step4. Stop application and containers

```bash
Ctrl+C
```

```bash
docker compose down
```
