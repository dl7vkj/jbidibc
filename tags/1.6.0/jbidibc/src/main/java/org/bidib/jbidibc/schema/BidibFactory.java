package org.bidib.jbidibc.schema;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.bidib.jbidibc.schema.bidib.BiDiB;
import org.bidib.jbidibc.schema.bidib.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidibFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.schema.bidib";

    protected BidibFactory() {
    }

    public static List<MessageType> getMessageTypes() {

        return new BidibFactory().loadMessageTypes();
    }

    protected List<MessageType> loadMessageTypes() {
        String fileName = "/xml/protocol/Protocol.bidib";

        InputStream is = BidibFactory.class.getResourceAsStream(fileName);
        if (is != null) {
            BiDiB bidib = loadBiDiBFile(is);
            if (bidib != null) {
                try {
                    List<MessageType> messageTypes = bidib.getProtocol().getMessageTypes().getMessageType();
                    return messageTypes;
                }
                catch (Exception ex) {
                    LOGGER.warn("Get the message types failed.", ex);
                }
            }
        }
        else {
            LOGGER.warn("Load protocol file failed.");
        }
        return null;
    }

    private BiDiB loadBiDiBFile(InputStream is) {

        BiDiB bidib = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            XMLInputFactory factory = XMLInputFactory.newInstance();

            XMLStreamReader xmlr = factory.createXMLStreamReader(is);

            JAXBElement<BiDiB> jaxbElement = (JAXBElement<BiDiB>) unmarshaller.unmarshal(xmlr, BiDiB.class);
            bidib = jaxbElement.getValue();
        }
        catch (JAXBException | XMLStreamException ex) {
            LOGGER.warn("Load content from file failed.", ex);
        }
        return bidib;
    }
}
