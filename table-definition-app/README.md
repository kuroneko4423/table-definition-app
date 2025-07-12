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
./gradlew bootRun
```

#### 本番環境（Supabase）
```bash
# 環境変数ファイルをコピー
cp .env.example .env

# .envファイルを編集してSupabase情報を設定
nano .env
```

### 3. アプリケーション起動

#### Gradle起動
```bash
# 開発環境
./gradlew bootRun

# 本番環境
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

#### Docker起動
```bash
# .envファイル設定後
docker-compose up -d
```

### 4. アクセス

ブラウザで http://localhost:8080 にアクセス

## 📁 プロジェクト構造

```
table-definition-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/tabledef/
│   │   │   ├── controller/          # Webコントローラー
│   │   │   ├── dto/                 # データ転送オブジェクト
│   │   │   ├── model/               # JPA エンティティ
│   │   │   ├── repository/          # データアクセス層
│   │   │   ├── service/             # ビジネスロジック層
│   │   │   ├── config/              # 設定クラス
│   │   │   └── util/                # ユーティリティ
│   │   └── resources/
│   │       ├── templates/           # Thymeleafテンプレート
│   │       ├── static/              # CSS、JS、画像
│   │       ├── i18n/                # 国際化メッセージ
│   │       ├── application*.yml     # 設定ファイル
│   │       └── schema*.sql          # データベーススキーマ
│   └── test/                        # テストコード
├── docs/                            # ドキュメント
├── docker-compose.yml               # Docker設定
├── .env.example                     # 環境変数テンプレート
├── .gitignore                       # Git除外設定
└── README.md                        # このファイル
```

## ⚙️ 設定

### 環境変数

本番環境（Supabase）で使用する環境変数：

| 環境変数 | 説明 | デフォルト値 | 例 |
|---------|------|------------|-----|
| `SPRING_PROFILES_ACTIVE` | アクティブプロファイル | - | `prod` |
| `SUPABASE_DB_HOST` | データベースホスト | `db.your-project.supabase.co` | `aws-0-ap-northeast-1.pooler.supabase.com` |
| `SUPABASE_DB_PORT` | ポート番号 | `5432` | `6543` (Pooler使用時) |
| `SUPABASE_DB_NAME` | データベース名 | `postgres` | `postgres` |
| `SUPABASE_DB_USERNAME` | ユーザー名 | `postgres` | `postgres.myzzlgiavhouevrjvjfa` |
| `SUPABASE_DB_PASSWORD` | パスワード | - | `your-password` |

### .envファイルの設定例

```env
# Spring設定
SPRING_PROFILES_ACTIVE=prod

# Supabase設定
SUPABASE_DB_HOST=aws-0-ap-northeast-1.pooler.supabase.com
SUPABASE_DB_PORT=6543
SUPABASE_DB_NAME=postgres
SUPABASE_DB_USERNAME=postgres.myzzlgiavhouevrjvjfa
SUPABASE_DB_PASSWORD=your-password
```

**注意事項:**
- Supabase PoolerのConnection Poolingを使用する場合、ポート番号は通常`6543`
- ユーザー名にはプロジェクトIDが含まれる場合あり（例: `postgres.xxxxx`）
- パスワードはSupabaseダッシュボードのSettings > Database > Connection stringから確認可能

## 📊 データベーススキーマ

### プロジェクト (projects)
- **id** (BIGINT, PK) - プライマリキー
- **code** (VARCHAR(50), UK) - プロジェクトコード
- **name** (VARCHAR(100)) - プロジェクト名
- **description** (TEXT) - 説明
- **created_at** (TIMESTAMP) - 作成日時
- **updated_at** (TIMESTAMP) - 更新日時

### テーブル定義 (table_definitions)
- **id** (BIGINT, PK) - プライマリキー
- **project_id** (BIGINT, FK) - プロジェクトID
- **schema_name** (VARCHAR(255)) - スキーマ名
- **physical_name** (VARCHAR(255)) - 物理名
- **logical_name** (VARCHAR(255)) - 論理名
- **description** (TEXT) - 説明
- **created_at** (TIMESTAMP) - 作成日時
- **updated_at** (TIMESTAMP) - 更新日時

