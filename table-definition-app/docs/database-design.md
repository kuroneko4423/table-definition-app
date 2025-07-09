# データベース設計書

## 1. データベース概要

### 1.1 データベース構成
- **開発環境**: H2 Database (インメモリ)
- **本番環境**: PostgreSQL

### 1.2 文字コード
- UTF-8

### 1.3 命名規則
- テーブル名: スネークケース（小文字）
- カラム名: スネークケース（小文字）
- インデックス名: `idx_<table_name>_<column_name>`
- 外部キー名: `fk_<table_name>_<referenced_table_name>`

## 2. テーブル定義詳細

### 2.1 projects（プロジェクト）

#### テーブル情報
| 項目 | 値 |
|------|-----|
| テーブル名（物理） | projects |
| テーブル名（論理） | プロジェクト |
| 説明 | テーブル定義を管理するプロジェクト |

#### カラム定義
| # | カラム名（物理） | カラム名（論理） | データ型 | 長さ | NULL | PK | UK | デフォルト | 説明 |
|---|-----------------|-----------------|----------|------|------|----|----|-----------|------|
| 1 | id | ID | BIGINT | - | × | ○ | × | AUTO_INCREMENT | プライマリキー |
| 2 | name | プロジェクト名 | VARCHAR | 255 | × | × | ○ | - | プロジェクト名 |
| 3 | description | 説明 | TEXT | - | ○ | × | × | - | プロジェクトの説明 |
| 4 | created_at | 作成日時 | TIMESTAMP | - | × | × | × | CURRENT_TIMESTAMP | レコード作成日時 |
| 5 | updated_at | 更新日時 | TIMESTAMP | - | × | × | × | CURRENT_TIMESTAMP | レコード更新日時 |

#### インデックス
| インデックス名 | カラム | 種別 |
|---------------|--------|------|
| PRIMARY | id | PRIMARY KEY |
| uk_projects_name | name | UNIQUE |

### 2.2 table_definitions（テーブル定義）

#### テーブル情報
| 項目 | 値 |
|------|-----|
| テーブル名（物理） | table_definitions |
| テーブル名（論理） | テーブル定義 |
| 説明 | データベースのテーブル定義情報 |

#### カラム定義
| # | カラム名（物理） | カラム名（論理） | データ型 | 長さ | NULL | PK | UK | デフォルト | 説明 |
|---|-----------------|-----------------|----------|------|------|----|----|-----------|------|
| 1 | id | ID | BIGINT | - | × | ○ | × | AUTO_INCREMENT | プライマリキー |
| 2 | project_id | プロジェクトID | BIGINT | - | × | × | × | - | 所属プロジェクト |
| 3 | schema_name | スキーマ名 | VARCHAR | 255 | ○ | × | × | - | データベーススキーマ名 |
| 4 | physical_name | 物理名 | VARCHAR | 255 | × | × | × | - | テーブル物理名 |
| 5 | logical_name | 論理名 | VARCHAR | 255 | × | × | × | - | テーブル論理名 |
| 6 | description | 説明 | TEXT | - | ○ | × | × | - | テーブルの説明 |
| 7 | created_at | 作成日時 | TIMESTAMP | - | × | × | × | CURRENT_TIMESTAMP | レコード作成日時 |
| 8 | updated_at | 更新日時 | TIMESTAMP | - | × | × | × | CURRENT_TIMESTAMP | レコード更新日時 |

#### インデックス
| インデックス名 | カラム | 種別 |
|---------------|--------|------|
| PRIMARY | id | PRIMARY KEY |
| idx_table_definitions_project_id | project_id | INDEX |
| uk_table_definitions_project_physical | project_id, physical_name | UNIQUE |

#### 外部キー
| 外部キー名 | カラム | 参照テーブル | 参照カラム | ON DELETE |
|-----------|--------|-------------|-----------|-----------|
| fk_table_definitions_project | project_id | projects | id | CASCADE |

### 2.3 column_definitions（カラム定義）

#### テーブル情報
| 項目 | 値 |
|------|-----|
| テーブル名（物理） | column_definitions |
| テーブル名（論理） | カラム定義 |
| 説明 | テーブルのカラム定義情報 |

