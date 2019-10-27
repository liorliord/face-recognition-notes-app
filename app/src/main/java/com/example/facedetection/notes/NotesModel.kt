package com.example.facedetection.notes

import com.google.firebase.database.*
import java.util.*


class NotesModel(private val listener: Listener) {
    private lateinit var database: DatabaseReference

    fun updateList(dataSnapshot: DataSnapshot) {
        val list = mutableListOf<String>()

        for (item in dataSnapshot.children) {
            list.add(item.value.toString())
        }
        listener.updateList(list)
    }

    fun init(id: String) {
        database = FirebaseDatabase.getInstance().reference
        database.child(id).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                updateList(p0)
            }

        })
    }

    fun addString(id: String, value: String?) {
        val currentTime = Calendar.getInstance().time

        database.child(id).child(currentTime.toString()).setValue(value)
    }

    interface Listener {
        fun updateList(list: List<String>)
    }
}