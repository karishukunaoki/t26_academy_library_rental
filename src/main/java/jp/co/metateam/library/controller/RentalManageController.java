package jp.co.metateam.library.controller;

import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.service.RentalService;
import jp.co.metateam.library.values.RentalStatus;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rental")
public class RentalManageController {

    private final RentalService rentalService;

    public RentalManageController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {

        model.addAttribute("rentalManageDto", new RentalManageDto());
        model.addAttribute("rentalStatus", RentalStatus.values());

        setPulldownList(model);

        return "rental/add";
    }

    @PostMapping("/add")
    public String addRental(
            @ModelAttribute RentalManageDto dto,
            Model model) {

        rentalService.insert(dto);

        model.addAttribute("message", "保存しました");
        model.addAttribute("rentalManageDto", new RentalManageDto());
        model.addAttribute("rentalStatus", RentalStatus.values());

        setPulldownList(model);

        return "rental/add";
    }

    private void setPulldownList(Model model) {
        model.addAttribute("accounts", rentalService.findAll());
        model.addAttribute("stocks", rentalService.findStocks());
    }
}