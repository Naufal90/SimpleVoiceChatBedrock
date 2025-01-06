package org.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.webrtc.*

class MainActivity : AppCompatActivity() {

    private lateinit var serverUrlInput: EditText
    private lateinit var saveUrlButton: Button
    private lateinit var muteButton: Button
    private lateinit var toggleVoiceChatButton: Button
    private var peerConnection: PeerConnection? = null
    private var audioTrack: AudioTrack? = null
    private var isMuted = false
    private var isVoiceChatEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serverUrlInput = findViewById(R.id.et_server_url)
        saveUrlButton = findViewById(R.id.saveUrlButton)
        muteButton = findViewById(R.id.btn_mute)
        toggleVoiceChatButton = findViewById(R.id.btn_enable_voice_chat)

        saveUrlButton.setOnClickListener {
            val serverUrl = serverUrlInput.text.toString()
            if (serverUrl.isNotEmpty()) {
                getSharedPreferences("appPrefs", MODE_PRIVATE).edit().putString("serverUrl", serverUrl).apply()
                Toast.makeText(this, "Server URL saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show()
            }
        }

        muteButton.setOnClickListener {
            toggleMute()
        }

        toggleVoiceChatButton.setOnClickListener {
            toggleVoiceChat()
        }

        val savedUrl = getSharedPreferences("appPrefs", MODE_PRIVATE).getString("serverUrl", "")
        if (!savedUrl.isNullOrEmpty()) {
            initializeWebRTC(savedUrl)
        }
    }

    private fun initializeWebRTC(serverUrl: String) {
        val options = PeerConnectionFactory.InitializationOptions.builder(this)
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        val factory = PeerConnectionFactory.builder().createPeerConnectionFactory()
        val iceServers = listOf(
            PeerConnection.IceServer.builder(serverUrl).createIceServer()
        )

        val config = PeerConnection.RTCConfiguration(iceServers)
        peerConnection = factory.createPeerConnection(config, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {}
            override fun onAddStream(stream: MediaStream) {
                audioTrack = stream.audioTracks.firstOrNull()
            }
            override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {}
            override fun onDataChannel(dataChannel: DataChannel) {}
            override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState) {}
            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) {}
            override fun onRenegotiationNeeded() {}
            override fun onTrack(transceiver: RtpTransceiver) {}
        })
    }

    private fun toggleMute() {
        if (audioTrack != null) {
            isMuted = !isMuted
            audioTrack?.setEnabled(!isMuted)
            muteButton.text = if (isMuted) getString(R.string.unmute) else getString(R.string.mute)
        }
    }

    private fun toggleVoiceChat() {
        if (isVoiceChatEnabled) {
            peerConnection?.close()
            isVoiceChatEnabled = false
            toggleVoiceChatButton.text = getString(R.string.enable_voice_chat)
        } else {
            val savedUrl = getSharedPreferences("appPrefs", MODE_PRIVATE).getString("serverUrl", "")
            if (!savedUrl.isNullOrEmpty()) {
                initializeWebRTC(savedUrl)
                isVoiceChatEnabled = true
                toggleVoiceChatButton.text = getString(R.string.disable_voice_chat)
            } else {
                Toast.makeText(this, "Please set the server URL first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