### カラム定義 (column_definitions)
- **id** (BIGINT, PK) - プライマリキー
- **table_definition_id** (BIGINT, FK) - テーブル定義ID
- **physical_name** (VARCHAR(255)) - 物理名
- **logical_name** (VARCHAR(255)) - 論理名
- **data_type** (VARCHAR(50)) - データ型
- **length** (INTEGER) - 長さ
- **precision** (INTEGER) - 精度
- **scale** (INTEGER) - スケール
- **nullable** (BOOLEAN) - NULL許可
- **primary_key** (BOOLEAN) - プライマリキー
- **unique_key** (BOOLEAN) - ユニークキー
- **default_value** (VARCHAR(255)) - デフォルト値
- **description** (TEXT) - 説明
- **column_order** (INTEGER) - カラム順序

## 🎯 使用方法

### 1. プロジェクト管理

#### プロジェクトの作成
1. プロジェクト一覧画面で「新規プロジェクト作成」ボタンをクリック
2. 必要事項を入力：
   - **プロジェクトコード** (必須): 英数字・ハイフン・アンダースコアのみ
   - **プロジェクト名** (必須): プロジェクト名
   - **説明** (任意): プロジェクトの詳細説明
3. 「保存」ボタンで作成

#### プロジェクトの管理
- 「編集」ボタンで既存プロジェクトの情報を変更
- 「削除」ボタンでプロジェクトを削除（関連データも削除）
- 「テーブル管理」ボタンまたはプロジェクト名クリックでテーブル一覧へ

### 2. テーブル定義管理

#### テーブルの作成
1. テーブル一覧画面で「テーブル新規作成」ボタンをクリック
2. テーブル情報を入力：
   - **スキーマ名** (任意): データベーススキーマ名
   - **物理名** (必須): テーブルの物理名（英字開始、英数字・アンダースコアのみ）
   - **論理名** (必須): テーブルの論理名
   - **説明** (任意): テーブルの詳細説明

#### カラム定義の設定
1. 「カラムを追加」ボタンでカラムを追加
2. 各カラムの詳細情報を設定：
   - **物理名・論理名** (必須)
   - **データ型** (必須): VARCHAR, INTEGER, TIMESTAMP等
   - **長さ・精度・スケール** (数値型の場合)
   - **制約**: NULL許可、プライマリキー、ユニークキー
   - **デフォルト値・説明** (任意)
   - **カラム順序** (必須): 表示順序

#### サポートするデータ型
- **文字列**: VARCHAR, CHAR, TEXT, CLOB
- **数値**: INTEGER, BIGINT, DECIMAL, NUMERIC, FLOAT, DOUBLE
- **日時**: DATE, TIME, TIMESTAMP
- **その他**: BOOLEAN, BINARY, VARBINARY, BLOB, JSON, XML

### 3. エクスポート機能

1. テーブル一覧でエクスポート対象テーブルを選択（ラジオボタン）
2. 希望する形式のボタンをクリック：
   - **Excel出力**: 詳細なテーブル定義書（.xlsx）
   - **Markdown出力**: 表形式のドキュメント（.md）
   - **DDL出力**: CREATE TABLE文（.sql）

## 🛠 開発・運用

### ビルド
```bash
# アプリケーションビルド
./gradlew build

# テスト実行
./gradlew test

# JARファイル生成
./gradlew bootJar
```

### 本番環境デプロイ

