package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.ReadingCentersService
import ar.edu.unsam.algo3.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class ReadingCentersController(
    val readingCentersService: ReadingCentersService,
    val userService: UserService
) {
    @ModelAttribute("currentUser")
    fun getCurrentUser(@RequestHeader("Authorization") authorizationReq: String): Usuario {
        return userService.getUserFromBearerToken(authorizationReq)
    }

    @DeleteMapping("api/inactiveCenters")
    fun deleteInactiveCenters(@ModelAttribute("currentUser") currentUser: Usuario) {
        readingCentersService.deleteInactiveCenters()
    }
}