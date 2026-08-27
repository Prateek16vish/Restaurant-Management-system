package cafe_backend.controller;

import cafe_backend.entity.Cafe;
import cafe_backend.entity.MenuItem;
import cafe_backend.repository.CafeRepository;
import cafe_backend.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@CrossOrigin(origins = "*")
public class MenuItemController {

    private final MenuItemRepository menuItemRepository;
    private final CafeRepository cafeRepository;

    public MenuItemController(
            MenuItemRepository menuItemRepository,
            CafeRepository cafeRepository) {

        this.menuItemRepository = menuItemRepository;
        this.cafeRepository = cafeRepository;
    }

    // CREATE MENU ITEM
    @PostMapping
    public MenuItem createMenuItem(@RequestBody MenuItem menuItem) {

        if (menuItem.getCafe() == null || menuItem.getCafe().getId() == null) {
            throw new RuntimeException("Cafe ID is required");
        }

        Long cafeId = menuItem.getCafe().getId();

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        menuItem.setCafe(cafe);

        return menuItemRepository.save(menuItem);
    }

    // GET ALL MENU ITEMS
    @GetMapping
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // GET MENU ITEM BY ID
    @GetMapping("/{id}")
    public MenuItem getMenuItem(@PathVariable Long id) {

        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    // UPDATE MENU ITEM
    @PutMapping("/{id}")
    public MenuItem updateMenuItem(
            @PathVariable Long id,
            @RequestBody MenuItem menuItemDetails) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        menuItem.setName(menuItemDetails.getName());
        menuItem.setDescription(menuItemDetails.getDescription());
        menuItem.setPrice(menuItemDetails.getPrice());
        menuItem.setCategory(menuItemDetails.getCategory());
        menuItem.setAvailable(menuItemDetails.getAvailable());

        if (menuItemDetails.getCafe() != null &&
                menuItemDetails.getCafe().getId() != null) {

            Long cafeId = menuItemDetails.getCafe().getId();

            Cafe cafe = cafeRepository.findById(cafeId)
                    .orElseThrow(() -> new RuntimeException("Cafe not found"));

            menuItem.setCafe(cafe);
        }

        return menuItemRepository.save(menuItem);
    }

    // DELETE MENU ITEM
    @DeleteMapping("/{id}")
    public String deleteMenuItem(@PathVariable Long id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        menuItemRepository.delete(menuItem);

        return "Menu item deleted successfully";
    }
}