/*******************************************************************************
 * Copyright (c) 2007 Topcased and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *      Obeo - initial API and implementation     
 *******************************************************************************/
package org.eclipse.umlgen.gen.c.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property page for common properties of code generators.
 * 
 * @author awalrawens
 */
public class CodeGenerationPropertyPage extends PropertyPage {

	/* output folder */
	private Label outputLabel;

	private Text outputText;

	private Button browseButton;

	/* author */
	private Button genAuthorButton;

	private Label authorLabel;

	private Text authorText;

	/* header */
	private Button genHeaderButton;

	private Label headerLabel;

	private Text headerText;

	/* generate accessors */
	private Button genAccessorsButton;

	/* generate array methods */
	private Button genArrayMethodsButton;

	/* generate main */
	private Button genMainButton;

	/* topcased checkstyle */
	private Button useTopcasedChkstButton;

	/* force static */
	private Button forceStaticButton;

	/* stop if warnings */
	private Button stopIfWarningButton;

	/**
	 * Constructor for CodeGenerationPropertyPage.
	 */
	public CodeGenerationPropertyPage() {
		super();
	}

	/**
	 * Create and initialize widget on the property page
	 * 
	 * @param parent
	 *            is the composite
	 */
	private void addFirstSection(Composite parent) {

		/* 1. Construction of the widgets */

		FormData data;

		// Output : label + text + button

		outputLabel = new Label(parent, SWT.NONE);
		outputLabel.setText("Output Folder");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.width = 68;
		outputLabel.setLayoutData(data);

		outputText = new Text(parent, SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(outputLabel, 15);
		data.width = 266;
		outputText.setLayoutData(data);

		browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("Browse...");
		browseButton.addMouseListener(new BrowseButtonListener(outputText));
		data = new FormData();
		data.left = new FormAttachment(84, 0);
		data.width = 70;
		browseButton.setLayoutData(data);

		// Author : check box + label + text

		genAuthorButton = new Button(parent, SWT.CHECK);
		genAuthorButton.setText("Generate author");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(outputLabel, 15);
		genAuthorButton.setLayoutData(data);

		authorLabel = new Label(parent, SWT.NONE);
		authorLabel.setText("Author Name");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(genAuthorButton, 5);
		data.width = 68;
		authorLabel.setLayoutData(data);

		authorText = new Text(parent, SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(authorLabel, 15);
		data.top = new FormAttachment(genAuthorButton, 5);
		data.width = 266;
		authorText.setLayoutData(data);

		genAuthorButton.addMouseListener(new CheckboxListener(genAuthorButton,
				authorText));

		// Header : check box + label + text
		genHeaderButton = new Button(parent, SWT.CHECK);
		genHeaderButton.setText("Generate header");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(authorLabel, 15);
		genHeaderButton.setLayoutData(data);

		headerLabel = new Label(parent, SWT.NONE);
		headerLabel.setText("Header");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.width = 68;
		data.top = new FormAttachment(genHeaderButton, 5);
		headerLabel.setLayoutData(data);

		headerText = new Text(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		data = new FormData();
		data.left = new FormAttachment(headerLabel, 15);
		data.width = 250;
		data.height = 100;
		data.top = new FormAttachment(genHeaderButton, 5);
		headerText.setLayoutData(data);

		genHeaderButton.addMouseListener(new CheckboxListener(genHeaderButton,
				headerText));

		// Generate accessors
		genAccessorsButton = new Button(parent, SWT.CHECK);
		genAccessorsButton.setText("Generate Accessors");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(headerText, 10);
		genAccessorsButton.setLayoutData(data);

		// Generate array methods
		genArrayMethodsButton = new Button(parent, SWT.CHECK);
		genArrayMethodsButton.setText("Generate add/remove item for arrays");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(genAccessorsButton, 5);
		genArrayMethodsButton.setLayoutData(data);

		// Generate main
		genMainButton = new Button(parent, SWT.CHECK);
		genMainButton.setText("Generate <Main> method");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(genArrayMethodsButton, 5);
		genMainButton.setLayoutData(data);

		// Topcased checkstyle
		useTopcasedChkstButton = new Button(parent, SWT.CHECK);
		useTopcasedChkstButton
				.setText("Generate code conform to Topcased coding style");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(genMainButton, 5);
		useTopcasedChkstButton.setLayoutData(data);

		// Stop if warning
		stopIfWarningButton = new Button(parent, SWT.CHECK);
		stopIfWarningButton.setText("Stop if warning");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(useTopcasedChkstButton, 5);
		stopIfWarningButton.setLayoutData(data);

		// Force static
		forceStaticButton = new Button(parent, SWT.CHECK);
		forceStaticButton.setText("Make all static");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -5);
		data.top = new FormAttachment(stopIfWarningButton, 5);
		forceStaticButton.setLayoutData(data);

		/* 2. Set current values */

		IResource model = (IResource) getElement();

		try {
			outputText.setText(CodeGenerationProperties
					.getOutputPathProperty(model));
		} catch (CoreException e) {
			outputText.setText(CodeGenerationProperties
					.getDefaultOutputPathProperty(model));
		}

		try {
			genAuthorButton.setSelection(CodeGenerationProperties
					.getGenAuthorProperty(model));
		} catch (CoreException e) {
			genAuthorButton.setSelection(CodeGenerationProperties
					.getDefaultGenAuthorProperty());
		}

		try {
			authorText.setText(CodeGenerationProperties
					.getAuthorNameProperty(model));
		} catch (CoreException e) {
			authorText.setText(CodeGenerationProperties
					.getDefaultAuthorNameProperty());
		}

		if (genAuthorButton.getSelection()) {
			authorText.setEnabled(true);
		} else {
			authorText.setEnabled(false);
		}

		try {
			genHeaderButton.setSelection(CodeGenerationProperties
					.getGenHeaderProperty(model));
		} catch (CoreException e) {
			genHeaderButton.setSelection(CodeGenerationProperties
					.getDefaultGenHeaderProperty());
		}

		try {
			headerText.setText(CodeGenerationProperties
					.getHeaderProperty(model));
		} catch (CoreException e) {
			headerText.setText(CodeGenerationProperties
					.getDefaultHeaderProperty());
		}

		if (genHeaderButton.getSelection()) {
			headerText.setEnabled(true);
		} else {
			headerText.setEnabled(false);
		}

		try {
			genAccessorsButton.setSelection(CodeGenerationProperties
					.getGenAccessorsProperty(model));
		} catch (CoreException e) {
			genAccessorsButton.setSelection(CodeGenerationProperties
					.getDefaultGenAccessorsProperty());
		}

		try {
			genArrayMethodsButton.setSelection(CodeGenerationProperties
					.getGenArrayMethodsProperty(model));
		} catch (CoreException e) {
			genArrayMethodsButton.setSelection(CodeGenerationProperties
					.getDefaultGenArrayMethodsProperty());
		}

		try {
			genMainButton.setSelection(CodeGenerationProperties
					.getGenMainProperty(model));
		} catch (CoreException e) {
			genMainButton.setSelection(CodeGenerationProperties
					.getDefaultGenMainProperty());
		}

		try {
			useTopcasedChkstButton.setSelection(CodeGenerationProperties
					.getUseTopcasedChkstProperty(model));
		} catch (CoreException e) {
			useTopcasedChkstButton.setSelection(CodeGenerationProperties
					.getDefaultUseTopcasedChkstProperty());
		}

		try {
			stopIfWarningButton.setSelection(CodeGenerationProperties
					.getStopIfWarningProperty(model));
		} catch (CoreException e) {
			stopIfWarningButton.setSelection(CodeGenerationProperties
					.getDefaultStopIfWarningProperty());
		}

		try {
			forceStaticButton.setSelection(CodeGenerationProperties
					.getForceStaticProperty(model));
		} catch (CoreException e) {
			forceStaticButton.setSelection(CodeGenerationProperties
					.getDefaultForceStaticProperty());
		}
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		composite.setLayout(layout);
		FormData data = new FormData();
		composite.setLayoutData(data);

		addFirstSection(composite);

		return composite;
	}

	/**
	 * Initialize every widget to a default value. Event executed when the
	 * Defaults button is pressed.
	 */
	@Override
	protected void performDefaults() {
		genAuthorButton.setSelection(CodeGenerationProperties
				.getDefaultGenAuthorProperty());
		genHeaderButton.setSelection(CodeGenerationProperties
				.getDefaultGenHeaderProperty());
		genAccessorsButton.setSelection(CodeGenerationProperties
				.getDefaultGenAccessorsProperty());
		genArrayMethodsButton.setSelection(CodeGenerationProperties
				.getDefaultGenArrayMethodsProperty());
		genMainButton.setSelection(CodeGenerationProperties
				.getDefaultGenMainProperty());
		useTopcasedChkstButton.setSelection(CodeGenerationProperties
				.getDefaultUseTopcasedChkstProperty());
		stopIfWarningButton.setSelection(CodeGenerationProperties
				.getDefaultStopIfWarningProperty());
		forceStaticButton.setSelection(CodeGenerationProperties
				.getDefaultForceStaticProperty());
		authorText.setText(CodeGenerationProperties
				.getDefaultAuthorNameProperty());
		authorText.setEnabled(genAuthorButton.getSelection());
		headerText.setText(CodeGenerationProperties.getDefaultHeaderProperty());
		headerText.setEnabled(genHeaderButton.getSelection());
		outputText.setText(CodeGenerationProperties
				.getDefaultOutputPathProperty((IResource) getElement()));
	}

	/**
	 * All properties are saved by Eclipse. Event executed when validating the
	 * property page.
	 */
	@Override
	public boolean performOk() {
		// store the value in the owner text field
		try {
			IResource model = (IResource) getElement();
			CodeGenerationProperties.setGenAuthorProperty(model,
					genAuthorButton.getSelection());
			CodeGenerationProperties.setGenHeaderProperty(model,
					genHeaderButton.getSelection());
			CodeGenerationProperties.setGenAccessorsProperty(model,
					genAccessorsButton.getSelection());
			CodeGenerationProperties.setGenArrayMethodsProperty(model,
					genArrayMethodsButton.getSelection());
			CodeGenerationProperties.setGenMainProperty(model,
					genMainButton.getSelection());
			CodeGenerationProperties.setUseTopcasedChkstProperty(model,
					useTopcasedChkstButton.getSelection());
			CodeGenerationProperties.setStopIfWarningProperty(model,
					stopIfWarningButton.getSelection());
			CodeGenerationProperties.setForceStaticProperty(model,
					forceStaticButton.getSelection());
			CodeGenerationProperties.setAuthorNameProperty(model,
					authorText.getText());
			CodeGenerationProperties.setHeaderProperty(model,
					headerText.getText());
			CodeGenerationProperties.setOutputPathProperty(model,
					outputText.getText());
		} catch (CoreException e) {
			return false;
		}

		return true;
	}

	/**
	 * Mouse listener for the "browse" button. Opens an Eclipse dialog for
	 * selecting the output folder.
	 */
	private class BrowseButtonListener implements MouseListener {

		private Text outputText;

		/**
		 * @param outputText
		 */
		public BrowseButtonListener(Text outputText) {
			this.outputText = outputText;
		}

		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent arg0) {
			// do nothing
		}

		public void mouseDown(org.eclipse.swt.events.MouseEvent arg0) {
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(
					getShell(), null, true, "Select output :");
			dialog.open();
			Object[] objects = dialog.getResult();
			if (objects != null && objects.length == 1) {
				if (objects[0] instanceof IPath) {
					IPath p = (IPath) objects[0];
					if (p.segmentCount() > 0) {
						String s = p.toPortableString();
						outputText.setText(s);
					}
				}
			}
		}

		public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {
			// do nothing
		}

	}

	/**
	 * Mouse listener for a check box button linked to a text field (author,
	 * header). Enables/Disables the linked text field.
	 */
	private class CheckboxListener implements MouseListener {

		private Button checkbox;

		private Text linkedText;

		/**
		 * @param checkbox
		 * @param linkedText
		 */
		public CheckboxListener(Button checkbox, Text linkedText) {
			this.checkbox = checkbox;
			this.linkedText = linkedText;
		}

		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent arg0) {
			// do nothing
		}

		public void mouseDown(org.eclipse.swt.events.MouseEvent arg0) {
			// do nothing
		}

		public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {
			if (checkbox.getSelection()) {
				linkedText.setEnabled(true);
			} else {
				linkedText.setEnabled(false);
			}
		}

	}

}