package com.tpp.rgrtpp.controllers;

import com.tpp.rgrtpp.models.City;
import com.tpp.rgrtpp.service.CityService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listCities(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "cities";
    }

    @GetMapping("/add")
    public String addCityForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("city", new City());
        return "add-city";
    }

    @PostMapping("/add")
    public String addCity(@AuthenticationPrincipal UserDetails userDetails,
                          @Valid @ModelAttribute("city") City city,
                          BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "add-city";
        }
        cityService.saveCity(city);
        return "redirect:/cities";
    }

    @GetMapping("/edit/{id}")
    public String editCityForm(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id,
                               Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        City city = cityService.findCityById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
        model.addAttribute("city", city);
        return "edit-city";
    }

    @PostMapping("/update/{id}")
    public String updateCity(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id,
                             @Valid @ModelAttribute("city") City city,
                             BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "edit-city";
        }
        city.setCityId(id);
        cityService.updateCity(city);
        return "redirect:/cities";
    }

    @GetMapping("/delete/{id}")
    public String deleteCity(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        cityService.deleteCity(id);
        return "redirect:/cities";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
