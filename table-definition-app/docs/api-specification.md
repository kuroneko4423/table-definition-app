# API仕様書

## 1. API概要

### 1.1 基本情報
- **ベースURL**: `http://localhost:8080`
- **文字コード**: UTF-8
- **レスポンス形式**: HTML（Thymeleafテンプレート）
- **認証**: なし（現在の実装）

### 1.2 共通仕様
- すべてのPOSTリクエストは`application/x-www-form-urlencoded`形式
- 日時はISO 8601形式
- エラー時はエラーページへリダイレクト

## 2. プロジェクト管理API

### 2.1 プロジェクト一覧取得
プロジェクトの一覧を表示します。

**エンドポイント**
```
GET /projects
```

**レスポンス**
- 成功時: プロジェクト一覧画面（projects/index.html）
- Model属性:
  - `projects`: List<ProjectDto> - プロジェクト一覧

### 2.2 プロジェクト新規作成画面
新規プロジェクト作成フォームを表示します。

**エンドポイント**
```
GET /projects/new
```

**レスポンス**
- 成功時: プロジェクト作成画面（projects/form.html）
- Model属性:
  - `project`: ProjectDto - 空のプロジェクトDTO

### 2.3 プロジェクト作成・更新
プロジェクト情報を保存します。

**エンドポイント**
```
POST /projects
```

**リクエストパラメータ**
| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| id | Long | × | 更新時のみ必要 |
| name | String | ○ | プロジェクト名（最大255文字） |
| description | String | × | プロジェクトの説明 |

**レスポンス**
- 成功時: `/projects`へリダイレクト
- Flash属性:
  - `successMessage`: "project.created" - 作成成功メッセージキー
- エラー時: プロジェクト作成画面を再表示

### 2.4 プロジェクト編集画面
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
- 成功時: プロジェクト編集画面（projects/form.html）
- Model属性:
  - `project`: ProjectDto - 編集対象のプロジェクト

### 2.5 プロジェクト削除
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
  - `errorMessage`: "project.delete.error" - 削除エラーメッセージキー

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
  - `table`: TableDefinitionDto - 空のテーブルDTO

### 3.3 テーブル作成・更新
テーブル定義を保存します。

**エンドポイント**
```
POST /projects/{projectId}/tables
```

**パスパラメータ**
| パラメータ | 型 | 説明 |
|-----------|-----|------|
| projectId | Long | プロジェクトID |

**リクエストパラメータ**
| パラメータ | 型 | 必須 | 説明 |
|-----------|-----|------|------|
| id | Long | × | 更新時のみ必要 |
| schemaName | String | × | スキーマ名 |
| physicalName | String | ○ | 物理名（最大255文字） |
| logicalName | String | ○ | 論理名（最大255文字） |
| description | String | × | テーブルの説明 |
| columns[n].id | Long | × | カラムID（更新時） |
| columns[n].physicalName | String | ○ | カラム物理名 |
| columns[n].logicalName | String | ○ | カラム論理名 |
| columns[n].dataType | String | ○ | データ型 |
| columns[n].length | Integer | × | データ長 |
| columns[n].nullable | Boolean | × | NULL許可（デフォルト: true） |
| columns[n].primaryKey | Boolean | × | プライマリキー（デフォルト: false） |
| columns[n].uniqueKey | Boolean | × | ユニークキー（デフォルト: false） |
| columns[n].defaultValue | String | × | デフォルト値 |
| columns[n].description | String | × | カラムの説明 |
| columns[n].columnOrder | Integer | ○ | カラム順序 |

**レスポンス**
- 成功時: `/projects/{projectId}/tables`へリダイレクト
- エラー時: テーブル作成画面を再表示

### 3.4 テーブル編集画面
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
  - `successMessage`: 削除成功メッセージ

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
選択したテーブルをDDL形式でダウンロードします。

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

## 5. エラーハンドリング

### 5.1 バリデーションエラー
フォーム入力値が不正な場合、入力画面を再表示します。

**エラー情報**
- BindingResultにエラー情報が格納される
- Thymeleafのth:errorsで表示

### 5.2 システムエラー
予期しないエラーが発生した場合、エラーページへ遷移します。

**HTTPステータスコード**
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error

## 6. データ型定義

### 6.1 サポートするデータ型
| 値 | 説明 |
|-----|------|
| VARCHAR | 可変長文字列 |
| CHAR | 固定長文字列 |
| TEXT | 長文テキスト |
| INTEGER | 整数 |
| BIGINT | 長整数 |
| DECIMAL | 固定小数点数 |
| DATE | 日付 |
| TIMESTAMP | タイムスタンプ |
| BOOLEAN | 真偽値 |

## 7. 国際化対応

### 7.1 対応言語
- 日本語（ja）- メインサポート言語

### 7.2 言語切替
ブラウザの言語設定またはAccept-Languageヘッダーにより自動判定

## 8. セキュリティ考慮事項

### 8.1 現在の実装
- 認証・認可機能なし
- CSRF保護なし（Spring Security未実装）

### 8.2 推奨事項
- HTTPS通信の使用
- Spring Securityの導入
- 入力値のサニタイゼーション