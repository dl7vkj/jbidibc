/**
 * 
 */
package org.bidib.jbidibc.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The <code>LibraryPathManipulator</code> is used to manipulate the <code>java.library.path</code> after the JVM was started.
 * The solution is based on this blog: http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
 * </p>
 * 
 * The absolute path to location of the RXTX native libraries can be specified with the JVM property <code>jbidibc.path_to_RXTX_libs</code>.
 * <p>E.g. <code>-Djbidibc.path_to_RXTX_libs="E:/svn/jbidibc/jbidibc/jbidibc"</code> if the native libraries 
 * are located at <i>E:/svn/jbidibc/jbidibc/jbidibc/rxtx</i></p>   
 */
public class LibraryPathManipulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryPathManipulator.class);

    /**
     * Absolute path to location of the RXTX native libraries. 
     * This JVM property can be used in development environment to specify the path to the native libraries
     * if the jbidib project is not opened.
     * <p>E.g. <code>-Djbidibc.path_to_RXTX_libs="E:/svn/jbidibc/jbidibc/jbidibc"</code> if the native libraries 
     * are located at <i>E:/svn/jbidibc/jbidibc/jbidibc/rxtx</i></p>   
     */
    public static final String PROP_PATH_TO_RXTX_LIBS = "jbidibc.path_to_RXTX_libs";

    public enum Platform {
        Windows, Mac, Unix, Solaris, unsupported
    }

    /**
     * Manipulate the library path to include the path to the DLLs and SOs.
     * @param pathToDLLs the DLLs and SOs are expected to be in a directory rxtx with structure below. If this structure 
     * is in a sub directory of <code>user.dir</code> this parameter is used to extend the path to find the matching 
     * structure.   
     * <pre>
     * -- rxtx
     *    +-- windows
     *       +-- x86
     *       +-- x86_64
     *    +-- linux
     *       +-- i386
     *       +-- x86_64
     *       +-- amd64
     *       +-- ia64
     *    +-- mac
     * </pre>
     */
    public void manipulateLibraryPath(String pathToDLLs) {

        try {
            // get the classpath
            String cp = System.getProperty("java.class.path");
            LOGGER.debug("Classpath property is: {}", cp);

            File basePath = new File(System.getProperty("user.dir"));
            URL url = null;
            if (cp.indexOf("target\\classes") > -1 || cp.indexOf("target/classes") > -1) {
                // development env detected
                url = LibraryPathManipulator.class.getResource(".");

                if (url == null && System.getProperty(PROP_PATH_TO_RXTX_LIBS) != null) {
                    // search for RXTX libs at provided location
                    url = new URL("file", "localhost", System.getProperty(PROP_PATH_TO_RXTX_LIBS));
                }
                basePath = new File(prepareLibraryPath(url, basePath), "/target/test-classes");
            }
            else if (System.getProperty(PROP_PATH_TO_RXTX_LIBS) != null) {
                // search for RXTX libs at provided location
                url = new URL("file", "localhost", System.getProperty(PROP_PATH_TO_RXTX_LIBS));
                basePath = prepareLibraryPath(url, basePath);
            }

            if (pathToDLLs != null && pathToDLLs.trim().length() > 0) {
                // append the path to dlls 
                basePath = new File(basePath, pathToDLLs.trim());
            }

            // make the path dynamic on OS ...
            basePath = new File(basePath, resolveOSDependentPath());

            String libraryPath = basePath.toString();

            if (basePath.isDirectory()) {
                LOGGER.debug("Adding java.library.path: {}", libraryPath);

                String originalLibraryPath = System.getProperty("java.library.path");
                if (originalLibraryPath != null && originalLibraryPath.trim().length() > 0) {
                    LOGGER.debug("Adding original library path: {}", originalLibraryPath);
                    libraryPath += File.pathSeparatorChar + originalLibraryPath;
                }
                // adding native libs
                System.setProperty("java.library.path", libraryPath);

                Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
                fieldSysPath.setAccessible(true);
                fieldSysPath.set(null, null);
            }
            else {
                LOGGER.error("Skipped adding java.library.path because the directory does not exist: {}", libraryPath);
            }
        }
        catch (Exception ex) {
            LOGGER.warn("Manipulate the library path for RXTX accessfailed.");
            throw new RuntimeException("Manipulate library path for RXTX access failed.", ex);
        }
    }

    private File prepareLibraryPath(URL url, File basePath) {
        File result = basePath;

        if (url != null) {
            LOGGER.debug("Fetched url: {}, protocol: {}", url.getPath(), url.getProtocol());
            if ("file".equals(url.getProtocol())) {
                // file:/E:/svn/jbidibc/jbidibc/jbidibc/target/classes/org/bidib/jbidibc/utils/
                String filePath = url.getPath();
                try {
                    result = new File(filePath.substring(0, filePath.indexOf("target/classes")));
                }
                catch (IndexOutOfBoundsException ioob) {
                    LOGGER.debug("Use full filePath to append.");
                    result = new File(filePath);
                }
            }
        }
        else {
            LOGGER
                .warn("Development environment detected but class not available in flat filesystem (e.g. jbidib project not open). "
                    + "Make sure the RXTX libs are located under target/test-classes in your project!");
        }
        return result;
    }

    private String resolveOSDependentPath() {

        String osDependentPathToLibrary = null;
        switch (getOsName()) {
            case Windows:
                if ("32".equals(getDataModel())) {
                    osDependentPathToLibrary = "/rxtx/windows/x86";
                }
                else {
                    osDependentPathToLibrary = "/rxtx/windows/x86_64";
                }
                break;
            case Unix:
                if ("32".equals(getDataModel())) {
                    osDependentPathToLibrary = "/rxtx/linux/i386";
                }
                else {
                    osDependentPathToLibrary = "/rxtx/linux/x86_64";
                }
                break;
            case Solaris:
                // TODO for unix 
                LOGGER
                    .error("The SOLARIS OS was detected but I don't know how to distinguish between 64 and 32 bit version ...");
                break;
            case Mac:
                osDependentPathToLibrary = "/rxtx/mac";
                break;
            default:
                LOGGER.error("Unsupported OS detected!");
                break;
        }

        return osDependentPathToLibrary;
    }

    private Platform getOsName() {

        String osName = System.getProperty("os.name").toLowerCase();

        Platform os = Platform.unsupported;
        if (osName.indexOf("win") > -1)
            os = Platform.Windows; // Windows
        if (osName.indexOf("mac") > -1)
            os = Platform.Mac; // Mac
        if (osName.indexOf("nux") > -1)
            os = Platform.Unix; // Linux
        if (osName.indexOf("nix") > -1)
            os = Platform.Unix; // Unix
        if (osName.indexOf("sunos") > -1)
            os = Platform.Solaris; // Solaris

        return os;
    }

    public String getDataModel() {

        String dataModel = System.getProperty("sun.arch.data.model").toLowerCase();

        return dataModel;
    }
}
