# API仕様書

## 1. API概要

### 1.1 基本情報
- **ベースURL**: `http://localhost:8080`
- **文字コード**: UTF-8
- **レスポンス形式**: HTML（Thymeleafテンプレート）
- **認証**: なし（現在の実装）
- **アプリケーションタイプ**: Spring MVC Webアプリケーション

### 1.2 共通仕様
- すべてのPOSTリクエストは`application/x-www-form-urlencoded`形式
- 日時はISO 8601形式
- バリデーションエラー時は元のフォーム画面を再表示
- 成功/エラーメッセージはFlash属性で管理

## 2. プロジェクト管理API

### 2.1 ルートリダイレクト
アプリケーションのルートにアクセスした際のリダイレクト処理。

**エンドポイント**
```
GET /
```

**レスポンス**
- `/projects`へリダイレクト

### 2.2 プロジェクト一覧取得
プロジェクトの一覧を表示します。

**エンドポイント**
```
GET /projects
```

**レスポンス**
- 成功時: プロジェクト一覧画面（project-list.html）
- Model属性:
  - `projects`: List<ProjectDto> - プロジェクト一覧

### 2.3 プロジェクト新規作成画面
新規プロジェクト作成フォームを表示します。

**エンドポイント**
```
GET /projects/new
```

**レスポンス**
- 成功時: プロジェクト作成画面（project-form.html）
- Model属性:
  - `project`: ProjectDto - 空のプロジェクトDTO

### 2.4 プロジェクト作成
新規プロジェクトを作成します。

**エンドポイント**
```
POST /projects
```

**リクエストパラメータ**
| パラメータ | 型 | 必須 | 説明 | バリデーション |
|-----------|-----|------|------|--------------|
| code | String | ○ | プロジェクトコード | 最大50文字、英数字・ハイフン・アンダースコアのみ |
| name | String | ○ | プロジェクト名 | 最大100文字 |
| description | String | × | プロジェクトの説明 | 最大500文字 |

**レスポンス**
- 成功時: `/projects`へリダイレクト
- Flash属性:
  - `successMessage`: "project.created" - 作成成功メッセージキー
- エラー時: プロジェクト作成画面を再表示（バリデーションエラー含む）

### 2.5 プロジェクト編集画面
既存プロジェクトの編集フォームを表示します。

**エンドポイント**
```
GET /projects/{id}/edit
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| id | Long | プロジェクトID |

**レスポンス**
- 成功時: プロジェクト編集画面（project-form.html）
- Model属性:
  - `project`: ProjectDto - 編集対象のプロジェクト

### 2.6 プロジェクト更新
既存プロジェクトを更新します。

**エンドポイント**
```
POST /projects/{id}
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| id | Long | プロジェクトID |

**リクエストパラメータ**
| パラメータ | 型 | 必須 | 説明 | バリデーション |
|-----------|-----|------|------|--------------|
| code | String | ○ | プロジェクトコード | 最大50文字、英数字・ハイフン・アンダースコアのみ |
| name | String | ○ | プロジェクト名 | 最大100文字 |
| description | String | × | プロジェクトの説明 | 最大500文字 |

**レスポンス**
- 成功時: `/projects`へリダイレクト
- Flash属性:
  - `successMessage`: "project.updated" - 更新成功メッセージキー
- エラー時: プロジェクト編集画面を再表示

### 2.7 プロジェクト削除
プロジェクトを削除します（関連するテーブル定義も削除されます）。

**エンドポイント**
```
POST /projects/{id}/delete
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| id | Long | プロジェクトID |

**レスポンス**
- 成功時: `/projects`へリダイレクト
- Flash属性:
  - `successMessage`: "project.deleted" - 削除成功メッセージキー
  - `errorMessage`: "project.delete.error" - 削除エラーメッセージキー（エラー時）

## 3. テーブル定義管理API

### 3.1 テーブル一覧取得
指定プロジェクト内のテーブル一覧を表示します。

**エンドポイント**
```
GET /projects/{projectId}/tables
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**レスポンス**
- 成功時: テーブル一覧画面（index.html）
- Model属性:
  - `project`: ProjectDto - プロジェクト情報
  - `tables`: List<TableDefinitionDto> - テーブル一覧

