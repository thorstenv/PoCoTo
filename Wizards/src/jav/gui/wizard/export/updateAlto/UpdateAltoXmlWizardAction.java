package jav.gui.wizard.export.updateAlto;

import jav.gui.actions.ContextAction;
import jav.gui.actions.ExportAsDocXML;
import jav.gui.cookies.DocumentLoadedCookie;
import jav.gui.dialogs.CustomErrorDialog;
import jav.gui.dialogs.UnsavedChangesDialog;
import jav.gui.main.MainController;
import static jav.gui.wizard.export.updateAlto.UpdateAltoXmlVisualPanel1.PROP_ALTODIR;
import static jav.gui.wizard.export.updateAlto.UpdateAltoXmlVisualPanel1.PROP_ALTOENC;
import static jav.gui.wizard.export.updateAlto.UpdateAltoXmlVisualPanel1.PROP_ALTORES;
import static jav.gui.wizard.export.updateAlto.UpdateAltoXmlVisualPanel2.PROP_TARGETDIR;
import static jav.gui.wizard.export.updateAlto.UpdateAltoXmlVisualPanel2.PROP_TARGETENC;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import javax.swing.JComponent;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressRunnable;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

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

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
// @ActionID(category="...", id="updateAlto.UpdateAltoXmlWizardAction")
// @ActionRegistration(displayName="Open UpdateAltoXml Wizard")
// @ActionReference(path="Menu/Tools", position=...)
public final class UpdateAltoXmlWizardAction extends ContextAction<DocumentLoadedCookie> {

    public UpdateAltoXmlWizardAction() {        
        this(Utilities.actionsGlobalContext());
    }
    
    public UpdateAltoXmlWizardAction(Lookup context) {        
        super(context);
        putValue(NAME, NbBundle.getMessage(UpdateAltoXmlWizardAction.class, "updateAlto"));
    }

    @Override
    public Class<DocumentLoadedCookie> contextClass() {
        return DocumentLoadedCookie.class;
    }

    @Override
    public void performAction(DocumentLoadedCookie context) {
        if(MainController.findInstance().hasUnsavedChanges()) {
            Object retval = new UnsavedChangesDialog().showDialog();
            if (retval.equals(java.util.ResourceBundle.getBundle("jav/gui/dialogs/Bundle").getString("save"))) {
                try {
                    MainController.findInstance().getSaver().save();
                    doIt();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else if (retval.equals(java.util.ResourceBundle.getBundle("jav/gui/dialogs/Bundle").getString("discard"))) {
                doIt();
            }
        } else {
            doIt();
        }        
    }
    
    private void doIt() {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new UpdateAltoXmlWizardPanel1());
        panels.add(new UpdateAltoXmlWizardPanel2());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle(NbBundle.getMessage(UpdateAltoXmlWizardAction.class, "updateAlto"));        
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            UpdateAltoXmlWizardAction.NativeMethodRunner runner = new UpdateAltoXmlWizardAction.NativeMethodRunner(wiz.getProperty(PROP_ALTODIR).toString(), wiz.getProperty(PROP_ALTOENC).toString(), wiz.getProperty(PROP_ALTORES).toString(), wiz.getProperty(PROP_TARGETDIR).toString(), wiz.getProperty(PROP_TARGETENC).toString());
                int retval = ProgressUtils.showProgressDialogAndRun(runner, java.util.ResourceBundle.getBundle("jav/gui/main/Bundle").getString("exporting"), true);
                if( retval == 0) {
                    new CustomErrorDialog().showDialog("Error while exporting the document!\n");
                }            
        }        
    }

    @Override
    public boolean enable(DocumentLoadedCookie context) {
        return true;
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new UpdateAltoXmlWizardAction();
    }

    private class NativeMethodRunner implements ProgressRunnable<Integer> {
        
        private String altoPath;
        private String altoEnc;
        private String altoRes;
        private String targetPath;
        private String targetEnc;
        
        public NativeMethodRunner(String a, String ae, String ar, String t, String te) {
            this.altoPath = a;
            this.altoEnc = ae;
            this.altoRes = ar;
            this.targetPath = t;
            this.targetEnc = te;
        }

        @Override
        public Integer run(ProgressHandle ph) {
            try {
                ph.progress(java.util.ResourceBundle.getBundle("jav/gui/main/Bundle").getString("exporting"));
                ph.setDisplayName(java.util.ResourceBundle.getBundle("jav/gui/main/Bundle").getString("exporting"));
                MainController.findInstance().getDocument().updateAltoXML(altoPath, altoEnc, altoRes, targetPath, targetEnc);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }    
}
