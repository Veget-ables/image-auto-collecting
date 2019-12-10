# Image Auto Collecting
Flickr Services APIの [flickr.photos.search](https://www.flickr.com/services/api/flickr.photos.search.html) 
から取得した画像をダウンロードするスクリプト

## Usage
前準備として、Flickr APIを利用するために [こちら](https://www.flickr.com/services/apps/create/apply) からAPIKeyを取得する。<br>

スクリプトの実行には次の手順を行う。
1. `git clone <repository>`
2. スクリプトが読み込むための [config.properties](#configproperties) を任意の場所に配置
3. リポジトリのルートに移動しスクリプトを実行<br>
`./runcollecting.sh [OPTIONS]`

スクリプトが成功すると Flickr API により取得した画像が出力先のフォルダにダウンロードされている。<br>
スクリプトで使用するオプションは [Options of Script](#options-of-script) で要確認。

### config.properties
設定ファイルとしてconfig.propertiesを作成する。
ファイルの中身はFlickr APIを利用するために必要な API_KEY と SECRET の情報を書く。
```properties#
API_KEY = <取得したapi keyの値>
SECRET = <取得したsecretの値>
```

### Options of Script
実行時に指定するオプションは、以下のように `./runcollecting.sh --help` で確認することができる。
```shell script
./runcollecting.sh --help
Usage: imageautocollecting [OPTIONS]

Options:
  -cp, --configPath TEXT  Defaults to './config.properties'. Specify the path
                          to config.properties.
  -op, --outputPath TEXT  Default to './output'. Specify the path to output
                          the images.
  -tg, --tags             A comma-delimited list of tags. Photos with one or
                          more of the tags listed will be returned. You can
                          exclude results that match a term by prepending it
                          with a - character.
  -tm, --tagMode TEXT     Defaults to 'any'. The possible values are: 'any'
                          and 'all'
  -t, --text TEXT         A free text search. Photos who's title, description
                          or tags contain the text will be returned. You can
                          exclude results that match a term by prepending it
                          with a - character.
  -s, --sort INT          Defaults to 6 (relevance). The possible values are:
                          0 (date-posted-desc), 1 (date-posted-asc), 2
                          (date-taken-desc) 3 (date-taken-asc), 4
                          (interestingness-desc), 5 (interestingness-asc), and
                          6 (relevance)
  -m, --media TEXT        Defaults to 'all'. The possible values are 'all',
                          'photos' and 'videos'
  -ex, --extras           The possible values are: description, license,
                          date_upload, date_taken, owner_name, icon_server,
                          original_format, last_update, geo, tags,
                          machine_tags, o_dims, views, media, path_alias,
                          url_sq, url_t, url_s, url_q, url_m, url_n, url_z,
                          url_c, url_l, url_o
  -pp, --perPage INT      Defaults to 100. The maximum allowed value is 500.
  -p, --page INT          Defaults to 1. The page of results to return.
  -h, --help              Show this message and exit
```
`-cp, --configPath` はconfig.propertiesの場所を指定する。省略すると `リポジトリのルートパス/config.properties` を探す。<br>
`-op, --outputPath` はダウンロード画像を出力するディレクトリの場所を指定する。省略すると `リポジトリのルートパス/output` に出力される<br>

それ以外のオプションは [flickr.photos.search](https://www.flickr.com/services/api/flickr.photos.search.html) の
Argumentsとして定義されているリクエストパラメータに対応している。

スクリプト実行例
```shell script
設定ファイルパスと出力先パスを指定して キーワード"cat" で実行
./runcollecting.sh -cp=/Users/taro/Desktop/flickr/config.properties -op=/Users/taro/Desktop/flickr/output -t="cat" 

指定した画像サイズ毎に10個の画像をダウンロードするように実行
./runcollecting.sh -t="cat" -ex="url_sq,url_t" -pp=10

```
