<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
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
		select="'http://www.bidib.org/jbidibc/vendorcv ./../xsd/vendor_cv.xsd'" />


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

	<!-- support format of monitor-0.4.2.3 -->
	<!--
	<xsl:template match="CV[not(@number)]" priority="10">
		<xsl:element name="{name()}" namespace="{$nsVendorCV}"
			exclude-result-prefixes="#default">
			<xsl:for-each select="./*">
				<xsl:attribute name="{local-name()}" >
					<xsl:value-of
						select="node()" />
				</xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>
	-->
	<!--
	<xsl:template match="Version" priority="10">
		<xsl:element name="{name()}" namespace="{$nsVendorCV}"
			exclude-result-prefixes="#default">
			<xsl:for-each select="@*">
					<xsl:attribute name="{lower-case(local-name())}"><xsl:value-of
						select="." /></xsl:attribute>
					<xsl:apply-templates select="./*" />
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	-->
	<xsl:template match="LEDS" priority="10">
		<xsl:element name="{name()}" namespace="{$nsVendorCV}"
			exclude-result-prefixes="#default">
			<xsl:for-each select="*">
				<xsl:element name="LedDefinition" namespace="{$nsVendorCV}"
					exclude-result-prefixes="#default">
					<xsl:attribute name="number"><xsl:value-of
						select="substring(current()/local-name(),4)" /></xsl:attribute>
					<xsl:apply-templates select="./*" />
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<xsl:template match="Servos" priority="10">
		<xsl:element name="{name()}" namespace="{$nsVendorCV}"
			exclude-result-prefixes="#default">
			<xsl:for-each select="*">
				<xsl:element name="ServoDefinition" namespace="{$nsVendorCV}"
					exclude-result-prefixes="#default">
					<xsl:attribute name="number"><xsl:value-of
						select="substring(current()/local-name(),6)" /></xsl:attribute>
					<xsl:apply-templates select="./*" />
				</xsl:element>
			</xsl:for-each>
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
			<!--
			<xsl:element name="Sectors" namespace="{$nsVendorCV}"
				exclude-result-prefixes="#default">
				<xsl:for-each select="*/.[starts-with(name(), 'Sector')]">
					<xsl:element name="SectorDefinition" namespace="{$nsVendorCV}"
						exclude-result-prefixes="#default">
						<xsl:attribute name="number"><xsl:value-of
							select="substring(current()/local-name(),7)" /></xsl:attribute>
						<xsl:apply-templates select="./*" />
					</xsl:element>
				</xsl:for-each>
			</xsl:element>
			-->
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
