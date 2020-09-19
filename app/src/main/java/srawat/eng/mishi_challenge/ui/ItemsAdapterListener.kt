package srawat.eng.mishi_challenge.ui

import srawat.eng.mishi_challenge.data.Item

interface ItemsAdapterListener {
    fun onRemoveItem(item: Item)
    fun refreshList()
}