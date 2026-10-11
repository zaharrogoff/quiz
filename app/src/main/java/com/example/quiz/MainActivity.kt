package com.example.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Модель вопроса ----------
data class Question(
    val text: String,
    val answerTrue: Boolean
)

// ---------- Тема ----------
@Composable
fun GeoQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColorScheme() else lightColorScheme()
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GeoQuizScreen()
                }
            }
        }
    }
}

@Composable
fun GeoQuizScreen() {

    // ---------- Список вопросов ----------
    val questions = remember {
        listOf(
            Question("Canberra is the capital of Australia.", true),
            Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
            Question("The Suez Canal connects the Red Sea and the Indian Ocean.", false),
            Question("The source of the Nile River is in Egypt.", false),
            Question("The Amazon River is the longest river in the Americas.", true),
            Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
        )
    }

    // ---------- Состояние ----------
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentQuestion = questions[currentIndex]

    // ---------- UI ----------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Номер вопроса
        Text(
            text = "Вопрос ${currentIndex + 1} из ${questions.size}",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(Modifier.height(16.dp))

        // Текст вопроса
        Text(
            text = currentQuestion.text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(48.dp))

        // Кнопка перехода к следующему вопросу
        Button(
            onClick = {
                if (currentIndex < questions.lastIndex) currentIndex++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("NEXT")
        }
    }
}