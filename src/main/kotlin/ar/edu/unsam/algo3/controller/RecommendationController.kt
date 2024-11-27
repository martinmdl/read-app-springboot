@file:Suppress("SpellCheckingInspection")

package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.NewValorationDTO
import ar.edu.unsam.algo3.dto.RecommendationDTO
import ar.edu.unsam.algo3.dto.RecommendationDetailsDTO
import ar.edu.unsam.algo3.dto.SearchDTO
import ar.edu.unsam.algo3.model.Recomendacion
import ar.edu.unsam.algo3.model.Usuario
import ar.edu.unsam.algo3.service.RecommendationService
import ar.edu.unsam.algo3.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class RecommendationController(
    private val recommendationService: RecommendationService,
    private val userService: UserService
) {

    @ModelAttribute("currentUser")
    fun getCurrentUser(@RequestHeader("Authorization") authorizationReq: String): Usuario {
        return userService.getUserFromBearerToken(authorizationReq)
    }

    @GetMapping("api/allRecommendations")
    fun getRecommendations(@ModelAttribute("currentUser") currentUser: Usuario): List<RecommendationDTO> {
        val recommendations = recommendationService.getAllRecommendationsAllowedById(currentUser)
        return recommendationToDto(currentUser, recommendations)
    }

    @GetMapping("api/ownRecommendations")
    fun getOwnRecommendations(@ModelAttribute("currentUser") currentUser: Usuario): List<RecommendationDTO> {
        val recommendations = recommendationService.getOwnRecommendationsAllowedById(currentUser)
        return recommendationToDto(currentUser, recommendations)
    }

    @DeleteMapping("api/deleteRecommendations/{id}")
    fun deleteRecommendation(@ModelAttribute("currentUser") currentUser: Usuario, @PathVariable id: Int) {
        recommendationService.deleteRecommendation(currentUser, id)
    }

    @GetMapping("api/getRecommendationsToValorate")
    fun getRecommendationsToEvaluate(@ModelAttribute("currentUser") currentUser: Usuario): List<RecommendationDTO> {
        val recommendations = recommendationService.recommendationToEvaluate(currentUser)
        return recommendationToDto(currentUser, recommendations)
    }

    @PostMapping("api/addRecommendationToValorate/{idRecommendation}")
    fun addRecommendationsToEvaluate(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idRecommendation: Int
    ) {
        recommendationService.addRecommendationToEvaluate(currentUser, idRecommendation)
    }

    @PutMapping("api/deleteRecommendationToValorate")
    fun removeRecommendationToEvaluete(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @RequestBody idRecommendation: Int
    ) {
        recommendationService.removeRecommendationToEvaluate(currentUser, idRecommendation)
    }

    @GetMapping("api/favoriteRecommendationStatus/{id}")
    fun favoriteRecommendationStatus(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable id: Int
    ): Boolean {
        return recommendationService.favoriteRecommendationStatus(currentUser, id)
    }

    @GetMapping("api/recommendationDetails/{idRecommendation}")
    fun getRecommendationDetails(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idRecommendation: Int
    ): RecommendationDetailsDTO {
        val recommendation = recommendationService.getRecommendationById(idRecommendation)
        return RecommendationDetailsDTO.fromRecommendation(currentUser, recommendation)
    }

    @PutMapping("api/recommendationEdition/{idRecommendation}")
    fun putRecommendationEdition(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idRecommendation: Int,
        @RequestBody recommendationDTO: RecommendationDetailsDTO
    ) {
        val recommendation = recommendationService.getRecommendationById(idRecommendation)
        recommendationService.updateRecommendation(currentUser, recommendation, recommendationDTO)
    }

    @PostMapping("api/valoration/{idRecomendacion}")
    fun createValoration(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @PathVariable idRecomendacion: Int,
        @RequestBody valorationDTO: NewValorationDTO
    ) {
        recommendationService.createValoration(currentUser, idRecomendacion, valorationDTO)
    }

    @PostMapping("api/filterRecommendation")
    fun filterRecommendation(
        @ModelAttribute("currentUser") currentUser: Usuario,
        @RequestBody filter: SearchDTO
    ): List<RecommendationDTO> {
        val recommendations = recommendationService.filterRecommendations(filter, currentUser)
        return recommendationToDto(currentUser, recommendations)
    }

    private fun recommendationToDto(user: Usuario, recommendations: List<Recomendacion>): List<RecommendationDTO> {
        return recommendations.map { RecommendationDTO.fromRecommendation(user, it) }
    }
}