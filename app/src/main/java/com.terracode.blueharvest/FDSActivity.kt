package com.terracode.blueharvest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class FDSActivity : AppCompatActivity() {

    private lateinit var firstCardView: CardView
    private lateinit var secondCardView: CardView
    private lateinit var thirdCardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fds)

        // Initialize your views
        firstCardView = findViewById(R.id.fdsFirstCard)
        secondCardView = findViewById(R.id.fdsSecondCard)
        thirdCardView = findViewById(R.id.fdsThirdCard)

        // Set click listeners for each card
        firstCardView.setOnClickListener {
            toggleCardExpansion(firstCardView)
        }

        secondCardView.setOnClickListener {
            toggleCardExpansion(secondCardView)
        }

        thirdCardView.setOnClickListener {
            toggleCardExpansion(thirdCardView)
        }
    }


    private fun toggleCardExpansion(view: View) {
        val isExpanded = view.tag as? Boolean ?: false

        if (!isExpanded) {
            // If the card is not expanded, add the expanded view
            when (view.id) {
                R.id.fdsFirstCard -> {
                    val expandedView = layoutInflater.inflate(R.layout.fds_first_card_expanded, null)
                    (view as ViewGroup).addView(expandedView)
                }
                R.id.fdsSecondCard -> {
                    val expandedView = layoutInflater.inflate(R.layout.fds_second_card_expanded, null)
                    (view as ViewGroup).addView(expandedView)
                }
                R.id.fdsThirdCard -> {
                    val expandedView = layoutInflater.inflate(R.layout.fds_third_card_expanded, null)
                    (view as ViewGroup).addView(expandedView)
                }
            }
        } else {
            // If the card is already expanded, remove the last added view
            val expandedViewIndex = (view as ViewGroup).childCount - 1
            if (expandedViewIndex >= 0) {
                view.removeViewAt(expandedViewIndex)
            }
        }

        // Toggle the tag to indicate the new state
        view.tag = !isExpanded
    }







}