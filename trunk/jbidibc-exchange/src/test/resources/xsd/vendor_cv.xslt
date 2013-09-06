<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:vendorcv="http://www.bidib.org/vendorcv" >
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
	<xsl:template match="vendorcv:LEDS">
		<LEDS>
		<xsl:for-each select="*">
			<!--b><xsl:value-of select="current()/local-name()" /></b-->
			<LED><xsl:attribute name="number"><xsl:value-of select="substring(current()/local-name(),4)" /></xsl:attribute>
				<xsl:copy-of select="./*" />
			</LED>
		</xsl:for-each>
		</LEDS>
	</xsl:template>
</xsl:stylesheet>
