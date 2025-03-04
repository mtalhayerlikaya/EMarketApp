package com.example.emarketapp.view


import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.emarketapp.R
import com.example.emarketapp.base.BaseActivity
import com.example.emarketapp.databinding.ActivityMainBinding
import com.example.emarketapp.utils.gone
import com.example.emarketapp.utils.updateStatusBarColor
import com.example.emarketapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this@MainActivity.updateStatusBarColor(R.color.toolbar_color)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                binding.bottomNavigationView.gone()
            } else {
                binding.bottomNavigationView.visible()
            }
        }
    }

    fun setBadge(badgeCount: Int) {
        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.basketFragment)
        if (badgeCount != 0) {
            badge.isVisible = true
            badge.number = badgeCount
        } else {
            badge.isVisible = false
        }
    }
}