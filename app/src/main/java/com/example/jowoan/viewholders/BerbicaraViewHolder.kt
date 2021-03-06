package com.example.jowoan.viewholders

import android.Manifest
import android.R.drawable.ic_media_pause
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jowoan.R.drawable.ic_baseline_mic_128
import com.example.jowoan.adapters.LessonAdapter
import com.example.jowoan.config.LessonConfig
import com.example.jowoan.custom.AppCompatActivity
import com.example.jowoan.models.lesson.Lesson
import kotlinx.android.synthetic.main.item_berbicara.view.*

class BerbicaraViewHolder(
    view: View,
    val activity: AppCompatActivity,
    action: LessonAdapter.Action
) :
    LessonAdapter.LessonViewHolder(view, action) {
    private val title = view.title
    private val jowoLang = view.jowoLang
    private val listen = view.listen
    private val indoLang = view.indoLang
    private val speak = view.speak
    private val skip = view.skip
    private val note = view.note
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var lesson: Lesson

    private val RecordAudioRequestCode = 1

    override fun bind(lesson: Lesson) {
        this.lesson = lesson
        val berbicara = lesson.berbicara
        if (berbicara != null) {
            title.text = berbicara.title
            jowoLang.text = berbicara.jowoLang
            indoLang.text = berbicara.indoLang

            disableAnswerOption()

            skip.setOnClickListener {
                checkAnswer(berbicara.jowoLang, berbicara.jowoLang)
            }

            loadVoice("http://${berbicara.voice}")

            listen.setOnClickListener {
                mediaPlayer?.start()
            }

            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermission()
            }

            val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
            val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "jv-ID")

            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(p0: Bundle?) {
                    //TODO("Not yet implemented")
                }

                override fun onRmsChanged(p0: Float) {
                    //TODO("Not yet implemented")
                }

                override fun onBufferReceived(p0: ByteArray?) {
                    //TODO("Not yet implemented")
                }

                override fun onPartialResults(p0: Bundle?) {
                    //TODO("Not yet implemented")
                }

                override fun onEvent(p0: Int, p1: Bundle?) {
                    //TODO("Not yet implemented")
                }

                override fun onBeginningOfSpeech() {
                    note.text = "Mendengarkan..."
                }

                override fun onEndOfSpeech() {
                    //TODO("Not yet implemented")
                }

                override fun onError(p0: Int) {
                    //TODO("Not yet implemented")
                }

                override fun onResults(bundle: Bundle?) {
                    speak.setImageDrawable(
                        ContextCompat.getDrawable(
                            activity,
                            ic_baseline_mic_128
                        )
                    )
                    val data = bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    note.text = ""
                    checkAnswer(berbicara.jowoLang, data!![0])
                }

            })

            speak.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                    if (motionEvent?.action == MotionEvent.ACTION_UP) {
                        speechRecognizer.stopListening()
                    }
                    if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                        speak.setImageResource(ic_media_pause)
                        speechRecognizer.startListening(speechRecognizerIntent)
                    }
                    return false
                }
            })
        }
    }

    override fun onViewShowed() {
        enableAnswerOption()
    }

    private fun loadVoice(url: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }

    private fun checkAnswer(jowoLang: String, plainUserAnswer: String) {
        val correctAnswer = removeNonAlphaNumeric(jowoLang)
        val userAnswer = removeNonAlphaNumeric(plainUserAnswer)
        var result = ""
        if (correctAnswer.equals(userAnswer, ignoreCase = true)) {
            action.questionAnswered(LessonConfig.ANSWER_CORRECT)
            result = "Correct"
            action.showCorrectDisplay("Jawaban Kamu:", userAnswer, null)
        } else {
            action.questionAnswered(LessonConfig.ANSWER_WRONG)
            result = "False"
            action.showWrongDisplay(
                "Jawaban Kamu:",
                userAnswer,
                "Pastikan anda mengucapkan kalimat di atas dengan keras dan lantang"
            )
            action.retryNextTime(lesson)
        }
        disableAnswerOption()
        Log.d("Coba", "$result; $correctAnswer = $userAnswer")
    }

    private fun removeNonAlphaNumeric(text: String): String {
        val re = Regex("[^A-Za-z0-9 ]")
        return re.replace(text, "")
    }

    private fun enableAnswerOption() {
        speak.isEnabled = true
        listen.isEnabled = true
        skip.isEnabled = true
    }

    private fun disableAnswerOption() {
        speak.isEnabled = false
        listen.isEnabled = false
        skip.isEnabled = false
        mediaPlayer?.release()
        mediaPlayer = null
    }

}