package org.example.app

import de.maxhenkel.voicechat.api.VoiceChatAPI
import org.bukkit.entity.Player

class VoiceChatManager {

    private val voiceChatAPI: VoiceChatAPI = VoiceChatAPI.getInstance()

    // Memulai suara proximity untuk pemain
    fun startProximityVoice(player: Player) {
        voiceChatAPI.startProximityVoice(player) // Mengaktifkan suara berdasarkan jarak pemain
    }

    // Mute pemain
    fun mutePlayer(player: Player) {
        voiceChatAPI.mutePlayer(player) // Mute pemain
    }

    // Unmute pemain
    fun unmutePlayer(player: Player) {
        voiceChatAPI.unmutePlayer(player) // Unmute pemain
    }

    // Mengecek status mute pemain
    fun isMuted(player: Player): Boolean {
        return voiceChatAPI.isMuted(player) // Mengecek apakah pemain dalam status mute
    }
}
