package com.terracode.blueharvest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import android.transition.AutoTransition
import android.transition.TransitionManager

class FdsActivity : AppCompatActivity() {

    private lateinit var firstCardView: CardView
    private lateinit var secondCardView: CardView
    private lateinit var thirdCardView: CardView
    private lateinit var constraintLayout: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fds)

        firstCardView = findViewById(R.id.fdsFirstCard)
        secondCardView = findViewById(R.id.fdsSecondCard)
        thirdCardView = findViewById(R.id.fdsThirdCard)
        constraintLayout = findViewById(R.id.constraintLayout)

        // Set initial state of cards
        val initialConstraintSet = ConstraintSet()
        initialConstraintSet.clone(this, R.layout.fds_first_card)
        initialConstraintSet.applyTo(constraintLayout)

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
        val constraintSet = ConstraintSet()
        val isExpanded = view.tag as? Boolean ?: false

        if (!isExpanded) {
            constraintSet.clone(
                this,
                R.layout.fds_first_card_expanded
            ) // Use the expanded layout file
        } else {
            constraintSet.clone(
                this,
                R.layout.fds_first_card
            ) // Use the original layout file
        }

        val transition = AutoTransition()
        transition.duration = 200 // Set animation duration
        TransitionManager.beginDelayedTransition(
            constraintLayout,
            transition
        )
        constraintSet.applyTo(constraintLayout)

        view.tag = !isExpanded // Update the tag to toggle state
    }
}
