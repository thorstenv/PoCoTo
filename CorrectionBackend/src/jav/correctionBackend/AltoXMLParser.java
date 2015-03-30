package jav.correctionBackend;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDirectory;
import jav.correctionBackend.alto1_4.Alto1_4Document;
import jav.correctionBackend.alto1_4.BlockType;
import jav.correctionBackend.alto1_4.ComposedBlockType;
import jav.correctionBackend.alto1_4.StringType;
import jav.correctionBackend.alto1_4.TextBlockType;
import jav.correctionBackend.alto1_4.TextBlockType.TextLine;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

/**
 * Copyright (c) 2012, IMPACT working group at the Centrum für Informations- und
 * Sprachverarbeitung, University of Munich. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * This file is part of the ocr-postcorrection tool developed by the IMPACT
 * working group at the Centrum für Informations- und Sprachverarbeitung,
 * University of Munich. For further information and contacts visit
 * http://ocr.cis.uni-muenchen.de/
 *
 * @author thorsten (thorsten.vobl@googlemail.com)
 */
public class AltoXMLParser implements Parser {

    private int pages = 0;
    private int tokenIndex_ = 0;
    private Document doc_ = null;
    private Token temptoken_ = null;

    public AltoXMLParser(Document doc) {
        this.doc_ = doc;
    }

    @Override
    public void parse(String xmlFile, String imgFile, String encoding) {
        int horizontalResolution;
        int verticalResolution;
        String measurementUnit;

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new StreamSource(Alto1_4Document.class.getResourceAsStream("/jav/correctionBackend/alto1_4/alto-1-4.xsd")));

            TIFFDirectory tif = new TIFFDirectory(new FileSeekableStream(new RandomAccessFile(imgFile, "r")), 0);
            horizontalResolution = (int) tif.getFieldAsFloat(282);
            verticalResolution = (int) tif.getFieldAsFloat(283);
            
            System.out.println("RESOLUTION: " + horizontalResolution + " " + verticalResolution);

            InputStreamReader reader = new InputStreamReader(new FileInputStream(xmlFile), encoding);
            JAXBContext context = JAXBContext.newInstance(Alto1_4Document.class);
            Unmarshaller umarshaller = context.createUnmarshaller();
            umarshaller.setSchema(schema);
            Alto1_4Document alt = (Alto1_4Document) umarshaller.unmarshal(reader);
            measurementUnit = alt.getDescription().getMeasurementUnit();

