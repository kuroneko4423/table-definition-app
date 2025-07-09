# テーブル定義書管理システム

プロジェクト単位でデータベースのテーブル定義を管理し、ドキュメントを生成するWebアプリケーションです。

## 機能

- **プロジェクト管理**: プロジェクトごとにテーブル定義を整理・管理
- **GUI操作によるテーブル定義作成**: ユーザーフレンドリーなWeb画面でテーブル定義を作成・編集
- **カラム管理**: 詳細なプロパティでカラムの追加・編集・削除
- **エクスポート機能**: Excel、Markdown、DDL形式でテーブル定義書を出力
- **データ検証**: 適切なエラーハンドリングによるフォーム検証
- **レスポンシブデザイン**: デスクトップ・モバイル対応
- **マルチデータベース対応**: 開発環境ではH2、本番環境ではPostgreSQLを使用

## 技術スタック

- **バックエンド**: Java 17, Spring Boot 3.2.0, Spring Data JPA
- **フロントエンド**: Thymeleaf, Bootstrap 5, JavaScript
- **データベース**: H2 Database (開発環境)、PostgreSQL (本番環境)
- **ビルドツール**: Gradle
- **エクスポート**: Apache POI (Excel)、カスタムMarkdown/DDLジェネレータ
- **多言語対応**: Spring MessageSourceによる国際化

## 使用方法

### 前提条件

- Java 17以上
- Gradle 7.0以上

### アプリケーションの起動

1. プロジェクトディレクトリに移動:
   ```bash
   cd table-definition-app
   ```

2. アプリケーションをビルド・実行:
   ```bash
   ./gradlew bootRun
   ```

3. ブラウザでアクセス: http://localhost:8080

### 操作方法

1. **プロジェクトの作成**:
   - プロジェクト一覧画面で「新規プロジェクト作成」ボタンをクリック
   - プロジェクト名、説明を入力
   - 「保存」ボタンでプロジェクトを作成

2. **テーブル定義の管理**:
   - プロジェクト一覧からプロジェクトを選択
   - 「テーブル管理」ボタンをクリックまたはプロジェクト名をクリック
   - プロジェクトのテーブル一覧画面が表示される

3. **新しいテーブルの作成**:
   - テーブル一覧画面で「テーブル新規作成」ボタンをクリック
   - テーブル情報（スキーマ名、物理名、論理名、説明）を入力
   - 「カラムを追加」ボタンでカラムを追加
   - カラムのプロパティ（データ型、長さ、NULL許可、主キー等）を設定
   - 「保存」ボタンでテーブルを作成

4. **既存テーブルの編集**:
   - テーブル一覧の「編集」ボタンをクリック
   - テーブル情報やカラム情報を変更
   - 「保存」ボタンで更新

5. **テーブルの削除**:
   - テーブル一覧の「削除」ボタンをクリック
   - 削除確認

6. **テーブル定義書のエクスポート**:
   - テーブル一覧で出力したいテーブルのラジオボタンを選択
   - 「Excel出力」ボタンで選択されたテーブルのExcelファイルをダウンロード
   - 「Markdown出力」ボタンで選択されたテーブルのMarkdownファイルをダウンロード
   - 「DDL出力」ボタンで選択されたテーブルのSQLファイル（CREATE TABLE文）をダウンロード
   - ⚠️ テーブルを選択せずに出力ボタンを押すとアラートが表示されます

## データベーススキーマ

### プロジェクト (projects)
- id (主キー)
- name (ユニーク)
- description
- created_at
- updated_at

### テーブル定義 (table_definitions)
- id (主キー)
- project_id (外部キー)
- schema_name
- physical_name
- logical_name
- description
- created_at
- updated_at
- ユニーク制約: (project_id, physical_name)

### カラム定義 (column_definitions)
- id (主キー)
- table_definition_id (外部キー)
- physical_name
- logical_name
- data_type
- length
- nullable
- primary_key
- unique_key
- default_value
- description
- column_order
- created_at
- updated_at

