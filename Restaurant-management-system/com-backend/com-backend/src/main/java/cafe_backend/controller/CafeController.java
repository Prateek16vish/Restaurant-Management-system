package cafe_backend.controller;

import cafe_backend.entity.Cafe;
import cafe_backend.repository.CafeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafes")
@CrossOrigin(origins = "*")
public class CafeController {

    private final CafeRepository cafeRepository;

    public CafeController(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    // CREATE CAFE
    @PostMapping
    public Cafe createCafe(@RequestBody Cafe cafe) {
        return cafeRepository.save(cafe);
    }

    // GET ALL CAFES
    @GetMapping
    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    // GET CAFE BY ID
    @GetMapping("/{id}")
    public Cafe getCafe(@PathVariable Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));
    }

    // UPDATE CAFE
    @PutMapping("/{id}")
    public Cafe updateCafe(@PathVariable Long id, @RequestBody Cafe cafeDetails) {

        Cafe cafe = cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        cafe.setName(cafeDetails.getName());
        cafe.setAddress(cafeDetails.getAddress());
        cafe.setPhone(cafeDetails.getPhone());

        return cafeRepository.save(cafe);
    }

    // DELETE CAFE
    @DeleteMapping("/{id}")
    public String deleteCafe(@PathVariable Long id) {

        Cafe cafe = cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        cafeRepository.delete(cafe);

        return "Cafe deleted successfully";
    }
}