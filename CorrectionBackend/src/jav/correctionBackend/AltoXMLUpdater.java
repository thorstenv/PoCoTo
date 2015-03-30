package jav.correctionBackend;

import jav.correctionBackend.alto1_4.Alto1_4Document;
import jav.correctionBackend.alto1_4.BlockType;
import jav.correctionBackend.alto1_4.ComposedBlockType;
import jav.correctionBackend.alto1_4.StringType;
import jav.correctionBackend.alto1_4.TextBlockType;
import jav.correctionBackend.alto1_4.TextBlockType.TextLine.SP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 *Copyright (c) 2012, IMPACT working group at the Centrum für Informations- und Sprachverarbeitung, University of Munich.
 *All rights reserved.

 *Redistribution and use in source and binary forms, with or without
 *modification, are permitted provided that the following conditions are met:

 *Redistributions of source code must retain the above copyright
 *notice, this list of conditions and the following disclaimer.
 *Redistributions in binary form must reproduce the above copyright
 *notice, this list of conditions and the following disclaimer in the
 *documentation and/or other materials provided with the distribution.

 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * This file is part of the ocr-postcorrection tool developed
 * by the IMPACT working group at the Centrum für Informations- und Sprachverarbeitung, University of Munich.
 * For further information and contacts visit http://ocr.cis.uni-muenchen.de/
 * 
 * @author thorsten (thorsten.vobl@googlemail.com)
 */
public class AltoXMLUpdater {
    
    private Document document;
    
    public AltoXMLUpdater() {
    }
    
