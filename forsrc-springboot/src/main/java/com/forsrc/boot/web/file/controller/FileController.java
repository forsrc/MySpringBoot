package com.forsrc.boot.web.file.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
// @RequestMapping(FileController.BASE_URL)
public class FileController {

    public static final String BASE_URL = "/demo/file";
    private final Path rootLocation = Paths.get("/files");

    @RequestMapping(value = "/demo/file", method = { RequestMethod.GET })
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files",
                loadAll().map(path -> MvcUriComponentsBuilder
                        .fromMethodName(FileController.class, "serveFile", path.getFileName().toString()).build()
                        .toString()).collect(Collectors.toList()));

        return "/html/file/fileupload";
    }

    private Stream<Path> loadAll() throws IOException {
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new IOException("Failed to read stored files", e);
        }

    }

    @RequestMapping(value = "/demo/file/files/{filename:.+}", method = { RequestMethod.GET })
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename)
            throws MalformedURLException, FileNotFoundException {

        Resource file = loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    private Resource loadAsResource(String filename) throws MalformedURLException, FileNotFoundException {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new MalformedURLException("Could not read file: " + filename);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @RequestMapping(value = "/demo/file", method = { RequestMethod.POST })
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
            throws IOException {

        store(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return String.format("redirect:%s", BASE_URL);
    }

    public void store(MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new IOException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleFileNotFound(FileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity handleMalformedURLException(MalformedURLException exc) {
        return ResponseEntity.notFound().build();
    }
}
