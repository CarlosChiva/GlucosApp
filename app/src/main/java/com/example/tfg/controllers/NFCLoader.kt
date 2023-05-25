package com.example.tfg.controllers
import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.nfc.NfcAdapter.CreateNdefMessageCallback
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback
import android.content.Context
import android.content.Intent
import android.os.Parcelable

class NFCLoader(private val context: Context) : CreateNdefMessageCallback, OnNdefPushCompleteCallback {
    private var nfcAdapter: NfcAdapter? = null

    init {
        // Obtener una instancia del adaptador NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    }

    fun enableNFC() {
        nfcAdapter?.let {
            // Verificar si NFC está habilitado en el dispositivo
            if (!it.isEnabled) {
                // Si no está habilitado, se puede mostrar un diálogo para solicitar al usuario que lo habilite
                // o redirigir al usuario a la configuración de NFC para habilitarlo.
            }

            // Registrar el callback para crear un mensaje NDEF cuando se toque otro dispositivo
            it.setNdefPushMessageCallback(this, context as Activity?)

            // Registrar el callback para recibir una notificación cuando se complete la transferencia
            it.setOnNdefPushCompleteCallback(this, context)
        }
    }

    fun disableNFC() {
        nfcAdapter?.let {
            // Eliminar los callbacks registrados
            it.setNdefPushMessageCallback(null, context as Activity?)
            it.setOnNdefPushCompleteCallback(null, context)
        }
    }

    override fun createNdefMessage(event: NfcEvent?): NdefMessage? {
        // Lógica para crear un mensaje NDEF
        return null
    }

    override fun onNdefPushComplete(event: NfcEvent?) {
        // Lógica para manejar la notificación de transferencia completa
    }

    fun processIntent(intent: Intent): List<Int> {
        val action = intent.action

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == action) {

            // Obtener los datos recibidos del intent
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMessages != null) {
                val ndefMessages = arrayOfNulls<NdefMessage>(rawMessages.size)
                for (i in rawMessages.indices) {
                    ndefMessages[i] = rawMessages[i] as NdefMessage
                }

                // Procesar los mensajes NDEF y extraer los datos en formato hexadecimal
                return processNdefMessages(ndefMessages)
            }
        }

        return emptyList()
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>): List<Int> {
        val hexData = mutableListOf<Int>()

        for (message in ndefMessages) {
            message?.let {
                val records = it.records

                for (record in records) {
                    val payload = record.payload

                    // Convertir el payload en formato hexadecimal a un número entero
                    val hexString = payload.joinToString("") { byte -> String.format("%02X", byte) }
                    val intValue = hexString.toInt(16)

                    // Agregar el número entero a la lista
                    hexData.add(intValue)
                }
            }
        }

        return hexData
    }
}