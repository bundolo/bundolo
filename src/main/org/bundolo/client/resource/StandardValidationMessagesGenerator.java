package org.bundolo.client.resource;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bundolo.shared.Utils;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class StandardValidationMessagesGenerator extends Generator {
	
	private static Logger logger = Logger.getLogger(StandardValidationMessagesGenerator.class.getName());

	@Override
	public String generate(TreeLogger treeLogger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		JClassType classType;

		try {
			classType = context.getTypeOracle().getType(typeName);
			SourceWriter src = getSourceWriter(classType, context, treeLogger);
			if (src != null) {
				JMethod[] methods = classType.getMethods();
				if (methods != null && methods.length > 0) {
					for (JMethod method : methods) {
						if (method != null) {
							String parametersString = "";
							src.println(method.getReadableDeclaration() + "{");
							JParameter[] parameters = method.getParameters();
							if (parameters != null && parameters.length > 0) {
								for (JParameter parameter : parameters) {
									parametersString += ", " + parameter.getName(); 
								}
							}
							if (!Utils.hasText(parametersString)) {
								parametersString = ", null";
							}
							src.println("return LocalStorage.getInstance().getMessageResource().getLabel(LabelType." + method.getName() + parametersString + ");");
							src.println("}");
						}
					}
				}
				src.commit(treeLogger);
//				logger.log(Level.SEVERE, "Generated: " + src);
			}
//			logger.log(Level.SEVERE, "Generating for: " + typeName);			
			return typeName + "Generated";

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SourceWriter getSourceWriter(JClassType classType, GeneratorContext context, TreeLogger treeLogger) {
//		logger.log(Level.SEVERE, "getSourceWriter: " + context);
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(packageName, simpleName);
		composer.setSuperclass(classType.getName());

		composer.addImport("eu.maydu.gwt.validation.client.i18n.StandardValidationMessages");
		composer.addImport("org.bundolo.client.LocalStorage");
		composer.addImport("org.bundolo.shared.model.enumeration.LabelType");
		composer.addImport("org.bundolo.client.resource.StandardValidationMessagesImpl");

//		logger.log(Level.SEVERE, "treeLogger: " + treeLogger);
//		logger.log(Level.SEVERE, "packageName: " + packageName);
//		logger.log(Level.SEVERE, "simpleName: " + simpleName);
		PrintWriter printWriter = context.tryCreate(treeLogger, packageName, simpleName);
		if (printWriter == null) {
//			logger.log(Level.SEVERE, "printWriter: " + null);
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
//			logger.log(Level.SEVERE, "sw: " + sw);
			return sw;
		}
	}

}
