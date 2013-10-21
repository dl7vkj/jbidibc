<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:vendorcv="http://www.bidib.org/jbidibc/vendorcv" exclude-result-prefixes="#default" xmlns="http://www.bidib.org/jbidibc/vendorcv">
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		exclude-result-prefixes="xsl xs" indent="yes" />

	<xsl:strip-space elements="*" />

<!--
	<xsl:template match="*" priority="1">      
		<xsl:element name="{lower-case(local-name())}">
			<xsl:apply-templates/>
		</xsl:element>
	  </xsl:template>
 	<xsl:template match="@*" priority="15">
        <xsl:attribute name="{lower-case(local-name(.))}" >
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>
 -->
    
	<!-- string for default namespace uri and schema location -->
	<xsl:variable name="nsVendorCV" select="'http://www.bidib.org/jbidibc/vendorcv'" />
	<xsl:variable name="schemaLoc"
		select="'http://www.bidib.org/jbidibc/vendorcv /xsd/vendor_cv.xsd'" />


	<xsl:template match="@*|node()">
		<xsl:copy inherit-namespaces="yes">
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/*" priority="2">
		<xsl:element name="{local-name()}" namespace="{$nsVendorCV}">
			<xsl:attribute name="xsi:schemaLocation"
				namespace="http://www.w3.org/2001/XMLSchema-instance">
				<xsl:value-of select="$schemaLoc" />
			</xsl:attribute>
			<xsl:namespace name="xsi"
				select="'http://www.w3.org/2001/XMLSchema-instance'" />
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>

	<!--template for elements without a namespace -->
	<xsl:template match="*[namespace-uri() = '']">
		<xsl:element name="{local-name()}" namespace="{$nsVendorCV}">
			<xsl:apply-templates select="@* | node()" />
		</xsl:element>
	</xsl:template>

	<!-- 	 
	<xsl:template match="Templates/*" priority="10">
		<xsl:element name="Template" namespace="{$nsVendorCV}"
			exclude-result-prefixes="#default">
			<xsl:apply-templates select="@* | node()" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="CVDefinition" priority="9">
		<xsl:element name="CVDefinition" namespace="{$nsVendorCV}"
			exclude-result-prefixes="#default">
			<xsl:for-each select="*/.[not(starts-with(name(), 'Sector'))]">
				<xsl:element name="{name()}" namespace="{$nsVendorCV}"
					exclude-result-prefixes="#default">
						<xsl:apply-templates select="@* | node()" />
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	 -->
	
</xsl:stylesheet>
