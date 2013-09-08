<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:vendorcv="http://www.bidib.org/vendorcv" exclude-result-prefixes="#default"  >
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:strip-space elements="*"/>
	
	<!-- string for default namespace uri and schema location -->
	<xsl:variable name="nsVendorCV" select="'http://www.bidib.org/vendorcv'"/>
    <xsl:variable name="schemaLoc" select="'http://www.bidib.org/vendorcv vendor_cv.xsd'"/>
	
	<xsl:template match="@*|node()">
        <xsl:copy inherit-namespaces="yes">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/*" priority="1">
        <xsl:element name="{local-name()}" namespace="{$nsVendorCV}">
			<xsl:attribute name="xsi:schemaLocation"
				namespace="http://www.w3.org/2001/XMLSchema-instance">
				<xsl:value-of select="$schemaLoc"/>
			</xsl:attribute>
            <xsl:namespace name="xsi" select="'http://www.w3.org/2001/XMLSchema-instance'"/>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <!--template for elements without a namespace -->
  <xsl:template match="*[namespace-uri() = '']">
    <xsl:element name="{local-name()}" namespace="{$nsVendorCV}">
      <xsl:apply-templates select="@* | node()"/>
    </xsl:element>
  </xsl:template>
	
	<!--
    <xsl:template match="*" priority="2">
        <xsl:element name="{local-name()}" namespace="{$nsVendorCV}" exclude-result-prefixes="#default">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    -->
    
	<xsl:template match="LEDS" priority="10">
		<xsl:element name="{name()}" namespace="{$nsVendorCV}" exclude-result-prefixes="#default">
		<xsl:for-each select="*">
			<xsl:element name="LedDefinition" namespace="{$nsVendorCV}" exclude-result-prefixes="#default">
			<xsl:attribute name="number"><xsl:value-of select="substring(current()/local-name(),4)" /></xsl:attribute>
				<xsl:apply-templates select="./*" />
			</xsl:element>
		</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<xsl:template match="Servos" priority="10">
		<xsl:element name="{name()}" namespace="{$nsVendorCV}" exclude-result-prefixes="#default">
		<xsl:for-each select="*">
			<!--b><xsl:value-of select="current()/local-name()" /></b-->
			<xsl:element name="ServoDefinition" namespace="{$nsVendorCV}" exclude-result-prefixes="#default">
			<xsl:attribute name="number"><xsl:value-of select="substring(current()/local-name(),6)" /></xsl:attribute>
				<xsl:apply-templates select="./*" />
			</xsl:element>
		</xsl:for-each>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
