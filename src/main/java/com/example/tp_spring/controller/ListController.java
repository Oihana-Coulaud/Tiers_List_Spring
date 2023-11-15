package com.example.tp_spring.controller;

import com.example.tp_spring.entity.*;
import com.example.tp_spring.repository.RankingRepository;
import com.example.tp_spring.repository.TiersListRepository;
import com.example.tp_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ListController {

    private int numberOfExtraFields = 1;

    @Autowired
    private TiersListRepository tiersListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankingRepository rankingRepository;
    @PostMapping("/create-list")
    public String handleFileUpload(
            @RequestParam String name,
            RedirectAttributes redirectAttributes,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername);
        if (name.isEmpty()) {
            model.addAttribute("error", "Veuillez renseigner un nom pour la liste.");
            return "addList";
        }

        TiersList tl = new TiersList(name, currentUser);
        tiersListRepository.save(tl);

        redirectAttributes.addAttribute("listName", name);

        return "redirect:/add-rank";

    }

    @GetMapping("/add-rank")
    public String addRank(@RequestParam String listName, Model model) {
            numberOfExtraFields = 1;
            model.addAttribute("listName", listName);
            model.addAttribute("inputField", "rankName");
            model.addAttribute("numberOfExtraFields", numberOfExtraFields);

            return "addRank";
    }

    @PostMapping("/save-ranks")
    public String saveRanks(
            @RequestParam String listName,
            @RequestParam Map<String, String> allParams,
            @RequestParam(name = "addRank", defaultValue = "false") boolean addRank,
            Model model) {
        model.addAttribute("listName", listName);
        if (addRank) {
            numberOfExtraFields += 1;
            model.addAttribute("numberOfExtraFields", numberOfExtraFields);
            return "addRank";
        }
        TiersList tiersList = tiersListRepository.findByName(listName);

        List<String> rankNames = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("rankName"))
                .map(Map.Entry::getValue)
                .toList();

        for (String rankName : rankNames) {
            Ranking ranking = new Ranking(rankName, tiersList);
            rankingRepository.save(ranking);
        }

        return "choiceList";
    }
}
