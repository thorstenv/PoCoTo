package jav.gui.wizard.export.updateAlto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import org.openide.util.Exceptions;

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
public final class UpdateAltoXmlVisualPanel2 extends JPanel implements DocumentListener, ItemListener {

    public static final String PROP_TARGETDIR = "targetPath";
    public static final String PROP_TARGETENC = "targetEnc";
    private static String DEFAULT_ENCODING = "UTF-8";
    
    /**
     * Creates new form UpdateAltoXmlVisualPanel1
     */
    public UpdateAltoXmlVisualPanel2() {
        initComponents();
        
        jTextField1.setText("");
        jTextField1.getDocument().addDocumentListener(this);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle(java.util.ResourceBundle.getBundle("jav/gui/wizard/export/updateAlto/Bundle").getString("targetPath"));
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return java.util.ResourceBundle.getBundle("jav/gui/wizard/export/updateAlto/Bundle").getString("targetPath");
                    }
                });

                if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = jfc.getSelectedFile();
                        jTextField1.setText(file.getCanonicalPath());
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });
        
        String[] encodingNames = Charset.availableCharsets().keySet().toArray(new String[0]);
        String def = null;
        for (int i = 0; i < encodingNames.length; ++i) {
            if (DEFAULT_ENCODING.equalsIgnoreCase(encodingNames[i])) {
                def = encodingNames[i];
                break;
            }
        }
        
        jComboBox1.setModel(new DefaultComboBoxModel(encodingNames));
        jComboBox1.setSelectedItem(def);
        jComboBox1.addItemListener(this);
        
    }
    
    public String getTargetPath() {
        return this.jTextField1.getText();
    }
    
    @Override
    public String getName() {
        return java.util.ResourceBundle.getBundle("jav/gui/wizard/export/updateAlto/Bundle").getString("targetPath");
    }
    
    public String getEncoding() {
        if (jComboBox1.getSelectedItem() == null) {
            return null;
        } else {
            return (String) jComboBox1.getSelectedItem();
        }
        
    }    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(UpdateAltoXmlVisualPanel2.class, "UpdateAltoXmlVisualPanel1.jLabel2.text")); // NOI18N

        jTextField1.setEditable(false);
        jTextField1.setText(org.openide.util.NbBundle.getMessage(UpdateAltoXmlVisualPanel2.class, "UpdateAltoXmlVisualPanel2.jTextField1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(UpdateAltoXmlVisualPanel2.class, "UpdateAltoXmlVisualPanel2.jButton1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(UpdateAltoXmlVisualPanel2.class, "UpdateAltoXmlVisualPanel2.jLabel2.text")); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(203, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void insertUpdate(DocumentEvent e) {
        firePropertyChange(PROP_TARGETDIR, 0, 1);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        firePropertyChange(PROP_TARGETDIR, 0, 1);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        firePropertyChange(PROP_TARGETDIR, 0, 1);        
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        firePropertyChange(PROP_TARGETENC, 0, 1);
    }
}