    public void doIt( Document doc, String altoPath, String altoEncoding, String altoResolution, String targetPath, String targetEncoding ) {
        
        this.document = doc;
        FilenameFilter fil = new FilenameFilter() {
            @Override
            public boolean accept(File d, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        };
        File xmld = new File(altoPath);
        File[] altoFiles = xmld.listFiles(fil);
        for( File altoFile : altoFiles ) {
            File targetFile = new File(targetPath + File.separator + altoFile.getName());
            this.updateAlto(altoFile, altoEncoding, altoResolution, targetFile, targetEncoding);
        }               
    }
    
    private void updateAlto( File altoInput, String altoEncoding, String altoResolution, File targetFile, String targetEncoding ) {
        int horizontalResolution = Integer.parseInt(altoResolution);
        int verticalResolution = Integer.parseInt(altoResolution);
        String measurementUnit;

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource schemaSource = new StreamSource(Alto1_4Document.class.getResourceAsStream("/jav/correctionBackend/alto1_4/alto-1-4.xsd"));
            Schema schema = sf.newSchema(schemaSource);
           
            InputStreamReader reader = new InputStreamReader(new FileInputStream(altoInput), altoEncoding);
            JAXBContext context = JAXBContext.newInstance(Alto1_4Document.class);
            Unmarshaller umarshaller = context.createUnmarshaller();
            umarshaller.setSchema(schema);
            Alto1_4Document alt = (Alto1_4Document) umarshaller.unmarshal(reader);
            measurementUnit = alt.getDescription().getMeasurementUnit();

            if (alt != null) {
                for (BlockType blockType : alt.getLayout().getPage().get(0).getPrintSpace().getTextBlockOrIllustrationOrGraphicalElement()) {
                    this.processBlockType(blockType, horizontalResolution, verticalResolution, measurementUnit);
                };
            }
            
            String noNamespaceSchemaLocation = "";
            try {
                XMLInputFactory xif = XMLInputFactory.newInstance();
                FileInputStream fis = new FileInputStream(altoInput);
                XMLStreamReader xsr;
                xsr = xif.createXMLStreamReader(fis);
                xsr.nextTag();
                noNamespaceSchemaLocation = xsr.getAttributeValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "noNamespaceSchemaLocation");
            } catch (XMLStreamException ex) {
                Logger.getLogger(AltoXMLUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setSchema(schema);
            if( !noNamespaceSchemaLocation.isEmpty()) {
                marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, noNamespaceSchemaLocation);
            }
            marshaller.marshal(alt, targetFile);
            
        } catch (SAXException | JAXBException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void processBlockType(BlockType blockType, int horizontalResolution, int verticalResolution, String measurementUnit) {
//        System.out.println("BLOCKTYPE: " + blockType.getClass().getSimpleName());
        if (blockType.getClass().getSimpleName().equals("TextBlockType")) {
            TextBlockType textBlock = (TextBlockType) blockType;
            for (TextBlockType.TextLine textLine : textBlock.getTextLine()) {
                this.processTextLine(textBlock, textLine, horizontalResolution, verticalResolution, measurementUnit);
            };
        } else if (blockType.getClass().getSimpleName().equals("ComposedBlockType")) {
            ComposedBlockType composedBlock = (ComposedBlockType) blockType;
            for (BlockType innerBlockType : composedBlock.getTextBlockOrIllustrationOrGraphicalElement()) {
                this.processBlockType(innerBlockType, horizontalResolution, verticalResolution, measurementUnit);
            };
        }
    }
    
    private void processTextLine(TextBlockType textBlock, TextBlockType.TextLine textLine, int horizontalResolution, int verticalResolution, String measurementUnit) {

//        System.out.println("TEXTLINE: " + textLine.getID());
        ListIterator<Object> lineIter = textLine.getStringAndSP().listIterator();
        while( lineIter.hasNext()) {
            Object obj = lineIter.next();
            if (obj.getClass().getSimpleName().equals("StringType")) {
                this.processStringType(lineIter, (StringType) obj, horizontalResolution, verticalResolution, measurementUnit);
            } else if (obj.getClass().getSimpleName().equals("SP")) {
                this.processSpace(lineIter, (TextBlockType.TextLine.SP) obj, horizontalResolution, verticalResolution, measurementUnit);
            }
        }
        if (textLine.getHYP() != null) {
            this.processHyp(lineIter, textLine.getHYP(), textLine.getID(), horizontalResolution, verticalResolution, measurementUnit);
        }
        if( textLine.getStringAndSP().isEmpty()) {
            textBlock.getTextLine().remove(textLine);            
        }
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
    
    private int getMeasurement( int input, int resolution, String measurementUnit) {
        if( measurementUnit.equals("mm10")) {
            return input / resolution * 254;
        } else if( measurementUnit.equals("inch1200")) {
            return input / resolution * 1200;
        } else {
            return input;
        }
    }

    private void processHyp(ListIterator<Object> lineIter, TextBlockType.TextLine.HYP hyp, String lineID, int horizontalResolution, int verticalResolution, String measurementUnit) {
//        temptoken_ = new Token(hyp.getCONTENT());
//        temptoken_.setSpecialSeq(SpecialSequenceType.HYPHEN);
//        temptoken_.setIndexInDocument(tokenIndex_);
//        temptoken_.setIsSuspicious(false);
//        temptoken_.setIsCorrected(false);
//        temptoken_.setPageIndex(pages);
//        temptoken_.setIsNormal(false);
//        temptoken_.setNumberOfCandidates(0);
//
//        int left = this.getPixel((int) hyp.getHPOS(), horizontalResolution, measurementUnit);
//        int right = this.getPixel((int) (hyp.getHPOS() + hyp.getWIDTH()), horizontalResolution, measurementUnit);
//                
//        // if document has coordinates
//        if (left > 0) {
//            TokenImageInfoBox tiib = new TokenImageInfoBox();
//            tiib.setCoordinateBottom(lineBottom);
//            tiib.setCoordinateLeft(left);
//            tiib.setCoordinateRight(right);
//            tiib.setCoordinateTop(lineTop);
//            tiib.setImageFileName(imgFile);
//            temptoken_.setTokenImageInfoBox(tiib);
//        } else {
//            temptoken_.setTokenImageInfoBox(null);
//        }
//
//        temptoken_.setOrigID(lineID);
//        int retval = doc_.addToken(temptoken_);
//        System.out.println("token add " + temptoken_.getWOCR() + " " + temptoken_.isSuspicious());
//        tokenIndex_++;
//        temptoken_ = null;
////                System.out.println("HYP: " + lineID + " " + lineTop + " " + lineBottom + " " + horizontalResolution + " " + measurementUnit);
//        return retval;
    }

    private void processStringType(ListIterator<Object> lineIter, StringType word, int horizontalResolution, int verticalResolution, String measurementUnit) {
        
        List<Token> tokenForOrigId = document.getTokensByOrigID(word.getID());
        
        if( tokenForOrigId == null ) {
            lineIter.remove();
        } else {
            if( tokenForOrigId.size() == 1) {
                Token tok = tokenForOrigId.get(0);
                if( !word.getCONTENT().equals(tok.getWDisplay())) {
                    word.setCONTENT(tok.getWDisplay());
                    word.setVPOS(this.getMeasurement(tok.getTokenImageInfoBox().getCoordinateLeft(), verticalResolution, measurementUnit));
                    word.setWIDTH(this.getMeasurement(tok.getTokenImageInfoBox().getCoordinateRight()-tok.getTokenImageInfoBox().getCoordinateLeft(), verticalResolution, measurementUnit));
                }
            } else if( tokenForOrigId.size() > 1) {
                Token tok = tokenForOrigId.get(0);
                if( !word.getCONTENT().equals(tok.getWDisplay())) {
                    word.setCONTENT(tok.getWDisplay());
                    word.setVPOS(this.getMeasurement(tok.getTokenImageInfoBox().getCoordinateLeft(), verticalResolution, measurementUnit));
                    word.setWIDTH(this.getMeasurement(tok.getTokenImageInfoBox().getCoordinateRight()-tok.getTokenImageInfoBox().getCoordinateLeft(), verticalResolution, measurementUnit));
                }
                for( int i = 1; i < tokenForOrigId.size(); i++) {
                    tok = tokenForOrigId.get(i);
                    if( tok.getSpecialSeq() == SpecialSequenceType.SPACE) {
                        SP newSpace = new SP();
                        newSpace.setID(word.getID()+"_"+i);
                        lineIter.add(newSpace);
                    } else {
                        StringType newWord = new StringType();                        
                        newWord.setCONTENT(tok.getWDisplay());
                        newWord.setID(word.getID()+"_"+i);
                        lineIter.add(newWord);
                    }
                }
            }
        }
    }

    private void processSpace(ListIterator<Object> lineIter, TextBlockType.TextLine.SP space, int horizontalResolution, int verticalResolution, String measurementUnit) {
        List<Token> tokenForOrigId = document.getTokensByOrigID(space.getID());
        if( tokenForOrigId == null ) {
            lineIter.remove();
        }
    }    
}
