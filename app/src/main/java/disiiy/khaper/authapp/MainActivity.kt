package disiiy.khaper.authapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import disiiy.khaper.authapp.Utils.Const.COLLECTION_PATH
import disiiy.khaper.authapp.Utils.Const.PATH_NAME
import disiiy.khaper.authapp.adapter.RecipeAdapter
import disiiy.khaper.authapp.model.Recipes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val user : MutableMap<String, Any> = HashMap()
    private  lateinit var recipeAdapter: FirestoreRecyclerAdapter<Recipes, RecipeAdapter.ViewHolder>
    private val recipeCollection = db.collection(COLLECTION_PATH)
    private val recipeQuery = recipeCollection.orderBy(PATH_NAME, Query.Direction.ASCENDING)

    companion object{
        fun getLaunchService(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setConfigurationUi()
        SetUp()
    }

    private fun SetUp() {
        val options = FirestoreRecyclerOptions.Builder<Recipes>().setQuery(recipeQuery, Recipes::class.java).build()
        recipeAdapter = RecipeAdapter(this, recipeCollection, options)
        recipeAdapter.notifyDataSetChanged()
        rv_main.adapter = recipeAdapter
    }

    private fun setConfigurationUi() {
        btn_sign_out.setOnClickListener {
            signOut()
        }
        supportActionBar?.title = "Foods"
        rv_main.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        fb_main.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java).apply {
                putExtra(AddActivity.EDIT_DATA, false)
            })
        }
    }

    private fun signOut() {
        startActivity(LoginActivity.getLaunchService(this))
        FirebaseAuth.getInstance().signOut()
    }

    override fun onStart() {
        super.onStart()
        recipeAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        recipeAdapter.stopListening()
    }

}