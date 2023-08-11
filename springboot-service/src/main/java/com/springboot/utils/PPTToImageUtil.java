package com.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * pptx 转为图片
 */
@Slf4j
public class PPTToImageUtil {
    /**
     * 将后缀为.pptx的PPT转换为图片
     * @param imgFile 将PPT转换为图片后的路径
     * @return
     */
    public static List doPPT2007toImage(File file, String imgFile) {
        List<String> list = new ArrayList<>();
        FileInputStream is = null ;
        try {
            is = new FileInputStream(file);

            XMLSlideShow xmlSlideShow = new XMLSlideShow(is);
            is.close();
            // 获取大小
            Dimension pgsize = xmlSlideShow.getPageSize();
            // 获取幻灯片
            List<XSLFSlide> slides = xmlSlideShow.getSlides();

            for (int i = 0 ; i < slides.size() ; i++) {
                // 解决乱码问题
                List<XSLFShape> shapes1 = slides.get(i).getShapes();
                for (XSLFShape shape : shapes1) {

                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape sh = (XSLFTextShape) shape;
                        List<XSLFTextParagraph> textParagraphs = sh.getTextParagraphs();

                        for (XSLFTextParagraph xslfTextParagraph : textParagraphs) {
                            List<XSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                            for (XSLFTextRun xslfTextRun : textRuns) {
                                Double fontSize = xslfTextRun.getFontSize();
                                xslfTextRun.setFontSize(fontSize);
                                xslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
                }
                //根据幻灯片大小生成图片
                BufferedImage img = new BufferedImage(pgsize.width,pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,pgsize.height));

                // 最核心的代码
                slides.get(i).draw(graphics);

                //图片将要存放的路径
                //String absolutePath = imgFile +"/"+ (i + 1) + ".jpeg";
                String absolutePath = imgFile +"/"+ (i + 1) + ".png";
                File jpegFile = new File(absolutePath);
                // 图片路径存放
               // list.add((i + 1) + ".jpeg");
                list.add((i + 1) + ".png");
                //如果图片存在，则不再生成
                if (jpegFile.exists()) {
                    continue;
                }
                // 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
                FileOutputStream out = new FileOutputStream(jpegFile);

                // 写入到图片中去
                ImageIO.write(img, "jpeg", out);
                out.close();
            }
            return list;
        } catch (Exception e) {
            log.error("PPT转换成图片 发生异常",e);
        }
        return list;

    }


    public static int convertPdf2Png(File in, String outputFile) {
        int pageCounter = 0;
        try(PDDocument document=PDDocument.load(in)) {
            // 保存图片的位置
            File file = new File(outputFile);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imageFormat = "png";

            PDFRenderer pdfRenderer = new PDFRenderer(document);

            PDPageTree pdPageTree = document.getPages();
            String fileName;
            for (PDPage page : pdPageTree) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, 100, ImageType.RGB);
                fileName = outputFile + "/" + (++pageCounter) + "." + imageFormat;
                ImageIOUtil.writeImage(bim, fileName, 100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageCounter;
    }



}

