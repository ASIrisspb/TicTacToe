import kotlin.random.Random

fun main() {
    // Введение в игру
    println("Игра Крестики-Нолики. Ты играешь крестиками")
    println("Чтобы сделать ход нужно написать координаты клетки цифрами: сначала цифра строки, " +
            "потом цифра столбца, " +
            "например 12 - первая строка, вторая клетка в ней")
    //переменная - ход пользователя
    var userStep: String
    //игровое поле
    val field = Array(3) { Array(3, {" "}) }

    //игровой цикл
    while (canGo(field) && !checkWin(field, "x") && !checkWin(field, "o")) {
        //Делаем пока: 1. можно ходить, 2. не выиграл игрок или компьютер
        printField(field) //нарисовали поле
        println("Твой ход. Я похожу сразу после тебя ))")
        userStep = readln() //считали ход игрока
        //делаем проверку, что ход имеет допустимые значения
        if (userStep !in "111213212223313233") {
            println("Недопустимый ход. Сделай корректный ход")
            continue //если не допустимые значения, то возвращаем в начало
        }
        //если клетка не занята, то ставим там крестик
        if (field[userStep.toInt() / 10 - 1][userStep.toInt() % 10 - 1] == " "){
            field[userStep.toInt() / 10 - 1][userStep.toInt() % 10 - 1] = "x"
        } else {
            println("Данная клетка занята! Нужно сделать другой ход")
            continue
        }
        //проверяем есть ли возможность хода и ходит ПК
        if (canGo(field)) pcStep(field)
    }
    //завершение игры
    printField(field)
    //определяем победителя или ничью
    if (checkWin(field, "x")) println("Поздравляю! Ты победил!")
    else if (checkWin(field, "o")) println("Что ж, в этот раз я оказался умнее")
    else if (!canGo(field)) println("Ходы закончились - ничья")
    else println("Что-то пошло не так!!!")
}