### 3.2 テーブル新規作成画面
新規テーブル作成フォームを表示します。

**エンドポイント**
```
GET /projects/{projectId}/tables/new
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**レスポンス**
- 成功時: テーブル作成画面（table-form.html）
- Model属性:
  - `project`: ProjectDto - プロジェクト情報
  - `table`: TableDefinitionDto - 空のテーブルDTO（projectId設定済み）

### 3.3 テーブル編集画面
既存テーブルの編集フォームを表示します。

**エンドポイント**
```
GET /projects/{projectId}/tables/{id}/edit
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |
| id | Long | テーブルID |

**レスポンス**
- 成功時: テーブル編集画面（table-form.html）
- Model属性:
  - `project`: ProjectDto - プロジェクト情報
  - `table`: TableDefinitionDto - 編集対象のテーブル

### 3.4 テーブル作成・更新
テーブル定義を保存します（新規作成または更新）。

**エンドポイント**
```
POST /projects/{projectId}/tables
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**リクエストパラメータ（テーブル情報）**
| パラメータ | 型 | 必須 | 説明 | バリデーション |
|-----------|-----|------|------|--------------|
| id | Long | × | テーブルID（更新時のみ） | - |
| schemaName | String | × | スキーマ名 | - |
| physicalName | String | ○ | 物理名 | 英字開始、英数字とアンダースコアのみ |
| logicalName | String | ○ | 論理名 | - |
| description | String | × | テーブルの説明 | - |

**リクエストパラメータ（カラム情報）**
| パラメータ | 型 | 必須 | 説明 | バリデーション |
|-----------|-----|------|------|--------------|
| columns[n].id | Long | × | カラムID（更新時） | - |
| columns[n].physicalName | String | ○ | カラム物理名 | 英字開始、英数字とアンダースコアのみ |
| columns[n].logicalName | String | ○ | カラム論理名 | - |
| columns[n].dataType | String | ○ | データ型 | - |
| columns[n].length | Integer | × | データ長 | 0以上 |
| columns[n].precision | Integer | × | 精度 | 0以上 |
| columns[n].scale | Integer | × | スケール | 0以上 |
| columns[n].nullable | Boolean | × | NULL許可 | デフォルト: true |
| columns[n].primaryKey | Boolean | × | プライマリキー | デフォルト: false |
| columns[n].uniqueKey | Boolean | × | ユニークキー | デフォルト: false |
| columns[n].defaultValue | String | × | デフォルト値 | - |
| columns[n].description | String | × | カラムの説明 | - |
| columns[n].columnOrder | Integer | ○ | カラム順序 | 1以上 |

**レスポンス**
- 成功時: `/projects/{projectId}/tables`へリダイレクト
- Flash属性:
  - `successMessage`: "table.created" または "table.updated" - 成功メッセージキー
- エラー時: テーブル作成/編集画面を再表示

### 3.5 テーブル削除
テーブル定義を削除します。

**エンドポイント**
```
POST /projects/{projectId}/tables/{id}/delete
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |
| id | Long | テーブルID |

**レスポンス**
- 成功時: `/projects/{projectId}/tables`へリダイレクト
- Flash属性:
  - `successMessage`: "table.deleted" - 削除成功メッセージキー

## 4. エクスポートAPI

### 4.1 Excel形式エクスポート
選択したテーブルをExcel形式でダウンロードします。

**エンドポイント**
```
GET /projects/{projectId}/export/excel
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**クエリパラメータ**
| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| tableId | Long | ○ | エクスポート対象のテーブルID |

**レスポンス**
- Content-Type: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- ファイル名: `table_definitions_YYYYMMDD_HHmmss.xlsx`

### 4.2 Markdown形式エクスポート
選択したテーブルをMarkdown形式でダウンロードします。

**エンドポイント**
```
GET /projects/{projectId}/export/markdown
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**クエリパラメータ**
| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| tableId | Long | ○ | エクスポート対象のテーブルID |

