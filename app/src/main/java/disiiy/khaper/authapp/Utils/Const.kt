package disiiy.khaper.authapp.Utils

object Const {
    val COLLECTION_PATH = "recipes"
    val PATH_NAME = "recipeName"

    fun setTimeStamp (): Long{
        val time = (-1 *System.currentTimeMillis())
        return time
    }
}