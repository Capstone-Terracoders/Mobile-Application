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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fds)

        val firstCardView: CardView = findViewById(R.id.fdsFirstCard)
        val secondCardView: CardView = findViewById(R.id.fdsSecondCard)
        val thirdCardView: CardView = findViewById(R.id.fdsThirdCard)

        class FdsActivity : AppCompatActivity() {

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_fds)

                val firstCardView: CardView = findViewById(R.id.fdsFirstCard)
                val secondCardView: CardView = findViewById(R.id.fdsSecondCard)
                val thirdCardView: CardView = findViewById(R.id.fdsThirdCard)

                // Set initial state of cards
                val initialConstraintSet = ConstraintSet()
                initialConstraintSet.clone(this, R.layout.fds_first_card)
                initialConstraintSet.applyTo(findViewById<ConstraintLayout>(R.id.constraintLayout))

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
                    constraintSet.clone(this, R.layout.fds_first_card_expanded) // Use the expanded layout file
                } else {
                    constraintSet.clone(this, R.layout.fds_first_card) // Use the original layout file
                }

                val transition = AutoTransition()
                transition.duration = 200 // Set animation duration
                TransitionManager.beginDelayedTransition(findViewById(R.id.constraintLayout), transition)
                constraintSet.applyTo(findViewById(R.id.constraintLayout))

                view.tag = !isExpanded // Update the tag to toggle state
            }
        }

        // Set initial state of cards
        val initialConstraintSet = ConstraintSet()
        initialConstraintSet.clone(this, R.layout.fds_first_card)
        initialConstraintSet.applyTo(findViewById<ConstraintLayout>(R.id.constraintLayout))

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
            constraintSet.clone(this, R.layout.fds_first_card_expanded) // Use the expanded layout file
        } else {
            constraintSet.clone(this, R.layout.fds_first_card) // Use the original layout file
        }

        val transition = AutoTransition()
        transition.duration = 200 // Set animation duration
        TransitionManager.beginDelayedTransition(findViewById(R.id.constraintLayout), transition)
        constraintSet.applyTo(findViewById(R.id.constraintLayout))

        view.tag = !isExpanded // Update the tag to toggle state
    }
}
