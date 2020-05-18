package ca.psdev.android.sandbox.listview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as Toolbar)


        for (i in 1..101) {
            mItems.add("Item # %03d".format(i))
        }

        lv_items.setOnItemClickListener { _, _, position, _ ->
            val item = mItems.get(position)
            Toast.makeText(this, "Clicked '$item'", Toast.LENGTH_SHORT).show()
        }

        // val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mItems)
        // lv_items.adapter = adapter
        lv_items.adapter = StringAdapter(this, mItems)
    }

    // get more info here - https://www.raywenderlich.com/155-android-listview-tutorial-with-kotlin
    class StringAdapter(private val context: Context,
                        private val data: ArrayList<String>) : BaseAdapter() {

        private val inflater: LayoutInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return data.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
            view.text = data[position].toUpperCase()
            return view;
        }

        override fun getItem(position: Int): Any {
            return data.get(position).toUpperCase()
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }

}
