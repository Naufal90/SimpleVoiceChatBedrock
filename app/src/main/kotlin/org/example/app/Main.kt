package org.example.app

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Main : JavaPlugin() {

    private val playersPositions = mutableMapOf<Player, Location>()
    private val voiceRange = 20.0 // Jarak untuk proximity voice chat dalam blok

    override fun onEnable() {
        logger.info("SimpleVoiceChat plugin enabled")

        // Jadwal task untuk memperbarui posisi pemain setiap detik
        object : BukkitRunnable() {
            override fun run() {
                updatePlayerPositions()
                handleProximityVoiceChat()
            }
        }.runTaskTimer(this, 0, 20) // 0 delay, 20 ticks (1 detik) interval
    }

    override fun onDisable() {
        logger.info("SimpleVoiceChat plugin disabled")
    }

    private fun updatePlayerPositions() {
        // Memperbarui posisi semua pemain
        for (player in Bukkit.getOnlinePlayers()) {
            playersPositions[player] = player.location
        }
    }

    private fun handleProximityVoiceChat() {
        // Proses logika proximity voice chat berdasarkan jarak antar pemain
        for ((player, location) in playersPositions) {
            val nearbyPlayers = playersPositions.filter {
                it.key != player && it.value.distance(location) <= voiceRange
            }.keys

            // Logika untuk mengaktifkan komunikasi suara dengan pemain di sekitar
            startVoiceChat(player, nearbyPlayers)
        }
    }

    private fun startVoiceChat(player: Player, nearbyPlayers: Set<Player>) {
        // Implementasi untuk memulai komunikasi suara menggunakan WebRTC
        // Di sini kamu dapat menambahkan kode untuk menginisialisasi sesi WebRTC
        // dan mengatur koneksi antara pemain yang berada dalam jangkauan.
        player.sendMessage("Voice chat aktif dengan ${nearbyPlayers.size} pemain di dekatmu.")
    }
}