fun pcStep(field: Array<Array<String>>) {
    //функция хода ПК
    //создаем массив возможных победных линий
    val preWin= arrayOf(
        arrayOf("11","12","13"),
        arrayOf("21","22","23"),
        arrayOf("31","32","33"),
        arrayOf("11","21","31"),
        arrayOf("12","22","32"),
        arrayOf("13","23","33"),
        arrayOf("11","22","33"),
        arrayOf("13","22","31")
    )
    //массив - копия для хранения индексов возможного хода
    val preWinCopy = Array(8, { Array(3, {""})})
    //заполняем этот массив тем, что есть на поле
    for (i in preWin.indices) {
        for (j in preWin[i].indices) {
            preWinCopy[i][j] = preWin[i][j] //заполняем массив-копию индексами
            //и копируем текущее состояние клетки поля в наш массив победных линий
            preWin[i][j] = field[preWin[i][j].toInt() / 10 - 1][preWin[i][j].toInt() % 10 - 1]
        }
    }
//    for (f in preWin) println(f.asList()) //тест для демонстрации
    //теперь ищем такой подмассив, в котором есть два o и один пробел - для победного хода
    for (i in preWin.indices) {
        var countO = 0
        var countSpace = 0
        var indexSpace = "00"
        for (j in preWin[i].indices) {
            //считаем o
            if (preWin[i][j] == "o") countO++
            //считаем пробелы
            if (preWin[i][j] == " ") {
                countSpace++
                //найденный пробел фиксируем для координат хода
                indexSpace = preWinCopy[i][j]
                println(indexSpace)
            }
        }
        //если нашелся такой подмассив, где есть 2 х и 1 пробел, то делаем ход в этот пробел
        //на первом месте победный ход
        if (countO == 2 && countSpace == 1) {
            field[indexSpace.toInt() / 10 - 1][indexSpace.toInt() % 10 - 1] = "o"
            //если сделали ход, то нужно прекратить метод
            return
        }
    }
    //теперь ищем такой подмассив, в котором есть два х и один пробел - для предотвращения поражения
    for (i in preWin.indices) {
        var countX = 0
        var countSpace = 0
        var indexSpace = "00"
        for (j in preWin[i].indices) {
            //считаем х
            if (preWin[i][j] == "x") countX++
            //считаем пробелы
            if (preWin[i][j] == " ") {
                countSpace++
                //найденный пробел фиксируем для координат хода
                indexSpace = preWinCopy[i][j]
                println(indexSpace)
            }
        }
        //если нашелся такой подмассив, где есть 2 х и 1 пробел, то делаем ход в этот пробел
        //на втором месте ход предотвращения поражения
        if (countX == 2 && countSpace == 1) {
            field[indexSpace.toInt() / 10 - 1][indexSpace.toInt() % 10 - 1] = "o"
            //если сделали ход, то нужно прекратить метод
            return
        }
    }


    //если ход не было сделан для предотвращения победы пользователя, то ставим рандомно
    //создаем массив всех клеток
    val possibleSteps = Array(9, {" "})
    var count = 0
    //проходим по полю и заносим НЕ занятые клетки в массив возможных ходов
    for (i in field.indices) {
        for (j in field[i].indices) {
            if (field[i][j] == " ") possibleSteps[count] = "${i+1}${j+1}"
            count++
        }
    }
//    println(possibleSteps.asList()) //test
    //считаем количество свободных для хода клеток
    var countPossibleSteps = 0 // = possibleSteps.size - possibleSteps.count{ i -> i == " "}
    for (n in possibleSteps) if (n != " ") countPossibleSteps++
//    println("countPossibleSteps = $countPossibleSteps") //test
    //создаем новый массив по количеству свободных ходов
    val freeSteps = Array(countPossibleSteps, {" "})
    var index = 0
    //заполняем его адресами клеток
    for (i in possibleSteps.indices) {
        if (possibleSteps[i] != " ") {
            freeSteps[index] = possibleSteps[i]
            index++
        }
    }
//    println(freeSteps.asList()) //test
    //выбираем случайную клетку из свободных для хода ПК
    val pcStep = freeSteps[Random.nextInt(0, freeSteps.size)]
    field[pcStep.toInt() / 10 - 1][pcStep.toInt() % 10 - 1] = "o"
}
fun canGo(field: Array<Array<String>>): Boolean {
    //функция проверки возможности хода
    //создаем одномерный массив всех клеток (от 1 до 9)
    val cells = Array(9, {0})
    var count = 0
    //проходим по полю и занятые клетки помечаем числом
    for (i in field.indices) {
        for (j in field[i].indices) {
            if (field[i][j] == "x" || field[i][j] == "o") cells[count] = count+1
            count++
        }
    }
    //если есть хотя бы одна не занятая клетка, то можно ходить
    return 0 in cells
}
fun checkWin(field: Array<Array<String>>, who: String): Boolean {
    //функция проверки победителя
    //создали массив всех клеток
    val userSteps = Array(9, {0})
    var count = 0
    //проходим по полю и помечаем занятые клетки числом - номером клетки
    for (i in field.indices) {
        for (j in field[i].indices) {
            if (field[i][j] == who) userSteps[count] = count+1
            count++
        }
    }
    //проверяем комбинации победных клеток для конкретного игрока или ПК
    if (1 in userSteps && 2 in userSteps && 3 in userSteps) return true
    if (4 in userSteps && 5 in userSteps && 6 in userSteps) return true
    if (7 in userSteps && 8 in userSteps && 9 in userSteps) return true
    if (1 in userSteps && 4 in userSteps && 7 in userSteps) return true
    if (2 in userSteps && 5 in userSteps && 8 in userSteps) return true
    if (3 in userSteps && 6 in userSteps && 9 in userSteps) return true
    if (1 in userSteps && 5 in userSteps && 9 in userSteps) return true
    if (3 in userSteps && 5 in userSteps && 7 in userSteps) return true
    return false
}
fun printField(field: Array<Array<String>>) {
    //функция отображения игрового поля
    println("----1---2---3-- ")
    for (i in field.indices) {
        print("${i+1} |")
        for (j in field[i].indices) {
            print(" ${field[i][j]} |")
        }
        println()
        println("---------------")
    }
}