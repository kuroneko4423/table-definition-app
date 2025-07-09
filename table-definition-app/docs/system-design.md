# テーブル定義管理システム 設計書

## 1. システム概要

### 1.1 目的
本システムは、データベースのテーブル定義情報を管理し、ドキュメント化を支援するWebアプリケーションです。プロジェクト単位でテーブル定義を管理し、Excel、Markdown、DDL形式でのエクスポート機能を提供します。

### 1.2 主要機能
- プロジェクト管理
- テーブル定義の作成・編集・削除
- カラム定義の管理
- 多言語対応（日本語/英語）
- エクスポート機能（Excel/Markdown/DDL）

## 2. アーキテクチャ

### 2.1 技術スタック
- **バックエンド**: Spring Boot 3.2.0
- **フロントエンド**: Thymeleaf, Bootstrap 5
- **データベース**: 
  - 開発環境: H2 Database（インメモリ）
  - 本番環境: PostgreSQL
- **ビルドツール**: Gradle
- **コンテナ**: Docker, Docker Compose

### 2.2 レイヤー構成
```
┌─────────────────────────────────────┐
│      Presentation Layer             │
│  (Controller, Thymeleaf Templates)  │
├─────────────────────────────────────┤
│       Business Logic Layer          │
│         (Service Classes)           │
├─────────────────────────────────────┤
│      Data Access Layer              │
│   (Repository, Entity Classes)      │
├─────────────────────────────────────┤
│         Database Layer              │
│    (H2/PostgreSQL Database)         │
└─────────────────────────────────────┘
```

## 3. データモデル

### 3.1 エンティティ関連図
```
┌─────────────┐      ┌──────────────────┐      ┌──────────────────┐
│   Project   │ 1──* │ TableDefinition  │ 1──* │ ColumnDefinition │
└─────────────┘      └──────────────────┘      └──────────────────┘
```

### 3.2 Project（プロジェクト）
| カラム名 | 型 | 制約 | 説明 |
|---------|-----|------|------|
| id | BIGINT | PRIMARY KEY | プライマリキー |
| name | VARCHAR(255) | NOT NULL, UNIQUE | プロジェクト名 |
| description | TEXT | | プロジェクトの説明 |
| created_at | TIMESTAMP | NOT NULL | 作成日時 |
| updated_at | TIMESTAMP | NOT NULL | 更新日時 |

### 3.3 TableDefinition（テーブル定義）
| カラム名 | 型 | 制約 | 説明 |
|---------|-----|------|------|
| id | BIGINT | PRIMARY KEY | プライマリキー |
| project_id | BIGINT | NOT NULL, FOREIGN KEY | プロジェクトID |
| schema_name | VARCHAR(255) | | スキーマ名 |
| physical_name | VARCHAR(255) | NOT NULL | 物理名 |
| logical_name | VARCHAR(255) | NOT NULL | 論理名 |
| description | TEXT | | テーブルの説明 |
| created_at | TIMESTAMP | NOT NULL | 作成日時 |
| updated_at | TIMESTAMP | NOT NULL | 更新日時 |

### 3.4 ColumnDefinition（カラム定義）
| カラム名 | 型 | 制約 | 説明 |
|---------|-----|------|------|
| id | BIGINT | PRIMARY KEY | プライマリキー |
| table_definition_id | BIGINT | NOT NULL, FOREIGN KEY | テーブル定義ID |
| physical_name | VARCHAR(255) | NOT NULL | 物理名 |
| logical_name | VARCHAR(255) | NOT NULL | 論理名 |
| data_type | VARCHAR(50) | NOT NULL | データ型 |
| length | INTEGER | | 長さ |
| nullable | BOOLEAN | NOT NULL DEFAULT true | NULL許可 |
| primary_key | BOOLEAN | NOT NULL DEFAULT false | プライマリキー |
| unique_key | BOOLEAN | NOT NULL DEFAULT false | ユニークキー |
| default_value | VARCHAR(255) | | デフォルト値 |
| description | TEXT | | カラムの説明 |
| column_order | INTEGER | NOT NULL | カラム順序 |
| created_at | TIMESTAMP | NOT NULL | 作成日時 |
| updated_at | TIMESTAMP | NOT NULL | 更新日時 |

## 4. パッケージ構成

