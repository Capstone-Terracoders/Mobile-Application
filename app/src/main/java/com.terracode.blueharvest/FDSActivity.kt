package com.terracode.blueharvest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class FdsActivity : AppCompatActivity() {

    private lateinit var firstCardView: CardView
    private lateinit var secondCardView: CardView
    private lateinit var thirdCardView: CardView
    private lateinit var cardContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fds)

        firstCardView = findViewById(R.id.fdsFirstCard)
        secondCardView = findViewById(R.id.fdsSecondCard)
        thirdCardView = findViewById(R.id.fdsThirdCard)

        firstCardView.setOnClickListener {
            toggleCardExpansion(it)
        }

        secondCardView.setOnClickListener {
            toggleCardExpansion(it)
        }

        thirdCardView.setOnClickListener {
            toggleCardExpansion(it)
        }
    }


    private fun toggleCardExpansion(view: View) {
        val layoutParams = view.layoutParams
        val isExpanded = view.tag as? Boolean ?: false

        if (!isExpanded) {
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
            // Remove the expanded view if already added
            (view as ViewGroup).removeAllViews()
        }

        // Update the tag to indicate the new state
        view.tag = !isExpanded
    }



}





