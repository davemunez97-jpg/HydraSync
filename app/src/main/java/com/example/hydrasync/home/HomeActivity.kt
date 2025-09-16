package com.example.hydrasync.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.hydrasync.R
import com.example.hydrasync.login.LoginActivity
import com.example.hydrasync.settings.SettingsActivity
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var tvUserName: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var tvProgress: TextView
    private lateinit var tvLastDrink: TextView
    private lateinit var tvTimeAgo: TextView
    private lateinit var tvConnectionStatus: TextView
    private lateinit var btnAddIntake: MaterialButton
    private lateinit var btnHistory: ImageButton
    private lateinit var btnHome: ImageButton
    private lateinit var btnSettings: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var progressBar: ProgressBar

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
        presenter = HomePresenter(this)
        setupClickListeners()

        presenter.loadHomeData()
    }

    private fun initViews() {
        tvUserName = findViewById(R.id.tvUserName)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvProgress = findViewById(R.id.tvProgress)
        tvLastDrink = findViewById(R.id.tvLastDrink)
        tvTimeAgo = findViewById(R.id.tvTimeAgo)
        tvConnectionStatus = findViewById(R.id.tvConnectionStatus)
        btnAddIntake = findViewById(R.id.btnAddIntake)
        btnHistory = findViewById(R.id.btnHistory)
        btnHome = findViewById(R.id.btnHome)
        btnSettings = findViewById(R.id.btnSettings)
        btnProfile = findViewById(R.id.btnProfile)
        progressBar = findViewById(R.id.circularProgress)
    }

    private fun setupClickListeners() {
        btnAddIntake.setOnClickListener { presenter.onAddIntakeClicked() }
        btnHistory.setOnClickListener { presenter.onHistoryClicked() }
        btnSettings.setOnClickListener { presenter.onSettingsClicked() }
        btnProfile.setOnClickListener { presenter.onLogoutClicked() }
        btnHome.setOnClickListener { /* Already on home */ }
    }

    override fun displayHomeData(homeData: HomeData) {
        tvUserName.text = homeData.user.getFullName()
        updateWaterProgress(homeData.waterIntake)

        tvConnectionStatus.text = if (homeData.isConnected) "Connected" else "Disconnected"
        tvConnectionStatus.setTextColor(
            if (homeData.isConnected)
                resources.getColor(R.color.hydra_green, null)
            else
                resources.getColor(R.color.gray, null)
        )
    }

    override fun updateWaterProgress(intake: WaterIntake) {
        tvPercentage.text = "${intake.getPercentage()}%"
        tvProgress.text = intake.getProgressText()
        tvLastDrink.text = "Last Drink: ${intake.lastDrink}"
        tvTimeAgo.text = intake.timeAgo
        progressBar.progress = intake.getPercentage()
    }

    override fun showAddIntakeDialog() {
        val amounts = arrayOf("250ml", "500ml", "750ml", "1000ml")
        val values = intArrayOf(250, 500, 750, 1000)

        AlertDialog.Builder(this)
            .setTitle("Add Water Intake")
            .setItems(amounts) { _, which ->
                presenter.addWaterIntake(values[which])
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun navigateToHistory() {
        Toast.makeText(this, "History feature coming soon!", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}