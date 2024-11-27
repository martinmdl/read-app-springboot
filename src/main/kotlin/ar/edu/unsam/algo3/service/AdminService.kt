package ar.edu.unsam.algo3.service

import org.springframework.stereotype.Service

@Service
class AdminService(
    val bookService: BookService,
    val recommendationService: RecommendationService,
    val userService: UserService,
    val readingCentersService: ReadingCentersService
) {

    fun getCardsAmounts(): Map<String, Int> {
        val cardsData: MutableMap<String, Int> = mutableMapOf()
        cardsData["users"] = userService.getUserAmount()
        cardsData["readingCenters"] = readingCentersService.getCentersAmount()
        cardsData["books"] = bookService.getBooksAmount()
        cardsData["recommendations"] = recommendationService.getRecommendationAmount()
        cardsData["inactiveUsers"] = userService.getAllInactiveUsers()
        cardsData["inactiveCenters"] = readingCentersService.getAllInactiveCenters()

        return cardsData
    }
}