#### カラム定義
| # | カラム名（物理） | カラム名（論理） | データ型 | 長さ | NULL | PK | UK | デフォルト | 説明 |
|---|-----------------|-----------------|----------|------|------|----|----|-----------|------|
| 1 | id | ID | BIGINT | - | × | ○ | × | AUTO_INCREMENT | プライマリキー |
| 2 | table_definition_id | テーブル定義ID | BIGINT | - | × | × | × | - | 所属テーブル |
| 3 | physical_name | 物理名 | VARCHAR | 255 | × | × | × | - | カラム物理名 |
| 4 | logical_name | 論理名 | VARCHAR | 255 | × | × | × | - | カラム論理名 |
| 5 | data_type | データ型 | VARCHAR | 50 | × | × | × | - | データ型 |
| 6 | length | 長さ | INTEGER | - | ○ | × | × | - | データ長 |
| 7 | nullable | NULL許可 | BOOLEAN | - | × | × | × | true | NULL値の許可 |
| 8 | primary_key | プライマリキー | BOOLEAN | - | × | × | × | false | PKフラグ |
| 9 | unique_key | ユニークキー | BOOLEAN | - | × | × | × | false | UKフラグ |
| 10 | default_value | デフォルト値 | VARCHAR | 255 | ○ | × | × | - | デフォルト値 |
| 11 | description | 説明 | TEXT | - | ○ | × | × | - | カラムの説明 |
| 12 | column_order | カラム順序 | INTEGER | - | × | × | × | - | 表示順序 |
| 13 | created_at | 作成日時 | TIMESTAMP | - | × | × | × | CURRENT_TIMESTAMP | レコード作成日時 |
| 14 | updated_at | 更新日時 | TIMESTAMP | - | × | × | × | CURRENT_TIMESTAMP | レコード更新日時 |

#### インデックス
| インデックス名 | カラム | 種別 |
|---------------|--------|------|
| PRIMARY | id | PRIMARY KEY |
| idx_column_definitions_table_id | table_definition_id | INDEX |
| uk_column_definitions_table_physical | table_definition_id, physical_name | UNIQUE |

#### 外部キー
| 外部キー名 | カラム | 参照テーブル | 参照カラム | ON DELETE |
|-----------|--------|-------------|-----------|-----------|
| fk_column_definitions_table | table_definition_id | table_definitions | id | CASCADE |

## 3. データ型マッピング

### 3.1 サポートするデータ型
| データ型 | PostgreSQL | H2 | 説明 |
|---------|------------|-----|------|
| VARCHAR | VARCHAR(n) | VARCHAR(n) | 可変長文字列 |
| CHAR | CHAR(n) | CHAR(n) | 固定長文字列 |
| TEXT | TEXT | TEXT | 長文テキスト |
| INTEGER | INTEGER | INTEGER | 整数 |
| BIGINT | BIGINT | BIGINT | 長整数 |
| DECIMAL | DECIMAL(p,s) | DECIMAL(p,s) | 固定小数点数 |
| DATE | DATE | DATE | 日付 |
| TIMESTAMP | TIMESTAMP | TIMESTAMP | タイムスタンプ |
| BOOLEAN | BOOLEAN | BOOLEAN | 真偽値 |

## 4. ER図

```mermaid
erDiagram
    projects ||--o{ table_definitions : "has"
    table_definitions ||--o{ column_definitions : "has"
    
    projects {
        bigint id PK
        varchar name UK
        text description
        timestamp created_at
        timestamp updated_at
    }
    
    table_definitions {
        bigint id PK
        bigint project_id FK
        varchar schema_name
        varchar physical_name
        varchar logical_name
        text description
        timestamp created_at
        timestamp updated_at
    }
    
    column_definitions {
        bigint id PK
        bigint table_definition_id FK
        varchar physical_name
        varchar logical_name
        varchar data_type
        integer length
        boolean nullable
        boolean primary_key
        boolean unique_key
        varchar default_value
        text description
        integer column_order
        timestamp created_at
        timestamp updated_at
    }
```

## 5. シーケンス定義

### 5.1 PostgreSQL
各テーブルのIDカラムは、PostgreSQLの`SERIAL`型または`IDENTITY`列を使用して自動採番されます。

### 5.2 H2 Database
H2データベースでは、`AUTO_INCREMENT`を使用して自動採番されます。

## 6. トリガー定義

### 6.1 updated_atの自動更新
現在の実装では、アプリケーション側（JPA）で`updated_at`の更新を管理しています。

## 7. パフォーマンス考慮事項

### 7.1 インデックス戦略
- 外部キーには自動的にインデックスを作成
- 検索頻度の高いカラムにインデックスを設定
- 複合ユニークキーにはインデックスを設定

### 7.2 接続プール設定
- 最大接続数: 20
- 最小アイドル接続数: 5
- 接続タイムアウト: 30秒

## 8. バックアップ・リカバリ

### 8.1 開発環境（H2）
- インメモリDBのため、再起動時にデータは失われる
- 初期データは`data.sql`で投入

### 8.2 本番環境（PostgreSQL）
- 定期的なバックアップを推奨
- `pg_dump`コマンドを使用したバックアップ
- ポイントインタイムリカバリの設定を推奨

### 8.3 スキーマ管理
- **H2用スキーマ**: `schema-h2.sql`
- **PostgreSQL用スキーマ**: `schema-postgresql.sql`
- 環境に応じて適切なスキーマファイルが自動選択される