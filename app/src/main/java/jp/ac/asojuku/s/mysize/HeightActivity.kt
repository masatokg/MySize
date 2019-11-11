package jp.ac.asojuku.s.mysize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.Spinner
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_height.*

class HeightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_height)
    }
    // 再表示のときに呼ばれるライフサイクルのコールバックメソッドonResumeをoverride
    override fun onResume() {
        super.onResume()
        // スピナーにitem(選択肢)が選ばれた時のコールバックメソッドを設定
        spinner.onItemSelectedListener =   // スピナーにアイテムを選んだ時の動きを持ったクラスの匿名インスタンスを代入
            object : AdapterView.OnItemSelectedListener {   // アイテムを選んだ時の動きを持ったクラスの継承クラスを定義して匿名インスタンスにする
                override fun onItemSelected(    // アイテムを選んだ時の処理
                    parent: AdapterView<*>?,    // 選択が発生したビュー（スピナーのこと）
                    view: View?,    // 選択されたビュー（選択したアイテムつまり値のこと）
                    position: Int,  // 選んだ選択肢が何番目か
                    id: Long    // 選択されたアイテムのID
                ) {
                    // 選択値を取得するためにスピナーのインスタンスを取得する
                    val spinner = parent as? Spinner;
                    // 選択値(今回は170などの文字列)を取得
                    val item = spinner?.selectedItem as? String;
                    // 取得した値を身長の値のテキストビューに上書きする
                    item?.let {
                        if( it.isNotEmpty() ){ height.text = it } // it つまり身長の値が空文字でなければ、身長のテキストビュー(height)に代入
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {   // アイテムを何も選ばなかった時の処理
                    // 何もしない
                }
            }
        // シークバーの処理を定義する
        // 共有プリファレンスから身長設定値を取得する（シークバーの表示値のため）
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        val heightVal = pref.getInt("HEIGHT", 160); // 身長値を変数に保存
        height.text = heightVal.toString(); // 「身長」ラベルの値もこの取得値で上書き
        // シークバーの現在値（progress）も取得値で上書き
        seekBar.progress = heightVal;
        // シークバーの値が変更されたらコールバックされるメソッドをもつ
        // 匿名クラスのインスタンスを引き渡す
        seekBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener{
                // 1つめのoverrideメソッド
                override fun onProgressChanged(
                    seekBar: SeekBar?,  // 値が変化したシークバーのインスタンス
                    progress: Int,  // 値が変化したシークバーの現在値
                    fromUser: Boolean   // ユーザーが操作したか
                ) {
                    // ユーザーの指定値で「身長」の表示を変える
                    height.text = progress.toString(); // heightラベルの表示値を上書き
                }
                // 2つめのoverrideメソッド
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // ここでは今回処理を何もしない
                }
                // 3つめのoverrideメソッド
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // ここでは今回処理を何もしない
                }
            }
        )

        // ラジオボタンの処理を実装する
        // ラジオグループに選択された時に反応するコールバックメソッドを待機するリスナーを設定
        radioGroup.setOnCheckedChangeListener {
                // 2つの引数（第1引数：ラジオボタングループ）、第2引数：選択されたラジオボタンのid）を受け取って実行する処理
                group, checkedId ->
                    // 「身長」ラベルを上書き(ラジオグループの選ばれたIDのボタンのText属性の値で上書き)
                    height.text = findViewById<RadioButton>(checkedId).text;
        }


    }
    // 画面が閉じられるときに呼ばれるライフサイクルコールバックメソッドをoverride
    override fun onPause() {
        super.onPause()
        // 身長の現在値を共有プリファレンスに保存する処理を実装
        // 共有プリファレンスのインスタンスを取得
        val pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit {
            // 身長ラベルの表示値を取得してStringに変換したのち、Intに変換して保存
            this.putInt("HEIGHT", height.text.toString().toInt());
        }
    }
}
