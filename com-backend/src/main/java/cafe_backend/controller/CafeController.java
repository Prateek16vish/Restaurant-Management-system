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

    @PostMapping
    public Cafe createCafe(@RequestBody Cafe cafe) {
        return cafeRepository.save(cafe);
    }

    @GetMapping
    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cafe getCafe(@PathVariable Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));
    }
}