#### 1. Supabaseセットアップ
1. [Supabase](https://supabase.com)でプロジェクト作成
2. SQL Editorで`schema-postgresql.sql`を実行
3. Settings > Databaseから接続情報を確認

#### 2. 環境変数設定
```bash
export SPRING_PROFILES_ACTIVE=prod
export SUPABASE_DB_HOST=your-host
export SUPABASE_DB_PORT=5432
export SUPABASE_DB_NAME=postgres
export SUPABASE_DB_USERNAME=postgres
export SUPABASE_DB_PASSWORD=your-password
```

#### 3. アプリケーション実行
```bash
# JARファイル実行
java -jar build/libs/table-definition-app-0.0.1-SNAPSHOT.jar

# または Gradle実行
./gradlew bootRun
```

#### 4. Docker環境
```bash
# .envファイル設定後
docker-compose up -d

# ログ確認
docker-compose logs -f app

# 停止
docker-compose down
```

### 環境の切り替え
```bash
# 開発環境（H2インメモリDB）
./gradlew bootRun

# 本番環境（PostgreSQL/Supabase）
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

## 📚 ドキュメント

詳細なドキュメントは`docs/`ディレクトリに格納されています：

- [API仕様書](docs/api-specification.md) - REST APIの詳細仕様
- [データベース設計書](docs/database-design.md) - データベーススキーマとER図
- [操作マニュアル](docs/operation-manual.md) - ユーザー向け操作ガイド
- [システム設計書](docs/system-design.md) - アーキテクチャと技術仕様

## 🔍 トラブルシューティング

### よくある問題

#### 1. アプリケーションが起動しない
```bash
# Java バージョン確認
java -version

# ポート確認
lsof -i :8080

# ログ確認
./gradlew bootRun --debug
```

#### 2. データベース接続エラー
- 環境変数が正しく設定されているか確認
- Supabaseプロジェクトが起動しているか確認
- ネットワーク接続を確認

#### 3. エクスポートできない
- テーブルが選択されているか確認
- ブラウザのポップアップブロックを無効化
- ダウンロード権限を確認

#### 4. 文字化け
- ブラウザのエンコーディングをUTF-8に設定
- ファイルをUTF-8対応アプリケーションで開く

### ログ確認
```bash
# アプリケーションログ
tail -f logs/application.log

# Dockerログ
docker-compose logs -f app

# 詳細ログ（デバッグモード）
./gradlew bootRun --debug
```

## 🤝 貢献

1. このリポジトリをフォーク
2. フィーチャーブランチを作成 (`git checkout -b feature/AmazingFeature`)
3. 変更をコミット (`git commit -m 'Add some AmazingFeature'`)
4. ブランチにプッシュ (`git push origin feature/AmazingFeature`)
5. プルリクエストを作成

## 📋 変更履歴

### v2.0.0 (Latest)
- ✨ プロジェクト管理機能を追加
- 🔧 URLパス構造を `/projects/{projectId}/tables` 形式に変更
- 🛡️ プロジェクト内でのテーブル物理名のユニーク制約を追加
- 🗄️ PostgreSQL/Supabase対応（本番環境）
- 🐳 Docker Compose設定を追加
- 🌐 多言語対応（日本語メッセージ）
- 🎨 UI改善（メッセージ表示の最適化、タイトル表示の修正）
- ⚡ 環境変数による設定管理
- 📊 precision、scaleカラムの追加

### v1.0.0
- 🎉 初期リリース
- ✨ テーブル定義の作成・編集・削除
- 📤 Excel、Markdown、DDL形式でのエクスポート
- 🗄️ H2インメモリデータベース使用

## 🔮 今後の予定

### 短期計画
- 🔐 Spring Security導入
- 👤 ユーザー認証・認可機能
- 📱 モバイル対応強化

### 中期計画
- 📊 ER図自動生成
- 📥 データベーススキーマインポート
- 🔄 バージョン管理機能
- 🚀 REST API提供

### 長期計画
- 🏢 マルチテナント対応
- 🔄 ワークフロー・承認機能
- ☁️ クラウドネイティブ化
- 🔧 CI/CD強化

## ⚠️ 制約事項

- 認証機能なし（現在のバージョン）
- 同時編集の排他制御なし
- 一度に1テーブルのみエクスポート可能
- Internet Explorer非対応

## 📄 ライセンス

このプロジェクトはMITライセンスの下で公開されています。詳細は[LICENSE](LICENSE)ファイルを参照してください。

## 📞 サポート

- 🐛 バグレポート: [Issues](../../issues)
- 💡 機能リクエスト: [Issues](../../issues)
- 📧 お問い合わせ: システム管理者まで

---

**Table Definition Management System** - プロジェクト単位でデータベーステーブル定義を効率的に管理