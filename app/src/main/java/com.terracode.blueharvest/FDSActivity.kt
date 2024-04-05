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
    private lateinit var cardContainer: LinearLayout
    private var isExpandedFirstCard = false
    private var isExpandedSecondCard = false
    private var isExpandedThirdCard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fds)

        firstCardView = findViewById(R.id.fdsFirstCard)
        secondCardView = findViewById(R.id.fdsSecondCard)
        thirdCardView = findViewById(R.id.fdsThirdCard)

        firstCardView.setOnClickListener {
            toggleCardExpansion(it, isExpandedFirstCard)
            isExpandedFirstCard = !isExpandedFirstCard
        }

        secondCardView.setOnClickListener {
            toggleCardExpansion(it, isExpandedSecondCard)
            isExpandedSecondCard = !isExpandedSecondCard
        }

        thirdCardView.setOnClickListener {
            toggleCardExpansion(it, isExpandedThirdCard)
            isExpandedThirdCard = !isExpandedThirdCard
        }
    }

    private fun toggleCardExpansion(view: View, isExpanded: Boolean) {
        if (!isExpanded) {
            // Add expanded view
            val expandedView = layoutInflater.inflate(getExpandedLayoutId(view.id), null)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = resources.getDimensionPixelSize(R.dimen.expanded_card_margin_top)
            expandedView.layoutParams = params
            (view as ViewGroup).addView(expandedView)
        } else {
            // Remove expanded view
            (view as ViewGroup).removeAllViews()
        }
    }


    private fun getExpandedLayoutId(viewId: Int): Int {
        return when (viewId) {
            R.id.fdsFirstCard -> R.layout.fds_first_card_expanded
            R.id.fdsSecondCard -> R.layout.fds_second_card_expanded
            R.id.fdsThirdCard -> R.layout.fds_third_card_expanded
            else -> throw IllegalArgumentException("Invalid view ID")
        }
    }




}