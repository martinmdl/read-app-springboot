@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.model.Lenguaje
import ar.edu.unsam.algo3.model.Libro
import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.*
import org.springframework.web.bind.annotation.*

@RestController
class AdminController(
    private val adminService: AdminService,
    private val userService: UserService
) {

    @ModelAttribute("currentUser")
    fun getCurrentUser(@RequestHeader("Authorization") authorizationReq: String): Usuario {
        return userService.getUserFromBearerToken(authorizationReq)
    }

    @GetMapping("api/cardsData")
    fun getHomeCardsData(@ModelAttribute("currentUser") currentUser: Usuario): CardsDTO {
        return CardsDTO.mapToDTO(adminService.getCardsAmounts())
    }

}

data class CardsDTO(
    val usersAmount: Int,
    val readingCentersAmount: Int,
    val booksAmount: Int,
    val recommendationsAmount: Int,
    val inactiveUsersAmount: Int,
    val inactiveCentersAmount: Int
) {
    companion object {
        fun mapToDTO(cardsAmount: Map<String, Int>): CardsDTO {
            return CardsDTO(
                usersAmount = cardsAmount["users"]!!,
                readingCentersAmount = cardsAmount["readingCenters"]!!,
                booksAmount = cardsAmount["books"]!!,
                recommendationsAmount = cardsAmount["recommendations"]!!,
                inactiveUsersAmount = cardsAmount["inactiveUsers"]!!,
                inactiveCentersAmount = cardsAmount["inactiveCenters"]!!,
            )
        }
    }
}