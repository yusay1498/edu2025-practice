# Nginxの軽量なイメージを使用
FROM nginx:alpine

# ビルド済みの静的ファイルをNginxの公開ディレクトリにコピー
COPY ./build /usr/share/nginx/html

# Nginxの設定ファイルをコピー
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# ポート80を開放
EXPOSE 80

# Nginxをエントリーポイントとして設定
ENTRYPOINT ["nginx", "-g", "daemon off;"]