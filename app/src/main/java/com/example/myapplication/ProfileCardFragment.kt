package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileCardFragment : Fragment() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.card_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("Profile").document(currentUser.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val profile = documentSnapshot.data
                        val fullNameTextView = view.findViewById<TextView>(R.id.cardName)
                        val firstName = profile?.get("firstName").toString()
                        val lastName = profile?.get("lastName").toString()
                        fullNameTextView.text = "$firstName $lastName"

                        val bioTextView = view.findViewById<TextView>(R.id.cardBio)
                        bioTextView.text = profile?.get("biography").toString()
                        bioTextView.movementMethod = ScrollingMovementMethod()

                    }
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error fetching document", e)
                }
        } else {
            Log.w(ContentValues.TAG, "Error: current user is null")
        }

    }
}