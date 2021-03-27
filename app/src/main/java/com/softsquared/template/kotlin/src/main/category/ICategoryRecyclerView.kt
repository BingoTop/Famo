package com.softsquared.template.kotlin.src.main.category

interface ICategoryRecyclerView {

    fun onItemDeleteBtnClicked(categoryID: Int)

    fun onDeleteImpossible()

    fun onCategoryID(categoryID: Int) : Int

    fun onCategoryID2() : Int

    fun onMoveFragment(categoryID : Int, text : String)

    fun getColor(color : String)

    fun onMoveDeleteUpdate()


}