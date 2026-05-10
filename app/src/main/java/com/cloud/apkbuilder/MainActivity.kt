package com.cloud.apkbuilder

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // SoundPool ड्रम पैड के लिए सबसे ज़रूरी है (ज़ीरो लैग साउंड के लिए)
    private lateinit var soundPool: SoundPool
    
    // हर साउंड की अपनी एक ID होगी
    private var crashId: Int = 0
    private var kickId: Int = 0
    private var snareId: Int = 0

    // पैड की सेटिंग्स (JS वाले ऑब्जेक्ट की तरह)
    private var crashVolume = 1.0f
    private var kickVolume = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // यह लाइन XML से आपका डिज़ाइन (UI) जोड़ेगी
        setContentView(R.layout.activity_main) 

        setupSoundPool()
        loadSounds()
        setupButtons()
    }

    private fun setupSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME) // गेम्स वाला यूसेज ताकि आवाज़ तुरंत आए
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(8) // एक साथ 8 पैड बज सकते हैं
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun loadSounds() {
        // आवाज़ों को res/raw फोल्डर से लोड करना होगा
        // (अभी के लिए कोड बना दिया है, बाद में हम यहाँ असली साउंड डालेंगे)
        /*
        crashId = soundPool.load(this, R.raw.crash_sound, 1)
        kickId = soundPool.load(this, R.raw.kick_sound, 1)
        snareId = soundPool.load(this, R.raw.snare_sound, 1)
        */
    }

    private fun setupButtons() {
        // XML में बनाए गए बटन्स को ID से खोजना (JS के getElementById की तरह)
        
        /* val btnCrash = findViewById<Button>(R.id.btn_crash)
        btnCrash.setOnClickListener {
            playSound(crashId, crashVolume)
        }

        val btnKick = findViewById<Button>(R.id.btn_kick)
        btnKick.setOnClickListener {
            playSound(kickId, kickVolume)
        }

        // पेज एडिट वाला हरा बटन
        val btnEditPage = findViewById<Button>(R.id.btn_edit_page)
        btnEditPage.setOnClickListener {
            showEditMachineDialog()
        }
        */
    }

    // साउंड प्ले करने का फंक्शन (JS के playSound की तरह)
    private fun playSound(soundId: Int, volume: Float) {
        if (soundId != 0) {
            // parameters: soundId, leftVolume, rightVolume, priority, loop, playbackRate
            soundPool.play(soundId, volume, volume, 0, 0, 1.0f)
        } else {
            Toast.makeText(this, "साउंड अभी लोड नहीं हुआ है!", Toast.LENGTH_SHORT).show()
        }
    }

    // एडिट मशीन का ओवरले/पॉपअप खोलने का फंक्शन
    private fun showEditMachineDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("EDIT MACHINE")
        builder.setMessage("यहाँ से आप कौन से पैड को एडिट करना चाहते हैं चुनें:")
        
        // पॉपअप के अंदर के बटन
        builder.setPositiveButton("CRASH EDIT") { dialog, _ ->
            showPadEditDialog("CRASH")
            dialog.dismiss()
        }
        builder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
        }
        
        builder.show()
    }

    // किसी एक पैड (जैसे CRASH) की सेटिंग बदलने का पॉपअप
    private fun showPadEditDialog(padName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("EDIT $padName PAD")
        builder.setMessage("Volume, Panning और Tuning की सेटिंग यहाँ आएगी।")
        
        builder.setPositiveButton("SAVE (Correct)") { dialog, _ ->
            Toast.makeText(this, "$padName की सेटिंग सेव हो गई!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // ऐप बंद होने पर मेमोरी फ्री करना
        soundPool.release() 
    }
}
