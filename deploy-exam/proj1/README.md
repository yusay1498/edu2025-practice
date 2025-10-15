# proj1

## 目次

- [概要](#概要)
- [要件](#要件)
- [ビルド＆起動方法](#ビルド起動方法)
- [APIのリクエストサンプル](#APIのリクエストサンプル)
- [一括処理](#一括処理)

## 概要

開発演習1<br/>
このプロジェクトでは、カスタマーごとに所有しているポイント数を確認するためのAPIを開発し、ユーザーがポイント情報を確認できるUIアプリケーションを実装します。

## 要件

- Java 25
- Node.js 24
- Docker

## ビルド＆起動方法

```bash
cd customer_portal/

npm i

npm run build -- --mode deployment
```

```bash
./mvnw clean package

docker compose up --build
```

## APIのリクエストサンプル

GET (findAll)

```bash
curl -X GET http://localhost:8080/points
```

GET (findById)

```bash
curl -X GET http://localhost:8080/points/testId
```

### 一括処理

```bash
(cd customer-portal/ && \

npm i && \

npm run build -- --mode deployment)

./mvnw clean package

docker compose up --build -d

echo "Check here: http://localhost:5173/"
```
