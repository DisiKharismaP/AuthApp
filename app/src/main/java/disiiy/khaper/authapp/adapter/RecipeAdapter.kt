package disiiy.khaper.authapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.AlteredCharSequence
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import disiiy.khaper.authapp.AddActivity
import disiiy.khaper.authapp.R
import disiiy.khaper.authapp.model.Recipes
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeAdapter(private val context: Context, private val collection : CollectionReference, options : FirestoreRecyclerOptions<Recipes>): FirestoreRecyclerAdapter<Recipes, RecipeAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false))
    }

    class ViewHolder(val view : View): RecyclerView.ViewHolder(view) {
        fun bind(recipes: Recipes){
            view.apply {
                val name = "Name : ${recipes.recipeName}"
                val jenis = "Jenis : ${recipes.recipeJenis}"

                tv_name_food.text = name
                tv_jenis_food.text = jenis
            }
        }
    }

    override fun onBindViewHolder(holder: RecipeAdapter.ViewHolder, position: Int, model: Recipes) {
        holder.bind(model)
        holder.itemView.setOnClickListener {
            showOptions(model)
        }
    }

    private fun showOptions(model: Recipes) {
        //popup option
        val buider = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        val option = arrayOf("Edit", "Delete")
            buider.setItems(option) { dialog, which ->
                when (which){
                //0 buat pindah Activity
                    0 -> context.startActivity(Intent(context, AddActivity::class.java).apply {
                        putExtra(AddActivity.EDIT_DATA, true)
                        putExtra(AddActivity.EXTRA_DATA, model)
                    })
                    1 -> optionDelete(model.recipeId)
                }
            }
        buider.create().show()
    }

    private fun optionDelete(recipeId: String) {
        //popup
        val buider = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            .setTitle("Hapus Data")
            .setMessage("Yakin mau hapus?")
            .setPositiveButton(context.getString(R.string.txt_yes)){
                dialog, which -> deleteById(recipeId)
            }
            .setNegativeButton(android.R.string.cancel, null)
        buider.create().show()
    }

    private fun deleteById(recipeId: String) {
        //untuk ngehapus
        collection.document(recipeId).delete()
            .addOnCompleteListener{
            Toast.makeText(context, "Succes", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show()
            }

    }

}