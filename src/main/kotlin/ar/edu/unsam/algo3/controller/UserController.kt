@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.FriendDTO
import ar.edu.unsam.algo3.dto.UserDTO
import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {

    @ModelAttribute("currentUser")
    fun getCurrentUser(@RequestHeader("Authorization") authorizationReq: String): Usuario {
        return userService.getUserFromBearerToken(authorizationReq)
    }

    @GetMapping("api/user")
    fun getUserProfile(@ModelAttribute("currentUser") currentUser: Usuario): UserDTO {
        return UserDTO.fromUserToDTO(currentUser)
    }

    @PutMapping("api/user")
    fun putUserProfile(@ModelAttribute("currentUser") currentUser: Usuario, @RequestBody userDTO: UserDTO) {
        userService.updateProfile(currentUser, userDTO)
    }

    @GetMapping("api/friends")
    fun getAmigos(@ModelAttribute("currentUser") currentUser: Usuario): List<FriendDTO> {
        val userFriends = userService.getAllFriends(currentUser)
        return userFriends.map { FriendDTO.fromFriendsToDTO(it) }
    }

    @DeleteMapping("api/friends/{idFriend}")
    fun deleteFriend(@ModelAttribute("currentUser") currentUser: Usuario, @PathVariable idFriend: Int) {
        userService.deleteFriend(idFriend, currentUser)
    }

    @PostMapping("api/friends")
    fun addFriend(@ModelAttribute("currentUser") currentUser: Usuario, @RequestBody idFriend: Int) {
        userService.addFriend(idFriend, currentUser)
    }

    @GetMapping("api/users")
    fun getAllUsers(
        @ModelAttribute("currentUser") currentUser: Usuario
    ): List<FriendDTO> {
        val listUsers = userService.getAllUsers(currentUser)
        return listUsers.map { FriendDTO.fromFriendsToDTO(it) }
    }

    @DeleteMapping("api/inactiveUsers")
    fun deleteInactiveUsers(@ModelAttribute("currentUser") currentUser: Usuario) {
        userService.deleteInactiveUsers()
    }
}
