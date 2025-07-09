# PostgreSQL設定ガイド

## 概要
本番環境でPostgreSQLを使用するための設定を追加しました。開発環境では引き続きH2データベースを使用し、本番環境でPostgreSQLを使用する構成になっています。

## 実装内容

### 1. Gradle依存関係
`build.gradle`にPostgreSQLドライバーを追加：
```gradle
runtimeOnly 'org.postgresql:postgresql'
```

### 2. プロファイル別設定

#### デフォルト設定（application.properties）
- デフォルトプロファイルを`dev`に設定
- 共通設定（国際化、ファイルアップロード制限等）を定義

#### 開発環境（application-dev.properties）
- H2インメモリデータベースを使用
- デバッグログ有効化
- DDL自動作成（create-drop）

#### 本番環境（application-prod.properties）
- PostgreSQLを使用
- 環境変数による設定：
  - `DB_HOST`: データベースホスト（デフォルト: localhost）
  - `DB_PORT`: ポート番号（デフォルト: 5432）
  - `DB_NAME`: データベース名（デフォルト: tabledef）
  - `DB_USERNAME`: ユーザー名（デフォルト: postgres）
  - `DB_PASSWORD`: パスワード（必須）
- HikariCP接続プール設定
- DDL検証モード（validate）

### 3. データベーススキーマ
`src/main/resources/schema.sql`にPostgreSQL用DDLスクリプトを作成：
- `table_definitions`テーブル
- `column_definitions`テーブル
- インデックス
- 更新日時自動更新トリガー

### 4. Docker設定

#### 本番環境用（docker-compose.yml）
- PostgreSQLとアプリケーションの両方を起動
- ヘルスチェック付き
- 初期スキーマ自動作成

#### 開発環境用（docker-compose-dev.yml）
- PostgreSQLのみ起動（ポート5433）
- アプリケーションはIDEから起動

## 使用方法

### 開発環境（H2データベース）
```bash
./gradlew bootRun
```

### 開発環境（PostgreSQL使用）
1. PostgreSQLを起動：
```bash
docker-compose -f docker-compose-dev.yml up -d
```

2. プロファイルを指定してアプリケーション起動：
```bash
SPRING_PROFILES_ACTIVE=prod DB_HOST=localhost DB_PORT=5433 DB_NAME=tabledef_dev DB_PASSWORD=postgres ./gradlew bootRun
```

### 本番環境
1. 環境変数を設定：
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_PASSWORD=your-secure-password
```

2. JARファイルをビルド：
```bash
./gradlew build
```

3. 実行：
```bash
java -jar build/libs/table-definition-app-0.0.1-SNAPSHOT.jar
```

### Dockerを使用した本番環境
```bash
docker-compose up -d
```

## 注意事項

1. **初回起動時**: 本番環境では`spring.jpa.hibernate.ddl-auto=validate`に設定されているため、事前にデータベーススキーマを作成する必要があります。`schema.sql`を実行してください。

2. **パスワード管理**: 本番環境では環境変数でパスワードを設定してください。設定ファイルに直接記載しないでください。

3. **接続プール**: 本番環境の負荷に応じてHikariCPの設定を調整してください。

4. **バックアップ**: 本番環境では定期的なバックアップを設定してください。