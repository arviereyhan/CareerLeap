package com.example.carrerleap.data.dummy

object QuestionDataSource {
    private val dummyData = mapOf(
        "Mobile Development" to listOf(
            "Dasar Kotlin atau Java",
            "Melakukan testing",
            "Membuat View"
        ),
        "Machine Learning" to listOf(
            "memahami pra procces data",
            "memahami proses normalisasi data"
        ),
        "Cloud Computing" to listOf(
            "memahami dasar javascript",
            "memahami dasar backend",
            "memahami terkait server",
            "memahami cara membuat API"
        )
    )

    fun getQuestions(selectedOption: String): List<String>? {
        return dummyData[selectedOption]
    }
}