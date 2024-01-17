package hr.ferit.antonioparadzik.tunesforyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp;
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        FirebaseApp.initializeApp(this)

        auth= FirebaseAuth.getInstance()

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeNav -> {
                    loadFragment(HomepageFragment())
                    true
                }
                R.id.myPlaylistsNav -> {
                    loadFragment(MyPlaylistsFragment())
                    true
                }
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, LoginFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container, HomepageFragment())
                .commit()
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    fun hideBottomNavigation(){
        bottomNavigationView.visibility= View.GONE;
    }

    fun showBottomNavigation(){
        bottomNavigationView.visibility=View.VISIBLE;
    }
}