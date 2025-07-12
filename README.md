# テーブル定義書管理システム

プロジェクト単位でデータベースのテーブル定義を管理し、ドキュメントを生成するWebアプリケーションです。

## 📋 機能

- **プロジェクト管理**: プロジェクト単位でテーブル定義を整理・管理
- **GUI操作によるテーブル定義作成**: ユーザーフレンドリーなWeb画面でテーブル定義を作成・編集
- **詳細なカラム管理**: データ型、制約、精度、スケール等の詳細設定
- **エクスポート機能**: Excel、Markdown、DDL形式でテーブル定義書を出力
- **データ検証**: 適切なエラーハンドリングによるフォーム検証
- **レスポンシブデザイン**: デスクトップ・モバイル対応
- **マルチデータベース対応**: 開発環境ではH2、本番環境ではPostgreSQL/Supabaseを使用
- **Docker対応**: コンテナ環境での簡単デプロイ

## 🛠 技術スタック

### バックエンド
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring MVC**

### フロントエンド
- **Thymeleaf**
- **Bootstrap 5**
- **JavaScript**

### データベース
- **H2 Database** (開発環境・インメモリ)
- **PostgreSQL** (本番環境)
- **Supabase** (クラウドPostgreSQL)

### その他
- **Gradle** (ビルドツール)
- **Apache POI** (Excel出力)
- **Docker & Docker Compose** (コンテナ化)
- **国際化対応** (Spring MessageSource)

## 🚀 クイックスタート

### 前提条件

- Java 17以上
- Gradle 7.6以上（Gradle Wrapper使用可）
- Docker & Docker Compose（コンテナ使用時）

### 1. リポジトリクローン

```bash
git clone <repository-url>
cd table-definition-app
```

### 2. 環境設定

#### 開発環境（H2データベース）
```bash
# そのまま起動可能（追加設定不要）
cd table-definition-app
./gradlew bootRun
```

#### 本番環境（Supabase）
```bash
# 環境変数ファイルをコピー
cd table-definition-app
cp .env.example .env

# .envファイルを編集してSupabase情報を設定
nano .env
```

### 3. アプリケーション起動

#### Gradle起動
```bash
# 開発環境
cd table-definition-app
./gradlew bootRun

# 本番環境
cd table-definition-app
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

#### Docker起動
```bash
# .envファイル設定後
cd table-definition-app
docker-compose up -d
```

### 4. アクセス

ブラウザで http://localhost:8080 にアクセス

## 📁 プロジェクト構造

```
/
├── table-definition-app/        # メインアプリケーション
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/tabledef/
│   │   │   │   ├── controller/          # Webコントローラー
│   │   │   │   ├── dto/                 # データ転送オブジェクト
│   │   │   │   ├── model/               # JPA エンティティ
│   │   │   │   ├── repository/          # データアクセス層
│   │   │   │   ├── service/             # ビジネスロジック層
│   │   │   │   ├── config/              # 設定クラス
│   │   │   │   └── util/                # ユーティリティ
│   │   │   └── resources/
│   │   │       ├── templates/           # Thymeleafテンプレート
│   │   │       ├── static/              # CSS、JS、画像
│   │   │       ├── i18n/                # 国際化メッセージ
│   │   │       ├── application*.yml     # 設定ファイル
│   │   │       └── schema*.sql          # データベーススキーマ
│   │   └── test/                        # テストコード
│   ├── docs/                            # ドキュメント
│   ├── docker-compose.yml               # Docker設定
│   ├── .env.example                     # 環境変数テンプレート
│   └── README.md                        # アプリケーション詳細
├── .gitignore                           # Git除外設定
└── README.md                            # このファイル
```

## 📚 詳細ドキュメント

詳細な情報については、アプリケーションディレクトリ内のドキュメントを参照してください：

- [アプリケーション詳細README](table-definition-app/README.md) - 機能詳細、使用方法
- [API仕様書](table-definition-app/docs/api-specification.md) - REST APIの詳細仕様
- [データベース設計書](table-definition-app/docs/database-design.md) - データベーススキーマとER図
- [操作マニュアル](table-definition-app/docs/operation-manual.md) - ユーザー向け操作ガイド
- [システム設計書](table-definition-app/docs/system-design.md) - アーキテクチャと技術仕様

## 🛠 開発・運用

### ビルド
```bash
cd table-definition-app
./gradlew build
```

### テスト実行
```bash
cd table-definition-app
./gradlew test
```

### 本番環境デプロイ
詳細は [アプリケーションREADME](table-definition-app/README.md) を参照してください。

## 📄 ライセンス

このプロジェクトはMITライセンスの下で公開されています。詳細はLICENSEファイルを参照してください。