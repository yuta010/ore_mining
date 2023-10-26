# Name
<h3>鉱石採掘ゲーム(OreMiningGame)</h2>

・Minecraft内で特定の鉱石を採掘するとスコアが加算されるゲームをするためのプラグインです。</br>
・５分間のうちにハイスコアを目指すゲームです。取得難易度が高くスコアの高い鉱石を狙って採掘するか、取得難易度が低くスコアの低い鉱石を狙って採掘するかはPlayerの戦略次第です。

# DEMO

動画は後日掲載予定

# Requirement
* Java版 Minecraft version 1.20.1
* Spigot 1.20.1
* JDK 17
* MySQL

# Usage
<h3>コマンドの説明</h3>
１　/oremining -> 鉱石採掘ゲームを実行</br>
２　/oremining list -> 過去の結果を表示することができます。</br>
３　/gamecancel -> 鉱石採掘ゲームを強制終了します。</br>

<h3>コマンド機能詳細</h3>
<h3>１　/oreminingコマンドについて</h3>
(1)　制限時間は５分間です。</br>
(2)　ゲーム開始時に体力・空腹値が最大化され、装備も変更（メインハンドにダイヤモンドピッケル、オフハンドに松明６４個）されます。</br>
(3)　特定の鉱石（石炭、鉄、金、ダイヤモンド）を破壊するとスコアが加算されます。スコアの内訳は石炭１０点、鉄、１００点、金８００点、ダイヤモンド１，０００点です。</br>
(4)　特殊効果や異常状態はゲーム開始時、終了時に無効化されます。</br>
(5)　ゲーム終了後にプレイヤー名、スコア、日時がDBに保存されます。

<h3>２　/oremining list</h3>
・　DBに保存されたプレイヤー名、スコア、日時を確認することができます。

<h3>３　/gamecancelコマンドについて</h3>
(1)　鉱石採掘ゲーム実行中にコマンドを実行するとゲームを強制終了することができます。</br>
(2)　ゲームを実行していない場合は、「ゲームは実行されていません。」と表示されます。

# Note

・メインハンドのアイテムは上書きされてしまうので注意してください。</br>
・ゲームを途中で中断する場合は必ずgamecancelコマンドを実行してください。</br>
・動作確認はMacのみでしか行えていません。</br>
・ローカル環境でご使用ください。</br>