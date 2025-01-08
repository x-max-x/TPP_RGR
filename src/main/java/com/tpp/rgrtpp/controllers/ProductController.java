package com.tpp.rgrtpp.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.tpp.rgrtpp.models.Product;
import com.tpp.rgrtpp.service.ProductService;
import com.tpp.rgrtpp.service.ShopService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

		private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

		@GetMapping
    public String listProducts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(@AuthenticationPrincipal UserDetails userDetails,Model model) {
			if (!isAdmin(userDetails)) {
				return "access-denied";
		}
			  model.addAttribute("product", new Product());
        model.addAttribute("shops", shopService.getAllShops());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@AuthenticationPrincipal UserDetails userDetails,@Valid @ModelAttribute("product") Product product, BindingResult result, Model model) {
      if (!isAdmin(userDetails)) {
				return "access-denied";
		}  
			if (result.hasErrors()) {
            model.addAttribute("shops", shopService.getAllShops());
            return "add-product";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") Integer id, Model model) {
			if (!isAdmin(userDetails)) {
				return "access-denied";
		}
			Product product = productService.findProductById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resident not found"));
				
				
            model.addAttribute("product", product);
            model.addAttribute("shops", shopService.getAllShops());
            return "edit-product";       
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") Integer id, @Valid @ModelAttribute("product") Product product,
                                BindingResult result, Model model) {
						if (!isAdmin(userDetails)) {
						return "access-denied";
						}
						if (result.hasErrors()) {
            model.addAttribute("shops", shopService.getAllShops());
            return "edit-product";
        }
        product.setId(id);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("id") Integer id) {
      if (!isAdmin(userDetails)) {
				return "access-denied";
		}  
			productService.deleteProduct(id);
        return "redirect:/products";
    }
		@ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}