            if (alt != null) {
                for (BlockType blockType : alt.getLayout().getPage().get(0).getPrintSpace().getTextBlockOrIllustrationOrGraphicalElement()) {
                    this.processBlockType(blockType, horizontalResolution, verticalResolution, measurementUnit, FilenameUtils.getName(imgFile));
                };
                pages++;
            }
        } catch (SAXException | JAXBException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void processBlockType(BlockType blockType, int horizontalResolution, int verticalResolution, String measurementUnit, String imgFile) {
//        System.out.println("BLOCKTYPE: " + blockType.getClass().getSimpleName());
        if (blockType.getClass().getSimpleName().equals("TextBlockType")) {
            TextBlockType textBlock = (TextBlockType) blockType;
            for (TextLine textLine : textBlock.getTextLine()) {
                this.processTextLine(textLine, horizontalResolution, verticalResolution, measurementUnit, imgFile);
            };

            temptoken_ = new Token("\n");
            temptoken_.setSpecialSeq(SpecialSequenceType.NEWLINE);
            temptoken_.setIndexInDocument(tokenIndex_);
            temptoken_.setIsSuspicious(false);
            temptoken_.setIsCorrected(false);
            temptoken_.setIsNormal(false);
            temptoken_.setNumberOfCandidates(0);
            temptoken_.setPageIndex(pages);
            temptoken_.setTokenImageInfoBox(null);

            doc_.addToken(temptoken_);
            temptoken_ = null;
            tokenIndex_++;

        } else if (blockType.getClass().getSimpleName().equals("ComposedBlockType")) {
            ComposedBlockType composedBlock = (ComposedBlockType) blockType;
            for (BlockType innerBlockType : composedBlock.getTextBlockOrIllustrationOrGraphicalElement()) {
                this.processBlockType(innerBlockType, horizontalResolution, verticalResolution, measurementUnit, imgFile);
            };
        }
    }

    private void processTextLine(TextBlockType.TextLine textLine, int horizontalResolution, int verticalResolution, String measurementUnit, String imgFile) {

        int top = this.getPixel((int) textLine.getVPOS(), verticalResolution, measurementUnit);
        int bottom = this.getPixel((int) (textLine.getVPOS() + textLine.getHEIGHT()), verticalResolution, measurementUnit);

//        System.out.println("TEXTLINE: " + textLine.getID());
        for (Object obj : textLine.getStringAndSP()) {
            if (obj.getClass().getSimpleName().equals("StringType")) {
                this.processStringType((StringType) obj, top, bottom, horizontalResolution, measurementUnit, imgFile);
            } else if (obj.getClass().getSimpleName().equals("SP")) {
                this.processSpace((TextBlockType.TextLine.SP) obj, top, bottom, horizontalResolution, measurementUnit, imgFile);
            }
        };
        if (textLine.getHYP() != null) {
            this.processHyp(textLine.getHYP(), textLine.getID(), top, bottom, horizontalResolution, measurementUnit, imgFile);
        }

        temptoken_ = new Token("\n");
        temptoken_.setSpecialSeq(SpecialSequenceType.NEWLINE);
        temptoken_.setIndexInDocument(tokenIndex_);
        temptoken_.setIsSuspicious(false);
        temptoken_.setIsCorrected(false);
        temptoken_.setIsNormal(false);
        temptoken_.setNumberOfCandidates(0);
        temptoken_.setPageIndex(pages);
        temptoken_.setTokenImageInfoBox(null);

        doc_.addToken(temptoken_);
        temptoken_ = null;
        tokenIndex_++;
    }

    private int getPixel(int input, int resolution, String measurementUnit) {
        if (measurementUnit.equals("mm10")) {
            return input * resolution / 254;
        } else if (measurementUnit.equals("inch1200")) {
            return input * resolution / 1200;
        } else {
            return input;
        }
    }

    private int processHyp(TextBlockType.TextLine.HYP hyp, String lineID, int lineTop, int lineBottom, int horizontalResolution, String measurementUnit, String imgFile) {
        temptoken_ = new Token(hyp.getCONTENT());
        temptoken_.setSpecialSeq(SpecialSequenceType.HYPHEN);
        temptoken_.setIndexInDocument(tokenIndex_);
        temptoken_.setIsSuspicious(false);
        temptoken_.setIsCorrected(false);
        temptoken_.setPageIndex(pages);
        temptoken_.setIsNormal(false);
        temptoken_.setNumberOfCandidates(0);

        int left = this.getPixel((int) hyp.getHPOS(), horizontalResolution, measurementUnit);
        int right = this.getPixel((int) (hyp.getHPOS() + hyp.getWIDTH()), horizontalResolution, measurementUnit);
                
        // if document has coordinates
        if (left > 0) {
            TokenImageInfoBox tiib = new TokenImageInfoBox();
            tiib.setCoordinateBottom(lineBottom);
            tiib.setCoordinateLeft(left);
            tiib.setCoordinateRight(right);
            tiib.setCoordinateTop(lineTop);
            tiib.setImageFileName(imgFile);
            temptoken_.setTokenImageInfoBox(tiib);
        } else {
            temptoken_.setTokenImageInfoBox(null);
        }

        temptoken_.setOrigID(lineID);
        int retval = doc_.addToken(temptoken_);
        System.out.println("token add " + temptoken_.getWOCR() + " " + temptoken_.isSuspicious());
        tokenIndex_++;
        temptoken_ = null;
//                System.out.println("HYP: " + lineID + " " + lineTop + " " + lineBottom + " " + horizontalResolution + " " + measurementUnit);
        return retval;
    }

    private int processStringType(StringType word, int lineTop, int lineBottom, int horizontalResolution, String measurementUnit, String imgFile) {

        temptoken_ = new Token(word.getCONTENT());
        temptoken_.setSpecialSeq(SpecialSequenceType.NORMAL);
        temptoken_.setIndexInDocument(tokenIndex_);
        temptoken_.setIsSuspicious(false);
        temptoken_.setIsCorrected(false);
        temptoken_.setPageIndex(pages);
        temptoken_.setIsNormal(true);
        temptoken_.setNumberOfCandidates(0);

        int left = this.getPixel((int) word.getHPOS(), horizontalResolution, measurementUnit);
        int right = this.getPixel((int) (word.getHPOS() + word.getWIDTH()), horizontalResolution, measurementUnit);
        // if document has coordinates
        if (left > 0) {
            TokenImageInfoBox tiib = new TokenImageInfoBox();
            tiib.setCoordinateBottom(lineBottom);
            tiib.setCoordinateLeft(left);
            tiib.setCoordinateRight(right);
            tiib.setCoordinateTop(lineTop);
            tiib.setImageFileName(imgFile);
            temptoken_.setTokenImageInfoBox(tiib);
        } else {
            temptoken_.setTokenImageInfoBox(null);
        }

        temptoken_.setOrigID(word.getID());
        int retval = doc_.addToken(temptoken_);
        System.out.println("token add " + temptoken_.getWOCR() + " " + temptoken_.isSuspicious());
        tokenIndex_++;
        temptoken_ = null;
//        System.out.println("STRINGTYPE: " + word.getCONTENT() + " " + lineTop + " " + lineBottom + " " + horizontalResolution + " " + measurementUnit);
        return retval;
    }

    private int processSpace(TextBlockType.TextLine.SP space, int lineTop, int lineBottom, int horizontalResolution, String measurementUnit, String imgFile) {
        temptoken_ = new Token(" ");
        temptoken_.setSpecialSeq(SpecialSequenceType.SPACE);
        temptoken_.setIndexInDocument(tokenIndex_);
        temptoken_.setIsSuspicious(false);
        temptoken_.setIsCorrected(false);
        temptoken_.setPageIndex(pages);
        temptoken_.setIsNormal(true);
        temptoken_.setNumberOfCandidates(0);

        temptoken_.setOrigID(space.getID());
        int retval = doc_.addToken(temptoken_);
        System.out.println("token add " + temptoken_.getWOCR() + " " + temptoken_.isSuspicious());
        tokenIndex_++;
        temptoken_ = null;
//        System.out.println("SPACE: " + lineTop + " " + lineBottom + " " + horizontalResolution + " " + measurementUnit);
        return retval;
    }

}
