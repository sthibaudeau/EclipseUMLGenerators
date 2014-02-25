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

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.umlgen.reverse.c.internal.bundle.Activator;
import org.eclipse.umlgen.reverse.c.resource.ModelManager;
import org.eclipse.umlgen.reverse.c.util.ModelUtil;

/**
 * Event related to changes performed on a function body.
 * 
 * @author <a href="mailto:sebastien.gabel@c-s.fr">Sebastien GABEL</a>
 * @author <a href="mailto:christophe.le-camus@c-s.fr">Christophe LE CAMUS</a>
 * @since 4.0.0
 */
public class FunctionBodyChanged extends FunctionBodyEvent {
	/**
	 * @see org.eclipse.umlgen.reverse.c.CModelChangedEvent#notifyChanges(org.eclipse.umlgen.reverse.c.resource.ModelManager)
	 */
	@Override
	public void notifyChanges(ModelManager manager) {
		Class myClass = ModelUtil.findClassInPackage(
				manager.getSourcePackage(), getUnitName());
		if (myClass != null) {
			// if a behavior already exists we link the operation to the
			// behavior
			OpaqueBehavior function = (OpaqueBehavior) myClass
					.getOwnedBehavior(getCurrentName(), false,
							UMLPackage.Literals.OPAQUE_BEHAVIOR, false);
			if (function != null) {
				try {
					String oldBody = getOldBody();
					String newBoby = cleanInvalidXmlChars(getBody());
					if (!oldBody.equals(newBoby)) {
						// FIXME MIGRATION reference to org.topcased.facilities
						// EMFMarkerUtil.removeMarkerFor(function);

						// removes the former function body
						function.getBodies().remove(oldBody);
						// sets the new function body
						function.getBodies().add(newBoby);

						// FIXME MIGRATION reference to org.topcased.facilities
						// EMFMarkerUtil.addMarkerFor(function,
						// "Function behavior body has changed. Existing Activity diagrams need to be reversed.",
						// IMarker.SEVERITY_WARNING);
					}
				} catch (Exception e) {
					Activator.log(e);
				}
			}
		}
	}

	/**
	 * Gets the right builder
	 * 
	 * @return the builder for this event
	 */
	public static Builder<FunctionBodyChanged> builder() {
		return new Builder<FunctionBodyChanged>() {
			private FunctionBodyChanged event = new FunctionBodyChanged();

			/**
			 * @see org.eclipse.umlgen.reverse.c.FunctionBodyBuilder#getEvent()
			 */
			@Override
			protected FunctionBodyChanged getEvent() {
				return event;
			}
		};
	}
}
