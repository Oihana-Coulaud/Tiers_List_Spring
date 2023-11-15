package com.example.tp_spring.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.tp_spring.entity.Image;
import com.example.tp_spring.entity.Tag;
import com.example.tp_spring.entity.User;
import com.example.tp_spring.repository.ImageRepository;
import com.example.tp_spring.repository.TagRepository;
import com.example.tp_spring.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.tp_spring.storage.StorageFileNotFoundException;
import com.example.tp_spring.storage.StorageService;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    @Autowired
    public FileUploadController(StorageService storageService, ImageRepository imageRepository, UserRepository userRepository) {
        this.storageService = storageService;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/files")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(name = "tag", required = false) String selectedTag,
            RedirectAttributes redirectAttributes,
            Model model) {

        List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        if (files == null || files.length == 0) {
            model.addAttribute("tagList", tags);
            model.addAttribute("error", "Veuillez ajouter une image");
            return "uploadForm";
        }

        if (selectedTag == null) {
            model.addAttribute("tagList", tags);
            model.addAttribute("error", "Veuillez sélectionner un tag");
            return "uploadForm";
        }

        Tag tag = tagRepository.findByName(selectedTag);

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String generatedFilename = generateUniqueFilename(file.getOriginalFilename());

            Image image = new Image(generatedFilename, "created", tag, currentUser);
            imageRepository.save(image);

            storageService.store(file, generatedFilename);
        }

        redirectAttributes.addFlashAttribute("message", "You successfully uploaded the images!");

        return "redirect:/account";
    }

    private String generateUniqueFilename(String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomString = UUID.randomUUID().toString().substring(0, 6);
        String fileExtension = FilenameUtils.getExtension(originalFilename);

        return timestamp + "_" + randomString + "." + fileExtension;
    }

    @PostMapping("/add-tag")
    public String addTag(@RequestParam(name = "newTag") String newTag, Model model) {
        if (newTag != null && !newTag.isEmpty()) {
            tagRepository.save(new Tag(newTag));
            List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();
            model.addAttribute("tagList", tags);
            model.addAttribute("success", "Nouveau tag ajouté avec succès: " + newTag);
        }
        return "uploadForm";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/upload-form")
    public String uploadForm(Model model) {
        List<String> tags = tagRepository.findAllTagNamesOrderedAlphabetically();
        model.addAttribute("tagList", tags);
        return "uploadForm";
    }


}