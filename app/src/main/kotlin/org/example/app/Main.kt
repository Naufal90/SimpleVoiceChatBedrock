package org.example.app

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.webrtc.*

class Main : JavaPlugin() {

    private val playersPositions = mutableMapOf<Player, Location>()
    private val voiceRange = 20.0
    private lateinit var peerConnection: PeerConnection
    private lateinit var factory: PeerConnectionFactory

    override fun onEnable() {
        logger.info("SimpleVoiceChat plugin enabled")
        initializeWebRTC()

        object : BukkitRunnable() {
            override fun run() {
                updatePlayerPositions()
                handleProximityVoiceChat()
            }
        }.runTaskTimer(this, 0, 20)
    }

    override fun onDisable() {
        logger.info("SimpleVoiceChat plugin disabled")
    }

    private fun initializeWebRTC() {
        val options = PeerConnectionFactory.InitializationOptions.builder(this)
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        factory = PeerConnectionFactory.builder().createPeerConnectionFactory()

        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )

        val config = PeerConnection.RTCConfiguration(iceServers)
        peerConnection = factory.createPeerConnection(config, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {
                sendSignalingMessageToAll("candidate:${candidate.toString()}")
            }

            override fun onAddStream(stream: MediaStream) {
                logger.info("Media stream added: ${stream.id}")
            }

            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
                logger.info("ICE connection state changed to: $state")
            }

            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {
                logger.info("ICE gathering state changed to: $state")
            }

            override fun onSignalingChange(state: PeerConnection.SignalingState) {
                logger.info("Signaling state changed to: $state")
            }

            override fun onDataChannel(dataChannel: DataChannel) {}
            override fun onRemoveStream(stream: MediaStream) {}
            override fun onRenegotiationNeeded() {}
            override fun onTrack(receiver: RtpReceiver, streams: Array<out MediaStream>) {}
        })
    }

    private fun updatePlayerPositions() {
        for (player in Bukkit.getOnlinePlayers()) {
            playersPositions[player] = player.location
        }
    }

    private fun handleProximityVoiceChat() {
        for ((player, location) in playersPositions) {
            val nearbyPlayers = playersPositions.filter {
                it.key != player && it.value.distance(location) <= voiceRange
            }.keys
            startVoiceChat(player, nearbyPlayers)
        }
    }

    private fun startVoiceChat(player: Player, nearbyPlayers: Set<Player>) {
        player.sendMessage("Voice chat aktif dengan ${nearbyPlayers.size} pemain di dekatmu.")
        peerConnection.createOffer(object : SdpObserver {
            override fun onCreateSuccess(sdp: SessionDescription) {
                peerConnection.setLocalDescription(this, sdp)
                sendSignalingMessage(player, "offer:${sdp.description}")
            }

            override fun onSetSuccess() {}
            override fun onCreateFailure(error: String) {
                logger.warning("Failed to create offer: $error")
            }

            override fun onSetFailure(error: String) {
                logger.warning("Failed to set local description: $error")
            }
        }, MediaConstraints())
    }

    private fun sendSignalingMessage(player: Player, message: String) {
        player.sendMessage("Signaling message: $message")
    }

    private fun sendSignalingMessageToAll(message: String) {
        for (player in Bukkit.getOnlinePlayers()) {
            sendSignalingMessage(player, message)
        }
    }

    private fun onSignalingMessageReceived(player: Player, message: String) {
        when {
            message.startsWith("offer:") -> handleOffer(player, message.removePrefix("offer:"))
            message.startsWith("answer:") -> handleAnswer(player, message.removePrefix("answer:"))
            message.startsWith("candidate:") -> handleIceCandidate(player, message.removePrefix("candidate:"))
        }
    }

    private fun handleOffer(player: Player, sdp: String) {
        val sessionDescription = SessionDescription(SessionDescription.Type.OFFER, sdp)
        peerConnection.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {
                peerConnection.createAnswer(object : SdpObserver {
                    override fun onCreateSuccess(sdp: SessionDescription) {
                        peerConnection.setLocalDescription(this, sdp)
                        sendSignalingMessage(player, "answer:${sdp.description}")
                    }

                    override fun onSetSuccess() {}
                    override fun onCreateFailure(error: String) {
                        logger.warning("Failed to create answer: $error")
                    }

                    override fun onSetFailure(error: String) {
                        logger.warning("Failed to set local description: $error")
                    }
                }, MediaConstraints())
            }

            override fun onSetFailure(error: String) {
                logger.warning("Failed to set remote description: $error")
            }
        }, sessionDescription)
    }

    private fun handleAnswer(player: Player, sdp: String) {
        val sessionDescription = SessionDescription(SessionDescription.Type.ANSWER, sdp)
        peerConnection.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {}
            override fun onSetFailure(error: String) {
                logger.warning("Failed to set remote description: $error")
            }
        }, sessionDescription)
    }

    private fun handleIceCandidate(player: Player, candidate: String) {
        val iceCandidate = IceCandidate.fromString(candidate)
        peerConnection.addIceCandidate(iceCandidate)
    }
}
