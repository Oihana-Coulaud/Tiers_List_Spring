    package com.example.tp_spring.storage;

    import org.springframework.core.io.Resource;
    import org.springframework.web.multipart.MultipartFile;

    import java.nio.file.Path;
    import java.util.stream.Stream;


    import org.springframework.core.io.Resource;
    import org.springframework.web.multipart.MultipartFile;

    import java.nio.file.Path;
    import java.util.stream.Stream;

    public interface StorageService {

        void init();

        void store(MultipartFile file, String filename);

        Stream<Path> loadAll();

        Path load(String filename);

        Resource loadAsResource(String filename);

        void deleteAll();

    }