**レスポンス**
- Content-Type: `text/markdown; charset=UTF-8`
- ファイル名: `table_definitions_YYYYMMDD_HHmmss.md`

### 4.3 DDL形式エクスポート
選択したテーブルをSQL DDL形式でダウンロードします。

**エンドポイント**
```
GET /projects/{projectId}/export/ddl
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**クエリパラメータ**
| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| tableId | Long | ○ | エクスポート対象のテーブルID |

**レスポンス**
- Content-Type: `text/plain; charset=UTF-8`
- ファイル名: `table_definitions_YYYYMMDD_HHmmss.sql`

## 5. データ転送オブジェクト（DTO）

### 5.1 ProjectDto
```java
{
  id: Long,
  code: String,          // 必須、^[A-Za-z0-9_-]+$、最大50文字
  name: String,          // 必須、最大100文字
  description: String,   // 最大500文字
  tableCount: Integer,   // テーブル数（読み取り専用）
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
}
```

### 5.2 TableDefinitionDto
```java
{
  id: Long,
  projectId: Long,
  schemaName: String,
  physicalName: String,  // 必須、^[a-zA-Z][a-zA-Z0-9_]*$
  logicalName: String,   // 必須
  description: String,
  columns: List<ColumnDefinitionDto>,
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
}
```

### 5.3 ColumnDefinitionDto
```java
{
  id: Long,
  physicalName: String,  // 必須、^[a-zA-Z][a-zA-Z0-9_]*$
  logicalName: String,   // 必須
  dataType: String,      // 必須
  length: Integer,       // 最小値: 0
  precision: Integer,    // 最小値: 0
  scale: Integer,        // 最小値: 0
  nullable: boolean,     // デフォルト: true
  primaryKey: boolean,   // デフォルト: false
  uniqueKey: boolean,    // デフォルト: false
  defaultValue: String,
  description: String,
  columnOrder: Integer   // 必須、最小値: 1
}
```

## 6. エラーハンドリング

### 6.1 バリデーションエラー
フォーム入力値が不正な場合、入力画面を再表示します。

**エラー情報**
- BindingResultにエラー情報が格納される
- Thymeleafのth:errorsで表示

### 6.2 ランタイムエラー
- 処理中のエラーはcatch節で捕捉
- Flash属性またはBindingResultにエラーメッセージを追加
- エクスポート時のtableId未指定はRuntimeExceptionをスロー

## 7. データ型定義

### 7.1 サポートするデータ型
| 値 | 説明 |
|-----|------|
| VARCHAR | 可変長文字列 |
| CHAR | 固定長文字列 |
| TEXT | 長文テキスト |
| INTEGER | 整数 |
| BIGINT | 長整数 |
| DECIMAL | 固定小数点数 |
| NUMERIC | 数値 |
| FLOAT | 浮動小数点数 |
| DOUBLE | 倍精度浮動小数点数 |
| DATE | 日付 |
| TIME | 時刻 |
| TIMESTAMP | タイムスタンプ |
| BOOLEAN | 真偽値 |
| BINARY | バイナリ |
| VARBINARY | 可変長バイナリ |
| BLOB | バイナリラージオブジェクト |
| CLOB | 文字ラージオブジェクト |
| JSON | JSON |
| XML | XML |

## 8. 国際化対応

### 8.1 対応言語
- 日本語（ja）- メインサポート言語

### 8.2 言語切替
ブラウザの言語設定またはAccept-Languageヘッダーにより自動判定

### 8.3 メッセージファイル
- `/src/main/resources/i18n/messages_ja.properties`

## 9. セキュリティ考慮事項

### 9.1 現在の実装
- 認証・認可機能なし
- CSRF保護なし（Spring Security未実装）

### 9.2 推奨事項
- HTTPS通信の使用
- Spring Securityの導入
- 入力値のサニタイゼーション
- SQLインジェクション対策（JPA使用により基本的に対策済み）