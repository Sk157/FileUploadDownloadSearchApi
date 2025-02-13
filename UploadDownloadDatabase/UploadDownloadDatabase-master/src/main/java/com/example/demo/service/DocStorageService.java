package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Doc;
import com.example.demo.repository.DocRepository;


@Service
public class DocStorageService {
	@PersistenceContext
	EntityManager em;
  @Autowired
  private DocRepository docRepository;
  
  public Doc saveFile(MultipartFile file) {
	  String docname = file.getOriginalFilename();
	  try {
		  Doc doc = new Doc(docname,file.getContentType(),file.getBytes());
		  return docRepository.save(doc);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  return null;
  }
  public Optional<Doc> getFile(Integer fileId) {
	  return docRepository.findById(fileId);
  }
  public List<Doc> getFiles(){
	  return docRepository.findAll();
  }
  public List <Doc> findByKeyword(String keyword){
		  return docRepository.findByKeyword(keyword);		  
  }
  public Doc readFile(MultipartFile file) throws IOException {
	  
	  InputStream doc = file.getInputStream();
	  PDDocument docs = PDDocument.load(doc);
	  PDFTextStripper pdf = new PDFTextStripper();
	  String pdfdata = pdf.getText(docs);
	  System.out.println(pdfdata);
	 
	  return null;
	  
  } 
}
