package jp.co.metateam.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.log4j.Log4j2;

import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.repository.AccountRepository;
import jp.co.metateam.library.repository.StockRepository;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.values.RentalStatus;

/**
 * 貸出管理関連クラスß
 */
@Log4j2
@Controller
public class RentalManageController {

        private final AccountRepository accountRepository;
        private final StockRepository stockRepository;
        private final RentalManageService rentalManageService;

        public RentalManageController(
                        AccountRepository accountRepository,
                        StockRepository stockRepository,
                        RentalManageService rentalManageService) {

                this.accountRepository = accountRepository;
                this.stockRepository = stockRepository;
                this.rentalManageService = rentalManageService;
        }

        /**
         * 貸出一覧画面初期表示
         * @param model
         * @return
         */
        @GetMapping("/rental/index")
        public String index(Model model) {
                // 貸出管理テーブルから全件取得

                // 貸出一覧画面に渡すデータをmodelに追加

                // 貸出一覧画面に遷移
                return "/rental/index";
        }

        @GetMapping("/rental/add")
        public String add(Model model) {

                model.addAttribute(
                                "rentalManageDto", new RentalManageDto());
                model.addAttribute(
                                "accounts", accountRepository.findAll());
                model.addAttribute(
                                "stockList", stockRepository.findAll());
                model.addAttribute(
                                "rentalStatus",
                                RentalStatus.values());

                return "/rental/add";
        }

        @PostMapping("/rental/add")
        public String add(
                        @Validated @ModelAttribute("rentalManageDto") RentalManageDto rentalManageDto,
                        BindingResult bindingResult,
                        Model model) {

                rentalManageService.validateRentalManage(
                                rentalManageDto,
                                bindingResult);

                // エラー判定
                if (bindingResult.hasErrors()) {

                        model.addAttribute(
                                        "accounts",
                                        accountRepository.findAll());

                        model.addAttribute(
                                        "stockList",
                                        stockRepository.findAll());

                        model.addAttribute(
                                        "rentalStatus",
                                        RentalStatus.values());

                        return "/rental/add";
                }

                rentalManageService.saveRentalManage(
                                rentalManageDto);

                return "redirect:/rental/index";
        }
}
