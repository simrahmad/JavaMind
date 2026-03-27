package com.simrahapp.chatboot.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.pdfbox.Loader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

@Service
public class FileExtractionService {

    // ── ABSTRACTION: main entry point ──────────────────────────
    // Detects file type and delegates to correct extractor
    public String extractText(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) return "Unknown file";

        String lower = filename.toLowerCase();

        try {
            if (lower.endsWith(".pdf"))
                return extractPDF(file);
            else if (lower.endsWith(".docx"))
                return extractWord(file);
            else if (lower.endsWith(".pptx"))
                return extractPowerPoint(file);
            else if (lower.endsWith(".xlsx") || lower.endsWith(".xls"))
                return extractExcel(file);
            else if (isImage(lower))
                return "IMAGE_FILE";
            else
                return "Unsupported file type: " + filename;

        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    // ── ENCAPSULATION: private extractors ─────────────────────

    private String extractPDF(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             PDDocument doc = Loader.loadPDF(is.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            return truncate(text, 8000);
        }
    }

    private String extractWord(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             XWPFDocument doc = new XWPFDocument(is)) {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph para : doc.getParagraphs()) {
                sb.append(para.getText()).append("\n");
            }
            return truncate(sb.toString(), 8000);
        }
    }

    private String extractPowerPoint(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             XMLSlideShow ppt = new XMLSlideShow(is)) {
            StringBuilder sb = new StringBuilder();
            for (XSLFSlide slide : ppt.getSlides()) {
                sb.append("--- Slide ---\n");
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        sb.append(((XSLFTextShape) shape).getText()).append("\n");
                    }
                }
            }
            return truncate(sb.toString(), 8000);
        }
    }

    private String extractExcel(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            StringBuilder sb = new StringBuilder();
            for (Sheet sheet : workbook) {
                sb.append("--- Sheet: ").append(sheet.getSheetName()).append(" ---\n");
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        sb.append(getCellValue(cell)).append("\t");
                    }
                    sb.append("\n");
                }
            }
            return truncate(sb.toString(), 8000);
        }
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default:      return "";
        }
    }

    private boolean isImage(String filename) {
        return filename.endsWith(".jpg") || filename.endsWith(".jpeg")
                || filename.endsWith(".png") || filename.endsWith(".gif")
                || filename.endsWith(".webp") || filename.endsWith(".bmp");
    }

    private String truncate(String text, int maxChars) {
        if (text == null) return "";
        text = text.trim();
        return text.length() > maxChars
                ? text.substring(0, maxChars) + "\n... [truncated]"
                : text;
    }
}