package io.vertigo.quarto.plugins.converter.openoffice;

import io.vertigo.dynamo.file.FileManager;
import io.vertigo.kernel.lang.Assertion;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.ucb.XFileIdentifierConverter;
import com.sun.star.uno.UnoRuntime;

/**
 * Conversion des fichiers � partir de OpenOffice.
 * @author npiedeloup
 * @version $Id: OpenOfficeLocalConverterPlugin.java,v 1.4 2014/01/28 18:49:24 pchretien Exp $
 */
public final class OpenOfficeLocalConverterPlugin extends AbstractOpenOfficeConverterPlugin {
	private static final Logger LOGGER = Logger.getLogger(OpenOfficeLocalConverterPlugin.class);

	/**
	 * Constructeur.
	 * @param fileManager Manager de gestion des fichiers
	 * @param unoPort Port de connexion au serveur OpenOffice
	 */
	@Inject
	public OpenOfficeLocalConverterPlugin(final FileManager fileManager, @Named("unoport") final String unoPort) {
		super(fileManager, "localhost", unoPort);

	}

	/** {@inheritDoc} */
	@Override
	protected void storeDocument(final File outputFile, final XComponent xDoc, final ConverterFormat targetFormat, final OpenOfficeConnection openOfficeConnection)
			throws Exception {
		final XFileIdentifierConverter fileContentProvider = openOfficeConnection.getFileContentProvider();
		final String outputUrl = fileContentProvider.getFileURLFromSystemPath("", outputFile.getAbsolutePath());
		final PropertyValue[] storeProps = getFileProperties(targetFormat);
		final XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, xDoc);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Storing to " + outputUrl);
		}
		xStorable.storeToURL(outputUrl, storeProps);
	}

	/** {@inheritDoc} */
	@Override
	protected XComponent loadDocument(final File inputFile, final OpenOfficeConnection openOfficeConnection) throws Exception {
		final XFileIdentifierConverter fileContentProvider = openOfficeConnection.getFileContentProvider();
		final String inputUrl = fileContentProvider.getFileURLFromSystemPath("", inputFile.getAbsolutePath());

		final String inputExtensionStr = inputUrl.substring(inputUrl.lastIndexOf('.') + 1).toUpperCase();
		final ConverterFormat inputDocType = ConverterFormat.valueOf(inputExtensionStr);
		final PropertyValue[] loadProps = getFileProperties(inputDocType);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Openning document... " + inputUrl);
		}
		final XComponent xDoc = openOfficeConnection.getDesktop().loadComponentFromURL(inputUrl, "_blank", 0, loadProps);
		Assertion.checkNotNull(xDoc, "Le document n''a pas �t� charg� : {0}", inputUrl);

		return xDoc;
	}
}