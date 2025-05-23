package com.chudofishe.shopito.util

import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import java.time.LocalDateTime
import kotlin.random.Random

typealias ShoppingListEntries = LinkedHashMap<Category, List<ShoppingListItem>>

fun createSampleShoppingList(id: Long = 1): ShoppingList {
    // Создаем случайное количество элементов (от 1 до 5)
    val itemCount = (1..5).random()
    val random = Random(System.currentTimeMillis() + id)

    // Различные названия продуктов для разнообразия
    val productNames = listOf(
        "Яблоки", "Молоко", "Хлеб", "Шампунь", "Сыр",
        "Йогурт", "Бананы", "Мясо", "Рыба", "Морковь",
        "Макароны", "Рис", "Печенье", "Кофе", "Чай"
    )

    // Случайные категории
    val categories = Category.entries.toList()

    // Создаем список элементов
    val items = List(itemCount) { index ->
        val name = productNames[(random.nextInt(productNames.size) + index) % productNames.size]
        val category = categories[random.nextInt(categories.size)]
        val isChecked = random.nextBoolean()

        ShoppingListItem(
            id = index,
            name = name,
            category = category,
            timeStamp = LocalDateTime.now().minusDays(random.nextLong(7)),
            currentCategory = if (isChecked) Category.COMPLETED else category,
            description = if (random.nextBoolean()) "Заметка для $name" else ""
        )
    }

    // Создаем различные даты для списков
    val daysAgo = random.nextInt(10)

    return ShoppingList(
        id = id,
        items = items,
        timestamp = LocalDateTime.now().minusDays(daysAgo.toLong()),
        isCompleted = random.nextInt(10) < 3, // 30% вероятность, что список завершен
    )
}