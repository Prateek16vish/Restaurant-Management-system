package cafe_backend.controller;

import cafe_backend.entity.Cafe;
import cafe_backend.entity.RestaurantTable;
import cafe_backend.repository.CafeRepository;
import cafe_backend.repository.RestaurantTableRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "*")
public class RestaurantTableController {

    private final RestaurantTableRepository tableRepository;
    private final CafeRepository cafeRepository;

    public RestaurantTableController(
            RestaurantTableRepository tableRepository,
            CafeRepository cafeRepository) {

        this.tableRepository = tableRepository;
        this.cafeRepository = cafeRepository;
    }

    // CREATE TABLE
    @PostMapping
    public RestaurantTable createTable(@RequestBody RestaurantTable table) {

        if (table.getCafe() == null || table.getCafe().getId() == null) {
            throw new RuntimeException("Cafe ID is required");
        }

        Long cafeId = table.getCafe().getId();

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));

        table.setCafe(cafe);

        return tableRepository.save(table);
    }

    // GET ALL TABLES
    @GetMapping
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    // GET TABLE BY ID
    @GetMapping("/{id}")
    public RestaurantTable getTable(@PathVariable Long id) {

        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
    }

    // UPDATE TABLE
    @PutMapping("/{id}")
    public RestaurantTable updateTable(
            @PathVariable Long id,
            @RequestBody RestaurantTable tableDetails) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        table.setTableNumber(tableDetails.getTableNumber());
        table.setOccupied(tableDetails.getOccupied());

        if (tableDetails.getCafe() != null &&
                tableDetails.getCafe().getId() != null) {

            Long cafeId = tableDetails.getCafe().getId();

            Cafe cafe = cafeRepository.findById(cafeId)
                    .orElseThrow(() -> new RuntimeException("Cafe not found"));

            table.setCafe(cafe);
        }

        return tableRepository.save(table);
    }

    // DELETE TABLE
    @DeleteMapping("/{id}")
    public String deleteTable(@PathVariable Long id) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        tableRepository.delete(table);

        return "Table deleted successfully";
    }

    // GET QR URL
    @GetMapping("/{id}/qr-url")
    public String getQrUrl(@PathVariable Long id) {

        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        String qrUrl = "http://localhost:5500/frontend/menu.html?table="
                + table.getQrToken();

        return qrUrl;
    }

    //GET QR TOKEN
    @GetMapping("/qr/{qrToken}")
    public RestaurantTable getTableByQrToken(@PathVariable String qrToken) {

        return tableRepository.findByQrToken(qrToken)
            .orElseThrow(() -> new RuntimeException("Invalid QR token"));
    }

}