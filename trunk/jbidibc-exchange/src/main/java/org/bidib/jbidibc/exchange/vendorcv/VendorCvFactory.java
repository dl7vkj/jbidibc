package org.bidib.jbidibc.exchange.vendorcv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.bidib.jbidibc.core.Node;
import org.bidib.jbidibc.core.SoftwareVersion;
import org.bidib.jbidibc.core.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class VendorCvFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorCvFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.exchange.vendorcv";

    public static final String XSD_LOCATION = "/xsd/vendor_cv.xsd";

    public static VendorCV getCvDefinition(Node node, String... searchPaths) {
        LOGGER.info("Load the vendor cv definition for node: {}", node);

        // TODO check the firmware version of the node and verify the correct definition is
        // loaded
        SoftwareVersion softwareVersion = node.getSoftwareVersion();

        return new VendorCvFactory().loadCvDefintionForNode(node.getUniqueId(), softwareVersion, searchPaths);
    }

    private VendorCV loadCvDefintionForNode(long uniqueId, SoftwareVersion softwareVersion, String... searchPaths) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}, software version: {}",
            NodeUtils.getUniqueIdAsString(uniqueId), pid, vid, softwareVersion);

        VendorCV vendorCV = null;
        for (String searchPath : searchPaths) {
            StringBuffer filename = new StringBuffer("BiDiBCV-");
            filename.append(vid).append("-").append(pid).append(".xml");

            LOGGER.info("Prepared filename to load vendorCv: {}", filename.toString());
            if (searchPath.startsWith("classpath:")) {
                int beginIndex = "classpath:".length();
                String lookup = searchPath.substring(beginIndex) + "/" + filename.toString();
                LOGGER.info("Lookup vendorCv file internally: {}", lookup);

                final StringBuffer filenameSearch = new StringBuffer("*\\BiDiBCV-");
                filenameSearch.append(vid).append("-").append(pid).append("*").append(".xml");

                final List<Path> files = new LinkedList<>();

                URL pathString = VendorCvFactory.class.getResource(lookup);
                try {
                    Path path = Paths.get(pathString.toURI());

                    Files.walkFileTree(path.getParent(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            LOGGER.trace("Current file: {}", file);

                            if (FilenameUtils.wildcardMatch(file.toString(), filenameSearch.toString(),
                                IOCase.INSENSITIVE)) {
                                LOGGER.info("Add matching file: {}", file);
                                files.add(file);
                            }

                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                            if (e == null) {
                                LOGGER.trace("Current directory: {}", dir);
                                return FileVisitResult.CONTINUE;
                            }
                            else {
                                // directory iteration failed
                                throw e;
                            }
                        }
                    });
                }
                catch (Exception e) {
                    LOGGER.warn("Convert uri to path failed.", e);
                }

                LOGGER.info("Found matching files: {}", files);
                if (CollectionUtils.isNotEmpty(files)) {
                    List<File> fileCollection = new LinkedList<>();
                    for (Path path : files) {

                        fileCollection.add(path.toFile());

                    }

                    File vendorCvFile = findMatchingVendorCV(fileCollection, filename.toString(), softwareVersion);
                    if (vendorCvFile != null && vendorCvFile.exists()) {
                        String lookupPath = vendorCvFile.getName();

                        lookup = searchPath.substring(beginIndex) + "/" + lookupPath.toString();
                        // LOGGER.info("Lookup vendorCv file internally: {}", lookup);
                        LOGGER.info("Prepared vendor CV lookup: {}", lookup);
                    }
                }

                InputStream is = VendorCvFactory.class.getResourceAsStream(lookup);
                if (is != null) {
                    vendorCV = loadVendorCvFile(is);
                    if (vendorCV != null) {
                        break;
                    }
                }
                else {
                    LOGGER.warn("Internal lookup for products file failed.");
                }
            }
            else {
                LOGGER.info("Search for files in searchPath: {}", searchPath);
                File productsFile = new File(searchPath, filename.toString());
                File searchDirectory = productsFile.getParentFile();
                if (searchDirectory.exists()) {
                    // use filename with wildcards
                    StringBuffer filenameSearch = new StringBuffer("BiDiBCV-");
                    filenameSearch.append(vid).append("-").append(pid).append("*").append(".xml");
                    IOFileFilter fileFilter = new WildcardFileFilter(filenameSearch.toString(), IOCase.INSENSITIVE);
                    Collection<File> files = FileUtils.listFiles(searchDirectory, fileFilter, TrueFileFilter.INSTANCE);

                    LOGGER.info("Found matching files: {}", files);

                    productsFile = findMatchingVendorCV(files, filename.toString(), softwareVersion);

                    LOGGER.info("Use productsFile: {}", productsFile);
                }
                else {
                    LOGGER.info("The directory to search does not exist: {}", searchDirectory.toString());
                }

                // File productsFile = new File(searchPath, filename.toString());
                if (productsFile.exists()) {
                    LOGGER.info("Found product file: {}", productsFile.getAbsolutePath());
                    // try to load products
                    vendorCV = loadVendorCvFile(productsFile);
                    if (vendorCV != null) {
                        break;
                    }
                }
                else {
                    LOGGER.info("File does not exist: {}", productsFile.getAbsolutePath());
                }
            }
        }
        LOGGER.trace("Loaded vendorCV: {}", vendorCV);
        return vendorCV;
    }

    private File findMatchingVendorCV(Collection<File> files, String defaultFilename, SoftwareVersion softwareVersion) {
        if (CollectionUtils.isNotEmpty(files)) {
            LOGGER.info("Search for matching CV definition for software version: {}", softwareVersion);

            File defaultCvDefinition = null;
            File cvDefinition = null;

            for (File file : files) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                LOGGER.info("Check if filename is matching: {}", fileName);

                int index = fileName.lastIndexOf("-");
                if (index > -1 && index < fileName.length()) {
                    String lastPart = fileName.substring(index + 1);
                    // scan version
                    if (lastPart.matches("^\\d(\\.\\d)+")) {
                        String[] splited = lastPart.split("\\.");

                        LOGGER.info("Found version schema: {}", (Object[]) splited);

                        int majorVersion = Integer.parseInt(splited[0]);
                        int minorVersion = Integer.parseInt(splited[1]);
                        int microVersion = 0;
                        if (splited.length > 2) {
                            microVersion = Integer.parseInt(splited[2]);
                        }

                        LOGGER.info("Found version, major: {}, minor: {}, micro: {}", majorVersion, minorVersion,
                            microVersion);

                        SoftwareVersion current = new SoftwareVersion(majorVersion, minorVersion, microVersion);
                        if (softwareVersion.compareTo(current) == 0) {
                            LOGGER.info("Found excactly matching version in file: {}", file);

                            cvDefinition = file;
                            break;
                        }
                    }
                    else {
                        LOGGER.info("Last part does not match version schema: {}", lastPart);
                    }
                }

                if (file.getName().equalsIgnoreCase(defaultFilename)) {
                    LOGGER.info("Found the default CV definition: {}", file);
                    defaultCvDefinition = file;
                }
            }

            if (cvDefinition == null) {
                LOGGER.info("Use defaultCvDefinition: {}", defaultCvDefinition);
                cvDefinition = defaultCvDefinition;
            }

            return cvDefinition;
        }

        return null;
    }

    private VendorCV loadVendorCvFile(File vendorCvFile) {
        VendorCV vendorCV = null;
        InputStream is;
        try {
            is = new FileInputStream(vendorCvFile);
            vendorCV = loadVendorCvFile(is);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No vendorCV file found.");
        }
        return vendorCV;
    }

    private VendorCV loadVendorCvFile(InputStream is) {

        VendorCV vendorCV = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(VendorCvFactory.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            vendorCV = (VendorCV) unmarshaller.unmarshal(is);
        }
        catch (JAXBException | SAXException ex) {
            LOGGER.warn("Load VendorCV from file failed.", ex);
        }
        return vendorCV;
    }

    public static Integer getNumberOfPorts(String portType, Node node, String... searchPaths) {
        LOGGER.info("Load number of configured port from the vendor cv definition for node: {}", node);

        return new VendorCvFactory().getNumberOfPorts(portType, node.getUniqueId(), searchPaths);
    }

    private Integer getNumberOfPorts(String portType, long uniqueId, String... searchPaths) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}",
            NodeUtils.getUniqueIdAsString(uniqueId), pid, vid);

        // VendorCV vendorCV = null;
        Integer numberOfPorts = null;
        for (String searchPath : searchPaths) {
            StringBuffer filename = new StringBuffer("BiDiBCV-");
            filename.append(vid).append("-").append(pid).append(".xml");

            LOGGER.info("Prepared filename to load vendorCv: {}", filename.toString());
            if (searchPath.startsWith("classpath:")) {
                int beginIndex = "classpath:".length();
                String lookup = searchPath.substring(beginIndex) + "/" + filename.toString();
                LOGGER.info("Lookup vendorCv file internally: {}", lookup);
                InputStream is = VendorCvFactory.class.getResourceAsStream(lookup);
                if (is != null) {
                    numberOfPorts = getNumberOfPorts(is, portType);
                    if (numberOfPorts != null) {
                        break;
                    }
                }
                else {
                    LOGGER.warn("Internal lookup for products file failed.");
                }
            }
            else {
                File productsFile = new File(searchPath, filename.toString());
                if (productsFile.exists()) {
                    LOGGER.info("Found product file: {}", productsFile.getAbsolutePath());
                    // try to load products
                    numberOfPorts = getNumberOfPorts(productsFile, portType);
                    if (numberOfPorts != null) {
                        break;
                    }
                }
                else {
                    LOGGER.info("File does not exist: {}", productsFile.getAbsolutePath());
                }
            }
        }
        LOGGER.trace("Loaded numberOfPorts: {}", numberOfPorts);
        return numberOfPorts;
    }

    public Integer getNumberOfPorts(InputStream is, String portType) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document xmlDocument = null;
        try {
            builder = builderFactory.newDocumentBuilder();

            xmlDocument = builder.parse(is);
        }
        catch (Exception e) {
            LOGGER.warn("Parse document failed.", e);
        }

        Integer numberOfPorts = null;
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "VendorCV/CVDefinition/Node/Node[@Template='" + portType + "']/@Count";
        try {
            String configuredPorts = xPath.compile(expression).evaluate(xmlDocument/* , XPathConstants.NODE */);
            LOGGER.info("Found requested node: {}", configuredPorts);
            numberOfPorts = Integer.valueOf(configuredPorts);
        }
        catch (XPathExpressionException e) {
            LOGGER.warn("Get number of port failed.", e);
        }

        return numberOfPorts;
    }

    public Integer getNumberOfPorts(File vendorCvFile, String portType) {
        Integer numberOfPorts = null;
        InputStream is;
        try {
            is = new FileInputStream(vendorCvFile);
            numberOfPorts = getNumberOfPorts(is, portType);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No vendorCV file found.");
        }
        return numberOfPorts;
    }
}
