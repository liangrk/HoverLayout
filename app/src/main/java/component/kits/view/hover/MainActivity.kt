package component.kits.view.hover

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import component.kits.view.hover.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.hover.setHoverClickListener {
            val params = it?.layoutParams
            val width = params?.width ?: 0
            val height = params?.height ?: 0
            params?.width = width - 10
            params?.height = height - 10
            it?.layoutParams = params
        }
    }
}