```
com.example.tabledef/
├── controller/          # コントローラークラス
│   ├── HomeController
│   ├── ProjectController
│   └── TableDefinitionController
├── dto/                 # データ転送オブジェクト
│   ├── ProjectDto
│   ├── TableDefinitionDto
│   └── ColumnDefinitionDto
├── entity/              # エンティティクラス
│   ├── Project
│   ├── TableDefinition
│   └── ColumnDefinition
├── repository/          # リポジトリインターフェース
│   ├── ProjectRepository
│   ├── TableDefinitionRepository
│   └── ColumnDefinitionRepository
├── service/             # サービスクラス
│   ├── ProjectService
│   ├── TableDefinitionService
│   └── ExportService
└── TableDefApplication  # メインクラス
```

## 5. 画面構成

### 5.1 画面遷移図
```
プロジェクト一覧
    ├── プロジェクト新規作成
    ├── プロジェクト編集
    └── テーブル一覧（プロジェクト選択後）
            ├── テーブル新規作成
            ├── テーブル編集
            └── エクスポート（Excel/Markdown/DDL）
```

### 5.2 画面一覧
| 画面名 | パス | 説明 |
|--------|------|------|
| プロジェクト一覧 | /projects | プロジェクトの一覧表示 |
| プロジェクト作成・編集 | /projects/new, /projects/{id}/edit | プロジェクト情報の入力・編集 |
| テーブル一覧 | /projects/{projectId}/tables | プロジェクト内のテーブル一覧 |
| テーブル作成・編集 | /projects/{projectId}/tables/new, /projects/{projectId}/tables/{id}/edit | テーブル定義の入力・編集 |

## 6. API仕様

### 6.1 プロジェクト関連
- `GET /projects` - プロジェクト一覧取得
- `GET /projects/new` - プロジェクト新規作成画面
- `POST /projects` - プロジェクト保存
- `GET /projects/{id}/edit` - プロジェクト編集画面
- `POST /projects/{id}/delete` - プロジェクト削除

### 6.2 テーブル定義関連
- `GET /projects/{projectId}/tables` - テーブル一覧取得
- `GET /projects/{projectId}/tables/new` - テーブル新規作成画面
- `POST /projects/{projectId}/tables` - テーブル保存
- `GET /projects/{projectId}/tables/{id}/edit` - テーブル編集画面
- `POST /projects/{projectId}/tables/{id}/delete` - テーブル削除

### 6.3 エクスポート関連
- `GET /projects/{projectId}/export/excel?tableId={tableId}` - Excel形式でエクスポート
- `GET /projects/{projectId}/export/markdown?tableId={tableId}` - Markdown形式でエクスポート
- `GET /projects/{projectId}/export/ddl?tableId={tableId}` - DDL形式でエクスポート

## 7. セキュリティ

### 7.1 現在の実装
- Spring Securityは未実装（CSRF保護なし）
- 認証・認可機能なし

### 7.2 今後の検討事項
- Spring Securityの導入
- ユーザー認証機能の追加
- プロジェクト単位でのアクセス制御

## 8. 国際化対応

### 8.1 対応言語
- 日本語（ja）- メインサポート言語

### 8.2 メッセージリソース
- `messages_ja.properties` - 日本語（メインサポート言語）

## 9. 開発環境

### 9.1 必要なソフトウェア
- JDK 17以上
- Gradle 7.6以上
- Docker & Docker Compose（オプション）

### 9.2 環境変数
#### 開発環境
- 特に設定不要（H2インメモリDBを使用）

#### 本番環境
- `DB_HOST` - PostgreSQLホスト
- `DB_PORT` - PostgreSQLポート（デフォルト: 5432）
- `DB_NAME` - データベース名（デフォルト: tabledef）
- `DB_USERNAME` - データベースユーザー名
- `DB_PASSWORD` - データベースパスワード

## 10. デプロイメント

### 10.1 ビルド
```bash
./gradlew build
```

### 10.2 実行
#### 開発環境
```bash
./gradlew bootRun
```

#### 本番環境
```bash
java -jar -Dspring.profiles.active=prod build/libs/table-definition-app-0.0.1-SNAPSHOT.jar
```

### 10.3 Docker
```bash
docker-compose up -d
```

## 11. 今後の拡張予定

1. **ユーザー管理機能**
   - ログイン機能
   - プロジェクトごとのアクセス制御

2. **インポート機能**
   - 既存DBからのスキーマ情報取得
   - DDLファイルからのインポート

3. **バージョン管理**
   - テーブル定義の変更履歴管理
   - 差分表示機能

4. **高度なエクスポート**
   - ER図の自動生成
   - PDF形式でのエクスポート

5. **API機能**
   - REST APIの提供
   - 他システムとの連携

6. **UI改善**
   - メッセージ表示の最適化
   - タイトル表示の修正
   - ユーザビリティの向上