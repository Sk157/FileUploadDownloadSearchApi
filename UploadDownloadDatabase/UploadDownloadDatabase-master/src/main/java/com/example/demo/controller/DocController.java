package com.example.demo.controller;

import java.io.IOException;
import java.security.cert.PKIXRevocationChecker.Option;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Doc;
import com.example.demo.service.DocStorageService;
import com.sun.el.stream.Optional;

@RestController
public class DocController {

	@Autowired 
	private DocStorageService docStorageService;
	
	@GetMapping("/")
	public ModelAndView get(Model model) {
		List<Doc> docs = docStorageService.getFiles();
		ModelAndView mod = new ModelAndView();
		mod.setViewName("doc");
		model.addAttribute("docs", docs);
		return mod;
	}
	@GetMapping("/search")
	public ModelAndView search(Model model,String keyword) {
		ModelAndView mod = new ModelAndView();
		mod.setViewName("search_result");
		model.addAttribute("keyword",keyword);
		if(keyword!=null) {
			model.addAttribute("doc",docStorageService.findByKeyword(keyword));
		}
		else {
			model.addAttribute("doc",docStorageService.getFiles());
		}
		model.addAttribute("pageTitle",  "Search result for " + keyword );
		return mod;
	}
	
	@PostMapping("/uploadFiles")
	public ModelAndView uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
		for (MultipartFile file: files) {
			docStorageService.saveFile(file);
			docStorageService.readFile(file);
			
		}
		return new ModelAndView("redirect:/");
	}
	
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
		Doc doc = docStorageService.getFile(fileId).get();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
				.body(new ByteArrayResource(doc.getData()));
	}
	
}
