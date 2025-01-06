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
    private lateinit var peerConnection: PeerConnection
    private var audioTrack: AudioTrack? = null
    private var isMuted = false
    private var isVoiceChatEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serverUrlInput = findViewById(R.id.serverUrlInput)
        saveUrlButton = findViewById(R.id.saveUrlButton)
        muteButton = findViewById(R.id.muteButton)
        toggleVoiceChatButton = findViewById(R.id.toggleVoiceChatButton)

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

        // Inisialisasi WebRTC
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
        audioTrack?.setEnabled(isMuted)
        isMuted = !isMuted
        muteButton.text = if (isMuted) "Unmute" else "Mute"
    }

    private fun toggleVoiceChat() {
        if (isVoiceChatEnabled) {
            peerConnection.close()
            isVoiceChatEnabled = false
            toggleVoiceChatButton.text = "Enable Voice Chat"
        } else {
            val savedUrl = getSharedPreferences("appPrefs", MODE_PRIVATE).getString("serverUrl", "")
            if (!savedUrl.isNullOrEmpty()) {
                initializeWebRTC(savedUrl)
                isVoiceChatEnabled = true
                toggleVoiceChatButton.text = "Disable Voice Chat"
            }
        }
    }
}