## 設定

### データベース設定

アプリケーションは環境別の設定ファイルを使用します：

- **開発環境** (`application-dev.properties`): H2インメモリデータベース
- **本番環境** (`application-prod.properties`): PostgreSQL

環境の切り替えは環境変数で行います：
```bash
# 開発環境（デフォルト）
./gradlew bootRun

# 本番環境
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

PostgreSQL接続設定（環境変数）：
- `DB_HOST`: データベースホスト（デフォルト: localhost）
- `DB_PORT`: ポート番号（デフォルト: 5432）
- `DB_NAME`: データベース名（デフォルト: tabledef）
- `DB_USERNAME`: ユーザー名（デフォルト: postgres）
- `DB_PASSWORD`: パスワード（デフォルト: postgres）

### メッセージファイル
メッセージファイルは `src/main/resources/i18n/` に配置されています：
- `messages_ja.properties` - 日本語メッセージ

## 本番環境での構築

### 通常のデプロイ

1. PostgreSQLデータベースを準備し、`schema-postgresql.sql`を実行してスキーマを作成

2. 環境変数を設定:
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export DB_PASSWORD=your-secure-password
   ```

3. アプリケーションをビルド:
   ```bash
   ./gradlew build
   ```

4. JARファイルを実行:
   ```bash
   java -jar build/libs/table-definition-app-0.0.1-SNAPSHOT.jar
   ```

### Dockerを使用したデプロイ

1. Docker Composeで起動:
   ```bash
   docker-compose up -d
   ```

   これにより、PostgreSQLとアプリケーションが自動的に起動します。

## 開発

### プロジェクト構造
```
src/
├── main/
│   ├── java/
│   │   └── com/example/tabledef/
│   │       ├── controller/     # RESTコントローラ
│   │       ├── service/        # ビジネスロジック
│   │       ├── repository/     # データアクセス層
│   │       ├── model/          # エンティティクラス
│   │       ├── dto/            # データ転送オブジェクト
│   │       ├── config/         # 設定クラス
│   │       └── util/           # ユーティリティクラス
│   └── resources/
│       ├── templates/          # Thymeleafテンプレート
│       ├── static/             # CSS、JS、画像
│       ├── i18n/               # メッセージプロパティ
│       ├── application.properties        # 共通設定
│       ├── application-dev.properties    # 開発環境設定
│       ├── application-prod.properties   # 本番環境設定
│       ├── schema-postgresql.sql         # PostgreSQL DDLスクリプト
│       └── schema-h2.sql                 # H2 DDLスクリプト
└── test/
    └── java/                   # ユニットテスト
```

### 開発環境でのPostgreSQL使用

開発時にPostgreSQLを使用したい場合：

1. PostgreSQLコンテナを起動:
   ```bash
   docker-compose -f docker-compose-dev.yml up -d
   ```

2. アプリケーションを起動:
   ```bash
   SPRING_PROFILES_ACTIVE=prod DB_HOST=localhost DB_PORT=5433 DB_NAME=tabledef_dev DB_PASSWORD=postgres ./gradlew bootRun
   ```

### 新しいデータ型の追加
カラムの新しいデータ型を追加するには、`table-form.html`のselectオプションを更新してください：

```html
<option value="NEW_TYPE">NEW_TYPE</option>
```

## 主な変更履歴

### v2.0.0
- プロジェクト管理機能を追加
- URLパス構造を `/projects/{projectId}/tables` 形式に変更
- プロジェクト内でのテーブル物理名のユニーク制約を追加
- PostgreSQL対応（本番環境）
- Docker Compose設定を追加
- 多言語対応（日本語メッセージ）
- UI改善（メッセージ表示の最適化、タイトル表示の修正）

### v1.0.0
- 初期リリース
- テーブル定義の作成・編集・削除
- Excel、Markdown、DDL形式でのエクスポート
- H2インメモリデータベース使用

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。