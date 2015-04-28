/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.bbva.ctacte.pdf;

/**
 *
 * @author ajbullon
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.TIFFDirectory;
import com.sun.media.imageio.plugins.tiff.TIFFField;
import com.sun.media.imageio.plugins.tiff.TIFFTag;

public class TiffHelper {

    private static final char[] INCH_RESOLUTION_UNIT = new char[]{2};
    private static final long[][] X_DPI_RESOLUTION = new long[][]{{150, 1}};
    private static final long[][] Y_DPI_RESOLUTION = new long[][]{{150, 1}};
    private static final char[] BITS_PER_SAMPLE = new char[]{1};
    private static final char[] COMPRESSION = new char[]{BaselineTIFFTagSet.COMPRESSION_LZW};
    private static final int HEIGHT = 1650;

    /**
     * ::Constructor()
     */
    private TiffHelper() {
    }

    public static void main(String args[]) {
        convertPDF("C:\\origen.pdf", "C:\\origen.tif");
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println(new File("D:\\PROJECTS\\PDFExp\\exp\\Map.pdf").renameTo(new File("D:\\PROJECTS\\PDFExp\\move\\Map.pdf")));
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println(new File("D:\\PROJECTS\\PDFExp\\exp\\Map.tif").renameTo(new File("D:\\PROJECTS\\PDFExp\\move\\Map.tif")));
    }

    /**
     * Convert a PDF document to a TIF file
     */
    public static boolean convertPDF(String pdf, String tif) {
        try {
            convert(pdf, tif);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Convert a PDF document to a TIF file
     */
    protected static void convert(String pdf, String tif) throws IOException {

        Document pdffile = new Document();
        try {
            pdffile.setFile(pdf);
        } catch (PDFException ex) {
            //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Error parsing PDF document " + ex);
        } catch (PDFSecurityException ex) {
            //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Error encryption not supported " + ex);
        } catch (FileNotFoundException ex) {
            //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Error file not found " + ex);
        } catch (IOException ex) {
            //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Error handling PDF document " + ex);
        }

        int numPgs = pdffile.getNumberOfPages();
//float scale = 2.084f; 
        float scale = 1.600f;
        float rotation = 0f;

        BufferedImage image[] = new BufferedImage[numPgs];

        for (int i = 0; i < numPgs; i++) {

            /*
             * Generate the image: Notes: 1275x1650 = 8.5 x 11 @ 150dpi ???
             */
            image[i] = (BufferedImage) pdffile.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);

        }
        try {
            save(image, tif);
        } catch (Exception e) {
// TODO: handle exception 
        } finally {
            pdffile.dispose();
            //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("pdffile getting free");
            pdffile = null;
            image = null;
        }

    }

    /**
     * Save tiff
     */
    @SuppressWarnings("unchecked")
    private static void save(BufferedImage[] b, String tif) throws IOException {

//Get a TIFF writer and set its output. 
        Iterator writers = ImageIO.getImageWritersByFormatName("TIFF");

        if (writers == null || !writers.hasNext()) {
            throw new RuntimeException("No writers for available.");
        }
        File temp = new File(tif);
        ImageWriter writer = (ImageWriter) writers.next();
        FileImageOutputStream fio = new FileImageOutputStream(temp);
        writer.setOutput(fio);
        writer.prepareWriteSequence(null);

        for (int i = 0; i < b.length; i++) {
            ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromRenderedImage(b[i]);
            IIOMetadata imageMetadata = writer.getDefaultImageMetadata(imageType, null);
            imageMetadata = createImageMetadata(imageMetadata);
            writer.writeToSequence(new IIOImage(b[i], null, imageMetadata), null);
        }


        writer.dispose();
        writer = null;
//writers=null; 
        //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Clearing cacheing doc");
        /*
         * temp=null; fio.close(); //+POR SOLICITUD BBVA+//+POR SOLICITUD BBVA+System.out..println("Clearing cacheing doc
         * 1"); 
fio=null;
         */



    }

    /**
     * Return the metadata for the new TIF image
     */
    private static IIOMetadata createImageMetadata(IIOMetadata imageMetadata) throws IIOInvalidTreeException {

//Get the IFD (Image File Directory) which is the root of all the tags 
//for this image. From here we can get all the tags in the image. 
        TIFFDirectory ifd = TIFFDirectory.createFromMetadata(imageMetadata);

//Create the necessary TIFF tags that we want to add to the image metadata 
        BaselineTIFFTagSet base = BaselineTIFFTagSet.getInstance();

//Resolution tags... 
        TIFFTag tagResUnit = base.getTag(BaselineTIFFTagSet.TAG_RESOLUTION_UNIT);
        TIFFTag tagXRes = base.getTag(BaselineTIFFTagSet.TAG_X_RESOLUTION);
        TIFFTag tagYRes = base.getTag(BaselineTIFFTagSet.TAG_Y_RESOLUTION);

//BitsPerSample tag 
        TIFFTag tagBitSample = base.getTag(BaselineTIFFTagSet.TAG_BITS_PER_SAMPLE);

//Row and Strip tags... 
        TIFFTag tagRowStrips = base.getTag(BaselineTIFFTagSet.TAG_ROWS_PER_STRIP);

//Compression tag 
        TIFFTag tagCompression = base.getTag(BaselineTIFFTagSet.TAG_COMPRESSION);

//Set the tag values 
        TIFFField fieldResUnit = new TIFFField(tagResUnit, TIFFTag.TIFF_SHORT, 1, INCH_RESOLUTION_UNIT);
        TIFFField fieldXRes = new TIFFField(tagXRes, TIFFTag.TIFF_RATIONAL, 1, X_DPI_RESOLUTION);
        TIFFField fieldYRes = new TIFFField(tagYRes, TIFFTag.TIFF_RATIONAL, 1, Y_DPI_RESOLUTION);
        TIFFField fieldBitSample = new TIFFField(tagBitSample, TIFFTag.TIFF_SHORT, 1, BITS_PER_SAMPLE);
        TIFFField fieldRowStrips = new TIFFField(tagRowStrips, TIFFTag.TIFF_LONG, 1, new long[]{HEIGHT});
        TIFFField fieldCompression = new TIFFField(tagCompression, TIFFTag.TIFF_SHORT, 1, COMPRESSION);

//Cleanup the fields 
//ifd.removeTIFFFields(); 

//Add the new tag/value sets to the image metadata 
        ifd.addTIFFField(fieldResUnit);
        ifd.addTIFFField(fieldXRes);
        ifd.addTIFFField(fieldYRes);
        ifd.addTIFFField(fieldBitSample);
        ifd.addTIFFField(fieldRowStrips);
        ifd.addTIFFField(fieldCompression);

        return ifd.getAsMetadata();

    }
}
