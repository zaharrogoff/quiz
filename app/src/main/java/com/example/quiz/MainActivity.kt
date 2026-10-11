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
    var isAnswered by rememberSaveable { mutableStateOf(false) }
    var isCurrentAnswerCorrect by rememberSaveable { mutableStateOf(false) }
    var correctAnswersCount by rememberSaveable { mutableIntStateOf(0) }
    var showResultDialog by rememberSaveable { mutableStateOf(false) }

    val currentQuestion = questions[currentIndex]
    val isLastQuestion = currentIndex == questions.lastIndex

    // ---------- Обработчики ----------
    fun checkAnswer(userAnswer: Boolean) {
        if (isAnswered) return
        isAnswered = true
        isCurrentAnswerCorrect = (userAnswer == currentQuestion.answerTrue)
        if (isCurrentAnswerCorrect) correctAnswersCount++
    }

    fun moveToNext() {
        if (isLastQuestion) {
            showResultDialog = true
            return
        }
        currentIndex++
        isAnswered = false
        isCurrentAnswerCorrect = false
    }

    fun resetQuiz() {
        currentIndex = 0
        isAnswered = false
        isCurrentAnswerCorrect = false
        correctAnswersCount = 0
        showResultDialog = false
    }

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

        // Кнопки True / False — скрываются после ответа
        if (!isAnswered) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { checkAnswer(true) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("TRUE")
                }
                Button(
                    onClick = { checkAnswer(false) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("FALSE")
                }
            }
        } else {
            // Показываем результат ответа
            val resultText = if (isCurrentAnswerCorrect) "✅ Правильно!" else "❌ Неправильно!"
            val resultColor = if (isCurrentAnswerCorrect)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error

            Text(
                text = resultText,
                fontSize = 20.sp,
                color = resultColor,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(32.dp))

        // Кнопка Next — только после ответа и НЕ на последнем вопросе
        if (isAnswered && !isLastQuestion) {
            Button(
                onClick = { moveToNext() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("NEXT")
            }
        }

        // На последнем вопросе после ответа — кнопка FINISH
        if (isAnswered && isLastQuestion) {
            Button(
                onClick = { moveToNext() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("FINISH")
            }
        }
    }

    // ---------- Диалог с результатом ----------
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { /* блокируем закрытие тапом вне */ },
            title = { Text("Тест завершён!") },
            text = {
                Column {
                    Text(
                        "Правильных ответов: $correctAnswersCount из ${questions.size}",
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    val percent = (correctAnswersCount * 100) / questions.size
                    Text("Результат: $percent%")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { resetQuiz() }
                ) {
                    Text("Пройти заново")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResultDialog = false }
                ) {
                    Text("Закрыть")
                }
            }
        )
    }
}