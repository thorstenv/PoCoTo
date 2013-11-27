package jav.gui.wizard.newProject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;

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
public class NewProjectWizardDescriptor extends WizardDescriptor {
   private JFileChooser jfc = new JFileChooser();
   private NewProjectWizardPanel0 panel0 = new NewProjectWizardPanel0(jfc);
   private NewProjectWizardPanel1 panel1 = new NewProjectWizardPanel1(jfc);
   private NewProjectWizardPanel2 panel2 = new NewProjectWizardPanel2(jfc);
//   private NewProjectWizardPanel3 panel3 = new NewProjectWizardPanel3();

   public NewProjectWizardDescriptor() {
      List<Panel<WizardDescriptor>> panels = new ArrayList<>();
      panels.add(panel0);
      panels.add(panel1);
      panels.add(panel2);
//      panels.add(panel3);
      this.setPanelsAndSettings(new ArrayIterator<>(panels), this);

      // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
      this.setTitleFormat(new MessageFormat("{0}"));
      this.setTitle(NbBundle.getBundle(NewProjectWizardDescriptor.class).getString("Wizard.Name"));

      putProperty("WizardPanel_autoWizardStyle",  Boolean.TRUE);
      putProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
      putProperty("WizardPanel_contentNumbered",  Boolean.TRUE);
      putProperty("WizardPanel_contentData", new String[]{panel0.getName(), panel1.getName(), panel2.getName()}); //, panel3.getName()});
   }
}
