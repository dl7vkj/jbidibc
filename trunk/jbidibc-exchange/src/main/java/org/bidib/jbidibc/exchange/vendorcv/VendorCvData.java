package org.bidib.jbidibc.exchange.vendorcv;

public class VendorCvData {

    private final VendorCV vendorCV;

    private final String filename;

    public VendorCvData(final VendorCV vendorCV, final String filename) {
        this.vendorCV = vendorCV;
        this.filename = filename;
    }

    /**
     * @return the vendorCV
     */
    public VendorCV getVendorCV() {
        return vendorCV;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
}
