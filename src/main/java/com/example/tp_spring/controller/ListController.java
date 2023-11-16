package com.example.tp_spring.controller;

import com.example.tp_spring.entity.*;
import com.example.tp_spring.repository.RankingRepository;
import com.example.tp_spring.repository.TagRepository;
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
import java.util.Optional;

@Controller
public class ListController {

    private int numberOfExtraFields = 1;

    @Autowired
    private TiersListRepository tiersListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private TagRepository tagRepository;

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

        for (int i = 0; i < rankNames.size(); i++) {
            String rankName = rankNames.get(i);
            int index = i + 1;
            Ranking ranking = new Ranking(rankName, index, tiersList);
            rankingRepository.save(ranking);
        }
        List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();

        model.addAttribute("listName", listName);
        model.addAttribute("ranks", rankNames);
        model.addAttribute("tagList", tags);
        return "addListTag";
    }

    @GetMapping("/addListTag")
    public String addListTag(
            @RequestParam List<String> ranks,
            @RequestParam String listName,
            Model model) {
        List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();
        model.addAttribute("tagList", tags);
        model.addAttribute("ranks", ranks);
        model.addAttribute("listName", listName);
        return "addListTag";
    }

    @PostMapping("/save-tag")
    public String choiceList(
            @RequestParam(name = "tag", required = false) String selectedTag,
            @RequestParam Map<String, String> allParams,
            @RequestParam String listName,
            Model model) {

        List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();
        List<String> rankNames = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("rankName"))
                .map(Map.Entry::getValue)
                .toList();

        if (selectedTag == null) {
            model.addAttribute("tagList", tags);
            model.addAttribute("ranks", rankNames);
            model.addAttribute("error", "Veuillez sélectionner un tag");
            return "addListTag";
        }
        model.addAttribute("tag", selectedTag);
        model.addAttribute("ranks", rankNames);
        model.addAttribute("listName", listName);
            return "choiceList";
    }

    @PostMapping("/add-tag")
    public String addTag(@RequestParam(name = "newTag") String newTag,
                         @RequestParam String listName,
                         @RequestParam Map<String, String> allParams,
                         Model model) {

        List<String> rankNames = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("rankName"))
                .map(Map.Entry::getValue)
                .toList();

        if (newTag != null && !newTag.isEmpty()) {
            tagRepository.save(new Tag(newTag));
            model.addAttribute("success", "Nouveau tag ajouté avec succès: " + newTag);
        }else {
            model.addAttribute("error", "Veuillez renseigner un nouveau tag");
        }

        List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();
        model.addAttribute("ranks", rankNames);
        model.addAttribute("tagList", tags);
        model.addAttribute("listName", listName);

        return "addListTag";
    }

    @GetMapping("/listDetails")
    public String addListDetails(     @RequestParam List<String> ranks,
                                      @RequestParam String listName,
                                      @RequestParam(name = "tag", required = false) String selectedTag,
                                        Model model) {
        model.addAttribute("listName", listName);
        model.addAttribute("ranks", ranks);
        model.addAttribute("selectedTag", selectedTag);

        return "addListDetails";
    }

    @GetMapping("/show-list")
    public String showList(
            @RequestParam("id") Integer id,
            Model model) {
        Optional<TiersList> tiersList = tiersListRepository.findById(id);
        System.out.println("list" + tiersList);
        model.addAttribute("tiersList", tiersList);

        return "viewList";
    }
}
