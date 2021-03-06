/*******************************************************************************
 * Copyright (c) 2010 Communication & Systems.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Christophe Le Camus (CS) - initial API and implementation 
 *     Sebastien Gabel (CS) - evolutions
 *******************************************************************************/
package org.eclipse.umlgen.reverse.c.event;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.umlgen.reverse.c.resource.ModelManager;
import org.eclipse.umlgen.reverse.c.util.AnnotationUtil;
import org.eclipse.umlgen.reverse.c.util.DiagramUtil;
import org.eclipse.umlgen.reverse.c.util.ModelUtil;
import org.eclipse.umlgen.reverse.c.util.ModelUtil.EventType;

public class CUnitRemoved extends CUnitEvent {

	/**
	 * @see org.eclipse.umlgen.reverse.c.event.CModelChangedEvent#notifyChanges(org.eclipse.umlgen.reverse.c.resource.ModelManager)
	 */
	@Override
	public void notifyChanges(ModelManager manager) {
		Classifier matchingClassifier = ModelUtil.findMatchingClassifier(
				manager, getTranslationUnit(), getCurrentName()
						.removeFileExtension().toString());
		if (matchingClassifier != null) {

			ModelUtil.setVisibility(matchingClassifier, getTranslationUnit(),
					EventType.REMOVE);
			AnnotationUtil.removeEAnnotations(matchingClassifier,
					getTranslationUnit());

			/*
			 * check if the class must be destroyed : if we delete only one file
			 * .c or .h it is not the case
			 */
			if (getTranslationUnit().isHeaderUnit()) {
				// delete all private model objects from this class
				ModelUtil.deleteAllVisibleObjects(matchingClassifier,
						VisibilityKind.PUBLIC_LITERAL, manager);
			} else if (getTranslationUnit().isSourceUnit()) {
				// delete all public model objects from this class
				ModelUtil.deleteAllVisibleObjects(matchingClassifier,
						VisibilityKind.PRIVATE_LITERAL, manager);
			}

			/* only if no details persists then we can delete the class */
			if (ModelUtil.isRemovable(matchingClassifier)) {
				DiagramUtil.removeGraphicalRepresentation(matchingClassifier,
						manager);
				matchingClassifier.destroy();
			}
		}
	}

	/**
	 * Gets the right builder
	 * 
	 * @return the builder for this event
	 */
	public static Builder<CUnitRemoved> builder() {
		return new Builder<CUnitRemoved>() {
			private CUnitRemoved event = new CUnitRemoved();

			/**
			 * @see org.eclipse.umlgen.reverse.c.event.CUnitEvent.Builder#getEvent()
			 */
			@Override
			protected CUnitRemoved getEvent() {
				return event;
			}
		};
	}
}
