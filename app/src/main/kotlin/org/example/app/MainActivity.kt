package org.example.app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.bukkit.entity.Player

class MainActivity : AppCompatActivity() {

    private lateinit var voiceChatManager: VoiceChatManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Pastikan sudah ada layout dengan id activity_main

        voiceChatManager = VoiceChatManager()

        // Tombol untuk mulai proximity voice
        val startVoiceChatButton: Button = findViewById(R.id.startVoiceChatButton)
        startVoiceChatButton.setOnClickListener {
            val player = getPlayer() // Ambil player yang aktif
            voiceChatManager.startProximityVoice(player)
        }

        // Tombol untuk mute/unmute
        val toggleMuteButton: Button = findViewById(R.id.toggleMuteButton)
        toggleMuteButton.setOnClickListener {
            val player = getPlayer() // Ambil player yang ingin di-mute/unmute
            if (voiceChatManager.isMuted(player)) {
                voiceChatManager.unmutePlayer(player)
            } else {
                voiceChatManager.mutePlayer(player)
            }
        }
    }

    // Fungsi untuk mendapatkan player (misalnya pemain yang sedang aktif)
    private fun getPlayer(): Player {
        // Implementasi untuk mendapatkan player yang aktif dari server Minecraft atau lainnya
        return somePlayerObject // Gantilah dengan player yang sesuai
    }
}
