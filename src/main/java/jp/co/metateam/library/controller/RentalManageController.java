package jp.co.metateam.library.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;

import jp.co.metateam.library.model.RentalManageDto;

import jp.co.metateam.library.model.SelectOption;

import jp.co.metateam.library.service.RentalService;

import lombok.extern.log4j.Log4j2;

@Log4j2

@Controller

public class RentalManageController {

    @Autowired

    private RentalService rentalService;

    @GetMapping("/rental/index")

    public String index(Model model) {

        return "/rental/index";

    }

    @GetMapping("/rental/add")

    public String add(Model model) {

        setPulldownList(model);

        model.addAttribute("rentalManageDto", new RentalManageDto());

        return "/rental/add";

    }

    @PostMapping("/rental/add")

    public String create(

            @ModelAttribute RentalManageDto rentalManageDto,

            Model model) {

        List<String> errors = rentalService.validate(rentalManageDto);

        if (!errors.isEmpty()) {

            model.addAttribute("employeeIdError", errors.contains("社員番号は必須です。"));
            model.addAttribute("expectedRentalOnError", errors.contains("貸出予定日は必須です。"));
            model.addAttribute("expectedReturnOnError", errors.contains("返却予定日は必須です。"));
            model.addAttribute("stockIdError", errors.contains("在庫管理番号は必須です。"));
            model.addAttribute("statusError", errors.contains("貸出ステータスは必須です。"));
            model.addAttribute("statusCheckError",
                    errors.contains("貸出ステータスは「貸出待ち」「貸出中」のどちらかに設定してください。"));

            model.addAttribute("futureStatusError",
                    errors.contains("未来日付では「貸出待ち」を選択してください"));

            model.addAttribute("pastStatusError",
                    errors.contains("過去日付では「貸出中」を選択してください"));

            model.addAttribute("dateRelationError", errors.contains("返却予定日は貸出予定日以降を入力してください。"));
            model.addAttribute("rentalManageDto", rentalManageDto);

            setPulldownList(model);

            return "/rental/add";

        }
        // DB登録処理
        rentalService.insert(rentalManageDto);

        // 登録完了後は登録画面へ戻る

        setPulldownList(model);

        model.addAttribute("rentalManageDto", new RentalManageDto());

        return "/rental/add";

    }

    private void setPulldownList(Model model) {

        List<SelectOption> accounts = new ArrayList<>();

        accounts.add(new SelectOption("000001", "000001 管理太郎"));

        accounts.add(new SelectOption("468", "468 石田"));

        List<SelectOption> stockList = new ArrayList<>();

        stockList.add(new SelectOption("A00001", "A00001 スッキリわかるJava入門 第4版"));

        stockList.add(new SelectOption("A00002", "A00002 かんたん合格ITパスポート"));

        stockList.add(new SelectOption("A00003", "A00003 13歳から分かる！7つの習慣"));

        stockList.add(new SelectOption("A00004", "A00004 チーズはどこへ消えた？"));

        List<SelectOption> rentalStatus = new ArrayList<>();

        rentalStatus.add(new SelectOption("0", "貸出待ち"));

        rentalStatus.add(new SelectOption("1", "貸出中"));

        rentalStatus.add(new SelectOption("2", "返却済み"));

        rentalStatus.add(new SelectOption("3", "キャンセル"));

        model.addAttribute("accounts", accounts);

        model.addAttribute("stockList", stockList);

        model.addAttribute("rentalStatus", rentalStatus);

    }

}
